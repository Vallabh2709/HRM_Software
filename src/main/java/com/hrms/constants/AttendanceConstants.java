package com.hrms.constants;

import java.time.LocalTime;

public final class AttendanceConstants {

    private AttendanceConstants() {
    }

    public static final LocalTime OFFICE_START_TIME = LocalTime.of(9, 0);

    public static final LocalTime OFFICE_END_TIME = LocalTime.of(18, 0);

    public static final LocalTime GRACE_TIME = LocalTime.of(9, 15);

    public static final LocalTime HALF_DAY_TIME = LocalTime.of(13, 0);

    public static final LocalTime ABSENT_AFTER_TIME = LocalTime.of(15, 0);

}