package co.songliao.guitvi.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Song on 1/17/15.
 */
public class SongProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SongDbhelper mOpenHelper;
    private static final SQLiteQueryBuilder queryBuilder;

    static {
        queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SongContract.SongData.TABLE_NAME);
    }
    private static final int SONG =100;
    private static final int SONG_ID = 101;

//    private static final int TITLE = 101;
    private static final int SINGER = 102;
//    private static final int ALBUM = 103;
//    private static final int ALBUM_PATH = 104;
//    private static final int LYRICS = 105;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SongContract.CONTENT_AUTHORITY;

        //first matcher the whole table: co.songliao.guitv/song
        matcher.addURI(authority,SongContract.SONG_PATH,SONG);

        //second matcher a specific row in the table,give it id 101: co.songliao.guitv/song/#
        matcher.addURI(authority,SongContract.SONG_PATH+"/#",SONG_ID);

        matcher.addURI(authority,SongContract.SONG_PATH+"/*",SINGER);

        return matcher;
    }


    public SongProvider() {
        super();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        final int matcher = sUriMatcher.match(uri);

        switch(sUriMatcher.match(uri)){

            //if the incoming URI is coming for everything in SONG table
            case SONG: //song
                if(TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                cursor = mOpenHelper.getReadableDatabase().query(
                        SongContract.SongData.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            //If incoming URI is coming for a single row
            case SONG_ID:
                //NOTE!!! selection here, if it's ID, _ID is enough, no need to specificy table_name.id
                selection = SongContract.SongData._ID +" = "+ uri.getLastPathSegment();
                cursor = mOpenHelper.getReadableDatabase().query(
                        SongContract.SongData.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case SINGER:

                //selection = SongContract.SongData.COL_SINGER +" = "+ SongContract.SongData.getSingerFromUri(uri);
                cursor = getSongBySinger(uri,projection,sortOrder);

                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri"+uri);

        }


        return cursor;
    }

    @Override
    public boolean onCreate() {

        mOpenHelper = new SongDbhelper(getContext());

        return true;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsUpdated;
        switch(match){
            case SONG:
                rowsUpdated = db.update(SongContract.SongData.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+ uri);

        }
        if(rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch(match){
            case SONG:
                rowsDeleted = db.delete(SongContract.SongData.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+ uri);
        }
        if(selection ==null || rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        //return number of rows deleted
        return rowsDeleted;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match){
            case SONG:
                return SongContract.SongData.CONTENT_TYPE;//multiple rows
            case SONG_ID:
                return SongContract.SongData.CONTENT_ITEM_TYPE;//one row
            case SINGER:
                return SongContract.SongData.CONTENT_TYPE;//multiple rows
            default:
                throw new UnsupportedOperationException("Unknown Uri "+uri);
        }


    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch(match){
            case SONG:
                long id = db.insert(SongContract.SongData.TABLE_NAME,null,values);

                if(id>0){
                    //giving it a specific id to access
                    //content://co.songliao.co/song/<id>
                    returnUri = SongContract.SongData.buildSongUri(id);
                }
                else{
                    throw new SQLException("Failed insert to row "+ uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }



        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    private static final String sSingerSelection =
            SongContract.SongData.TABLE_NAME+
                    "." + SongContract.SongData.COL_SINGER + " = ? ";

    private Cursor getSongBySinger(Uri uri, String [] projection, String sortOrder){
        String selection = sSingerSelection;
        String theSinger = SongContract.SongData.getSingerFromUri(uri);
        String [] selectionArgs = new String[] {theSinger };
        return queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

    }
}
