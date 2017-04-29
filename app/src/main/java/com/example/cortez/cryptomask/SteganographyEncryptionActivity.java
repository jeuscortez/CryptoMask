package com.example.cortez.cryptomask;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SteganographyEncryptionActivity extends AppCompatActivity {


    private Context context;
    private final Handler handler = new Handler();
    private Bitmap sourceBitmap;
    public static final int PICK_IMAGE = 1;
    public static final int PICK_CONTACT = 2;
    public static final int MESSAGE = 3;
    public static String BYTEIMAGE = "BYTEIMAGE";
    public static String TEXT = "TEXT";
    private String absoluteFilePathSource;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steganography_encryption);
        context = this;

        Button buttonSelectImage = (Button) findViewById(R.id.ButtonSelect);
        final Button buttonSave = (Button) findViewById(R.id.save);
        buttonSave.setEnabled(false);
        final Button buttonReturntomain = (Button) findViewById(R.id.returnFromStegoEncryption);


        buttonSelectImage.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                buttonSave.setEnabled(true);
                boolean result = Utility.checkPermission(SteganographyEncryptionActivity.this);
                if (result) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PICK_IMAGE);
                } else return;
            }

        });

        buttonReturntomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(SteganographyEncryptionActivity.this, HomeActivity.class);
                startActivity(j);
            }
        });

        buttonSave.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                closeContextMenu();
                closeOptionsMenu();
                Toast.makeText(SteganographyEncryptionActivity.this,"Wait 10 Seconds to Process",Toast.LENGTH_LONG).show();
                Thread tt = new Thread(new Runnable() {
                    public void run() {
                        encode();
                    }
                });
                tt.start();
            }
        });
    }

    public void onBackPressed() {
        Intent j = new Intent(getApplicationContext(), SteganographyOptionsActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case (PICK_IMAGE):
                if (resultCode == RESULT_OK) {

                    Uri photoUri = intent.getData();
                    if (photoUri != null) {
                        try {
                            TextView picPath = (TextView) this.findViewById(R.id.EditTxtImage);

                            Cursor cursor = getContentResolver().query(photoUri, null, null, null, null);
                            cursor.moveToFirst();

                            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            absoluteFilePathSource = cursor.getString(idx);
                            picPath.setText(absoluteFilePathSource);

                            BitmapFactory.Options opt = new BitmapFactory.Options();
                            opt.inDither = false;
                            opt.inScaled = false;
                            opt.inDensity = 0;
                            opt.inJustDecodeBounds = false;
                            opt.inPurgeable = false;
                            opt.inSampleSize = 1;
                            opt.inScreenDensity = 0;
                            opt.inTargetDensity = 0;

                            sourceBitmap = BitmapFactory.decodeFile(absoluteFilePathSource, opt);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

        }
    }

    private Uri encode() {

        Steganography staganography = new Steganography();
        Uri result = null;
        EditText text = (EditText) findViewById(R.id.message);
        String message = text.getText().toString();

        try {
            int width = sourceBitmap.getWidth();
            int height = sourceBitmap.getHeight();

            int[] oneD = new int[width * height];
            sourceBitmap.getPixels(oneD, 0, width, 0, 0, width, height);
            int density = sourceBitmap.getDensity();
            sourceBitmap.recycle();
            byte[] byteImage = staganography.encodeMessage(oneD, width, height, message);
            oneD = null;
            sourceBitmap = null;
            int[] oneDMod = staganography.byteArrayToIntArray(byteImage);
            byteImage = null;
            Log.v("Encode", "" + oneDMod[0]);
            Log.v("Encode Alpha", "" + (oneDMod[0] >> 24 & 0xFF));
            Log.v("Encode Red", "" + (oneDMod[0] >> 16 & 0xFF));
            Log.v("Encode Green", "" + (oneDMod[0] >> 8 & 0xFF));
            Log.v("Encode Blue", "" + (oneDMod[0] & 0xFF));

            System.gc();
            Log.v("Free memory", Runtime.getRuntime().freeMemory() + "");
            Log.v("Image mesure", (width * height * 32 / 8) + "");

            Bitmap destBitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);

            destBitmap.setDensity(density);
            int partialProgr = height * width / 50;
            int masterIndex = 0;
            for (int j = 0; j < height; j++)
                for (int i = 0; i < width; i++) {
                    // The unique way to write correctly the sourceBitmap, android bug!!!
                    destBitmap.setPixel(i, j, Color.argb(0xFF,
                            oneDMod[masterIndex] >> 16 & 0xFF,
                            oneDMod[masterIndex] >> 8 & 0xFF,
                            oneDMod[masterIndex++] & 0xFF));
                }

            Log.v("Encode", "" + destBitmap.getPixel(0, 0));
            Log.v("Encode Alpha", "" + (destBitmap.getPixel(0, 0) >> 24 & 0xFF));
            Log.v("Encode Red", "" + (destBitmap.getPixel(0, 0) >> 16 & 0xFF));
            Log.v("Encode Green", "" + (destBitmap.getPixel(0, 0) >> 8 & 0xFF));
            Log.v("Encode Blue", "" + (destBitmap.getPixel(0, 0) & 0xFF));

            String sdcardState = android.os.Environment.getExternalStorageState();

            String destPath = null;
            int indexSepar = absoluteFilePathSource.lastIndexOf(File.separator);
            int indexPoint = absoluteFilePathSource.lastIndexOf(".");
            if (indexPoint <= 1)
                indexPoint = absoluteFilePathSource.length();
            String fileNameDest = absoluteFilePathSource.substring(indexSepar + 1, indexPoint);
            fileNameDest += "_stego";
            if (sdcardState.contentEquals(android.os.Environment.MEDIA_MOUNTED))
                destPath = android.os.Environment.getExternalStorageDirectory()
                        + File.separator + fileNameDest + ".png";

            OutputStream fout = null;
            try {

                Log.v("Path", destPath);
                fout = new FileOutputStream(destPath);
                destBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
                //MediaStore.Images.Media.insertImage(getContentResolver(), destBitmap, "SteganographyImage" , "Testing stego");
                //Media.insertImage(getContentResolver(),destPath, fileNameDest, "MobiStego Encoded");
                result = Uri.parse("file://" + destPath);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
                fout.flush();
                fout.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            destBitmap.recycle();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return result;
    }

    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;
    private Button btnSelectPhoto;
    private ImageView ivImage;
    private String userChoosentask;
    private ImageMasking mask = new ImageMasking();


   @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosentask.equals("Take Photo")) {
                        cameraIntent();
                    } else if (userChoosentask.equals("Choose from Library")) {
                        galleryIntent();
                    }
                } else {
                    //code for deny

                }
                break;
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }
}
