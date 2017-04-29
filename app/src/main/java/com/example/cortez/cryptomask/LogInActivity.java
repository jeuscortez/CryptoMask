package com.example.cortez.cryptomask;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {
    DBHandler db;
    private String userChoosentask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        db = new DBHandler(this, null, null, 1);

        final Button signUp = (Button) findViewById(R.id.registerBttn);
        final Button login = (Button) findViewById(R.id.loginBttn);

        if (db.checkUserInfoTableisempty()==true){
            login.setEnabled(false);
        }
        else
        {
            signUp.setEnabled(false);
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                View v = LayoutInflater.from(LogInActivity.this).inflate(R.layout.activity_log_in_pop_up_screen,null);
                final EditText username = (EditText) v.findViewById(R.id.logusername);
                final EditText password = (EditText) v.findViewById(R.id.password);
                password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setSelection(password.getText().length());////////////
                final Intent i = new Intent(LogInActivity.this, HomeActivity.class);

                AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);

                builder.setView(v).setPositiveButton("Log In", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        String usernameInput = username.getText().toString();
                        String passwordInput = password.getText().toString();

                        String userInfo[] = getUserInfoToVerifyLogIn();
                        String username = userInfo[0];
                        String password = userInfo[1];

                        if(usernameInput.equals(username)&&passwordInput.equals(password)){
                            startActivity(i);
                        }
                        else if(usernameInput.equals("")||passwordInput.equals("")){
                            Toast.makeText(LogInActivity.this,"Empty Field",Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(LogInActivity.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel",null).setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    public String[] getUserInfoToVerifyLogIn(){
        //make db and affine cipher calls here to decrypt and compare
        String[] userInfo = db.decryptUserInfo();
        String cipherUsername = userInfo[0];
        String cipherpassword = userInfo[1];
        int alpha = Integer.parseInt(userInfo[2]);
        int beta = Integer.parseInt(userInfo[3]);

        AffineCipher affineCipher = new AffineCipher();
        String username = affineCipher.decryption(cipherUsername,alpha,beta);
        String password = affineCipher.decryption(cipherpassword,alpha,beta);

        String vault[] = {username,password};
        return vault;
    }



}
