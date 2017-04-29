package com.example.cortez.cryptomask;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChangePasswordActivity extends AppCompatActivity {
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        db = new DBHandler(this, null, null, 1);

        final Button UpdatePassword = (Button) findViewById(R.id.UpdatepasswordBttn);
        final EditText passwordInputed = (EditText) findViewById(R.id.newPassword);
        final EditText passwordconfirmInputed = (EditText) findViewById(R.id.ConfirmingnewPassword);
        final Intent i = new Intent(ChangePasswordActivity.this, HomeActivity.class);

        final AlertDialog.Builder warningBox = new AlertDialog.Builder(ChangePasswordActivity.this);
        warningBox.setMessage("Are You Sure You Want to Change your Password?");
        warningBox.setCancelable(true);

        UpdatePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                warningBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                String password = passwordInputed.getText().toString();
                                String passwordConfirm = passwordconfirmInputed.getText().toString();

                                if(!password.equals("")&&!passwordConfirm.equals("")){

                                    if (!(password.equals(passwordConfirm))){
                                        Toast.makeText(ChangePasswordActivity.this,"Password does not match",Toast.LENGTH_LONG).show();
                                        return;
                                    }else {
                                        //if success it will continue
                                        //adding User Info re-encrypted by Affine Cipher to database
                                        String userName = getUserNamefromDB();//retrieving the encrypted username in db
                                        securelyInsertUserInfo(userName, password);//storing updated info to the user table in database
                                        Toast.makeText(ChangePasswordActivity.this, userName + " Account Password Has Been Updated Successfully", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(ChangePasswordActivity.this,"Empty Field(s)", Toast.LENGTH_LONG).show();
                                }
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

                //Intent i = new Intent(ChangePasswordActivity.this, HomeActivity.class);
                //startActivity(i);
                //finish();
            }
        });
    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }

    public String getUserNamefromDB(){
        AffineCipher affine = new AffineCipher();
        String oldEncryptedUserName[] = db.getUserNameFromTable();

        String encryptedUserName = oldEncryptedUserName[0];
        int Alpha = Integer.parseInt(oldEncryptedUserName[1].toString());
        int Beta = Integer.parseInt(oldEncryptedUserName[2].toString());

        String oldUsername = affine.decryption(encryptedUserName,Alpha,Beta);
        return oldUsername;
    }

    public String generateDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        String properDate = dateformat.format(c.getTime());
        return properDate;
    }

    public void securelyInsertUserInfo(String username, String password){
        AffineCipher affineCipher = new AffineCipher();
        int[] keys = affineCipher.keyGenerator();
        int alpha = keys[0];
        int beta = keys[1];
        String encryptedUsername = affineCipher.encryption(username,alpha,beta);
        String encryptedPassword = affineCipher.encryption(password,alpha,beta);
        //adding updated info to the database table
        db.updateUserPassword(encryptedUsername,encryptedPassword,generateDate(),alpha,beta);
        db.close();
    }
}
