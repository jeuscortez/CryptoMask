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

public class CipherTextDeletionActivity extends AppCompatActivity {

    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cipher_text_deletion);
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
                CipherTextDeletionActivity.this, R.layout.activity_cipher_text_deletion_list_format, cursor, fieldnames, toViewIds);
        ListView rowtoDecrypt = (ListView) findViewById(R.id.thelisttodeletefrom);
        rowtoDecrypt.setAdapter(myCursorAdapter);
//when button is clicked we attempt to get ctext from database and pass to next activity to decrypt

        final Button deleteRow = (Button) findViewById(R.id.DeleteCtextButt);
        final Button mainMenu = (Button) findViewById(R.id.returntomainmenufromctextdeletion);

        final AlertDialog.Builder warningBox = new AlertDialog.Builder(CipherTextDeletionActivity.this);
        warningBox.setMessage("Are You Sure You Want to Delete the row?");
        warningBox.setCancelable(true);

        deleteRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                warningBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent i = new Intent(CipherTextDeletionActivity.this, CipherTextDeletionActivity.class);
                                final EditText UserInput = (EditText) findViewById(R.id.rowtoDelete);
                                String userIn = UserInput.getText().toString();
                                if (!userIn.equals("")) {
                                    int userSelection = Integer.parseInt(UserInput.getText().toString());
                                    if (userSelection > 0) {
                                        if(db.checkCipherExists(userSelection) == true){
                                        db.deleteEncryption(userSelection);
                                        Toast.makeText(CipherTextDeletionActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        startActivity(i);
                                        }else
                                            Toast.makeText(CipherTextDeletionActivity.this, "Id does not exist", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(CipherTextDeletionActivity.this, "You did not select by Id", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(CipherTextDeletionActivity.this, "Please enter Id", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(CipherTextDeletionActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), TextOptionsActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
