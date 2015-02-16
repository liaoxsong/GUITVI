package co.songliao.guitvi.adapter;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Song on 2/15/15.
 */
public class MainListAdapter extends SimpleCursorAdapter {


    public MainListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }


}
