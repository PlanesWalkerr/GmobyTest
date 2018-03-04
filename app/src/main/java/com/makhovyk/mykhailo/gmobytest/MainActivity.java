package com.makhovyk.mykhailo.gmobytest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.makhovyk.mykhailo.gmobytest.Model.ApiClient;
import com.makhovyk.mykhailo.gmobytest.Model.ApiResponse;
import com.makhovyk.mykhailo.gmobytest.Model.City;
import com.makhovyk.mykhailo.gmobytest.Model.DBHelper;
import com.makhovyk.mykhailo.gmobytest.Model.Trip;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Button mButton;
    Button mDeleteButton;
    Button mShowButton;
    ApiClient mApiClient;

    DBHelper mDBHelper;
    SQLiteDatabase database;
    List<Trip> trips;
    Set<City> set = new HashSet<City>();
    private final String BASE_URL = "http://projects.gmoby.org/web/index.php/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiClient = new ApiClient(BASE_URL);
        mDBHelper = new DBHelper(this);
        database =mDBHelper.getWritableDatabase();

        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*City city = new City(Long.valueOf(46),0,"Artemovsk");
                city.save();
                City city1 = new City(Long.valueOf(47),1,"Antracit");
                city1.save();
                Log.d("Test","saved");*/

              /* List<City> list = City.listAll(City.class);
                for (City c:list) {
                    Log.d("Test",c.toString());
                    Log.d("Test",c.getName());
                }*/

                final ContentValues contentValues = new ContentValues();

                mApiClient.getTrips("2016-01-01","2018-03-01")
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Observer<ApiResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d("Test","onsubscribe");
                            }

                            @Override
                            public void onNext(ApiResponse responseBody) {

                                trips = responseBody.getTrips();
                                for (Trip t: trips) {
                                    set.add(t.getFromCity());
                                    set.add(t.getToCity());
                                }

                                for (City c: set) {
                                    contentValues.put(DBHelper.CITY_ID,c.getCityId());
                                    contentValues.put(DBHelper.CITY_HIGHLIGHT,c.getHighlight());
                                    contentValues.put(DBHelper.CITY_NAME,c.getName());
                                    database.insert(DBHelper.TABLE_CITIES,null, contentValues);
                                }

                                ContentValues cv = new ContentValues();
                                for (Trip tr: trips) {
                                    cv.put(DBHelper.TRIP_ID,tr.getTripId());
                                    cv.put(DBHelper.FROM_CITY,tr.getFromCity().getCityId());
                                    cv.put(DBHelper.FROM_DATE,tr.getFromDate());
                                    cv.put(DBHelper.FROM_TIME,tr.getFromTime());
                                    cv.put(DBHelper.FROM_INFO,tr.getFromInfo());
                                    cv.put(DBHelper.TO_CITY,tr.getToCity().getCityId());
                                    cv.put(DBHelper.TO_DATE,tr.getToDate());
                                    cv.put(DBHelper.TO_TIME,tr.getToTime());
                                    cv.put(DBHelper.TO_INFO,tr.getToInfo());
                                    cv.put(DBHelper.INFO,tr.getInfo());
                                    cv.put(DBHelper.PRICE,tr.getPrice());
                                    cv.put(DBHelper.BUS_ID,tr.getBusId());
                                    cv.put(DBHelper.RESERVATION_COUNT,tr.getReservationCount());
                                    database.insert(DBHelper.TABLE_TRIPS, null, cv);
                                }



                                /*Log.d("Test","onnext");
                                //String url=responseBody.raw().request().url().toString();
                                Log.d("Test","df");

                                trips = responseBody.getTrips();

                                for (Trip trip: trips) {
                                   // List<City> fromCIty = City.find(City.class,"cityId=?",String.valueOf(trip.getFromCity().getCityId()),"1");
                                    List<City> fromCity = City.find(City.class,"city_id=?",String.valueOf(trip.getFromCity().getCityId()));
                                    if(fromCity.isEmpty()){
                                        trip.getFromCity().save();
                                        Log.d("Test","new city");
                                    }else {
                                        trip.setFromCity(fromCity.get(0));
                                        Log.d("Test","existing city");
                                    }

                                    List<City> toCity = City.find(City.class,"city_id=?",String.valueOf(trip.getToCity().getCityId()));

                                    if(toCity.isEmpty()){
                                        trip.getToCity().save();
                                        Log.d("Test","new city");

                                    }else {
                                       // trip.setToCity(toCity.get(0));
                                        Log.d("Test","existing city");

                                    }

                                }*/



                            }


                            @Override
                            public void onError(Throwable e) {
                                Log.d("Test",e.toString());
                            }

                            @Override
                            public void onComplete() {
                                Log.d("Test","oncomplete");
                                Log.d("Test",String.valueOf(set.size()));
                                for (City c: set) {
                                    Log.d("Test",c.toString());
                                }
//                                List<City> cities = City.listAll(City.class);
//                                Log.d("Test","Reading");
//                                for (City c:cities) {
//                                    Log.d("Test",c.toString());
//                                }
//                                for(Trip t: trips){
//                                    Log.d("Test",t.toString());
//                                    //t.save();
//                                }
//                                Log.d("Test","oncomplete");
                            }
                        });

        }});

        mDeleteButton = findViewById(R.id.deleteButton);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.delete(DBHelper.TABLE_CITIES,null,null);
                database.delete(DBHelper.TABLE_TRIPS,null,null);
//                City.deleteAll(City.class);
//                Trip.deleteAll(Trip.class);
            }
        });

        mShowButton = findViewById(R.id.showButton);
        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Cursor cursor = database.query(DBHelper.TABLE_CITIES,null,
                        null,null,null,null,null);
                if (cursor.moveToFirst()){
                    int idIndex = cursor.getColumnIndex(DBHelper.CITY_ID);
                    int highlightIndex = cursor.getColumnIndex(DBHelper.CITY_HIGHLIGHT);
                    int nameIndex = cursor.getColumnIndex(DBHelper.CITY_NAME);
                    do{
//                        Log.d("Test","ID = " + cursor.getInt(idIndex) +
//                                ", highlight = " + cursor.getInt(highlightIndex) +
//                                ", name = " + cursor.getString(nameIndex));
                        logCursor(cursor);

                    }while (cursor.moveToNext());

                }else {
                    Log.d("Test","0 values");
                }
                cursor.close();*/

                String sqlQuery = "select t._trip_id, t.from_city, t.to_city, c._id as from_id, " +
                        "c.name as from_name, c.highlight as from_hl, ct._id as to_id, " +
                        "ct.highlight as to_hl, ct.name as to_name "
                        + "from trips as t "
                        + "inner join cities as c "
                        + "on t.from_city = c._id "
                        + "inner join cities as ct "
                        + "on t.to_city = ct._id ";
                Cursor tripCursor = database.rawQuery(sqlQuery,null);

                /*Cursor tripCursor = database.query(DBHelper.TABLE_TRIPS,null,
                        null,null,null,null,null);
                /*if (tripCursor.moveToFirst()){
                    int idIndex = tripCursor.getColumnIndex(DBHelper.TRIP_ID);
                    int fromCityIndex = tripCursor.getColumnIndex(DBHelper.FROM_CITY);
                    int dromDateIndex = tripCursor.getColumnIndex(DBHelper.FROM_DATE);
                    int fromTimeIndex = tripCursor.getColumnIndex(DBHelper.FROM_TIME);
                    int fromInfoIndex = tripCursor.getColumnIndex(DBHelper.FROM_INFO);
                    int toCityIndex = tripCursor.getColumnIndex(DBHelper.TO_CITY);
                    int toDateIndex = tripCursor.getColumnIndex(DBHelper.TO_DATE);
                    int toTimeIndex = tripCursor.getColumnIndex(DBHelper.TO_TIME);
                    int toInfoIndex = tripCursor.getColumnIndex(DBHelper.TO_INFO);
                    int infoIndex = tripCursor.getColumnIndex(DBHelper.INFO);
                    int priceIndex = tripCursor.getColumnIndex(DBHelper.PRICE);
                    int busIdIndex = tripCursor.getColumnIndex(DBHelper.BUS_ID);
                    int reservationCountIndex = tripCursor.getColumnIndex(DBHelper.RESERVATION_COUNT);
                    do{
//                        Log.d("Test","ID = " + tripCursor.getInt(idIndex) +
//                                ", fromCity = " + tripCursor.getInt(fromCityIndex) +
//                                ", dromDate = " + tripCursor.getString(dromDateIndex) +
//                                ", fromTime = " + tripCursor.getString(fromTimeIndex) +
//                                ", fromInfo = " + tripCursor.getString(fromInfoIndex) +
//                                ", toCity = " + tripCursor.getInt(toCityIndex) +
//                                ", toDate = " + tripCursor.getString(toDateIndex) +
//                                ", toTime = " + tripCursor.getString(toTimeIndex) +
//                                ", toInfo = " + tripCursor.getString(toInfoIndex) +
//                                ", info = " + tripCursor.getString(infoIndex) +
//                                ", price = " + tripCursor.getDouble(priceIndex) +
//                                ", busId = " + tripCursor.getInt(busIdIndex) +
//                                ", reservationCount = " + tripCursor.getInt(reservationCountIndex));
                    }while (tripCursor.moveToNext());

                }else {
                    Log.d("Test","0 values");
                }*/
                logCursor(tripCursor);
                tripCursor.close();
              /*  List<City> cities = City.listAll(City.class);
                Log.d("Test","Reading");
                for (City c:cities) {
                    Log.d("Test",c.toString());
                }

                try {
                    Log.d("Test",trips.get(0).toString());
                        trips.get(5).save();
                    } catch (Exception e) {
                        Log.d("aaa",e.toString());

                    }

                List<Trip> list = Trip.listAll(Trip.class);
                Log.d("Test","Reading");
                for (Trip t:list) {
                    Log.d("Test",t.toString());
                }*/


            }
        });

    }

    void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d("bd", str);
                } while (c.moveToNext());
            }
        } else
            Log.d("bd", "Cursor is null");
    }
}
