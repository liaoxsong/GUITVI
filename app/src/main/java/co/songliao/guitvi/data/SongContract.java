package co.songliao.guitvi.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Song on 1/5/15.
 */
public class SongContract {
    //for all tables, if implemented in the future
    public static final String CONTENT_AUTHORITY = "co.songliao.guitvi";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String SONG_PATH = "song";

    //Song table
    public static final class SongData implements BaseColumns{



        //cotent://co.songliao.guitvi/song
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(SONG_PATH).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/"+CONTENT_AUTHORITY+"/"+SONG_PATH;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/"+CONTENT_AUTHORITY+"/" +SONG_PATH;

        //Song/1
        public static Uri buildSongUri(long rowid){
            return ContentUris.withAppendedId(CONTENT_URI,rowid);
            //content://co.songliao.guitvi/song/<id>
        }

        //Song/singer
        public static Uri buildSingerUri(String singer){
            return CONTENT_URI.buildUpon().appendPath(singer).build();
            //content://co.songliao.guitvi/song/<singer>
        }


        public static final String TABLE_NAME = "song_db";

        public static final String COL_TITLE = "title" ;//
        public static final String COL_SINGER = "singer";//
        public static final String COL_ALBUM = "album";//
        public static final String COL_ALBUMCOVER = "albumcover";//image path//
        public static final String COL_LYRICS = "lyrics";


        public static final String [] PROJECTION_ALL =  {
                _ID,COL_TITLE,COL_SINGER,COL_ALBUM,COL_ALBUMCOVER,COL_LYRICS
        };

        public static String getSingerFromUri(Uri uri){
            return  uri.getPathSegments().get(1);
        }
    }

}
