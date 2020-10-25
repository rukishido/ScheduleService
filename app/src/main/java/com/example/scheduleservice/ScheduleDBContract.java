package com.example.scheduleservice;

import android.provider.BaseColumns;

public class ScheduleDBContract {
    private ScheduleDBContract(){}

    public static final class ScheduleTableInfo implements BaseColumns {
        public static final String TABLE_NAME = "ScheduleData";
        public static final String COLUMN_CATEGORY = "Category";
        public static final String COLUMN_FILENAME = "fName";
    }
}
