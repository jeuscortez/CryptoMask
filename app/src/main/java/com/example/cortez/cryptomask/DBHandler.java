package com.example.cortez.cryptomask;

/**
 * Created by Cortez on 2/17/2017.
 * This class creates the database for encryptions
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteCursor;
import android.content.Context;
import android.content.ContentValues;
import android.hardware.camera2.CaptureResult;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.math.BigInteger;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASEVERSION = 1;
    private static final String DATABASENAME = "UserEncryptions.db";

    public static final String Table = "ENCRYPTIONS";
    public static final String ColumnId = "_id";
    public static final String ColumnDate = "date";
    public static final String ColumnCipherText = "ciphertext";
    public static final String ColumnKeys = "key";
    public static final String[] ALL_COLUMNS = new String[]{ColumnId, ColumnCipherText, ColumnDate};

    public static final String TableImages = "IMAGES";
    public static final String ColumnEncodeImage = "encodedimage";
    public static final String[] ALL_ENCODEDIMAGECOLUMNS = new String[]{ColumnId, ColumnEncodeImage, ColumnDate};

    public static final String TableUserLogin = "USER";
    public static final String ColumnUserName = "username";
    public static final String ColumnPassWord = "password";
    public static final String ColumnAlphaKey = "alphakey";
    public static final String ColumnBetaKey = "betakey";
    public static final String[] ALL_USERCOLUMNS = new String[]{
            ColumnId, ColumnUserName, ColumnPassWord, ColumnAlphaKey, ColumnBetaKey, ColumnDate};

    public static final String TableRSA = "RSAKEYS";
    public static final String ColumnPublicKeyN = "keyN";
    public static final String ColumnPublicKeyE = "keyE";
    public static final String ColumnPrivateKeyD = "keyD";
    public static final String[] ALL_RSACOLUMNS = new String[]{ColumnId, ColumnPublicKeyN, ColumnPublicKeyE, ColumnPrivateKeyD, ColumnDate};

    public static final String TableOthersPublicKeys = "OTHERSPUBLICKEYS";
    public static final String ColumnOthersPublicKeysName = "otherspublickeysidentifier";
    public static final String ColumnOthersPublicKeyN = "otherspublickeyN";
    public static final String ColumnOthersPublicKeyE = "otherspublickeyE";
    public static final String[] ALL_OTHERSPUBLICKEYSCOLUMNS = new String[]{
            ColumnId, ColumnOthersPublicKeysName, ColumnOthersPublicKeyN, ColumnOthersPublicKeyE, ColumnDate};

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASENAME, factory, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + Table + "(" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ColumnDate + " TEXT, " + ColumnCipherText + " TEXT, " + ColumnKeys + " TEXT " + ");";

        String query2 = "CREATE TABLE " + TableImages + "(" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ColumnDate + " TEXT, " + ColumnEncodeImage + " TEXT " + ");";

        String query3 = "CREATE TABLE " + TableUserLogin + "(" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ColumnUserName + " TEXT, " + ColumnPassWord + " TEXT, " + ColumnDate + " TEXT, "
                + ColumnAlphaKey + " INTEGER, " + ColumnBetaKey + " INTEGER " + ");";

        String query4 = "CREATE TABLE " + TableRSA + "(" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ColumnPublicKeyN + " TEXT, " + ColumnPublicKeyE + " TEXT, " + ColumnPrivateKeyD + " TEXT, "
                + ColumnDate + " TEXT " + ");";

        String query5 = "CREATE TABLE " + TableOthersPublicKeys + "(" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ColumnOthersPublicKeysName + " TEXT, " + ColumnOthersPublicKeyN + " TEXT, " + ColumnOthersPublicKeyE + " TEXT, "
                + ColumnDate + " TEXT " + ");";

        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Table);
        db.execSQL("DROP TABLE IF EXISTS " + TableImages);
        db.execSQL("DROP TABLE IF EXISTS " + TableUserLogin);
        db.execSQL("DROP TABLE IF EXISTS " + TableRSA);
        db.execSQL("DROP TABLE IF EXISTS " + TableOthersPublicKeys);
        onCreate(db);
    }

    //adding a new row to the Table
    public void addEncryption(String ciphertext, String date, String key) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ColumnCipherText, ciphertext); //adding ciphertext to the column*****
        values.put(ColumnDate, date);
        values.put(ColumnKeys, key);

        db.insert(Table, null, values); //inserts to the database
        db.close();
    }

    public void addEncodedImage(String encodedImage, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ColumnEncodeImage, encodedImage); //adding encodedImage to the column*****
        values.put(ColumnDate, date);

        db.insert(TableImages, null, values); //insert values into database
        db.close();
    }

    public void addUserInfo(String username, String password, String date, int alpha, int beta) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ColumnUserName, username);
        values.put(ColumnPassWord, password);
        values.put(ColumnDate, date);
        values.put(ColumnAlphaKey, alpha);
        values.put(ColumnBetaKey, beta);

        db.insert(TableUserLogin, null, values);//inserts values
        db.close();
    }

    public void addRSAKEYS(BigInteger publicKeyN, BigInteger publicKeyE, BigInteger privateKeyD, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ColumnPublicKeyN, publicKeyN.toString());
        values.put(ColumnPublicKeyE, publicKeyE.toString());
        values.put(ColumnPrivateKeyD, privateKeyD.toString());
        values.put(ColumnDate, date);

        db.insert(TableRSA, null, values);//inserts values
        db.close();

    }

    public void addOthersRSAPublicKEYS(String PublicKeysIdentifier, String publicKeyN, String publicKeyE, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ColumnOthersPublicKeysName, PublicKeysIdentifier);
        values.put(ColumnOthersPublicKeyN, publicKeyN);
        values.put(ColumnOthersPublicKeyE, publicKeyE);
        values.put(ColumnDate, date);

        db.insert(TableOthersPublicKeys, null, values);//inserts values
        db.close();

    }

    public boolean checkCipherExists(int cipherId) {
        String query = "SELECT * FROM " + Table + " WHERE " + ColumnId + "=" + cipherId;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            cursor.close();
        } else {
            return false;
        }
        db.close();
        return true;
    }

    public boolean checkEncodedImageExists(int encodedImageId) {
        String query = "SELECT * FROM " + TableImages + " WHERE " + ColumnId + "=" + encodedImageId;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            cursor.close();
        } else {
            return false;
        }
        db.close();
        return true;
    }

    public boolean checkOTHERSPublicKeysExist(int RowId) {
        String query = "SELECT * FROM " + TableOthersPublicKeys + " WHERE " + ColumnId + "=" + RowId;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            cursor.close();
        } else {
            return false;
        }
        db.close();
        return true;
    }


    public String[] decryptCipher(int cipherId) {
        String query = "SELECT * FROM " + Table + " WHERE " + ColumnId + "=" + cipherId;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String ciphertext = "";
        String key = "";
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            ciphertext = cursor.getString(cursor.getColumnIndex(ColumnCipherText));
            key = cursor.getString(cursor.getColumnIndex(ColumnKeys));
            cursor.close();
        } else {
            ciphertext = "";

        }
        db.close();
        String[] vault = {ciphertext, key};
        return vault;

    }

    public String decodeImage(int encodedImageId) {
        String query = "SELECT * FROM " + TableImages + " WHERE " + ColumnId + "=" + encodedImageId;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String imageStringBase64 = "";

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            imageStringBase64 = cursor.getString(cursor.getColumnIndex(ColumnEncodeImage));
            cursor.close();
        } else {
            imageStringBase64 = "";
        }
        db.close();
        return imageStringBase64;
    }

    public String getLastImageSaved(){
        String query= "SELECT * FROM " + TableImages +" ORDER BY " + ColumnId +" DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String str = "";
        if(cursor.moveToFirst())
            str  =  cursor.getString( cursor.getColumnIndex(ColumnEncodeImage) );
        cursor.close();
        return str;
    }

    public String[] decryptUserInfo() {
        int userId = 1;
        String query = "SELECT * FROM " + TableUserLogin + " WHERE " + ColumnId + "=" + userId;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String username = "";
        String password = "";
        String alpha = "";
        String beta = "";

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            username = cursor.getString(cursor.getColumnIndex(ColumnUserName));
            password = cursor.getString(cursor.getColumnIndex(ColumnPassWord));
            alpha = cursor.getString(cursor.getColumnIndex(ColumnAlphaKey));
            beta = cursor.getString(cursor.getColumnIndex(ColumnBetaKey));
            cursor.close();
        } else {
            username = "";
            password = "";
            alpha = "";
            beta = "";
        }
        db.close();
        String[] vault = {username, password, alpha, beta};
        return vault;
    }

    public BigInteger[] getRSAPublicKeys() {
        int userId = 1;
        String query = "SELECT * FROM " + TableRSA + " WHERE " + ColumnId + "=" + userId;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String KeyN;
        String KeyE;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            KeyN = cursor.getString(cursor.getColumnIndex(ColumnPublicKeyN));
            KeyE = cursor.getString(cursor.getColumnIndex(ColumnPublicKeyE));
            cursor.close();
        } else {
            KeyN = "";
            KeyE = "";
        }
        db.close();
        BigInteger keyN = new BigInteger(KeyN);
        BigInteger keyE = new BigInteger(KeyE);
        BigInteger[] vault = {keyN, keyE};
        return vault;
    }

    public BigInteger[] getRSAKeys() {
        int userId = 1;
        String query = "SELECT * FROM " + TableRSA + " WHERE " + ColumnId + "=" + userId;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String KeyN;
        String KeyE;
        String KeyD;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            KeyN = cursor.getString(cursor.getColumnIndex(ColumnPublicKeyN));
            KeyE = cursor.getString(cursor.getColumnIndex(ColumnPublicKeyE));
            KeyD = cursor.getString(cursor.getColumnIndex(ColumnPrivateKeyD));
            cursor.close();
        } else {
            KeyN = "";
            KeyE = "";
            KeyD = "";
        }
        db.close();
        BigInteger keyN = new BigInteger(KeyN);
        BigInteger keyE = new BigInteger(KeyE);
        BigInteger keyD = new BigInteger(KeyD);
        BigInteger[] vault = {keyN, keyE, keyD};
        return vault;
    }

    public BigInteger[] getOTHERSPublicKeys(String PublicKeysIdentifierName) {

        String query = "SELECT * FROM " + TableOthersPublicKeys + " WHERE " + ColumnOthersPublicKeysName + " = \"" + PublicKeysIdentifierName + "\";";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String KeyN;
        String KeyE;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            KeyN = cursor.getString(cursor.getColumnIndex(ColumnOthersPublicKeyN));
            KeyE = cursor.getString(cursor.getColumnIndex(ColumnOthersPublicKeyE));
            cursor.close();
        } else {
            return null;
            //KeyN="0";
            //KeyE="0";
        }
        db.close();
        BigInteger keyN = new BigInteger(KeyN);
        BigInteger keyE = new BigInteger(KeyE);
        BigInteger[] vault = {keyN, keyE};
        return vault;
    }

    public String[] getOTHERSPublicKeysById(int RowId) {
        String query = "SELECT * FROM " + TableOthersPublicKeys + " WHERE " + ColumnId + "=" + RowId;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String KeyN;
        String KeyE;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            KeyN = cursor.getString(cursor.getColumnIndex(ColumnOthersPublicKeyN));
            KeyE = cursor.getString(cursor.getColumnIndex(ColumnOthersPublicKeyE));
            cursor.close();
        } else {
            return null;
            //KeyN="0";
            //KeyE="0";
        }
        db.close();
        String[] vault = {KeyN, KeyE};
        return vault;

    }

    //delete an encryption from the database
    public void deleteEncryption(int IdNumber) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Table + " WHERE " + ColumnId + "=\"" + IdNumber + "\";");
    }

    public void deleteEncodedImage(int encodedImageId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TableImages + " WHERE " + ColumnId + "=\"" + encodedImageId + "\";");
    }

    public void deleteOTHERSPublicKeysByID(int othersPublicKeyId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TableOthersPublicKeys + " WHERE " + ColumnId + "=\"" + othersPublicKeyId + "\";");
    }

    public void deleteOTHERSPublicKeysByKeysName(String KeysName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TableOthersPublicKeys + " WHERE " + ColumnOthersPublicKeysName + "=\"" + KeysName + "\";");
    }

    public void updateUserPassword(String username, String password, String date, int alpha, int beta) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ColumnUserName, username);
        values.put(ColumnPassWord, password);
        values.put(ColumnDate, date);
        values.put(ColumnAlphaKey, alpha);
        values.put(ColumnBetaKey, beta);

        db.update(TableUserLogin, values, ColumnId + "=\"" + 1 + "\"", null);

    }

    public void UpdateRSAKeys(BigInteger publicKeyN, BigInteger publicKeyE, BigInteger privateKeyD, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ColumnPublicKeyN, publicKeyN.toString());
        values.put(ColumnPublicKeyE, publicKeyE.toString());
        values.put(ColumnPrivateKeyD, privateKeyD.toString());
        values.put(ColumnDate, date);

        db.update(TableRSA, values, ColumnId + "=\"" + 1 + "\"", null);
    }

    //printout database as string
    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + Table + " WHERE 1"; //select all rows of the table

        //cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("_id")) != null) {
                //string is assigned ID, ciphertext, date
                dbString += c.getString(c.getColumnIndex(ColumnId)) + " | " + c.getString(c.getColumnIndex(ColumnCipherText))
                        + " | " + c.getString(c.getColumnIndex(ColumnDate));
                dbString += "\n";//new line
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    public String imagesTableToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TableImages + " WHERE 1"; //select all rows of the table

        //cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("_id")) != null) {
                //string is assigned ID, encodedImage, date
                dbString += c.getString(c.getColumnIndex(ColumnId)) + " | " + c.getString(c.getColumnIndex(ColumnEncodeImage))
                        + " | " + c.getString(c.getColumnIndex(ColumnDate));
                dbString += "\n";//new line
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    public String userTableToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TableUserLogin + " WHERE 1"; //select all rows of the table

        //cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("_id")) != null) {
                //string is assigned ID, username, password, date, alpha, and beta
                dbString += c.getString(c.getColumnIndex(ColumnId)) + " | " + c.getString(c.getColumnIndex(ColumnUserName))
                        + " | " + c.getString(c.getColumnIndex(ColumnPassWord)) + " | " + c.getString(c.getColumnIndex(ColumnDate))
                        + " | " + c.getString(c.getColumnIndex(ColumnAlphaKey)) + " | " + c.getString(c.getColumnIndex(ColumnBetaKey));
                dbString += "\n";//new line
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }


    //recently added
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + Table, null);
        return data;
    }

    // method is used to print them into a custom view to have all 3 columns next to each other
    public Cursor getAllRows() {
        SQLiteDatabase db = getWritableDatabase();
        String where = null;
        Cursor c = db.query(Table, ALL_COLUMNS,
                null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllEncodedImageTableRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = null;
        Cursor c = db.query(TableImages, ALL_ENCODEDIMAGECOLUMNS,
                null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllOTHERSPublicKeysRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = null;
        Cursor c = db.query(TableOthersPublicKeys, ALL_OTHERSPUBLICKEYSCOLUMNS,
                null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public String[] getUserNameFromTable() {
        int userId = 1;
        String query = "SELECT * FROM " + TableUserLogin + " WHERE " + ColumnId + "=" + userId;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String username = "";
        String alpha = "";
        String beta = "";

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            username = cursor.getString(cursor.getColumnIndex(ColumnUserName));
            alpha = cursor.getString(cursor.getColumnIndex(ColumnAlphaKey));
            beta = cursor.getString(cursor.getColumnIndex(ColumnBetaKey));
            cursor.close();
        } else {
            username = "";
            alpha = "";
            beta = "";
        }
        db.close();
        String[] vault = {username, alpha, beta};
        return vault;
    }

    public boolean checkUserInfoTableisempty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TableUserLogin;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);
        if (rowCount > 0) {
            return false;
        } else return true;
    }

    public boolean checkRSAKEYSTableisEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TableRSA;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);
        if (rowCount > 0) {
            return false;
        } else return true;
    }

    public boolean checkOTHERSPublicKeysTableisEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TableOthersPublicKeys;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);
        if (rowCount > 0) {
            return false;
        } else return true;
    }

    public boolean CheckOTHERSKeyIsInDB(String fieldValue) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TableOthersPublicKeys + " WHERE " + ColumnOthersPublicKeysName + " = \"" + fieldValue + "\";";
        //Sample String Query = "Select * from " + TableName + " where " + dbfield + " = \"" + fieldValue + "\";";
        try {
            Cursor cursor = db.rawQuery(Query, null);

            if (cursor.moveToFirst()) {
                cursor.close();
                return true;
            }
            cursor.close();
            return false;
        } catch (SQLiteException e) {
            return false;
        }
    }
}
