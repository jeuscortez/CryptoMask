package com.example.cortez.cryptomask;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RSAKeysActivity extends AppCompatActivity {
    DBHandler db;
    RSA rsa = new RSA();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa_keys);
        db = new DBHandler(this, null, null, 1);

        final Button GenrateRSAKeysButton = (Button) findViewById(R.id.generatersakeys);
        final Button GetRSAPublicKeys = (Button) findViewById(R.id.showpublickeys);
        final Button REGenerateNewRSAKeysButton = (Button) findViewById(R.id.recreatenewrsakeys);

        if(db.checkRSAKEYSTableisEmpty()) {
            GenrateRSAKeysButton.setEnabled(true);
            GetRSAPublicKeys.setEnabled(false);
            REGenerateNewRSAKeysButton.setEnabled(false);
        }else {
            GenrateRSAKeysButton.setEnabled(false);
            GetRSAPublicKeys.setEnabled(true);
            REGenerateNewRSAKeysButton.setEnabled(true);
        }


        GenrateRSAKeysButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View w){
                //do stuff here
                boolean verification = generateandStoreKeys();
                if(verification==false){
                    Toast.makeText(RSAKeysActivity.this,"Please Hit Button Again",Toast.LENGTH_LONG).show();
                }
                else
                Toast.makeText(RSAKeysActivity.this,"Keys Were Successfully Generated!",Toast.LENGTH_LONG).show();
                Intent a = new Intent(RSAKeysActivity.this,RSAKeysActivity.class);
                startActivity(a);
            }
        });

        GetRSAPublicKeys.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View x) {
                //sending the public keys to next Activty to display
                Intent i = new Intent(RSAKeysActivity.this,RSAShowPublicKeysActivity.class);
                Bundle extras = new Bundle();
                String[] keys = getRSAPublicKeys();

                String keyN = keys[0];
                String keyE = keys[1];

                extras.putString("KeyN",keyN); //will be sending the public keys to the next activity to display
                extras.putString("KeyE",keyE);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        final AlertDialog.Builder warningBox = new AlertDialog.Builder(RSAKeysActivity.this);
        warningBox.setMessage("Are You Sure You Want to Create New Keys? Encryptions with old Keys will not be able to be decrypted.");
        warningBox.setCancelable(true);

        REGenerateNewRSAKeysButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View y) {

                warningBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ReCreateKeys();
                                Toast.makeText(RSAKeysActivity.this,"New Keys were Successfully Created",Toast.LENGTH_LONG).show();
                            }
                        }
                );
                warningBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );
                AlertDialog alert = warningBox.create();
                alert.show();
            }
        });
    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), RSAOptionsActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }

    public boolean generateandStoreKeys(){

        BigInteger[] RSAKeys = rsa.generateKeys();

        if(RSAKeys[0].equals(BigInteger.ZERO)){
            return false;
        }else {
            BigInteger KeyN = RSAKeys[0];
            BigInteger KeyE = RSAKeys[1];
            BigInteger KeyD = RSAKeys[2];

            String date = generateDate();

            db.addRSAKEYS(KeyN, KeyE, KeyD, date);
            //db.close();
            return true;
        }
    }

    public String generateDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        String properDate = dateformat.format(c.getTime());
        return properDate;

    }

    public String[] getRSAPublicKeys(){
        BigInteger[] publicKeys = db.getRSAPublicKeys();
        String KeyN = publicKeys[0].toString();
        String KeyE = publicKeys[1].toString();

        String[] keys = {KeyN,KeyE};
        return keys;
    }

    public void ReCreateKeys(){
        BigInteger RSAKeys[] = rsa.generateKeys();

        BigInteger KeyN = RSAKeys[0];
        BigInteger KeyE = RSAKeys[1];
        BigInteger KeyD = RSAKeys[2];

        String date = generateDate();
        db.UpdateRSAKeys(KeyN,KeyE,KeyD,date);
        //db.close();
    }
}
