package com.wilsonsinclair.scheduler.factories;

import com.wilsonsinclair.scheduler.Employee;
import com.wilsonsinclair.scheduler.Settings;
import com.wilsonsinclair.scheduler.time.Day;
import com.wilsonsinclair.scheduler.time.Schedule;
import com.wilsonsinclair.scheduler.time.Shift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ScheduleFactory {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleFactory.class);

    /*
         This method takes a list of employees and a starting date as input,
         and generates a schedule for the next week.

         Should assign the manager's shifts first as a manager has a target number of hours for each week.
         Then we can assign shift leads for the morning, afternoon and night shifts for each day. The manager
         will have already covered some of these.
         Finally, we can assign the remaining shifts to fill in the rest.
         This approach prioritizes dealing with hard constraints first, such as manager hours and needing shift
         leads for each part of the day.


        @param employees The list of employees to generate the schedule for.
        @param startDate The starting date of the schedule.
        @return The generated schedule.
    */
    public static Schedule generateSchedule(List<Employee> employees, LocalDate startDate, Settings settings) {

        logger.info("Generating schedule for {} through {} with {}", startDate, startDate.plusWeeks(1), settings);

        Schedule schedule = new Schedule(employees, startDate);

        // We use a random object to help us incorporate some pseudo randomness into the generated schedule
        // when choosing a possible employee or creating a shift's start and end time.
        Random r = new Random();

        try {
             Employee manager = employees.stream().filter(Employee::isManager).findFirst().orElseThrow();
             assignManagerShifts(manager, schedule.getDays(), r, settings.getManagerHours());
             optimizeManagerHours(manager, settings);
        } catch (NoSuchElementException e) {
            logger.error("No manager found in the employee list when generating schedule.");
            return null;
        }

        // Filter out the manager, since we already assigned them their shifts, and any employees that aren't opening or closing
        // shift leads.
        assignShiftLeads(schedule.getDays(), r, employees.stream().filter(e -> !e.isManager() && (e.canOpen() || e.canClose())).toList());
        return schedule;
    }

    /*
        NOTES:
        - Manager should try close two days of the week
        - Total number of hours should be at or close to managerHours
        - This is a greedy approach that assigns longer shifts first, as it will reach the target hours faster and allow room for minute optimizations later.
     */
    private static void assignManagerShifts(Employee manager, List<Day> days, Random r, int targetHours) {
        assert(manager.isManager());

        // Shuffle the days to help with making off days seem more random, as we are going to walk through the list of days in the order
        // they appear.
        Collections.shuffle(days, r);
        Iterator<Day> dayIterator = days.iterator();

        while (dayIterator.hasNext() && Math.abs(manager.getAssignedHours() - targetHours) > 5) {
            Day day = dayIterator.next();
            if (!manager.canWork(day.getDate())) {
                continue;
            }

            // Try to avoid OPEN_TO_CLOSE shifts, as they should be used as a last resort.
            // Also avoid shorter lunch shifts as we want to prioritize longer shifts at first to get to the target hours quickly, which allows room for optimizations later.
            List<Shift.ShiftType> allowedShiftTypes = Arrays.stream(Shift.ShiftType.values()).filter(s -> s == Shift.ShiftType.OPENER || s == Shift.ShiftType.LUNCH_TO_CLOSE).toList();

            // This can theoretically infinitely loop if a shift that the manager can work is never generated.
            // A better solution is needed here.
            do {
                Shift shift = createManagerShift(manager, day, allowedShiftTypes.get(r.nextInt(allowedShiftTypes.size())));
                if (manager.canWork(shift)) {
                    manager.assignShift(shift);
                    day.addShift(shift);
                }
            } while (!day.hasAssigned(manager));
        }
   }

    private static Shift createManagerShift(Employee manager, Day day, Shift.ShiftType shiftType) {
        switch (shiftType) {
            case OPENER -> {
                return new Shift(manager, day.getDate(), Shift.MANAGER_OPENING_SHIFT_START_TIME, Shift.FOUR_PM);
            }
            case CLOSER -> {
                return new Shift(manager, day.getDate(), Shift.TWO_PM, Shift.CLOSING_TIME);
            }
            case LUNCH -> {
                return new Shift(manager, day.getDate(), Shift.TEN_AM, Shift.FOUR_PM);
            }
            case OPEN_TO_CLOSE -> {
                return new Shift(manager, day.getDate(), Shift.OPENING_SHIFT_START_TIME, Shift.CLOSING_TIME);
            }
            case LUNCH_TO_CLOSE -> {
                return new Shift(manager, day.getDate(), Shift.ELEVEN_AM, Shift.CLOSING_TIME);
            }
            default -> throw new IllegalArgumentException("Invalid shift type");
        }
    }

    private static Shift createShiftLeadShift(Employee employee, Day day, Shift.ShiftType shiftType) {
        switch (shiftType) {
            case OPENER -> {
                return new Shift(employee, day.getDate(), Shift.OPENING_SHIFT_START_TIME, Shift.TWO_PM);
            }
            case CLOSER -> {
                return new Shift(employee, day.getDate(), Shift.FOUR_PM, Shift.CLOSING_TIME);
            }
            default -> throw new IllegalArgumentException("Invalid shift type");
        }
    }

    /*
        Attempt to optimize the manager's assigned shifts by fine-tuning the start and end times of the shifts
     */
    private static void optimizeManagerHours(Employee manager, Settings settings) {

        int targetHours = settings.getManagerHours();

        // If the manager has fewer than the target number of hours, but it is within the allowed variance, we do nothing.
        if ((manager.getAssignedHours() < targetHours) && (targetHours - manager.getAssignedHours() <= settings.getAllowedManagerHourVariance()) ) {
            return;
        }
        logger.info("Optimizing manager hours for {}", manager.getName());
        for (Shift shift : manager.getAssignedShifts()) {
            if (manager.getAssignedHours() > targetHours) {
                // If the manager has more hours than the target, we can only reduce the shift's end time.
                if (shift.getEndTime().equals(Shift.CLOSING_TIME) && !shift.getStartTime().equals(Shift.OPENING_SHIFT_START_TIME)) {
                    shift.delayStartTime();
                    if (targetHours - manager.getAssignedHours() <= settings.getAllowedManagerHourVariance()) { return; }
                }
            }
            if (manager.getAssignedHours() < targetHours) {
                // If the manager has fewer hours than the target, we can only increase the shift's start time.
                if (!shift.getEndTime().equals(Shift.CLOSING_TIME)) {
                    shift.delayEndTime();
                    if (targetHours - manager.getAssignedHours() <= settings.getAllowedManagerHourVariance()) { return; }
                }
            }
        }
    }

    private static void assignShiftLeads(List<Day> days, Random r, List<Employee> employees) {
        List<Employee> openers = employees.stream().filter(Employee::canOpen).toList();
        List<Employee> closers = employees.stream().filter(Employee::canClose).toList();
        for (Day day : days) {
            while (!day.hasOpener()) {
                Employee opener = openers.get(r.nextInt(openers.size()));
                Shift shift = createShiftLeadShift(opener, day, Shift.ShiftType.OPENER);
                if (day.hasAssigned(opener) || !opener.canWork(shift)) {
                    continue;
                }
                opener.assignShift(shift);
                day.addShift(shift);
            }
            while (!day.hasCloser()) {
                Employee closer = closers.get(r.nextInt(closers.size()));
                Shift shift = createShiftLeadShift(closer, day, Shift.ShiftType.CLOSER);
                if (day.hasAssigned(closer) || !closer.canWork(shift)) {
                    continue;
                }
                closer.assignShift(shift);
                day.addShift(shift);
            }
        }
    }
}
