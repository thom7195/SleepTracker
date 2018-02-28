package com.example.thomson.hci;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.LocaleList;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class serves as the class that will manage database operations an queries
 *
 */

public class DBHelper extends SQLiteOpenHelper {
    //define database string constants here
    private static  final String DATABASE_NAME = "HCI_db" ;

    //necessary fields for table login
    private static  final String LOGIN_TABLE_NAME = "Login" ;

    private static  final String LOGIN_COLUMN_USERNAME = "username" ;
    private static  final String LOGIN_COLUMN_ID = "id" ;
    private static  final String LOGIN_COLUMN_PASSWORD = "password" ;

    //necessary fields for table log
    private static final String LOG_TABLE_NAME = "Log" ;
    private static final String LOG_COLUMN_USERNAME = "username" ;
    private static final String LOG_COLUMN_ID = "id" ;
    private static final String LOG_COLUMN_TAG = "tag" ;
    private static final String LOG_COLUMN_ACTION = "action" ;
    private static final String LOG_COLUMN_DATE = "date" ;
    private static final String LOG_COLUMN_TIME = "time" ;

    //necessary field for table uptime

    private static final String UPTIME_TABLE_NAME = "Uptime" ;
    private static final String UPTIME_COLUMN_ID = "id" ;
    private static final String UPTIME_COLUMN_TAG = "tag" ;
    private static final String UPTIME_COLUMN_DURATION = "duration" ;

    //necessary fields required for sleeping data
    private static final String SLEEPDATA_TABLE_NAME = "SleepData" ;
    private static final String SLEEPDATA_COLUMN_ID = "id" ;
    private static final String SLEEPDATA_COLUMN_DATE = "date" ;
    private static final String SLEEPDATA_COLUMN_TIME = "time" ;
    private static final String SLEEPDATA_COLUMN_ACTIVE = "is_active" ;
    private static final String SLEEPDATA_COLUMN_WAKEUP = "wake_time" ;

    private static final String SLEEPDATA_COLUMN_USER = "user" ;
    //we can use the date to retrieve day of the year ( more convinient to prevent type conversions)
    //TODO: MOVE SLEEPDATA to a server database, and add in more functions


    //create statements to be called when database is first created
    private static final String CREATE_TABLE_LOGIN=
            " create table " + LOGIN_TABLE_NAME +
                    " (id integer primary key, username string, password string );";

    private static final String CREATE_TABLE_LOG =
            " create table " + LOG_TABLE_NAME +
                    " (id integer primary key, username string, tag string, action string, date string," +
                    " time string );";

    private static final String CREATE_TABLE_UPTIME =
            " create table " + UPTIME_TABLE_NAME +
                    " (id integer primary key, tag string, duration int);";
    private static final String CREATE_TABLE_SLEEPDATA =
            " create table " + SLEEPDATA_TABLE_NAME +
                    " (id integer primary key, user string, date string, time string , wake_time string, is_active int);";


    public DBHelper(Context context) {

            super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS "+LOGIN_TABLE_NAME);
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_LOG);
        db.execSQL(CREATE_TABLE_UPTIME);
        db.execSQL(CREATE_TABLE_SLEEPDATA);

        //insert dummy values for table
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOGIN_COLUMN_USERNAME, "admin");
        contentValues.put(LOGIN_COLUMN_PASSWORD, "pass");
        db.insert(LOGIN_TABLE_NAME, null, contentValues);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /*
    Functions below are reserved for the login function
    1. LOGIN
     */

    public boolean insertUser(String username, String password){

        Log.d("insertUser ", "Currently Inserting user " + username  ) ;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOGIN_COLUMN_USERNAME, username);
        contentValues.put(LOGIN_COLUMN_PASSWORD, password);

        return db.insert(LOGIN_TABLE_NAME, null, contentValues) > 0;

    }

    public boolean authenciateUser(String username, String password) {

        Log.d("authenciateUser", "Authenciating user "+ username + " with password "+ password) ;
        SQLiteDatabase db = this.getWritableDatabase() ;
        Cursor cursor = db.query(LOGIN_TABLE_NAME, new String[]{}
                ,LOGIN_COLUMN_USERNAME+" =? AND "
                + LOGIN_COLUMN_PASSWORD+" =?" ,new String[]{username,password}, null, null, null);

        if(cursor.getCount()!=0){
            //indicate record with matching username and password exist
            Log.d("authenciateUser", "Login Succesful " ) ;
            return true ;
        }else{
            // no match found for existing user and password combination
            Log.d("authenciateUser", "Login Failed" ) ;
            return false ;
        }
    }

 /*
    Functions below are reserved for the login function
    2. LOG
     */
    public boolean Log(String tag ,String action){
        //TODO:use session username instead of hardcoded string
        String username = "ADMIN" ;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LOG_COLUMN_USERNAME, username);
        contentValues.put(LOG_COLUMN_TAG, tag) ;
        contentValues.put(LOG_COLUMN_ACTION, action) ;

        //get current time and date
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String currentDate = new SimpleDateFormat("dd-MM-YYYY").format(new Date());
        Log.d("Logging ", "Category = " + tag + "Action "+ action +" At " + currentDate + "  " + currentTime ) ;

        contentValues.put(LOG_COLUMN_DATE,currentDate);
        contentValues.put(LOG_COLUMN_TIME,currentTime);
        return db.insert(LOG_TABLE_NAME, null, contentValues) > 0  ;


    }

    public String getLog(){
        final String LOG_NUM = "5" ;
        String log = "" ;
        SQLiteDatabase db = this.getWritableDatabase() ;
        Cursor cursor = db.query(
                LOG_TABLE_NAME,
                new String[]{LOG_COLUMN_DATE,LOG_COLUMN_TIME, LOG_COLUMN_ACTION,LOG_COLUMN_TAG,LOG_COLUMN_USERNAME}
                ,null
                , null
                , null
                ,null
                , LOG_COLUMN_ID+ " DESC"
                , LOG_NUM);

        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){

                String date = cursor.getString(cursor.getColumnIndex(LOG_COLUMN_DATE)) ;
                String time = cursor.getString(cursor.getColumnIndex(LOG_COLUMN_TIME)) ;
                String user = cursor.getString(cursor.getColumnIndex(LOG_COLUMN_USERNAME)) ;
                String action = cursor.getString(cursor.getColumnIndex(LOG_COLUMN_ACTION)) ;
                log += date + " " + time + " by " + user + "   "  + System.getProperty("line.separator")
                        + action ;
                log += System.getProperty("line.separator");
                log += System.getProperty("line.separator");



            }


        }else{

            log = "Log is Empty " ;
        }


        return log ;

    }


    /* Functions below are reserved to track record of the application uptime

    3. Uptime

     */
    public boolean RecordTime(String tag, long duration){

        ContentValues contentValues = new ContentValues();
        contentValues.put(UPTIME_COLUMN_TAG,tag);
        contentValues.put(UPTIME_COLUMN_DURATION, duration);

        SQLiteDatabase db = this.getWritableDatabase() ;
        return db.insert(UPTIME_TABLE_NAME,null ,contentValues)>0 ;

    }
    public long GetPowerConsumption(String tag){

        SQLiteDatabase db = this.getReadableDatabase() ;
        Cursor cursor = db.query(UPTIME_TABLE_NAME, new String[]{UPTIME_COLUMN_DURATION}
                ,UPTIME_COLUMN_TAG+" =?"
                ,new String[]{tag}, null, null, null);

        long total_duration = 0;

        while(cursor.moveToNext()){
            //get the time of each duration
            total_duration += cursor.getLong(cursor.getColumnIndex(UPTIME_COLUMN_DURATION)) ;
        }
        Log.d("GetPowerConsumption","Total Time "+ total_duration) ;
        return total_duration ;
    }


    /* Functions to store sleeping time of the user

    4.sleepingTime

     */
    public boolean InsertSleepData(){
        /*

           1. Get current time and date
           2. Check if time is still beore midnight.
           3. Adjust input date as necessary.

         */

        String user = "Admin" ; //// TODO: remove hard coded user
        //1. Get current time &date
        String this_date = new SimpleDateFormat("dd-MM-YYYY").format(new Date()) ;
        String this_time = new SimpleDateFormat("dd-MM-YYYY HH:mm").format(new Date()) ;

        //2.Check if time still before midnight
        Date time_noon = null ;
        try {
            //todo : 12 noon is used as threshold of sleeping time, might need more accurate way later on
            time_noon = new SimpleDateFormat("HH:mm").parse("12:00") ;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(new Date().getTime()-time_noon.getTime()<0){
            //indicate user sleeps before midnight
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, +1);
            Date tomorrow = cal.getTime();
            //insert to the db

            ContentValues contentValues = new ContentValues() ;
            contentValues.put(SLEEPDATA_COLUMN_DATE, new SimpleDateFormat("dd-MM-YYYY").format(tomorrow ));
            contentValues.put(SLEEPDATA_COLUMN_TIME, this_time);
            contentValues.put(SLEEPDATA_COLUMN_ACTIVE, 1);
            SQLiteDatabase db = this.getWritableDatabase() ;
            return db.insert(SLEEPDATA_TABLE_NAME,null,contentValues)>0 ;
        }else{
            //indicate user sleeps after midnight

            ContentValues contentValues = new ContentValues() ;
            contentValues.put(SLEEPDATA_COLUMN_DATE, this_date);
            contentValues.put(SLEEPDATA_COLUMN_TIME, this_time);
            contentValues.put(SLEEPDATA_COLUMN_ACTIVE, 1);
            SQLiteDatabase db = this.getWritableDatabase() ;
            Log.d("InsertSleepData" , " Inserting Sleep Time at date " + this_date + " "+ this_time) ;
            return db.insert(SLEEPDATA_TABLE_NAME,null,contentValues)>0 ;
        }




    }
    public boolean InsertWakeupData(){

        String user = "Admin" ; //// TODO: remove hard coded user
        //1. Get current time &date
        String this_date = new SimpleDateFormat("dd-MM-YYYY").format(new Date()) ;
        String this_time = new SimpleDateFormat("dd-MM-YYYY HH:mm").format(new Date()) ;

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(SLEEPDATA_COLUMN_WAKEUP, this_time);
        contentValues.put(SLEEPDATA_COLUMN_ACTIVE, 0);
        SQLiteDatabase db = this.getWritableDatabase() ;

        Log.d("InsertWakeupData" , " Updating Wakeup Time at date " + this_date + " "+ this_time) ;
        return db.update(SLEEPDATA_TABLE_NAME,contentValues,
                SLEEPDATA_COLUMN_DATE+" =? AND "
                        + SLEEPDATA_COLUMN_ACTIVE+" =?"
                ,new String[]{this_date,"1"}) >0 ;



    }
    public String GetSleepTime(Date date){

        SQLiteDatabase db = this.getReadableDatabase() ;
        String this_date = new SimpleDateFormat("dd-MM-YYYY").format(date) ;
        Log.d("GetSleepTime", "Querying for Date " + this_date ) ;

         Cursor cursor=db.query(SLEEPDATA_TABLE_NAME,new String[]{SLEEPDATA_COLUMN_TIME},
                SLEEPDATA_COLUMN_DATE +" =?",  new String[]{this_date},null,null,null);

        if(cursor.getCount()!=0){
            cursor.moveToNext();
            String time = cursor.getString(cursor.getColumnIndex(SLEEPDATA_COLUMN_TIME)) ;

            return time ;


        }else{


            Log.d("GetSleepTime" , " No sleeping record found for this day") ;

            return null ;
        }
    }

    public String GetWakeUpTime(Date date){

        SQLiteDatabase db = this.getReadableDatabase() ;
        String this_date = new SimpleDateFormat("dd-MM-YYYY").format(date) ;
        Log.d("GetWakeTime", "Querying for Date " + this_date ) ;

        Cursor cursor=db.query(SLEEPDATA_TABLE_NAME,new String[]{SLEEPDATA_COLUMN_WAKEUP},
                SLEEPDATA_COLUMN_DATE +" =?",  new String[]{this_date},null,null,null);

        if(cursor.getCount()!=0){
            cursor.moveToNext();
            String time = cursor.getString(cursor.getColumnIndex(SLEEPDATA_COLUMN_WAKEUP)) ;

            return time ;


        }else{

            Log.d("GetWakeTime" , " No wake up record found for this day") ;

            return null ;
        }
    }

    public long getSleepDuration(Date date) {
        SQLiteDatabase db = this.getReadableDatabase() ;
        String this_date = new SimpleDateFormat("dd-MM-YYYY").format(date) ;
        Log.d("GetWakeTime", "Querying for Date " + this_date ) ;

        Cursor cursor=db.query(SLEEPDATA_TABLE_NAME,new String[]{SLEEPDATA_COLUMN_TIME,SLEEPDATA_COLUMN_WAKEUP},
                SLEEPDATA_COLUMN_DATE +" =? AND "
                + SLEEPDATA_COLUMN_ACTIVE+" =?",  new String[]{this_date,"0"},null,null,null);

        long duration = 0;
        if(cursor.getCount()!=0){

            while(cursor.moveToNext()){
                //loop through each sleep record that exist for this day
                //1. Get the wake up time and wake up date
                String sleep_time_string = cursor.getString(cursor.getColumnIndex(SLEEPDATA_COLUMN_TIME)) ;
                String wake_time_string = cursor.getString(cursor.getColumnIndex(SLEEPDATA_COLUMN_WAKEUP)) ;

                //2. Convert them back to date objects
                SimpleDateFormat time_format = new SimpleDateFormat("dd-MM-YYYY HH:mm");
                Date wake_time = null ;
                Date sleep_time = null;
                try {
                    wake_time = time_format.parse(wake_time_string);
                    sleep_time = time_format.parse(sleep_time_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                duration+= wake_time.getTime()-sleep_time.getTime() ;
                Log.d("GetSleepDuration","Wake up Time " + wake_time + "Sleep time " + sleep_time + "Duration = "+ duration) ;


            }

        }else{
            Log.d("getSleepDuration","No sleep data found") ;
        }


        return duration;
    }
}
