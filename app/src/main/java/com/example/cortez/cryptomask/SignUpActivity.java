package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        db = new DBHandler(this, null, null, 1);

        final Button confirm = (Button) findViewById(R.id.createAccountBttn);
        final EditText usernameInputed = (EditText) findViewById(R.id.editTextUserName);
        final EditText passwordInputed = (EditText) findViewById(R.id.editTextPassword);
        final EditText passwordconfirmInputed = (EditText) findViewById(R.id.editTextConfirmPassword);
        final Intent i = new Intent(SignUpActivity.this, HomeActivity.class);

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String userName = usernameInputed.getText().toString();
                String password = passwordInputed.getText().toString();
                String passwordConfirm = passwordconfirmInputed.getText().toString();

                if(userName.equals("")||password.equals("")||passwordConfirm.equals("")){
                    Toast.makeText(SignUpActivity.this,"Field Vacant",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (!(password.equals(passwordConfirm))){
                    Toast.makeText(SignUpActivity.this,"Password does not match",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    //Toast.makeText(SignUpActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
                //adding User Info encrypted by Affine Cipher to database
                securelyInsertUserInfo(userName,password);
                Toast.makeText(SignUpActivity.this,"Account had been Created",Toast.LENGTH_LONG).show();
                Intent i = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(i);
                finish();
            }
        });
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
        //adding to the database table
        db.addUserInfo(encryptedUsername,encryptedPassword,generateDate(),alpha,beta);
    }

}
