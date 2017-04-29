package com.example.cortez.cryptomask;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import static android.R.id.list;

public class TextDecryptionActivity extends AppCompatActivity {
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_decryption);
        db = new DBHandler(this, null, null, 1);

        Cursor cursor = db.getAllRows();
        startManagingCursor(cursor);

        String[] fieldnames = new String[]{
                db.ColumnId, db.ColumnCipherText, db.ColumnDate
        };
        int[] toViewIds = new int[]{
                R.id.encryptIds, R.id.encryptioncolumn, R.id.encryptdate
        };

        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
                TextDecryptionActivity.this, R.layout.activity_text_decryption_entry, cursor, fieldnames, toViewIds);
        ListView rowtoDecrypt = (ListView) findViewById(R.id.thelistlist);
        rowtoDecrypt.setAdapter(myCursorAdapter);
//when button is clicked we attempt to get ctext from database and pass to next activity to decrypt

        final Button startDecryption = (Button) findViewById(R.id.searchButt);
        startDecryption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(TextDecryptionActivity.this, TextDecryptionResultActivity.class);
                final EditText UserInput = (EditText) findViewById(R.id.rowtoDecrypt);
                String userIn = UserInput.getText().toString();
                if (!userIn.equals("")) {
                    int userSelection = Integer.parseInt(UserInput.getText().toString());
                    if (userSelection > 0) {
                        String[] selectedCtext = db.decryptCipher(userSelection);
                        if(!selectedCtext[0].equals("")){
                            i.putExtra("TextInputed", selectedCtext[0]);
                            i.putExtra("Key", selectedCtext[1]);
                            startActivity(i);
                        } else
                            Toast.makeText(TextDecryptionActivity.this, "It does not exist, check that the Id is correct", Toast.LENGTH_LONG).show();
                        return;
                    } else
                        Toast.makeText(TextDecryptionActivity.this, "You did not select by Id", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(TextDecryptionActivity.this, "Please enter Id", Toast.LENGTH_SHORT).show();
                ;
            }
        });

    }

    //added to test inorder to fix crash on back press from result page!!!!!!!!!!!!!!!!!!!!!!!!!
    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), TextOptionsActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
