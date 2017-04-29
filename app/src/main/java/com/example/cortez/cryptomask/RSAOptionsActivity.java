package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RSAOptionsActivity extends AppCompatActivity {
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsaoptions);

        db = new DBHandler(this, null, null, 1);

        final Button RSAKeysButton = (Button) findViewById(R.id.rsageneratekeys);
        final Button RSAEncryptionButton = (Button) findViewById(R.id.rsaencryptmessage);
        final Button RSADecryptionButton = (Button) findViewById(R.id.rsadecryptmessage);
        final Button RSAManageOthersPublicKeys = (Button) findViewById(R.id.rsamanageOthersPks);

        if(db.checkRSAKEYSTableisEmpty()) {
            RSAKeysButton.setEnabled(true);
            RSAEncryptionButton.setEnabled(false);
            RSADecryptionButton.setEnabled(false);
            RSAManageOthersPublicKeys.setEnabled(false);
            db.close();
        }else {
            RSAKeysButton.setEnabled(true);
            RSAEncryptionButton.setEnabled(true);
            RSADecryptionButton.setEnabled(true);
            db.close();
        }

        if(db.checkOTHERSPublicKeysTableisEmpty()){
            RSAManageOthersPublicKeys.setEnabled(false);
            db.close();
        }else{RSAManageOthersPublicKeys.setEnabled(true);
        }

        RSAKeysButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View w){
                Intent i = new Intent(RSAOptionsActivity.this,RSAKeysActivity.class);
                startActivity(i);
            }
        });

        RSAEncryptionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {
                Intent j = new Intent(RSAOptionsActivity.this,RSAEncryptionSetUpActivity.class);
                startActivity(j);
            }
        });

        RSADecryptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                Intent k = new Intent(RSAOptionsActivity.this,RSADecryptionSetUpActivity.class);
                startActivity(k);
            }
        });

        RSAManageOthersPublicKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(RSAOptionsActivity.this,ManageOthersPublicKeysActivity.class);
                startActivity(l);
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
