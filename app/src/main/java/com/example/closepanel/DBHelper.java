package com.example.closepanel;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by ABBOSBEK on 08.03.2017.
 * Bu class Sqlite bazasi bilan ishlashga mo'jallangan.
 * Classni ishlatish uchun
 * DBHelper dbHelper = new DBHelper(getApplicationContext(),"baza.db");
 * try {
 * Cursor cursor = dbHelper.SQL_TEXT("select * from data");//SQL so'rovi yoziladi
 * }
 * catch (Exception e){
 * Toast.makeText(this, "SQL so'rovida xatolik", Toast.LENGTH_SHORT).show();//Xatolik sodir bo'lgandagi kodlar yoziladi
 * }
 **/

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "";
    private static final String DB_PATH_SUFFIX = "/databases/";
    private static Context ctx;

    public DBHelper(Context context, String dbname) {
        super(context, dbname, null, DATABASE_VERSION);
        ctx = context;
        this.DATABASE_NAME = dbname;
        openDataBase();
    }

    public Cursor SQL_TEXT(String sql) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor;
    }

    private void CopyDataBaseFromAsset() throws IOException {
        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);
        String outFileName = getDatabasePath();
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    private SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                InputStreamReader stream = new InputStreamReader(ctx.getAssets().open(DATABASE_NAME));
                CopyDataBaseFromAsset();
            } catch (IOException e) {
                File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
                if (!f.exists())
                    f.mkdir();
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}