package co.songliao.guitvi.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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


//        matcher.addURI(authority,SongContract.SONG_PATH,TITLE);
//        matcher.addURI(authority,SongContract.SONG_PATH,SINGER);
//        matcher.addURI(authority,SongContract.SONG_PATH,ALBUM);
//        matcher.addURI(authority,SongContract.SONG_PATH,ALBUM_PATH);
//        matcher.addURI(authority,SongContract.SONG_PATH,LYRICS);

        return matcher;
    }

    private static final String sSingerSelection =
            SongContract.SongData.TABLE_NAME+
                    "." + SongContract.SongData.COL_SINGER + " = ? ";

    public SongProvider() {
        super();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
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
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
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
        return null;
    }

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
