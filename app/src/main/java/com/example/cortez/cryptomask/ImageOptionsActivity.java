package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ImageOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_options);

        final Button ImageEncrypt = (Button) findViewById(R.id.EncryptImage);
        ImageEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ImageOptionsActivity.this,ImageCapturingActivity.class);
                startActivity(i);
            }
        });
//need to add the Image decryption button here

        final Button ImageDecrypt = (Button) findViewById(R.id.DecryptImage);
        ImageDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                Intent j = new Intent(ImageOptionsActivity.this,ImageDecryptionActivity.class);
                startActivity(j);
            }
        });

        final Button ImageDelition = (Button) findViewById(R.id.DeleteImageEncryption);
        ImageDelition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                Intent k = new Intent(ImageOptionsActivity.this,ImageDeletionActivity.class);
                startActivity(k);
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
