package co.songliao.guitvi.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Song on 1/5/15.
 */
public class SongDbhelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =1;

    public static final String DATABASE_NAME = "mysong.db";

    public SongDbhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public SongDbhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MYSONG_TABLE = "CREATE TABLE " + SongContract.SongData.TABLE_NAME + " ("+
                SongContract.SongData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+//0
                SongContract.SongData.COL_TITLE+" TEXT NOT NULL, "+
                SongContract.SongData.COL_SINGER+ " TEXT NOT NULL, " +
                SongContract.SongData.COL_ALBUM + " TEXT NULL, "+
                SongContract.SongData.COL_ALBUMCOVER+ " TEXT NULL, " +
                SongContract.SongData.COL_LYRICS+ " TEXT NULL ) ";
        db.execSQL(SQL_CREATE_MYSONG_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + SongContract.SongData.TABLE_NAME);
        onCreate(db);
    }


}
