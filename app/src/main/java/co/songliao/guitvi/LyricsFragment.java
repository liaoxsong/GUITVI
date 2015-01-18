package co.songliao.guitvi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Song on 1/6/15.
 */
public class LyricsFragment extends Fragment {

    public String titleAndLyrics;


    public LyricsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lyrics, container, false);


        Intent intent = getActivity().getIntent();



        if(intent!=null && getActivity().getIntent().hasExtra(Intent.EXTRA_TEXT)) {

            titleAndLyrics = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);//this is an ArrayList
            String [] split = titleAndLyrics.split("GUITVI");
            TextView titleText = (TextView) rootView.findViewById(R.id.titleView);
            TextView lyricsText = (TextView)rootView.findViewById(R.id.lyricsView);
            titleText.setText(split[0]);
            lyricsText.setText(split[1]);
        }




        return rootView;
    }

}
