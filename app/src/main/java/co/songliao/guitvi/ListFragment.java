package co.songliao.guitvi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.songliao.guitvi.data.SongContract;

/**
 * Created by Song on 1/4/15.
 */

    public class ListFragment extends Fragment {
        private Context mContext;
        private final String LIST_TAG = "list";

        private List<Song> songs = new ArrayList<Song>();

        public void populateSongData(){
          //  String [] lyrics1 = new String [] {"hey", "how are you doinng" , "my friend"};

            songs.add(new Song("I'm yours", "Jason Mraz","From A to Z","well you done done you better felt it \n I'm just so hot that you better melt it \n I felt rigtht through the cracks"));
            songs.add(new Song("Thinking out loud", "Ed Sheeran","X","when i cannot sweep off your feet \n I will love you till 70 \n my heart will beat hard as 23 "));
            songs.add(new Song("算什麼男人", "周杰倫","哎哟,不错哦", "親吻你的手 還靠著你的頭 \n 讓你躺胸口 那個人已不是我 \n 這些平常的舉動 現在叫做難過"));


            songs.add(new Song("I'm yours", "Jason Mraz","From A to Z","well you done done you better felt it \n I'm just so hot that you better melt it \n I felt rigtht through the cracks"));
            songs.add(new Song("Thinking out loud", "Ed Sheeran","X","when i cannot sweep off your feet \n I will love you till 70 \n my heart will beat hard as 23 "));
            songs.add(new Song("算什麼男人", "周杰倫","哎哟,不错哦", "親吻你的手 還靠著你的頭 \n 讓你躺胸口 那個人已不是我 \n 這些平常的舉動 現在叫做難過"));

        }

        public ListFragment() {
             setHasOptionsMenu(true);
        }

        ArrayAdapter<Song> adapter;
        SimpleCursorAdapter cursorAdapter ;

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
           menu.clear();
           inflater.inflate(R.menu.menu_list,menu);
           MenuItem searchItem = menu.findItem(R.id.search_song);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id ==R.id.search_song){
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                Toast.makeText(getActivity(),"Search clicked",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
            return super.onOptionsItemSelected(item);

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_guiv, container, false);
            rootView.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
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

            cursorAdapter = new SimpleCursorAdapter(
                    mContext,
                    R.layout.onerow,
                    cursorAllData,
                    columns,
                    to,
                    0
            );


            ListView list = (ListView) (rootView.findViewById(R.id.songList));
            // list.setAdapter(adapter);
            list.setAdapter(cursorAdapter);


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Object obj = cursorAdapter.getItem(position);
                    if(cursorAllData.moveToPosition(position)) {

                        int titleIndex = cursorAllData.getColumnIndex(SongContract.SongData.COL_TITLE);
                        int singerIndex = cursorAllData.getColumnIndex(SongContract.SongData.COL_SINGER);
                        int lyricsIndex = cursorAllData.getColumnIndex(SongContract.SongData.COL_LYRICS);

                        String title = cursorAllData.getString(titleIndex);
                        String singer =cursorAllData.getString(singerIndex);
                        String lyrics = cursorAllData.getString(lyricsIndex);

                        String all = title + "GUITVI"+ singer + "GUITVI"+ lyrics;
                        Intent intent = new Intent(getActivity(), LyricsActivity.class);
                        intent.putExtra(intent.EXTRA_TEXT,all);
                        startActivity(intent);

                    }

                }
            });

            return rootView;

        }


    }

