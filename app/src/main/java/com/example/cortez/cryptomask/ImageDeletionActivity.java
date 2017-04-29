package com.example.cortez.cryptomask;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ImageDeletionActivity extends AppCompatActivity {
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_deletion);
        db = new DBHandler(this, null, null, 1);

        Cursor cursor = db.getAllEncodedImageTableRows();
        startManagingCursor(cursor);

        String[] fieldnames = new String[]{
                db.ColumnId, db.ColumnEncodeImage, db.ColumnDate
        };
        int[] toViewIds = new int[]{
                R.id.encodedimagesIds, R.id.maskedimagecolumn, R.id.encodedimagedate
        };

        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
                ImageDeletionActivity.this, R.layout.activity_image_deletion_listview_format, cursor, fieldnames, toViewIds);
        ListView rowtoDelete = (ListView) findViewById(R.id.theimagelisttodeletefrom);
        rowtoDelete.setAdapter(myCursorAdapter);

        //when button is clicked we attempt to get encoded image by ID and delete from database

        final Button startDelition = (Button) findViewById(R.id.searcImagetoDeleteButt);
        final Button mainMenu = (Button) findViewById(R.id.returntomfromimagedeletion);

        final AlertDialog.Builder warningBox = new AlertDialog.Builder(ImageDeletionActivity.this);
        warningBox.setMessage("Are You Sure You Want to Delete the row?");
        warningBox.setCancelable(true);

        startDelition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                warningBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent i = new Intent(ImageDeletionActivity.this, ImageDeletionActivity.class);
                                final EditText UserInput = (EditText) findViewById(R.id.imagerowtodelete);
                                String userInput = UserInput.getText().toString();
                                if (!userInput.equals("")) {
                                    int userSelection = Integer.parseInt(UserInput.getText().toString());
                                    if (userSelection > 0) {
                                        if (db.checkEncodedImageExists(userSelection) == true) {
                                            db.deleteEncodedImage(userSelection);
                                            Toast.makeText(ImageDeletionActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                            startActivity(i);
                                        } else
                                            Toast.makeText(ImageDeletionActivity.this, "Id does not exist", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(ImageDeletionActivity.this, "You did not select by Id", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(ImageDeletionActivity.this, "Please enter Id", Toast.LENGTH_SHORT).show();
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

        mainMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ImageDeletionActivity.this, HomeActivity.class);
                startActivity(i);
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
