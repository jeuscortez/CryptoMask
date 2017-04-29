package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RSADecryptionResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa_decryption_result);
        final TextView RSADecryption = (TextView) findViewById(R.id.RSADecryptionResult);
        Button ReturnToMain = (Button) findViewById(R.id.returntoMainfromRSADecryptionresult);

        Bundle UserData = getIntent().getExtras();
        if (UserData == null) {
            return;
        }
        String DecryptedText = UserData.getString("Decryption");

        RSADecryption.setText(DecryptedText);

       ReturnToMain.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(RSADecryptionResultActivity.this,HomeActivity.class);
               startActivity(i);
           }
       });

    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), RSADecryptionSetUpActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
