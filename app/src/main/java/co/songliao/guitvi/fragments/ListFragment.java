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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import co.songliao.guitvi.R;
import co.songliao.guitvi.Song;
import co.songliao.guitvi.activities.LyricsActivity;
import co.songliao.guitvi.adapter.OnPageSelectedListener;
import co.songliao.guitvi.data.SongContract;

/**
 * Created by Song on 1/4/15.
 */

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,OnPageSelectedListener {
    private Context mContext;
    private final String LIST_TAG = "list";

    public void onPageSelected (){

//            FragmentManager fragmentManager = getChildFragmentManager();
//
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.add(R.id.FragmentContainer, ListFragment.this);
//            if(isAdded()) {
//                Toast.makeText(mContext,"on page selected in list fragment", Toast.LENGTH_SHORT).show();
//                getLoaderManager().restartLoader(LIST_LOADER, null, this);
//            }
    }

    private static final String [] projection = new String[]{
            SongContract.SongData.COL_TITLE,
            SongContract.SongData.COL_SINGER,
            SongContract.SongData.COL_ALBUM,
            SongContract.SongData.COL_ALBUMCOVER,
            SongContract.SongData.COL_LYRICS,
    };

    //these indices are linked to loaders
    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_SINGER = 2;
    public static final int COL_ALBUM = 3;
    public static final int COL_ALBUMCOVER = 4;
    public static final int COL_LYRICS = 5;


    public static final int LIST_LOADER = 1;

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    ArrayAdapter<Song> adapter;

    SimpleCursorAdapter mAdapter;

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
//         //**not used**/
//        private List<Song> songs = new ArrayList<Song>();
//        public void populateSongData(){
//            songs.add(new Song("I'm yours", "Jason Mraz", "From A to Z", "well you done done you better felt it \n I'm just so hot that you better melt it \n I felt rigtht through the cracks"));
//        }//**not used**/

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

           // getActivity().getContentResolver().registerContentObserver(SongContract.SongData.CONTENT_URI, true, mContentObserver);
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

            mAdapter = new SimpleCursorAdapter(
                    mContext,
                    R.layout.onerow,
                    cursorAllData,
                    columns,
                    to,
                    0
            );
            ListView list = (ListView) (rootView.findViewById(R.id.songList));
            list.setAdapter(mAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Cursor cursor = mAdapter.getCursor();
                    if(cursor.moveToPosition(position)) {

                        int titleIndex = cursor.getColumnIndex(SongContract.SongData.COL_TITLE);
                        int singerIndex = cursor.getColumnIndex(SongContract.SongData.COL_SINGER);
                        int lyricsIndex = cursor.getColumnIndex(SongContract.SongData.COL_LYRICS);

                        String title = cursor.getString(titleIndex);
                        String singer =cursor.getString(singerIndex);
                        String lyrics = cursor.getString(lyricsIndex);

                        String all = title + "GUITVI"+ singer + "GUITVI"+ lyrics;
                        Intent intent = new Intent(getActivity(), LyricsActivity.class);
                        intent.putExtra(intent.EXTRA_TEXT,all);
                        startActivity(intent);
                    }
                }
            });


            return rootView;
        }

//    private ContentObserver updateObserver = new ContentObserver(new Handler()) {
//        @Override
//        public void onChange(boolean selfChange) {
//           // getLoaderManager().restartLoader(LIST_LOADER, null,this); // With appropriate loader args here
//            getLoaderManager().restartLoader(LIST_LOADER, null, (android.app.LoaderManager.LoaderCallbacks<Cursor>) this);
//        }
//    };

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

