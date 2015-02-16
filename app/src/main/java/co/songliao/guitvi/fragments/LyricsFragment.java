package co.songliao.guitvi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.songliao.guitvi.R;

/**
 * Created by Song on 1/6/15.
 */
public class LyricsFragment extends Fragment {

    public String titleAndLyrics;

    private String all;

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

            all = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            String [] split = all.split("GUITVI");

            TextView titleText = (TextView) rootView.findViewById(R.id.titleView);
            TextView lyricsText = (TextView)rootView.findViewById(R.id.lyricsView);
            titleText.setText(split[0]);
            lyricsText.setText(split[2]);
        }




        return rootView;
    }

}
