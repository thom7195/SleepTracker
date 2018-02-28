package com.example.thomson.hci.Activities;
/*

    This acitvity serves as an authentication gateway for the user
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thomson.hci.DBHelper;
import com.example.thomson.hci.R;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username , et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //set views
        et_username = (EditText)findViewById(R.id.et_username) ;
        et_password = (EditText)findViewById(R.id.et_password) ;

        //initialize button
        Button button_login = (Button)findViewById(R.id.button_login) ;
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString() ;
                String password = et_password.getText().toString() ;
                DBHelper dbHelper = new DBHelper(LoginActivity.this) ;
                if(dbHelper.authenciateUser(username,password)){
                    //user succesfully authenciated
                    Intent i = new Intent(LoginActivity.this,MainActivity.class) ;
                    startActivity(i);
                }else{
                    //user is not authenciated
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
