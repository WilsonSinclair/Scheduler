package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class ScheduleFactory {

    /*
        This method takes a list of employees and a starting date as input,
        and generates a schedule for the next week.

        @param employees The list of employees to generate the schedule for.
        @param startDate The starting date of the schedule.
        @return The generated schedule.
    */
    public static Schedule generateSchedule(List<Employee> employees, LocalDate startDate, int numLunchers, int numClosers) {

        Schedule schedule = new Schedule(employees, startDate);

        // We use a random object to help us incorporate some pseudorandomness into the generated schedule
        // when choosing a possible employee or creating a shift's start and end time.
        Random r = new Random();

        List<Employee> openers = employees.stream().filter(Employee::canOpen).toList();

        //TODO: Implement schedule generation logic here
        for (Day day : schedule.getDays()) {
            assignOpener(openers, day, r);
            assignLunchers(employees,day, r, numLunchers);
        }
        return schedule;
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

    private static void assignLunchers(List<Employee> employees, Day day, Random r, int numLunchers) {
        while (!day.hasLunchers(numLunchers)) {
            Employee e = employees.get(r.nextInt(employees.size()));
            if (day.hasAssigned(e)) { continue; }

            // Assign a lunch shift that starts at 10:00 so that there are at least two people on shift when the store opens
            
        }
    }

}
