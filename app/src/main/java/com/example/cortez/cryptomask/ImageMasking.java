package com.example.cortez.cryptomask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import static com.example.cortez.cryptomask.R.attr.theme;

/**
 * Created by Cortez on 3/4/2017.
 */

public class ImageMasking {
    ImageMasking(){

    }

    public Bitmap manipulatingImage(Bitmap bitmap){

        int A, R, G, B;
        int pixelColor;
        int Width = bitmap.getWidth();
        int Height = bitmap.getHeight();

        Bitmap finalImage = Bitmap.createBitmap(Width,Height,bitmap.getConfig());

        for(int y=0; y<Height;y++){
            for (int x=0;x<Width;x++){
                pixelColor = bitmap.getPixel(x,y);
                A= Color.alpha(pixelColor);
                R=255-Color.red(pixelColor);
                G=255-Color.green(pixelColor);
                B=255-Color.blue(pixelColor);
                finalImage.setPixel(x,y,Color.argb(A,R,G,B));
            }
        }
        return finalImage;
    }

    public Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {

        //Bitmap bm2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.sampleTheme);
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, canvas.getWidth()/2 -bmp2.getWidth()/2,canvas.getHeight()/2-bmp2.getHeight()/2,null);
        //canvas.drawColor(Color.CYAN);
        //canvas.drawColor(Color.TRANSPARENT);
        return bmOverlay;
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    public Bitmap darkCover(Bitmap bm){
        Bitmap hold = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),bm.getConfig());

        for(int i=0; i<bm.getWidth(); i++){
            for(int j=0; j<bm.getHeight(); j++){
                int p = bm.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  r - 50;
                g =  g - 50;
                b =  b - 50;
                alpha = alpha -50;
                hold.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        return hold;
    }

    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public String encodeToStringBase64(Bitmap bm) {

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        byte[] b = byteStream.toByteArray();
        String encodedImage = Base64.encodeToString(b,Base64.DEFAULT);

        return encodedImage;
    }

    public Bitmap decodeStringBase64(String encodedBitmap){

        byte[] decodedString = Base64.decode(encodedBitmap, Base64.DEFAULT);
        Bitmap decodedBytes = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedBytes;
    }

    public Bitmap blur(Bitmap original, float radius,Context ctx) {
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(ctx);

        Allocation allocIn = Allocation.createFromBitmap(rs, original);
        Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, Element.U8_4(rs));
        blur.setInput(allocIn);
        blur.setRadius(radius);
        blur.forEach(allocOut);

        allocOut.copyTo(bitmap);
        rs.destroy();
        return bitmap;

    }


}
