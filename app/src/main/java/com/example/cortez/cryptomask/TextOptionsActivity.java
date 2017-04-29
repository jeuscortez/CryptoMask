package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.support.v4.content.ContextCompat.startActivity;

public class TextOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_options);

        final Button TextEncrypt = (Button) findViewById(R.id.TextEncrypter);
        final Button TextDecrypt = (Button) findViewById(R.id.TextDecrypter);
        final Button DeleteEncryption = (Button) findViewById(R.id.CTextDeletion);

        TextEncrypt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), TextEncryptionActivity.class);
                startActivity(i);
            }
        });

        TextDecrypt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {
                    Intent j = new Intent(w.getContext(), TextDecryptionActivity.class);
                startActivity(j);
                }
        });

        DeleteEncryption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View x) {
                Intent k = new Intent(x.getContext(), CipherTextDeletionActivity.class);
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
