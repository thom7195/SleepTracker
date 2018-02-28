package com.example.thomson.hci.Activities;
/*
This activity serves to give information to the user on the sleep tracker function

 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;

import com.example.thomson.hci.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //initialzie button
        Button button_close = (Button) findViewById(R.id.button_close) ;

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the help page
                finish();
            }
        });
    }
}
