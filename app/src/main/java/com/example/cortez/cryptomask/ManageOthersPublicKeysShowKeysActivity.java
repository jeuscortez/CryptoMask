package com.example.cortez.cryptomask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ManageOthersPublicKeysShowKeysActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_others_public_keys_show_keys);

        TextView PersonsKeyN = (TextView) findViewById(R.id.ManageOthersPersonsKeyNResult);
        TextView PersonsKeyE = (TextView) findViewById(R.id.ManageOthersPersonsKeyEResult);
        Button returnTomain = (Button) findViewById(R.id.returntoMainfromManageShowOthersKeys);

        Bundle UserData = getIntent().getExtras();
        if (UserData == null) {
            return;
        }

        String KeyN = UserData.getString("KeyN");
        String KeyE = UserData.getString("KeyE");

        PersonsKeyN.setText(KeyN);
        PersonsKeyE.setText(KeyE);

        returnTomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageOthersPublicKeysShowKeysActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), ManageOthersPublicKeysActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
