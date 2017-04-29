package com.example.cortez.cryptomask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageDecryptionResultActivity extends AppCompatActivity {
    private ImageView DecodedImage;
    private ImageMasking mask = new ImageMasking();
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_decryption_result);
        db = new DBHandler(this, null, null, 1);

        Bundle UserData = getIntent().getExtras();
        if (UserData == null) {
            return;
        }

        final Button ReturnToMain = (Button) findViewById(R.id.ReturntoMainFromDecoding);
        ReturnToMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ImageDecryptionResultActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        int encodedImageId = UserData.getInt("SelectedImageID");
        DecodedImage = (ImageView) findViewById(R.id.UnMaskedImage);

        String encoded = db.decodeImage(encodedImageId);
            Bitmap bm = mask.decodeStringBase64(encoded);//get the image back from the Bit64 string
            DecodedImage.setImageBitmap(bm);

    }

    public void onBackPressed() {
        //Intent i = new Intent(getApplicationContext(), TextDecryptionActivity.class);
        Intent j = new Intent(getApplicationContext(), ImageDecryptionActivity.class);
        startActivity(j);
        finish();
        super.onStop();
    }
}
