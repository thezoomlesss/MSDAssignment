package com.example.mushy.msd_assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mushy on 16-Nov-17.
 */

public class DataTaskList
{

    // First TABLE, for username, pass, NFC and Finger keys
    private static final String KEY_ROWID 	    = "_id";
    private static final String KEY_USERNAME = "KEY_USERNAME";
    private static final String KEY_PASS = "KEY_PASS";
    private static final String KEY_DOB = "KEY_DOB";
    private static final String KEY_NFC = "KEY_NFC";
    private static final String KEY_FINGER = "KEY_FINGER";

    private static final String DATABASE_TABLE 	= "TABLE1";
    private static final String DATABASE_NAME 	= "PassManagerDB";
    private static final int DATABASE_VERSION 	= 1; // since it is the first version of the dB

    // Crypt Key Table
    private static final String DATABASE_TABLE2 	= "TABLE2";
    private static final String KEY_ROWID2 	    = "_id2";
    private static final String KEY_USER_PASS= "KEY_USER_PASS";
    private static final String KEY_key_website= "KEY_WEBSITE";
    private static final String KEY_key_NFC= "KEY_NFC";
    private static final String KEY_key_FINGER= "KEY_FINGER";

    // Websites Table
    private static final String DATABASE_TABLE3 	= "TABLE3";
    private static final String KEY_ROWID3 	    = "_id3";
    private static final String KEY_WEBSITE= "KEY_WEBSITE";
    private static final String KEY_W_USERNAME= "KEY_W_USERNAME";
    private static final String KEY_W_PASS= "KEY_W_PASS";
    private static final String KEY_W_Notes= "KEY_W_Notes";


    // SQL statement to create the TABLE #1
    private static final String DATABASE_CREATE_1 = "CREATE TABLE "+ DATABASE_TABLE +
            " (" + KEY_ROWID + " integer primary key autoincrement, " +
            KEY_USERNAME + " text not null, " +
            KEY_PASS + " text not null, " +
            KEY_DOB + " text not null, " +
            KEY_NFC + " text NOT NULL, " +
            KEY_FINGER + " text NOT NULL ); ";

    // TABLE 2
    private static final String DATABASE_CREATE_2 = "CREATE TABLE "+ DATABASE_TABLE2 +
            " (" + KEY_ROWID2 + " integer primary key autoincrement, " +
            KEY_ROWID + " integer, " +
            KEY_USER_PASS + " text, " +
            KEY_key_website + " text not null, " +
            KEY_key_NFC + " text not null, " +
            KEY_key_FINGER + " text NOT NULL, " +
            "FOREIGN KEY( " +KEY_ROWID + " ) REFERENCES "+ DATABASE_TABLE + " ( " + KEY_ROWID +" ) ); ";

    // TABLE 3
    private static final String DATABASE_CREATE_3 = "CREATE TABLE "+ DATABASE_TABLE3 +
            " (" + KEY_ROWID3 + " integer primary key autoincrement, " +
            KEY_ROWID + " integer, " +
            KEY_WEBSITE + " text not null, " +
            KEY_W_USERNAME + " text not null, " +
            KEY_W_PASS + " text NOT NULL, " +
            KEY_W_Notes + " text NOT NULL, " +
            "FOREIGN KEY( " +KEY_ROWID + " ) REFERENCES "+ DATABASE_TABLE + " ( " + KEY_ROWID +" ) ); ";



    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    // Constructor
    public DataTaskList(Context ctx){
        this.context 	= ctx;
        DBHelper 		= new DatabaseHelper(context);
    }

    // nested dB helper class
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {


            db.execSQL(DATABASE_CREATE_1);// Execute SQL to create your tables (call the execSQL method of the SQLLiteDatabase class, passing in your create table(s) SQL)
            db.execSQL(DATABASE_CREATE_2);// Execute SQL to create your tables (call the execSQL method of the SQLLiteDatabase class, passing in your create table(s) SQL)
            db.execSQL(DATABASE_CREATE_3);// Execute SQL to create your tables (call the execSQL method of the SQLLiteDatabase class, passing in your create table(s) SQL)
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        }


    }



    // remainder of the Database Example methods to "use" the database
    public void close()

    {
        DBHelper.close();
    }

    public long insertUser(String username, String password, String dob, String nfc, String finger){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_PASS, password);
        initialValues.put(KEY_DOB, dob);
        initialValues.put(KEY_NFC, nfc);
        initialValues.put(KEY_FINGER, finger);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    public long insertKey(int userId, String key_user_pass, String key_website){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, userId);
        initialValues.put(KEY_USER_PASS, key_user_pass);
        initialValues.put(KEY_WEBSITE, key_website);
        initialValues.put(KEY_NFC, "1");
        initialValues.put(KEY_FINGER, "1");

        return db.insert(DATABASE_TABLE2, null, initialValues);
    }

    // Function to insert a single website into the db
    public long insertWebsite(int userID, String websiteName, String userName, String password, String note){

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_ROWID, userID);
        initialValues.put(KEY_WEBSITE, websiteName);
        initialValues.put(KEY_W_USERNAME, userName);
        initialValues.put(KEY_W_PASS, password);
        initialValues.put(KEY_W_Notes, note);

        return db.insert(DATABASE_TABLE3, null, initialValues);
    }

    public long updateWebsite(int userId, String original_wName,  String wName, String uwName, String uwPass, String uwNote){
        ContentValues cv = new ContentValues();

        cv.put(KEY_W_Notes,uwNote);
        cv.put(KEY_W_USERNAME, uwName);
        cv.put(KEY_W_PASS,uwPass);
        cv.put(KEY_WEBSITE, wName);

        return db.update(DATABASE_TABLE3, cv, "_id="+userId + " AND KEY_WEBSITE =\'"+ original_wName+"\';", null);
    }


    public boolean deleteWebsite(int userId, String wName){
        return db.delete(DATABASE_TABLE3, KEY_WEBSITE + "=\'" + wName+"\'", null) > 0;
    }
    // Function used to return the id of the website which we have to delete/update
    public Cursor getWebId(int userId, String wName){
        Cursor mCursor = db.rawQuery("SELECT "+ KEY_ROWID3 + " FROM " + DATABASE_TABLE3 +" WHERE " + KEY_ROWID +" = ? AND " +KEY_WEBSITE+ " = ?;", new String[] {String.valueOf(userId), wName});

        if (mCursor != null && mCursor.moveToFirst())
        {
            return mCursor;
        }
        else
        {
            return null;
        }
    }

    public Cursor getNamePass(int userId){

        Cursor mCursor = db.rawQuery("SELECT "+ KEY_USERNAME + ", "+ KEY_PASS+" FROM " + DATABASE_TABLE +" WHERE " + KEY_ROWID +" = ?;", new String[] {String.valueOf(userId)});

        if (mCursor != null && mCursor.moveToFirst())
        {
            return mCursor;
        }
        else
        {
            return null;
        }
    }

    public Cursor getUserId(String user, String pass){
        Cursor mCursor = db.rawQuery("SELECT "+ KEY_ROWID + " FROM " + DATABASE_TABLE +" WHERE " + KEY_USERNAME +" = ? AND " +KEY_PASS+ " = ?;", new String[] {user, pass});

        if (mCursor != null && mCursor.moveToFirst())
        {

            return mCursor;
        }
        else
        {
            return null;
        }
    }

    public Cursor loginUser(String username, String password) throws SQLException
    {
        Cursor mCursor = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE + " WHERE "+KEY_USERNAME+" = ? AND "+KEY_PASS+" = ?;", new String[] {username, password});

        if (mCursor != null && mCursor.moveToFirst())
        {

            return mCursor;
        }
        else
        {
            return null;
        }


    }

    // Function used to return all the websites
    public Cursor getWebsites(int userId){
        Cursor mCursor = db.rawQuery("SELECT " + KEY_WEBSITE + ", "+ KEY_W_USERNAME + ", "+ KEY_W_PASS+ ", "+ KEY_W_Notes + " FROM " + DATABASE_TABLE3 + " WHERE " + KEY_ROWID + " =?;", new String[] {String.valueOf(userId)} );

        if (mCursor != null && mCursor.moveToFirst())
        {
            return mCursor;
        }
        else
        {
            return null;
        }
    }
    public DataTaskList open() throws SQLException
    {
        db     = DBHelper.getWritableDatabase();
        return this;

    }

    /*
        Query that returns everything from the specified TABLE, without a key
     */


    public Cursor exampleCustomQuery() throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.


        Cursor mCursor =   db.query(true, DATABASE_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_ROWID,
                                KEY_USERNAME,
                                KEY_PASS,
                                KEY_NFC,
                                KEY_FINGER

                        },
                null, null, null, null, null, null);

        if (mCursor != null && mCursor.moveToFirst())
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    /*
            Query that returns a certain row that matches the id
         */
    public Cursor exampleCustomQuery2(long rowId) throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.

        Cursor mCursor =   db.query(true, DATABASE_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_ROWID,
                                KEY_USERNAME,
                                KEY_PASS,
                                KEY_NFC,
                                KEY_FINGER

                        },
                KEY_ROWID + " " + rowId, null, null, null, null, null);

        if (mCursor != null && mCursor.moveToFirst())
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


}
