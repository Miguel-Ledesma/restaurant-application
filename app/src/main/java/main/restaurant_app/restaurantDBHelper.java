package main.restaurant_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class restaurantDBHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String DOUBLE_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Employees";

    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + restaurantContract.Employee.TABLE_NAME + " ( " +
                restaurantContract.Employee._ID + TEXT_TYPE + " PRIMARY KEY," +
                restaurantContract.Employee.COLUMN_NAME_PERSON + TEXT_TYPE + COMMA_SEP +
                restaurantContract.Employee.COLUMN_NAME_FOHBOH + TEXT_TYPE + COMMA_SEP +
                restaurantContract.Employee.COLUMN_NAME_MANAGER + INT_TYPE + COMMA_SEP +
                restaurantContract.Employee.COLUMN_NAME_ADMIN + INT_TYPE + COMMA_SEP +
                restaurantContract.Employee.COLUMN_NAME_PAY + DOUBLE_TYPE + ");";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + restaurantContract.Employee.TABLE_NAME;

    public restaurantDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL((SQL_DELETE_ENTRIES));
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, newVersion, oldVersion);
    }
}