package com.makhovyk.mykhailo.gmobytest.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TripsDB";
    public static final String TABLE_TRIPS = "Trips";
    public static final String TABLE_CITIES = "Cities";

    public static final String CITY_ID = "_id";
    public static final String CITY_HIGHLIGHT = "highlight";
    public static final String CITY_NAME = "name";

    public static final String TRIP_ID = "_trip_id";
    public static final String FROM_CITY = "from_city";
    public static final String TO_CITY = "to_city";
    public static final String FROM_DATE = "from_date";
    public static final String FROM_TIME = "from_time";
    public static final String FROM_INFO = "from_info";
    public static final String TO_DATE = "to_date";
    public static final String TO_TIME = "to_time";
    public static final String TO_INFO = "to_info";
    public static final String INFO = "info";
    public static final String PRICE = "price";
    public static final String BUS_ID = "bus_id";
    public static final String RESERVATION_COUNT = "reservation_count";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CITIES_TABLE_CREATE = "create table " + TABLE_CITIES + " (" + CITY_ID
                + " integer primary key," + CITY_HIGHLIGHT + " integer," + CITY_NAME + " text)";
        final String TRIPS_TABLE_CREATE = "create table " + TABLE_TRIPS + " (" + TRIP_ID
                + " integer primary key," + FROM_CITY + " integer," + FROM_DATE + " text,"
                + FROM_TIME + " text," + FROM_INFO + " text," + TO_CITY + " integer," + TO_DATE
                + " text," + TO_TIME + " text," + TO_INFO + " text," + INFO + " text,"
                + PRICE + " real," + BUS_ID + " integer," + RESERVATION_COUNT + " integer,"
                + " FOREIGN KEY (" + FROM_CITY + ") REFERENCES " + TABLE_CITIES + "(" + CITY_ID + "),"
                + " FOREIGN KEY (" + TO_CITY + ") REFERENCES " + TABLE_CITIES + "(" + CITY_ID + "))";

        sqLiteDatabase.execSQL(CITIES_TABLE_CREATE);
        sqLiteDatabase.execSQL(TRIPS_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_CITIES);
        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_TRIPS);
        onCreate(sqLiteDatabase);
    }
}
