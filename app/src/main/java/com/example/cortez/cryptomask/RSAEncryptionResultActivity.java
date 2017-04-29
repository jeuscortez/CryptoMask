package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

public class RSAEncryptionResultActivity extends AppCompatActivity {
    DBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa_encryption_result);

        final TextView userText = (TextView) findViewById(R.id.RSAEncryptionResult);
        final Button returnToMain = (Button) findViewById(R.id.returntoMainfromRSAencryptionresult);
        final Button EMAILEncryption = (Button) findViewById(R.id.EmailEncryptionBttn);
        final EditText RecepientForEmailEncryption = (EditText) findViewById(R.id.EmailofRecepientsample);
        final EditText SubjectForEmailEncryption = (EditText) findViewById(R.id.SampleSubjectForEmailInEncryptionResult);

        db = new DBHandler(this, null, null, 1);

        Bundle UserData = getIntent().getExtras();
        if (UserData == null) {
            return;
        }
        String InputedText = UserData.getString("Plaintext");
        String keyN = UserData.getString("KeyN"); //these keys are not used since we do not have others' public keys to test
        String keyE = UserData.getString("KeyE"); // will use these two keys after deploying app

        BigInteger[] keys = db.getRSAKeys();
        BigInteger N = keys[0];
        BigInteger E = keys[1];
        BigInteger keyD = keys[2];

        RSA rsa = new RSA();
        final String encryption = rsa.encryption(InputedText,E,N);

        String encryptionInNumberForm = rsa.encryptionNumberform(InputedText,E,N);

        //decryption will be removed when putting out App
        //***This is just for testing purposes
        String decryption = rsa.decryption(encryption,keyD,N);

        userText.setText("Encryption In Number Form: " + encryptionInNumberForm + "\n\n" +"Encrypted base64 form: " + encryption +
                "\n" +"decrypted form: " + decryption); //will remove the decryption print out after presenting

        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RSAEncryptionResultActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

        EMAILEncryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String recepientEmail = RecepientForEmailEncryption.getText().toString();
                String SubjectforEmail = SubjectForEmailEncryption.getText().toString();

                if(!recepientEmail.equals("") && !SubjectforEmail.equals("")) {

                    Intent email = new Intent(Intent.ACTION_SEND);

                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{recepientEmail});
                    email.putExtra(Intent.EXTRA_SUBJECT, SubjectforEmail);
                    email.putExtra(Intent.EXTRA_TEXT, encryption);

                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Send Email Through Mail App"));
                }else
                    Toast.makeText(RSAEncryptionResultActivity.this,"One of the required fields for email is empty, please fill in", Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), RSAEncryptionSetUpActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
