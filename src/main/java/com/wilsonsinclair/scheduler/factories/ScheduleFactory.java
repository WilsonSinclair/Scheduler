package com.wilsonsinclair.scheduler.factories;

import com.wilsonsinclair.scheduler.Employee;
import com.wilsonsinclair.scheduler.Settings;
import com.wilsonsinclair.scheduler.time.Day;
import com.wilsonsinclair.scheduler.time.Schedule;
import com.wilsonsinclair.scheduler.time.Shift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ScheduleFactory {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleFactory.class);

    /*
        This method takes a list of employees and a starting date as input,
        and generates a schedule for the next week.

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

        List<Employee> openers = employees.stream().filter(Employee::canOpen).toList();

        //TODO: Implement schedule generation logic here

        /* Should assign the manager's shifts first as a manager has a target number of hours for each week.
        * Then we can assign shift leads for the morning, afternoon and night shifts for each day. The manager
        * will have already covered some of these.
        * Finally, we can assign the remaining shifts to fill in the rest.
        * This approach prioritizes dealing with hard constraints first, such as manager hours and needing shift
        * leads for each part of the day.
        */
        for (Day day : schedule.getDays()) {
            assignOpener(openers, day, r);
        }
        return schedule;
    }

    /*
        NOTES:
        - Manager should try close two days of the week
        - Total number of hours should be at or close to managerHours
        - Is it feasible to do this recursively with backtracking to find a solution?
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

        }
    }

    private static void assignOpener(List<Employee> employees, Day day, Random r) {
        while (!day.hasOpener()) {
            Employee e = employees.get(r.nextInt(employees.size()));
            if (day.hasAssigned(e)) { continue; }

            for (LocalTime time : Shift.OPENING_SHIFT_END_TIMES) {
                Shift s = new Shift(e, day.getDate(), Shift.OPENING_TIME, time);
                if (e.canWork(s)) {
                    e.assignShift(s);
                    day.addShift(s);
                    return;
                }
            }
        }
    }
}
