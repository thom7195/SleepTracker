package com.example.thomson.hci.Activities;
/*
This class handles the sleep tracking feature of this application

 */
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.thomson.hci.DBHelper;
import com.example.thomson.hci.R;
import com.example.thomson.hci.SleepListAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class SmartSleepActivity extends BasicActivity {

    private ListView list ;
    private final String TAG = "SmartSleepActivity" ;
    private ArrayList<Integer> day_list = new ArrayList<Integer>();
    private DBHelper dbHelper ;
    //prepare a bundle to save the sleeping state
    private Switch switch_sleeping;
    private static Bundle bundle = new Bundle();
    private LinearLayout linearLayout ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        //get the layout that corresponds to this layout
        getLayoutInflater().inflate(R.layout.activity_smart_sleep, contentFrameLayout);
        //initialize help button function
        ImageButton imgbutton_help = (ImageButton) findViewById(R.id.imgbutton_help);
        imgbutton_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //launch the help activity

            }
        });
        //initialize day_list array
        day_list.clear();
        day_list.add(Calendar.MONDAY);
        day_list.add(Calendar.TUESDAY);
        day_list.add(Calendar.WEDNESDAY);
        day_list.add(Calendar.THURSDAY);
        day_list.add(Calendar.FRIDAY);
        day_list.add(Calendar.SATURDAY);
        day_list.add(Calendar.SUNDAY);
        //create custom adapter sleeplistadapter
        final SleepListAdapter adapter = new SleepListAdapter(this,day_list) ;
        //set tup listview
        ListView listView = (ListView) findViewById(R.id.list) ;
        listView.setAdapter(adapter);

        // set up switch
        switch_sleeping = (Switch) findViewById(R.id.switch_sleeping) ;
        /*
        NOTICE: Onclicklistener will be seperated from onswitchcheckedlistener

        This is because onswitchchecked will be called each time onresume is called. We want to retain
        state change (UI) within the application, while not re-calling heavy load operations such as
        - refreshing the whole list
        - creating the dialog box
        - update database

         */

        //set up onclicklistener
        switch_sleeping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch_sleeping.isChecked()){
                    //check the switch
                    switch_sleeping.setChecked(true);
                    // insert into db recording sleeptime
                    dbHelper = new DBHelper(SmartSleepActivity.this) ;
                    //indicate user is asleep.
                    dbHelper.InsertSleepData() ;
                    //pop up notification for easy deactivation
                    NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(SmartSleepActivity.this)
                            .setContentTitle("Are You Awake")
                            .setContentText("Press to open application")
                            .setColor(Color.GREEN)
                            .setSmallIcon(R.drawable.main_app_icon)
                            .setAutoCancel(true);

                    Intent intent = new Intent(SmartSleepActivity.this,SmartSleepActivity.class);
                    intent.putExtra("source","notification");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pi = PendingIntent.getActivity(SmartSleepActivity.this,0,intent,0);
                    builder.setContentIntent(pi);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build()); //id should be unique within this application.

                    //pop up dialogbox if user wants to turn off light
                    new AlertDialog.Builder(SmartSleepActivity.this)
                            .setTitle("Turn Off Lights")
                            .setMessage("Do you also want to turn off your room lights?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    //turn off light
                                    DBHelper dbHelper = new DBHelper(getApplicationContext()) ;
                                    dbHelper.Log(SmartApp1Activity.TAG,"Turning off Lights") ;
                                    Intent i = new Intent(getApplicationContext(),SmartApp1Activity.class);
                                    i.putExtra("AutomatedLight",0) ;
                                    finish();
                                    startActivity(i);

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // return to original page
                                }
                            })
                            .setIcon(R.drawable.light_bulb_off)
                            .show();

                }else{
                    //check the switch
                    switch_sleeping.setChecked(false);
                    dbHelper = new DBHelper(SmartSleepActivity.this) ;
                    //indicate user is asleep.
                    dbHelper.InsertWakeupData() ;
                    adapter.notifyDataSetChanged();
                    //pop up dialogbox if user wants to turn on light
                    new AlertDialog.Builder(SmartSleepActivity.this)
                            .setTitle("Turn On Lights")
                            .setMessage("Do you also want to turn on your room lights?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {



                                    //turn off light
                                    DBHelper dbHelper = new DBHelper(getApplicationContext()) ;
                                    dbHelper.Log(SmartApp1Activity.TAG,"Turning on Lights") ;


                                    Intent i = new Intent(getApplicationContext(),SmartApp1Activity.class);
                                    i.putExtra("AutomatedLight",1) ;

                                    startActivity(i);

                                    dialog.dismiss();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // return to original page
                                }
                            })
                            .setIcon(R.drawable.light_bulb_on)
                            .show();

                }
            }
        });
        //set up onswitchchecked listener
        switch_sleeping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG," Onchecked Fired") ;

                if(isChecked){
                    Log.d(TAG," Is Checked true") ;

                    //dim background
                    linearLayout = (LinearLayout) findViewById(R.id.li) ;

                    linearLayout.setAlpha(0.3f);
                    //change text
                    TextView textView = (TextView)findViewById(R.id.status) ;
                    textView.setText(R.string.currently_awake);
                }else{
                   //means user is awake

                    //dim background
                    linearLayout = (LinearLayout) findViewById(R.id.li) ;
                    linearLayout.setAlpha(1);
                    //change text
                    TextView textView = (TextView)findViewById(R.id.status) ;
                    textView.setText(R.string.currently_sleeping);



                }
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        bundle.putBoolean("SwitchState", switch_sleeping.isChecked());
        //bundle.putFloat("LLState", linearLayout.getAlpha());
    }

    @Override
    public void onResume() {
        super.onResume();
        //on application resume, retain the application state before it is paused.
        switch_sleeping.setChecked(bundle.getBoolean("SwitchState",false));
        Intent intent = getIntent();
        String source = intent.getStringExtra("source");
        Log.d(TAG,"source is from "+ source);
        if(("notification").equals(source)){
            Log.d(TAG,"true");
            switch_sleeping.setChecked(true);
            switch_sleeping.performClick();
        }

    }

}
