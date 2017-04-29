package com.example.cortez.cryptomask;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ImageCapturingActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;
    private Button btnSelectPhoto;
    private ImageView ivImage;
    private String userChoosentask;
    private ImageMasking mask = new ImageMasking();
    DBHandler mydbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capturing);
        mydbHandler = new DBHandler(this, null, null, 1);

        btnSelectPhoto = (Button) findViewById(R.id.btnSelectPhoto);
        final Button ImageEncode = (Button) findViewById(R.id.imageselectconfirmation);
        ImageEncode.setEnabled(false);

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                ImageEncode.setEnabled(true);
            }
        });
        ivImage = (ImageView) findViewById(R.id.ivImage);


        ImageEncode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {
                Intent j = new Intent(w.getContext(), ImageEncodeActivity.class);
                Bitmap bm = null;
                ivImage.buildDrawingCache();
                bm = ivImage.getDrawingCache();
                String encoded = mask.encodeToStringBase64(bm); //we convert the image to a stringBase64

                if (!encoded.equals(null)) {
                    addEncodedImage(encoded, generateDate()); //add to db
                    startActivity(j);
                } else
                    Toast.makeText(ImageCapturingActivity.this, "You did not select Picture", Toast.LENGTH_SHORT).show();
                return;
            }
        });



    }

    public void onBackPressed() {
        Intent j = new Intent(getApplicationContext(), ImageOptionsActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }

    public String generateDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        String properDate = dateformat.format(c.getTime());
        return properDate;

    }

    public void addEncodedImage(String encodedImage, String date) {
        String encodeImage = encodedImage;
        mydbHandler.addEncodedImage(encodedImage, date);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageCapturingActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ImageCapturingActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosentask = "Take Photo";
                    if (result)
                        cameraIntent();
                    } else if (items[item].equals("Choose from Library")) {
                        userChoosentask = "Choose from Library";
                        if (result)
                            galleryIntent();
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }

            }
        });
        builder.show();
    }

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

    //handling image functions
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivImage.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(thumbnail);
    }
}
