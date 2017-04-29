package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SteganographyOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steganography_options);

        final Button encode = (Button) findViewById(R.id.SteganographyEncodeOption);
        final Button decode = (Button) findViewById(R.id.SteganographyDecodeption);

        encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SteganographyOptionsActivity.this,SteganographyEncryptionActivity.class);
                startActivity(i);
            }
        });

        decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(SteganographyOptionsActivity.this,SteganographyDecryptionActivity.class);
                startActivity(j);
            }
        });
    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), SecretMessageOptionsActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
