package com.example.thomson.hci.Activities;
/*
    the main page of the whole system. this page will load the log fragment

 */

import android.app.Fragment;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.thomson.hci.Activities.BasicActivity;
import com.example.thomson.hci.LogFragment;
import com.example.thomson.hci.R;

import java.util.Locale;

public class MainActivity extends BasicActivity
        implements NavigationView.OnNavigationItemSelectedListener,LogFragment.OnFragmentInteractionListener {

    private String TAG =" MainActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        //get the layout that corresponds to this layout
        getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = new LogFragment() ;
        fm.beginTransaction().replace(R.id.content_frame,fragment).commit() ;


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
        // as you specify a
        // parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_language) {
            //Change language here
            //check if language is in english
           String current_locale = String.valueOf(getResources().getConfiguration().locale) ;
            Log.d("Changing Language ", " Current Language " + current_locale);

            if(current_locale.equals("en"))
            {
                Log.d(TAG, "Converting to Indonesian") ;
                //change language to Indonesian
                Locale locale = new Locale("in");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
                Log.d("Changing Language", " Language  Changed To:" + String.valueOf(getResources().getConfiguration().locale));
            }
            else{
                Log.d(TAG, "Converting to English") ;
                //change back to English
                Locale locale = Locale.ENGLISH;
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
                Log.d("Changing Language", " Language Changed To :" + String.valueOf(getResources().getConfiguration().locale));

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
