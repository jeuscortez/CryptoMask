package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecretMessageOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_message_options);

        //will create two buttons here for next activities
        final Button SecretMessageTextButton = (Button) findViewById(R.id.SecretMessageText);
        SecretMessageTextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(SecretMessageOptionsActivity.this, RSAOptionsActivity.class);
                startActivity(i);
            }
        } );

        final Button SecretMessageImageButton = (Button) findViewById(R.id.SecretMessageImage);
        SecretMessageImageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View w){
                Intent j = new Intent(SecretMessageOptionsActivity.this,SteganographyOptionsActivity.class);
                startActivity(j);
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
