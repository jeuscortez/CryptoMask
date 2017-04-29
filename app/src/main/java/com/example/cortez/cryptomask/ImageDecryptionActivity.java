package com.example.cortez.cryptomask;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ImageDecryptionActivity extends AppCompatActivity {
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_decryption);

        db = new DBHandler(this, null, null, 1);

        Cursor cursor = db.getAllEncodedImageTableRows();
        startManagingCursor(cursor);

        String[] fieldnames = new String[]{
                db.ColumnId, db.ColumnEncodeImage, db.ColumnDate
        };
        int[] toViewIds = new int[]{
                R.id.encodeIds, R.id.encodedimagecolumn, R.id.encodedate
        };

        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
                ImageDecryptionActivity.this, R.layout.activity_image_decryption_entry, cursor, fieldnames, toViewIds);
        ListView rowtoDecrypt = (ListView) findViewById(R.id.theimagelist);
        rowtoDecrypt.setAdapter(myCursorAdapter);

        //when button is clicked we attempt to get ctext from database and pass to next activity to decrypt

        final Button startDecryption = (Button) findViewById(R.id.searchEncodedImageButt);
        startDecryption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ImageDecryptionActivity.this, ImageDecryptionResultActivity.class);
                final EditText UserInput = (EditText) findViewById(R.id.imagerowtoDecrypt);
                String userInput = UserInput.getText().toString();
                if (!userInput.equals("")) {
                    int userSelection = Integer.parseInt(UserInput.getText().toString());
                    if (userSelection > 0) {
                        String selectedEncodeImage = db.decodeImage(userSelection);
                        if(!selectedEncodeImage.equals("")){
                            i.putExtra("SelectedImageID", userSelection);
                            startActivity(i);
                        } else
                            Toast.makeText(ImageDecryptionActivity.this, "It does not exist, check that the Id is correct", Toast.LENGTH_SHORT).show();
                        return;
                    } else
                        Toast.makeText(ImageDecryptionActivity.this, "You did not select by Id", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ImageDecryptionActivity.this, "Please enter Id", Toast.LENGTH_SHORT).show();
                ;
            }
        });
    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), ImageOptionsActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
