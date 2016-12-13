package com.example.minaris.alarm;

import android.provider.BaseColumns;

/**
 * Created by jodeneve on 21/11/2016.
 */

public class AlarmContract {

    private AlarmContract() {};

    public static class AlarmEntry implements BaseColumns {
        public static final String TABLE_NAME = "Alarms";
        public static final String TIME_SLOT = "time_slot";
        public static final String RINGTONE = "ringtone";
        public static final String SNOOZABLE = "snoozable";
        public static final String ACTIVE = "active";
        public static final String REPEAT = "repeat";
    }

    public static class MotionEntry implements BaseColumns {
        public static final String TABLE_NAME = "Snooze";
        public static final String X = "x";
        public static final String Y = "y";
        public static final String Z = "z";
        public static final String MOTION_NAME = "motion_name";
        public static final String INTERVAL = "interval";
        public static final String ACTIVE = "active";

    }

}
