package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RSAShowPublicKeysActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa_show_public_keys);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        String keyN = extras.getString("KeyN");
        String keyE = extras.getString("KeyE");

        //will display the keys
        final TextView EncryptionKeyN = (TextView) findViewById(R.id.PKResultKeyN);
        final TextView EncryptionKeyE = (TextView) findViewById(R.id.PKResultKeyE);
        final EditText Recipient = (EditText) findViewById(R.id.EmailofRecepient);
        final EditText Subject = (EditText) findViewById(R.id.SubjectForEmail);
        final Button ReturnToMain = (Button) findViewById(R.id.returnFromPKeys);
        final Button SendEmail = (Button) findViewById(R.id.EmailButton);

        final String KeyNforEmail = "Public Key N: "+ keyN;
        EncryptionKeyN.setText(KeyNforEmail);
        final String KeyEforEmail = "Public Key E: "+ keyE;
        EncryptionKeyE.setText(KeyEforEmail);


        ReturnToMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(RSAShowPublicKeysActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        //must set send button to make email intent
        SendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String recepientEmail = Recipient.getText().toString();
                String SubjectforEmail = Subject.getText().toString();

                if(!recepientEmail.equals("")&&!SubjectforEmail.equals("")) {

                    Intent email = new Intent(Intent.ACTION_SEND);

                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{recepientEmail});
                    email.putExtra(Intent.EXTRA_SUBJECT, SubjectforEmail);
                    email.putExtra(Intent.EXTRA_TEXT, KeyNforEmail + "\n" + KeyEforEmail);

                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Send Email Through Mail App"));
                }else Toast.makeText(RSAShowPublicKeysActivity.this,"One of the required fields for email is empty",Toast.LENGTH_LONG).show();
                return;
            }
        });

    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(),RSAKeysActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
