package co.songliao.guitvi.songtest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.Map;
import java.util.Set;

import co.songliao.guitvi.data.SongContract;
import co.songliao.guitvi.data.SongDbhelper;

/**
 * Created by Song on 1/17/15.
 */
public class TestProvider extends AndroidTestCase {

    public static final String P_TAG = "provider";
    public void testDeleteDb()throws Throwable{
        mContext.deleteDatabase(SongDbhelper.DATABASE_NAME);
    }

    public void tsestDeleteAll(){
        mContext.getContentResolver().delete(
                SongContract.SongData.CONTENT_URI,
                null,
                null
        );

        Cursor dc = mContext.getContentResolver().query(
                SongContract.SongData.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        dc.close();

        assertEquals(dc.getCount(), 0);
    }



    public void testInsertRead(){
        SongDbhelper dbhelper = new SongDbhelper(mContext);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(SongContract.SongData.COL_TITLE,"Let it go");
        content.put(SongContract.SongData.COL_SINGER,"Idina Mendelz");
        content.put(SongContract.SongData.COL_ALBUMCOVER,"Album");
        content.put(SongContract.SongData.COL_LYRICS,"The wind is blowing like mountain tonight \n Let it go I don't care this anymore");

        ContentValues content2 = new ContentValues();
        content2.put(SongContract.SongData.COL_TITLE,"What makes you beautiful");
        content2.put(SongContract.SongData.COL_SINGER,"one Direction");
        content2.put(SongContract.SongData.COL_ALBUMCOVER,"Album");
        content2.put(SongContract.SongData.COL_LYRICS,"You don't know Oh Oh! \n What makes you beautiful ");
        long rowId = db.insert(SongContract.SongData.TABLE_NAME,null,content);
        long rowId2 = db.insert(SongContract.SongData.TABLE_NAME,null,content2);

        assertTrue(rowId!=-1);
        assertTrue(rowId2!=-1);

        //  content://co.songliao.guitv/song  is passed in getType()
        //returns: vnd.android.cursor.dir/co.songliao.guitv/song
        //this test makes sure it returns a table / multiple rows
        String type = mContext.getContentResolver().getType(SongContract.SongData.CONTENT_URI);
        assertEquals(type, SongContract.SongData.CONTENT_TYPE);

        //the testing Uri is a rowId, so it must return one row or vnd.android.cursor.item/co.songliao.co/song
        type = mContext.getContentResolver().getType(SongContract.SongData.buildSongUri(rowId));
        assertEquals(type,SongContract.SongData.CONTENT_ITEM_TYPE);

        //testing singer URI
        type = mContext.getContentResolver().getType(SongContract.SongData.buildSingerUri("One Direction"));
        assertEquals(type, SongContract.SongData.CONTENT_TYPE);


        //this is going to execute to SongProvider.query, which returns a cursor with value

//        Cursor cursor = mContext.getContentResolver().query(
//                SongContract.SongData.CONTENT_URI,
//                null,
//                null,
//                null,
//                null
//        );
        //validateCursor(cursor,content);


        //this query is same as "SELECT * FROM song_db where _ID is = rowId"

        Cursor idCursor = mContext.getContentResolver().query(
                SongContract.SongData.buildSongUri(rowId),
                null,
                null,
                null,
                null
        );

        validateCursor(idCursor,content);

        //SELECT * FROM song_db WHERE singer = "one direction"
        Cursor singerCursor = mContext.getContentResolver().query(
                SongContract.SongData.buildSingerUri("Idina Mendelz"),
                null,
                null,
                null,
                null
        );

        validateCursor(singerCursor,content);

        ContentValues content3 = new ContentValues();
        content3.put(SongContract.SongData.COL_TITLE,"I'm Alive");
        content3.put(SongContract.SongData.COL_SINGER,"JJ Lin");
        content3.put(SongContract.SongData.COL_ALBUMCOVER,"New World");
        content3.put(SongContract.SongData.COL_LYRICS,"Oh I am alive \n Life is so colorful ");


        Uri insertSong = mContext.getContentResolver().insert(SongContract.SongData.CONTENT_URI,content3);
        assertTrue(insertSong!=null);
        singerCursor.close();
        idCursor.close();


        Cursor cursorAll = mContext.getContentResolver().query(
                SongContract.SongData.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        //logCursor(cursorAll);

//       long deleteRow = mContext.getContentResolver().delete(SongContract.SongData.CONTENT_URI,
//                "_id = ? ",
//               new String []{ "2" }
//               );
//        assertEquals(deleteRow,1);


        ContentValues newC = new ContentValues();
        newC.put(SongContract.SongData._ID,rowId2);
        newC.put(SongContract.SongData.COL_SINGER,"Two Direction");

        long updateRows = mContext.getContentResolver().update(
                SongContract.SongData.CONTENT_URI,
                newC,
                SongContract.SongData._ID  +" = ?",
                new String [] {Long.toString(rowId2)}

        );

        assertEquals(updateRows,1);

        db.close();
    }





    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }

    static void logCursor(Cursor cursor){
        while(cursor.moveToNext()){
            int getTitleIndex = cursor.getColumnIndex(SongContract.SongData._ID);
            String id = cursor.getString(getTitleIndex);
            Log.d("cur",id);
        }

    }
}
