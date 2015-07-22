package com.example.kieuptn.demoapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kieuptn on 7/15/15.
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MediaDB";
    SQLiteDatabase mSQLiteDatabase;
    private File myDir;

    public Database (Context context) {
        super(context, DATABASE_NAME, null, 1);
        // get path to sdCard
        String root = Environment.getExternalStorageDirectory().toString();
        myDir = new File(root + "/DemoApplication");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        // create databasePath and check path exist
        File file = new File(myDir, DATABASE_NAME);
        if (!file.exists()) {
            mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
            onCreate(mSQLiteDatabase);
        } else {
            mSQLiteDatabase = SQLiteDatabase.openDatabase(file.toString(), null,mSQLiteDatabase.OPEN_READWRITE);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Favories
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Favories (ID INTEGER PRIMARY KEY NOT NULL, Song TEXT, " +
                "Artist TEXT, Image TEXT, Link TEXT, Type TEXT)";

        mSQLiteDatabase.execSQL(createTableQuery);

        // create table Playlist
        createTableQuery = "CREATE TABLE IF NOT EXISTS Playlist (ID TEXT PRIMARY KEY AUTOINCREMENT, Playlist TEXT, " +
                "Song TEXT Artist TEXT, Image TEXT, Link TEXT, Type TEXT)";

        mSQLiteDatabase.execSQL(createTableQuery);

//        // create table Account
//        createTableQuery = "CREATE TABLE IF NOT EXISTS Account (ID INTEGER PRIMARY KEY AUTOINCREMENT, Username TEXT, " +
//                "Password TEXT)";
//
//        mSQLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public boolean insertData(String tableName, ObjectItem objectItem, @Nullable String playlistName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", objectItem.getId());
        contentValues.put("Song", objectItem.getSong());
        contentValues.put("Artist", objectItem.getArtist());
        contentValues.put("Image", objectItem.getImageURLString());
        contentValues.put("Link", objectItem.getLinkString());
        contentValues.put("Type", objectItem.getType());
        if (playlistName != null) {
            contentValues.put("Playlist", playlistName);
        }

        long checkNumber = mSQLiteDatabase.insert(tableName, null, contentValues);
        if (checkNumber <= 0) {
            return false;
        }
        return true;
    }

    public boolean deleteData(String tableName, String key) {
        String columnName;
        if (tableName.equals("Favories")) {
            columnName = "ID";
        } else {
            columnName = "Playlist";
        }

        long checkNumber = mSQLiteDatabase.delete(tableName, columnName + " like ?", new String[]{key});
        if (checkNumber == 0) {
            return false;
        }
        return true;
    }

    public boolean checkDataExists(String tableName, Integer key) {
        boolean flag;
        String query = "SELECT * FROM " + tableName + " WHERE ID = " + key;
        Cursor cursor = mSQLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            flag = true;
        } else {
            flag = false;
        }

        cursor.close();
        return flag;
    }

    public ArrayList<ObjectItem> getDataFromTable(String tableName, @Nullable String playlistName) {
        ArrayList<ObjectItem> arrayList = new ArrayList<ObjectItem>();
        String query = "SELECT * FROM " + tableName;

        if (playlistName != null) {
            query = query + "WHERE Playlist like " + playlistName;
        }

        Cursor cursor = mSQLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                ObjectItem dataObject = new ObjectItem();
                dataObject.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                dataObject.setSong(cursor.getString(cursor.getColumnIndex("Song")));
                dataObject.setArtist(cursor.getString(cursor.getColumnIndex("Artist")));
                dataObject.setLinkString(cursor.getString(cursor.getColumnIndex("Link")));
                dataObject.setImageURLString(cursor.getString(cursor.getColumnIndex("Image")));
                dataObject.setType(cursor.getString(cursor.getColumnIndex("Type")));

                arrayList.add(dataObject);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public Integer countDataInTable (String tableName) {
        String countQuery = "SELECT COUNT(*) FROM " + tableName;
        Cursor cursor = mSQLiteDatabase.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        return count;
    }
}
