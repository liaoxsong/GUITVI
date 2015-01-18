package co.songliao.guitvi.songtest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import co.songliao.guitvi.data.SongContract;
import co.songliao.guitvi.data.SongDbhelper;

/**
 * Created by Song on 1/12/15.
 */
public class TestMain extends AndroidTestCase{

    private final String TAG = "TEST_TAG";
    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(SongDbhelper.DATABASE_NAME);
        SQLiteDatabase db = new SongDbhelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());


        ContentValues content = new ContentValues();
        content.put(SongContract.SongData.COL_TITLE,"Let it go");
        content.put(SongContract.SongData.COL_SINGER,"Idina Mendelz");
        content.put(SongContract.SongData.COL_ALBUMCOVER,"Album");
        content.put(SongContract.SongData.COL_LYRICS,"I never felt ");

        ContentValues content2 = new ContentValues();
        content2.put(SongContract.SongData.COL_TITLE,"What makes you beautiful");
        content2.put(SongContract.SongData.COL_SINGER,"one Direction");
        content2.put(SongContract.SongData.COL_ALBUMCOVER,"Album");
        content2.put(SongContract.SongData.COL_LYRICS,"IGo forwads ");



        SongDbhelper songDbhelper = new SongDbhelper(this.mContext);
        SQLiteDatabase database = songDbhelper.getWritableDatabase();

        long rowID = database.insert(SongContract.SongData.TABLE_NAME,null,content);
        long row2 = database.insert(SongContract.SongData.TABLE_NAME, null, content2);
        String [] columns =  new String []{SongContract.SongData.COL_SINGER, SongContract.SongData.COL_TITLE};

        assertTrue(rowID > 0);
        Cursor cursor = database.query(
                SongContract.SongData.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);

       while(cursor.moveToNext()){
           int getTitleIndex = cursor.getColumnIndex(SongContract.SongData.COL_TITLE);

           int getSingerIndex = cursor.getColumnIndex(SongContract.SongData.COL_SINGER);

           String title = cursor.getString(getTitleIndex);
           String singer = cursor.getString(getSingerIndex);
           Log.d(TAG,"result= "+ title + "  "+singer);

       }






        db.close();
    }

}
