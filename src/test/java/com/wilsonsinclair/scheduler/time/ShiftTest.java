package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ShiftTest {

    @Test
    void assignShiftTypeCorrectly() {
        Employee employee = new Employee("Test", true, true, true);

        Shift regularOpeningShift = new Shift(
                employee,
                LocalDate.now(),
                Shift.OPENING_SHIFT_START_TIME,
                Shift.TWO_PM
        );

        Shift managerOpeningShift = new Shift(
                employee,
                LocalDate.now(),
                Shift.MANAGER_OPENING_SHIFT_START_TIME,
                Shift.TWO_PM
        );

        Shift lunchShiftTenAM = new Shift(
                employee,
                LocalDate.now(),
                Shift.TEN_AM,
                Shift.FOUR_PM
        );

        Shift lunchShiftElevenAM = new Shift(
                employee,
                LocalDate.now(),
                Shift.ELEVEN_AM,
                Shift.TWO_PM
        );

        Shift closingShift = new Shift(
                employee,
                LocalDate.now(),
                Shift.TWO_PM,
                Shift.CLOSING_TIME
        );

        Shift lunchToCloseShift = new Shift(
                employee,
                LocalDate.now(),
                Shift.ELEVEN_AM,
                Shift.CLOSING_TIME
        );

        Shift noonToCloseShift = new Shift(
                employee,
                LocalDate.now(),
                Shift.NOON,
                Shift.CLOSING_TIME
        );

        Shift openToCloseShift = new Shift(
                employee,
                LocalDate.now(),
                Shift.OPENING_SHIFT_START_TIME,
                Shift.CLOSING_TIME
        );

        Shift managerOpenToCloseShift = new Shift(
                employee,
                LocalDate.now(),
                Shift.MANAGER_OPENING_SHIFT_START_TIME,
                Shift.CLOSING_TIME
        );

        assertAll(
                () -> assertThrows(NullPointerException.class, () -> new Shift(null, LocalDate.now(), LocalTime.now(), LocalTime.now())),
                () -> assertThrows(NullPointerException.class, () -> new Shift(employee, null, LocalTime.now(), LocalTime.now())),
                () -> assertThrows(NullPointerException.class, () -> new Shift(employee, LocalDate.now(), null, LocalTime.now())),
                () -> assertThrows(NullPointerException.class, () -> new Shift(employee, LocalDate.now(), LocalTime.now(), null)),
                () -> assertEquals(Shift.ShiftType.OPENER, regularOpeningShift.getType()),
                () -> assertEquals(Shift.ShiftType.OPENER, managerOpeningShift.getType()),
                () -> assertEquals(Shift.ShiftType.LUNCH, lunchShiftTenAM.getType()),
                () -> assertEquals(Shift.ShiftType.LUNCH, lunchShiftElevenAM.getType()),
                () -> assertEquals(Shift.ShiftType.CLOSER, closingShift.getType()),
                () -> assertEquals(Shift.ShiftType.LUNCH_TO_CLOSE, lunchToCloseShift.getType()),
                () -> assertEquals(Shift.ShiftType.LUNCH_TO_CLOSE, noonToCloseShift.getType()),
                () -> assertEquals(Shift.ShiftType.OPEN_TO_CLOSE, openToCloseShift.getType()),
                () -> assertEquals(Shift.ShiftType.OPEN_TO_CLOSE, managerOpenToCloseShift.getType())
        );
    }

    @Test
    void isShiftDurationInitializedCorrectly() {
        Shift tenToFour = new Shift(
                new Employee("Test", true, true, true),
                LocalDate.now(),
                Shift.TEN_AM,
                Shift.FOUR_PM
        );

        Shift openToFour = new Shift(
                new Employee("Test", true, true, true),
                LocalDate.now(),
                Shift.OPENING_SHIFT_START_TIME,
                Shift.FOUR_PM
        );

        Shift twoToClose = new Shift(
                new Employee("Test", true, true, true),
                LocalDate.now(),
                Shift.TWO_PM,
                Shift.CLOSING_TIME
        );

        Shift openToClose = new Shift(
                new Employee("Test", true, true, true),
                LocalDate.now(),
                Shift.OPENING_SHIFT_START_TIME,
                Shift.CLOSING_TIME
        );

        assertAll(
                () -> assertEquals(6, tenToFour.getDuration()),
                () -> assertEquals(8, openToFour.getDuration()),
                () -> assertEquals(7, twoToClose.getDuration()),
                () -> assertEquals(13, openToClose.getDuration())
        );
    }

    @Test
    void durationCorrectedAfterStartTimeDelay() {
        Shift tenToFour = new Shift(
                new Employee("Test", true, true, true),
                LocalDate.now(),
                Shift.TEN_AM,
                Shift.FOUR_PM
        );

        tenToFour.delayStartTime();
        assertEquals(5, tenToFour.getDuration());
    }

    @Test
    void durationCorrectedAfterEndTimeDelay() {
        Shift tenToFour = new Shift(
                new Employee("Test", true, true, true),
                LocalDate.now(),
                Shift.TEN_AM,
                Shift.FOUR_PM
        );

        tenToFour.delayEndTime();
        assertEquals(7, tenToFour.getDuration());
    }
}
