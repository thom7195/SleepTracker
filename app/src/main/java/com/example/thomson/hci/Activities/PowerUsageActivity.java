package com.example.thomson.hci.Activities;
/*
    This activity will show the power consumption of each application on its page


 */
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.thomson.hci.DBHelper;
import com.example.thomson.hci.R;

public class PowerUsageActivity extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener{

    DBHelper dbHelper ;
    //TODo: The rate is hard coded, real implementation will involve database to store respective application's power usage rate
    private final int app1_pc_rate = 500 ;
    private final int app2_pc_rate = 500 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        //get the layout that corresponds to this layout
        getLayoutInflater().inflate(R.layout.activity_power_usage, contentFrameLayout);

        //initialize views
        TextView view_pc_app1 = (TextView) findViewById(R.id.view_pc_app1) ;

        //get uptime from db
        dbHelper = new DBHelper(this) ;
        long time = dbHelper.GetPowerConsumption(SmartApp1Activity.TAG) ;
        //calculate power consumption
        long power_consumption = (time/1000000000) * app1_pc_rate ;
        String power_consumption_string = Long.toString(power_consumption );
        //show power consumption of app1 on screen.
        view_pc_app1.setText(power_consumption_string);

    }




}
