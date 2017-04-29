package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EncryptionResultActivity extends AppCompatActivity {

    DBHandler mydbHandler;
    TextView DatabaseSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption_result);

        Bundle UserData = getIntent().getExtras();
        if (UserData == null) {
            return;
        }
        String InputedText = UserData.getString("TextInputed");
        String Key = VigenereCipher.createKey();
        String cipherText = VigenereCipher.encrypt(InputedText, Key);
        final TextView userText = (TextView) findViewById(R.id.EncryptionResult);

        //printing the database
        mydbHandler = new DBHandler(this, null, null, 1);


        addButtonClicked(cipherText, generateDate(), Key);

        userText.setText(cipherText + "\n" + generateDate() + "\n" + Key);

        //DatabaseSample = (TextView) findViewById(R.id.DatabaseSample);
        //printDatabase();


        final Button ReturnToMain = (Button) findViewById(R.id.ConfirmButton);
        ReturnToMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(EncryptionResultActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), TextEncryptionActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }

    public String generateDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        String properDate = dateformat.format(c.getTime());
        return properDate;

    }

    public void addButtonClicked(String encryption, String date, String key) {
        String cipherText = encryption;
        mydbHandler.addEncryption(cipherText, date, key);
    }

    public void printDatabase() {
        String dbString = mydbHandler.databaseToString();
        DatabaseSample.setText(dbString);

    }
}
