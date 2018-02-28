package com.example.thomson.hci;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.TimeUnit;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * This class is an adapter class, which retrieves, and set the data for individual custom listview item
 * for the sleeptracker function. this adapter is then applied to the listview initialized in
 * SmartSleepActivity.class
 */

public class SleepListAdapter extends BaseAdapter {

    private static final String TAG = "SleepListAdapter" ;
    private final ArrayList<Integer> day_list;
    private final Context context;
    private DBHelper dbHelper;
    private LayoutInflater inflater;


    public SleepListAdapter(Context context, ArrayList<Integer> day_list){

        this.day_list = day_list ;
        this.context = context ;
            inflater = ( LayoutInflater )this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return day_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = inflater.inflate(R.layout.layout_sleep_list_item, null) ;
        TextView view_day = (TextView) rowView.findViewById(R.id.view_day ) ;
        TextView view_sleep_at = (TextView) rowView.findViewById(R.id.view_sleep_at ) ;
        int this_day = day_list.get(position) ;

        //set the textview to the corresponding date
        String day_of_week =  null ;
        switch (this_day){
            case 2 :
                day_of_week = "Mon" ;
                break;
            case 3 :
                day_of_week = "Tue" ;
                break;
            case 4 :
                day_of_week = "Wed" ;
                break;
            case 5 :
                day_of_week = "Thu" ;
                break;
            case 6 :
                day_of_week = "Fri" ;
                break;
            case 7 :
                day_of_week = "Sat" ;
                break;
            case 1 :
                day_of_week = "Sun" ;
        }


        //retrieve the time when you need to wake up


        /*
        retrieve the time you slept the previous night
        1. Get Current Date
        2. Get Sleeping time at said date
        3. Get Wake up time at said date
        4. Measure length of sleep.
         */

        // 1.Get Current Date
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, this_day);
        Date this_date = c.getTime();
        view_day.setText(day_of_week + System.lineSeparator() + new SimpleDateFormat("MMM dd").format(this_date));

        dbHelper = new DBHelper(context) ;
        // 2. Get Sleeping Time at said date
        String sleep_time = dbHelper.GetSleepTime(this_date) ;
        // 3. Get Wake up time at said date
        String wake_up_time = dbHelper.GetWakeUpTime(this_date) ;
        if(sleep_time!=null &wake_up_time!=null){

            //change visibility of some views when data is present
            view_sleep_at.setVisibility(View.VISIBLE);

            TextView view_sleeping_period = (TextView) rowView.findViewById(R.id.view_sleeping_period) ;

            view_sleeping_period.setText(sleep_time + " to "+wake_up_time);

            //4. get sleeping duration
            long difference = dbHelper.getSleepDuration(this_date) ;
            long difference_hour = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(difference)  ;
            long difference_minutes = (difference - difference_hour*(1000*60*60))/60000 ;
            if(difference_hour<0){
                //correct the mistake for sleeping time before 12 night
                difference_hour = 24 + difference_hour ;
            }

            //set color to red if duration of sleep is not enough
            if(difference_hour<8){

                Log.d(TAG,"Less than 8 Hours of sleep") ;
                view_day.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
                TextView view_insufficient_sleep = (TextView) rowView.findViewById(R.id.view_insufficient_sleep) ;
                view_insufficient_sleep.setVisibility(View.VISIBLE);

            }
            TextView view_sleeping_duration = (TextView) rowView.findViewById(R.id.view_sleeping_duration);
            view_sleeping_duration.setText("Sleep Duration : " + difference_hour + "Hours and " +difference_minutes + " Minutes");



        }






        return rowView;
    }
}
