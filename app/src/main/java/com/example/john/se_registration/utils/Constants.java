package com.example.john.se_registration.utils;

/**
 * Created by john on 12/30/17.
 */

public class Constants {
    public abstract class config {
        /****** URL DECLARATION ******************************/
        public static final String DB_NAME = "registration_db";
        public static final int DB_VERSION = 1;
        public static final int TOTAL_TABLES = 2;

        public static final String APP_FOLDER = "student_registration";
        public static final String IMAGE_SUB_FOLDER = "photos";

        public static final String TABLE_REGISTRATION = "registration_tb";
        public static final String REGISTRATION_ID = "registration_id";
        public static final String REGISTRATION_DATE = "registration_date";
        public static final String REGISTRATION_TIME = "registration_time";
        public static final String REGISTRATION_NUMBER = "registration_number";
        public static final String SEMESTER = "semester";
        public static final String COURSE = "course";
        public static final String REGISTRATION_STATUS = "registration_status";

        public static final String TABLE_PICTURE = "picture_tb";
        public static final String PICTURE_ID = "picture_id";
        public static final String PICTURE_NAME = "picture_name";
        public static final String PICTURE_STATUS = "picture_status";

    }
}
