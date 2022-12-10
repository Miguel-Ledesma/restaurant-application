package main.restaurant_app;

import android.provider.BaseColumns;

public final class restaurantContract {
    private restaurantContract(){}

    public static abstract class Employee implements BaseColumns {
        public static final String TABLE_NAME = "Employees";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_PERSON = "Person";
        public static final String COLUMN_NAME_FOHBOH = "FOHBOH";
        public static final String COLUMN_NAME_MANAGER = "Manager";
        public static final String COLUMN_NAME_ADMIN = "Admin";
        public static final String COLUMN_NAME_PAY = "PAY";
    }
}