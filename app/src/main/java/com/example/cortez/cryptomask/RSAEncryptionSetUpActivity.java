package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RSAEncryptionSetUpActivity extends AppCompatActivity {
    DBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa_encryption_setup);

        db = new DBHandler(this, null, null, 1);

        final Button SaveKeysandStartRSAEncryptionButton = (Button) findViewById(R.id.StartRSATextEncryptionBttn);
        final Button StartSearchandEncryptionButton = (Button) findViewById(R.id.getOthersRSAPKSTextandEncryptBttn);
        final Button StartEncryptionWithMyKeys = (Button) findViewById(R.id.UseMyKeysButton);
        final EditText UserInputNametoSaveKeys = (EditText) findViewById(R.id.UserInputNametoSaveKeysWith);
        final EditText UserInputKeyN = (EditText) findViewById(R.id.UserInputKeyN);
        final EditText UserInputKeyE = (EditText) findViewById(R.id.UserInputKeyE);
        final EditText UserInputToSearchKeysFor = (EditText) findViewById(R.id.UserInputToSearchDBforPKs);
        final Intent i = new Intent(RSAEncryptionSetUpActivity.this,RSAEncryptionResultActivity.class);
        final EditText UsersInput = (EditText) findViewById(R.id.UserInputForRSAEncryption);

        if(db.checkOTHERSPublicKeysTableisEmpty()){
            StartSearchandEncryptionButton.setEnabled(false);
        }else
        {
            SaveKeysandStartRSAEncryptionButton.setEnabled(true);
        }

        SaveKeysandStartRSAEncryptionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String UserText = UsersInput.getText().toString();
            String NametoLabelPKS = UserInputNametoSaveKeys.getText().toString();
            String KeyN = UserInputKeyN.getText().toString();
            String KeyE = UserInputKeyE.getText().toString();

            if (!UserText.equals("")&&!NametoLabelPKS.equals("")&&!KeyN.equals("")&&!KeyE.equals("")) {

                addtoDB(NametoLabelPKS,KeyN,KeyE);
                i.putExtra("Plaintext", UserText);
                i.putExtra("KeyN",KeyN);
                i.putExtra("KeyE",KeyE);
                startActivity(i);
            } else
                Toast.makeText(RSAEncryptionSetUpActivity.this, "One of the required fields is empty", Toast.LENGTH_LONG).show();
            return;
        }
    });
        StartSearchandEncryptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserText = UsersInput.getText().toString();
                String SearchKeysName = UserInputToSearchKeysFor.getText().toString();

                if (!UserText.equals("")&&!SearchKeysName.equals("")) {
                    if(db.CheckOTHERSKeyIsInDB(SearchKeysName)==false){
                        Toast.makeText(RSAEncryptionSetUpActivity.this, "Those Keys Do Not Exist", Toast.LENGTH_LONG).show();
                        return;
                    }else{

                    String[] keys = findKeysByName(SearchKeysName);

                    String keyN = keys[0];
                    String keyE = keys[1];

                    i.putExtra("Plaintext", UserText);
                    i.putExtra("KeyN",keyN);
                    i.putExtra("KeyE",keyE);
                    startActivity(i);
                    }
                } else
                    Toast.makeText(RSAEncryptionSetUpActivity.this, "One of the required fields is empty", Toast.LENGTH_LONG).show();
                return;
            }

        });

        StartEncryptionWithMyKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserText = UsersInput.getText().toString();
                if (!UserText.equals("")) {

                        String keys[] = getMyPublicKeys();
                        String keyN = keys[0];
                        String keyE = keys[1];

                        i.putExtra("Plaintext", UserText);
                        i.putExtra("KeyN",keyN);
                        i.putExtra("KeyE",keyE);
                        startActivity(i);
                } else
                    Toast.makeText(RSAEncryptionSetUpActivity.this, "The Input field is empty", Toast.LENGTH_LONG).show();
                return;
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

    public void addtoDB(String NametoLabelPKS,String KeyN, String KeyE){
        db.addOthersRSAPublicKEYS(NametoLabelPKS,KeyN,KeyE,generateDate());
    }

    public String[] findKeysByName(String KeysLabeName){
        BigInteger[] keys= db.getOTHERSPublicKeys(KeysLabeName);
        if(keys.equals(null)){
            return null;
        }
        else{
        String N = keys[0].toString();
        String E = keys[1].toString();
        String valut[] = {N,E};
        return valut;
        }
    }

    public String[] getMyPublicKeys(){
        BigInteger keys[] = db.getRSAPublicKeys();
        String KeyN = keys[0].toString();
        String KeyE = keys[1].toString();
        String[] vault = {KeyN,KeyE};
        return vault;
    }

    public String generateDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        String properDate = dateformat.format(c.getTime());
        return properDate;

    }
}
