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

public class ManageOthersPublicKeysActivity extends AppCompatActivity {
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_others_public_keys);

        db = new DBHandler(this, null, null, 1);

        Cursor cursor = db.getAllOTHERSPublicKeysRows();
        startManagingCursor(cursor);

        String[] fieldnames = new String[]{
                db.ColumnId, db.ColumnOthersPublicKeysName, db.ColumnDate
        };
        int[] toViewIds = new int[]{
                R.id.PublicKeysIds, R.id.IdentifierNameForPKScolumn, R.id.publickeysinsertiondate
        };

        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
                ManageOthersPublicKeysActivity.this, R.layout.activity_manage_others_public_keys_list_view, cursor, fieldnames, toViewIds);
        ListView rowtoDelete = (ListView) findViewById(R.id.thePublicKeyslisttodeletefrom); //name of the listview itself
        rowtoDelete.setAdapter(myCursorAdapter);


        final Button startDelition = (Button) findViewById(R.id.searchPublicKeystoDeleteButt);
        final Button mainMenu = (Button) findViewById(R.id.returnfromPKmanaging);
        final Button seeKeys = (Button) findViewById(R.id.SearchKeysToView);
        final EditText UserInput = (EditText) findViewById(R.id.pkrowtodelete);

        final AlertDialog.Builder warningBox = new AlertDialog.Builder(ManageOthersPublicKeysActivity.this);
        warningBox.setMessage("Are You Sure You Want to Delete the row");
        warningBox.setCancelable(true);

        startDelition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                warningBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent i = new Intent(ManageOthersPublicKeysActivity.this, ManageOthersPublicKeysActivity.class);

                                String userInput = UserInput.getText().toString();
                                if (!userInput.equals("")) {
                                    int userSelection = Integer.parseInt(UserInput.getText().toString());
                                    if (userSelection > 0) {
                                        if (db.checkOTHERSPublicKeysExist(userSelection) == true) {
                                            db.deleteOTHERSPublicKeysByID(userSelection);
                                            Toast.makeText(ManageOthersPublicKeysActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                            startActivity(i);
                                        } else
                                            Toast.makeText(ManageOthersPublicKeysActivity.this, "Id does not exist", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(ManageOthersPublicKeysActivity.this, "You did not select by Id", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(ManageOthersPublicKeysActivity.this, "Please enter Id", Toast.LENGTH_SHORT).show();
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

        seeKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(ManageOthersPublicKeysActivity.this, ManageOthersPublicKeysShowKeysActivity.class);
                String userInput = UserInput.getText().toString();
                if (!userInput.equals("")) {
                    int userSelection = Integer.parseInt(UserInput.getText().toString());
                    if (userSelection > 0) {
                        if (db.checkOTHERSPublicKeysExist(userSelection) == true) {
                            String otherpersonskeys[];
                            try {
                                otherpersonskeys = db.getOTHERSPublicKeysById(userSelection);
                            } catch (Exception e) {
                                Toast.makeText(ManageOthersPublicKeysActivity.this, "Row Does Not Exist", Toast.LENGTH_LONG).show();
                                return;
                            }

                            if (otherpersonskeys == null) {
                                Toast.makeText(ManageOthersPublicKeysActivity.this, "Row Does Not Exist", Toast.LENGTH_LONG).show();
                                return;
                            }
                            String keyN = otherpersonskeys[0];
                            String keyE = otherpersonskeys[1];

                            l.putExtra("KeyN", keyN);
                            l.putExtra("KeyE", keyE);
                            Toast.makeText(ManageOthersPublicKeysActivity.this, "Successfully retrieved keys", Toast.LENGTH_SHORT).show();
                            startActivity(l);
                        } else
                            Toast.makeText(ManageOthersPublicKeysActivity.this, "Id does not exist", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(ManageOthersPublicKeysActivity.this, "You did not select by Id", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ManageOthersPublicKeysActivity.this, "Please enter Id", Toast.LENGTH_SHORT).show();
                ;
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ManageOthersPublicKeysActivity.this, HomeActivity.class);
                startActivity(i);
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
}
