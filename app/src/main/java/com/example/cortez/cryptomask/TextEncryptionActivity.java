package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TextEncryptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_encryption);

        final Button StartEncryption = (Button) findViewById(R.id.StartTextEncryption);
        StartEncryption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(TextEncryptionActivity.this, EncryptionResultActivity.class);
                final EditText UsersInput = (EditText) findViewById(R.id.UserInput);
                String UserText = UsersInput.getText().toString();
                if (!UserText.equals("")) {
                    i.putExtra("TextInputed", UserText);
                    startActivity(i);
                } else
                    Toast.makeText(TextEncryptionActivity.this, "You did not enter a text", Toast.LENGTH_SHORT).show();
                return;
            }

        });
    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), TextOptionsActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
