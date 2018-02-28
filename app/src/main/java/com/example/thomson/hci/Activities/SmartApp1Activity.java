package com.example.thomson.hci.Activities;
/*
    This class handles the light controller function of the project


 */

import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher.ViewFactory;

import com.example.thomson.hci.DBHelper;
import com.example.thomson.hci.R;


public class SmartApp1Activity extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long start_time ,end_time ;
    private DBHelper dbHelper ;
    public static String TAG = "SmartApp1Activity";
    private ToggleButton toggle_status;
    private ImageSwitcher image_light_bulb;
    private static Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        //get the layout that corresponds to this layout
        getLayoutInflater().inflate(R.layout.activity_smart_app1, contentFrameLayout);


        //Start counting activity
        start_time = System.nanoTime() ;
        Log.d(TAG, "App1 Started at  " +start_time) ;
        //iniitialize imageswitcher
        image_light_bulb = (ImageSwitcher) findViewById(R.id.image_light_bulb) ;
        image_light_bulb.setFactory(new ViewFactory() {
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                return myView;
            }
        }) ;
        //set animations for turning on/off lights
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);//animation for fade in
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);//animation for fade out
        image_light_bulb.setInAnimation(in);
        image_light_bulb.setOutAnimation(out);


        //initialize the ontoggle function of togglebutton :
        toggle_status = (ToggleButton) findViewById(R.id.toggle_status) ;
          /*
        NOTICE: Onclicklistener will be seperated from onswitchcheckedlistener

        This is because onswitchchecked will be called each time onresume is called. We want to retain
        state change (UI) within the application, while not re-calling heavy load operations such as
        - sending command to the smart device ( in real application )
        - Updating the logs and databases




         */
        toggle_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggle_status.isChecked()){
                    //inform user of the current status of the application == On
                    image_light_bulb.setImageResource(R.drawable.light_bulb_on);
                    Toast.makeText(SmartApp1Activity.this, getResources().getString(R.string.light_on), Toast.LENGTH_SHORT).show();
                    DBHelper dbHelper = new DBHelper(SmartApp1Activity.this) ;
                    dbHelper.Log(TAG,"Turning on Lights") ;
                    toggle_status.setChecked(true);


                }else{
                    //inform user of the current status of the application == Off
                    image_light_bulb.setImageResource(R.drawable.light_bulb_off);
                    Toast.makeText(SmartApp1Activity.this,getResources().getString(R.string.light_off), Toast.LENGTH_SHORT).show();

                    DBHelper dbHelper = new DBHelper(SmartApp1Activity.this) ;
                    dbHelper.Log(TAG,"Turning off Lights") ;
                    toggle_status.setChecked(false);


                }
            }
        });
        toggle_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(toggle_status.isChecked()){
                    //inform user of the current status of the application == On
                    image_light_bulb.setImageResource(R.drawable.light_bulb_on);



                }else{
                    //inform user of the current status of the application == Off
                    image_light_bulb.setImageResource(R.drawable.light_bulb_off);


                }
            }
        });
    }
    @Override
    public  void onDestroy(){
        Log.d(TAG,"Ondestroy Called, Recording time ") ;
        end_time = System.nanoTime() ;
        long uptime = end_time - start_time ;
        dbHelper = new DBHelper(this);
        //Insert the Uptime to the database
        if(dbHelper.RecordTime(TAG,uptime)){
         Log.d(TAG,"Record Succesful");
        }else{

            Log.d(TAG,"Record Failed");
    }
        super.onDestroy();
    }

    @Override
    public void onPause(){

        Log.d(TAG,"OnPause Called, Recording time ") ;
        end_time = System.nanoTime() ;
        long uptime = end_time - start_time ;
        dbHelper = new DBHelper(this);
        //Insert the Uptime to the database
        if(dbHelper.RecordTime(TAG,uptime)){
            Log.d(TAG,"Record Succesful");
        }else{

            Log.d(TAG,"Record Failed");
        }
        //save state
        bundle.putBoolean("ToggleState", toggle_status.isChecked());

        super.onPause();
    }

    @Override 
    public void onRestart(){
        //upon application restart, start logging time again
        Log.d(TAG,"Application Restarted") ;
        start_time = System.nanoTime() ;
        super.onRestart();

    }
    @Override
    public void onResume() {
        super.onResume();
        toggle_status.setChecked(bundle.getBoolean("ToggleState",false));
        //retrieve bundle, to know if intent contain commands to automatically turn on/off the light
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            int automated_light= extras.getInt("AutomatedLight",999) ;
            Log.d(TAG,"automated light id = " + automated_light) ;
            if(automated_light==0){

                toggle_status.setChecked(true);
                toggle_status.performClick();

            }else if(automated_light==1){

                toggle_status.setChecked(false);
                toggle_status.performClick();
            }
            //start logging time to measure power consumption
            Log.d(TAG,"Application Resumed") ;
            start_time = System.nanoTime() ;


        }


    }

}
