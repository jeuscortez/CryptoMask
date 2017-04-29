package com.example.cortez.cryptomask;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TextDecryptionResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_decryption_result);

        Bundle UserData = getIntent().getExtras();
        if (UserData == null) {
            return;
        }
        String cText = UserData.getString("TextInputed");
        String Key = UserData.getString("Key");
        final TextView userText = (TextView) findViewById(R.id.decryptionbox);
        final Button ReturnToMain = (Button) findViewById(R.id.ReturntoMain);

            String pText = VigenereCipher.decrypt(cText, Key);
            userText.setText(pText);

        ReturnToMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(TextDecryptionResultActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
