package com.example.thomson.hci.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.example.thomson.hci.LogFragment;
import com.example.thomson.hci.R;

import java.util.Locale;
/*
This activity serves as a template for all the other activities. Other activies will inherit this class,
and also the functions of the navigation bar and toolbar


 */
public class BasicActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LogFragment.OnFragmentInteractionListener {

    private String TAG =" BasicActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //initialize drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_change_language) {
            //Change language here
            //check if language is in english
            String current_locale = String.valueOf(getResources().getConfiguration().locale) ;
            Log.d("Changing Language ", " Current Language " + current_locale);

            if(current_locale.equals("en")||current_locale.equals("en_US") )
            {
                Log.d(TAG, "Converting to Indonesian") ;
                //change language to Indonesian
                Locale locale = new Locale("in");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                //change device locale to indonesian
                getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
                Log.d("Changing Language", " Language  Changed To:" + String.valueOf(getResources().getConfiguration().locale));
                Toast.makeText(getApplicationContext(),"Bahasa DiRubah", Toast.LENGTH_SHORT).show(); ;
                //refresh page
                finish();
                startActivity(getIntent());
            }
            else{
                Log.d(TAG, "Converting to English") ;
                //change back to English
                Locale locale = Locale.ENGLISH;
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                //change device locale to English
                getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
                Log.d("Changing Language", " Language Changed To :" + String.valueOf(getResources().getConfiguration().locale));
                Toast.makeText(getApplicationContext(),"Language Changed", Toast.LENGTH_SHORT).show(); ;
                finish();
                //refresh page
                startActivity(getIntent());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_smartapp1) {
            Log.d(TAG, "SmartApp1 Selected");
            Intent i = new Intent(this,SmartApp1Activity.class);
            startActivity(i);

        } else if (id == R.id.nav_smartliving) {

            Intent i = new Intent (this,SmartSleepActivity.class) ;
            startActivity(i);

        } else if (id == R.id.nav_power_consumption) {
            Log.d(TAG," PowerUsageActivity Launched ");
            Intent i = new Intent(this,PowerUsageActivity.class) ;
            startActivity(i);

        }else if(id ==R.id.nav_home){

            Log.d(TAG," Home Launched ");
            Intent i = new Intent(this,MainActivity.class) ;
            startActivity(i);
        }
        else if(id ==R.id.nav_push_notif){



        }
        else if(id ==R.id.nav_logout){
            Log.d(TAG," Logout");
            //pop dialog to double confirm if user is logging out
            new AlertDialog.Builder(BasicActivity.this)
                    .setTitle(getResources().getString(R.string.logout))
                    .setMessage(getResources().getString(R.string.logout_confirmation))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            //return to the login page
                            Intent i = new Intent(BasicActivity.this,LoginActivity.class) ;
                            startActivity(i);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // return to original page
                        }
                    })
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
