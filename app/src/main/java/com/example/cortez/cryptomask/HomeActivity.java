package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.support.v4.content.ContextCompat.startActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//this class will be the one where users picks the type of activities
        final Button TextOption = (Button) findViewById(R.id.TextFunctions);
        TextOption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, TextOptionsActivity.class);
                startActivity(i);
            }
        } );

//this button will take us to the image activities
        final Button ImageOption = (Button) findViewById(R.id.ImageFunctions);
        ImageOption.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent j = new Intent(HomeActivity.this,ImageOptionsActivity.class);
                startActivity(j);
            }
        });

        //this button will take us to the messaging functions
        final Button SecretMessageFunction = (Button) findViewById(R.id.MessagingFunctions);
        SecretMessageFunction.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent k = new Intent(HomeActivity.this,SecretMessageOptionsActivity.class);
                startActivity(k);
            }
        });
        
//this button will take us to the Change Password activity
        final Button ChangePasswordOption = (Button) findViewById(R.id.UpdatePasswordFunctions);
        ChangePasswordOption.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                Intent l = new Intent(HomeActivity.this,ChangePasswordActivity.class);
                startActivity(l);
            }
        });

    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
