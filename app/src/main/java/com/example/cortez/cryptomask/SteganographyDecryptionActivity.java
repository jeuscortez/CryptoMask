package com.example.cortez.cryptomask;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SteganographyDecryptionActivity extends AppCompatActivity {

    public static final int PICK_ENCODER = 1;
    public static final int PICK_IMAGE = 2;

    Steganography stegonagraphy = new Steganography();
    private Handler handler;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steganography_decryption);


        final Button buttonDecode = (Button) findViewById(R.id.ButtonDecodeatation);
        final Button buttonReturn = (Button) findViewById(R.id.ReturnFromSteganographyDecryption);
        //final TextView textDec = (TextView) findViewById(R.id.EditTextDecodedMessage);

        buttonDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/png");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
                photoUri = getIntent().getData();
                handler = new Handler();
            }
        });

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(SteganographyDecryptionActivity.this, HomeActivity.class);
                startActivity(j);
            }
        });
    }

    public void onBackPressed() {
        Intent j = new Intent(getApplicationContext(), SteganographyOptionsActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }

    public String DecryptImage(Uri photoUri) {
        Bitmap image = null;
        String secretMessage = "";
        try {
            Cursor cursor = getContentResolver().query(photoUri, null, null, null, null);
            cursor.moveToFirst();

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String absoluteFilePath = cursor.getString(idx);

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inDither = false;
            opt.inScaled = false;
            opt.inDensity = 0;
            opt.inJustDecodeBounds = false;
            opt.inPurgeable = false;
            opt.inSampleSize = 1;
            opt.inScreenDensity = 0;
            opt.inTargetDensity = 0;
            image = BitmapFactory.decodeFile(absoluteFilePath, opt);

        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(),
                image.getHeight());
        Log.v("Decode", "" + pixels[0]);
        Log.v("Decode Alpha", "" + (pixels[0] >> 24 & 0xFF));
        Log.v("Decode Red", "" + (pixels[0] >> 16 & 0xFF));
        Log.v("Decode Green", "" + (pixels[0] >> 8 & 0xFF));
        Log.v("Decode Blue", "" + (pixels[0] & 0xFF));
        Log.v("Decode", "" + pixels[0]);
        Log.v("Decode", "" + image.getPixel(0, 0));
        byte[] bitsTodecipher = null;
        try {
            bitsTodecipher = stegonagraphy.convertArray(pixels);
        } catch (OutOfMemoryError er) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SteganographyDecryptionActivity.this);
            builder.setMessage("Image too large!!!!!!!!!!!!!!!").setCancelable(false).setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SteganographyDecryptionActivity.this.finish();
                        }
                    });
            // handler.post(runnableDismmiss);
            AlertDialog alert = builder.create();
            alert.show();
            return null;
        }
        final String deciphered = stegonagraphy.decodeMessage(bitsTodecipher, image.getWidth(), image
                .getHeight());
        //handler.post(runnableDismmiss);
        if (deciphered == null) {
            handler.post(new Runnable() {

                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            SteganographyDecryptionActivity.this);
                    builder.setMessage("Not An Encoded Image").setCancelable(false).setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SteganographyDecryptionActivity.this.finish();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        } else {
            secretMessage = deciphered;
            Log.v("Coded message", deciphered);

            Runnable runnableSetText = new Runnable() {

                public void run() {
                    //TextView textDec = (TextView) findViewById(R.id.EditTextDecodedMessage);
                    //textDec.setText(vvv);
                }
            };
            handler.post(runnableSetText);
        }
        return secretMessage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {
            case (PICK_IMAGE):
                if (resultCode == RESULT_OK) {
                    Uri photoUri = intent.getData();
                    if (photoUri != null) {
                        try {
                            final TextView textDec = (TextView) findViewById(R.id.EditTextDecodedMessage);
                            textDec.setText("it works");
                            String message = DecryptImage(photoUri);
                            textDec.setText("Decrypted Message: " + message);
                            Toast.makeText(SteganographyDecryptionActivity.this, "Finished", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
