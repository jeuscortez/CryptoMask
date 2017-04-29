package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;

public class RSADecryptionSetUpActivity extends AppCompatActivity {
    DBHandler db;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa_decryption_set_up);

        db = new DBHandler(this, null, null, 1);

        final Button DecryptRSAButton = (Button) findViewById(R.id.DecryptRSABttn);
        final EditText UserDecryptionInput = (EditText) findViewById(R.id.UserInputForRSADecryption);

        DecryptRSAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String encryption = UserDecryptionInput.getText().toString(); //this will come from the users input
                if(!encryption.equals("")){
            //create an intent to send encryption to next screen
                Intent i = new Intent(RSADecryptionSetUpActivity.this,RSADecryptionResultActivity.class);
                BigInteger[] keys = db.getRSAKeys();
                BigInteger keyN = keys[0];
                BigInteger keyE = keys[1];
                BigInteger keyD = keys[2];

                RSA rsa = new RSA();
                    String decryption = "";
                //final String encryption = rsa.encryption(InputedText,keyE,keyN);
                try {
                    decryption = rsa.decryption(encryption, keyD, keyN);
                } catch (Exception e) {
                    Toast.makeText(RSADecryptionSetUpActivity.this,"Bad input",Toast.LENGTH_LONG).show();
                    return;
                }
                    i.putExtra("Decryption",decryption);
                    db.close();
                startActivity(i);
                //send decryption result to next activity to display
                }else
                    Toast.makeText(RSADecryptionSetUpActivity.this, "The input fields is empty, Please enter encryption", Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(),RSAOptionsActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
