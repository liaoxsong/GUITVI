package co.songliao.guitvi.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import co.songliao.guitvi.R;
import co.songliao.guitvi.activities.LyricsActivity;
import co.songliao.guitvi.adapter.MainListAdapter;
import co.songliao.guitvi.data.SongContract;

/**
 * Created by Song on 1/4/15.
 */

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context mContext;
    private final String LIST_TAG = "list";

    private DynamicListView mDynamicListView;

    public static final int LIST_LOADER = 1;

    MainListAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LIST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                SongContract.SongData.CONTENT_URI,
                null,
                null,
                null,
                null
                );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch(loader.getId()){
            case LIST_LOADER:
                mAdapter.swapCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

        public ListFragment() {
             setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
           menu.clear();
           inflater.inflate(R.menu.menu_list,menu);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);
            mContext = getActivity().getApplicationContext();
            final Cursor cursorAllData = mContext.getContentResolver().query(
                    SongContract.SongData.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            final String [] columns = new String [] {
                    SongContract.SongData.COL_TITLE,
                    SongContract.SongData.COL_SINGER,};

            int [] to = new int [] {
                    R.id.titleView,
                    R.id.singerView,
            };

            mAdapter = new MainListAdapter(
                    mContext,
                    R.layout.onerow,
                    cursorAllData,
                    columns,
                    to,
                    0
            );

            mDynamicListView = (DynamicListView)rootView.findViewById(R.id.songList);
            mDynamicListView.setAdapter(mAdapter);
//            mDynamicListView.enableSwipeToDismiss( new OnDismissCallback() {
//                @Override
//                public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] ints) {
//
//                }
//            });

            mDynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Cursor cursor = mAdapter.getCursor();
                    if (cursor.moveToPosition(position)) {

                        int titleIndex = cursor.getColumnIndex(SongContract.SongData.COL_TITLE);
                        int singerIndex = cursor.getColumnIndex(SongContract.SongData.COL_SINGER);
                        int lyricsIndex = cursor.getColumnIndex(SongContract.SongData.COL_LYRICS);

                        String title = cursor.getString(titleIndex);
                        String singer = cursor.getString(singerIndex);
                        String lyrics = cursor.getString(lyricsIndex);

                        String all = title + "GUITVI" + singer + "GUITVI" + lyrics;
                        Intent intent = new Intent(getActivity(), LyricsActivity.class);
                        intent.putExtra(intent.EXTRA_TEXT, all);
                        startActivity(intent);
                    }
                }
            });

            mDynamicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    Cursor cursor = mAdapter.getCursor();
                    if(cursor.moveToPosition(position)){
                        int idIndex = cursor.getColumnIndex(SongContract.SongData._ID);

                        String theID = cursor.getString(idIndex);
                        Toast.makeText(mContext, "Long click and delete "+ theID, Toast.LENGTH_LONG).show();

                        mContext.getContentResolver().delete(
                                SongContract.SongData.CONTENT_URI,
                                SongContract.SongData._ID + "= ? ",
                                new String []{ theID }
                        );
                        getLoaderManager().restartLoader(LIST_LOADER,null,ListFragment.this);
                    }
                    return true;
                }
            });
            return rootView;
        }



    @Override
    public void onResume() {

        getLoaderManager().restartLoader(LIST_LOADER,null,this);
        super.onResume();
    }

    @Override
    public void onDestroy() {
       // getActivity().getContentResolver().unregisterContentObserver(mContentObserver);
        super.onDestroy();
    }
}

