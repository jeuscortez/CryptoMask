package com.example.cortez.cryptomask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ImageEncodeActivity extends AppCompatActivity {
    private ImageView ivImage;
    private ImageMasking mask = new ImageMasking();
    private TextView imageBase64;
    DBHandler mydbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_encode);

        //printing the database
        mydbHandler = new DBHandler(this, null, null, 1);

        String encoded = mydbHandler.getLastImageSaved();
        Bitmap bm = mask.decodeStringBase64(encoded);//get the image back from the Bit64 string

        ivImage = (ImageView) findViewById(R.id.encodedimage);
        imageBase64 = (TextView) findViewById(R.id.encodedimagetext);


        //this part is to test if inverting worked
        Bitmap dummy = Bitmap.createBitmap(bm); //created a copy of it

        //added the encode image to database
        //addEncodedImage(encoded, generateDate());

        //this is the part that is going to encode it to ascii code
        //String encodedByte = mask.encodeToStringBase64(bm);
        //Bitmap testSubject = mask.decodeStringBase64(encodedByte);
        //ivImage.setImageBitmap(testSubject);

        //Bitmap test = mask.manipulatingImage(bm);
        //ivImage.setImageBitmap(test);

        Bitmap bm2 = BitmapFactory.decodeResource(getResources(),R.drawable.customsecuritysprite);//get the drawable from resources
        bm2 = mask.getResizedBitmap(bm2,(int)(dummy.getWidth()/2),(int)(dummy.getHeight()/2));//make the drawable smaller to fit
        Bitmap blurred = mask.blur(dummy,15,this);//make the original image into a blurry form
        Bitmap sol = mask.overlay(blurred,bm2);// we create the final view which is the drawable over Original image

        ivImage.setImageBitmap(sol); //we paste it to the imageView
        imageBase64.setText(encoded);

        //this part will make it so that we can save it to db
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bm.compress(Bitmap.CompressFormat.PNG,100,stream);
        //byte[] byteArray = stream.toByteArray();

        //next step is to query the string into the db and then make them accessible by the decryption method.

        final Button ReturnToMain = (Button) findViewById(R.id.returntoMainfromimage);
        ReturnToMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ImageEncodeActivity.this, HomeActivity.class);
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
