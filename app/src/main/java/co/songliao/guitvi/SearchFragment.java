package co.songliao.guitvi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import co.songliao.guitvi.data.SongContract;

/**
 * Created by Song on 1/7/15.
 */



public class SearchFragment extends Fragment {

    private final String SEARCH_TAG = "search";

    public SearchFragment() {
        super();
    }



    private Context mContext ;
    private String finalizedTitle;
    private String finalizedSinger;
    private String finalizedLyrics;
    private boolean lyricsFound;

    AutoCompleteTextView titleText;
    AutoCompleteTextView singerText;

    Button searchButton;
    TextView lyricsText;


    public String [] getSingerArray(String search){

        String [] data;

        StringBuffer buffer = new StringBuffer();

        try {
            String base = "http://suggestqueries.google.com/complete/search?client=firefox&q="+search;
            Document rawresult = Jsoup.connect(base).get();


            buffer.append(rawresult.body().toString().
                    replaceAll("&quot;"," ").
                    replace("[ "+search+" ,[ ","").
                    replace("<body>","").
                    replace("]]","")
                    .replace("</body>", "")
                    .replace("\n",""));

        }
        catch(Throwable t){
            t.printStackTrace();//auto complete search.
        }

         data = buffer.toString().split(",");
        return data;
        //return buffer.toString();
    }


    public void initializeLayout(View rootView){

        String item[]={
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        };


        titleText = (AutoCompleteTextView) rootView.findViewById(R.id.editSong);
        singerText = (AutoCompleteTextView) rootView.findViewById(R.id.editSinger);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        lyricsText = (TextView) rootView.findViewById(R.id.textView);

        final ArrayAdapter<String> singerAdapter = new ArrayAdapter<String>
                (getActivity(),android.R.layout.simple_dropdown_item_1line,item);

        singerText.setText("");

        titleText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {

            }
        });
        titleText.setThreshold(0);

        singerText.setAdapter(singerAdapter);

        titleText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                titleText.setText("");
                return false;
            }
        });
        singerText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                singerText.setText("");
                return false;
            }
        });

        searchButton.setOnClickListener(searchHandler);

    }

    View.OnClickListener searchHandler = new View.OnClickListener() {
        public void onClick(View v) {
            lyricsFound = false;
            fetchData();
        }
    };

    public void fetchData(){
        FetchDataClass fetch = new FetchDataClass();

        if(titleText.getText().toString()!=null || singerText.getText().toString()!=null){


            finalizedTitle = titleText.getText().toString().replaceAll("[^A-Za-z0-9]", "").replaceAll("\\s+","").toLowerCase();
            finalizedSinger = singerText.getText().toString().replaceAll("[^A-Za-z0-9]", "").replaceAll("\\s+","").toLowerCase();

            String finalURL = "http://www.azlyrics.com/lyrics/"+finalizedSinger+"/"+ finalizedTitle+".html";

            fetch.execute(finalURL);
        }
        else{
            Toast.makeText(getActivity(), "Please input both song name and singer", Toast.LENGTH_SHORT).show();
        }
    }
    public class FetchDataClass extends AsyncTask<String,Void,String > {

        //private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... input) {
            StringBuffer buffer = new StringBuffer();
            try{
                Document doc = Jsoup.connect(input[0]).get();
              // Log.d("JSwa", "Connecting to [" + input[0] + "]");

                //Log.d("doc",doc.toString());
                String rawBody = doc.body().toString();
                String [] firstsplit = rawBody.split("<!-- start of lyrics -->");
                finalizedLyrics = firstsplit[1].split("<!-- end of lyrics -->")[0].replace("<br />","");

                buffer.append(finalizedLyrics);
            }
            catch(Throwable t){
                t.printStackTrace();
            }

            return buffer.toString();
        }


        protected void onPostExecute(String result) {
          //  myForecastAdapter.clear();
            if(result!=null && result.length()>10) {
                lyricsText.setText(result);
                lyricsFound = true;
              //  Log.v(SEARCH_TAG,"result length is "+result.length());
               // Message.message(mContext,"lyrics found!");
            }
            else{
                lyricsText.setText(R.string.lyrics_not_found);
                lyricsFound = false;
                Message.message(mContext,"lyrics not found!");
            }
            Log.v(SEARCH_TAG,String.valueOf(lyricsFound));
        }
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

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        mContext =getActivity().getApplicationContext();

        initializeLayout(rootView);

        return rootView;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id ==R.id.save){
            if(lyricsFound){
               //
                saveData(titleText.getText().toString(),//get unmodified title and singer name
                        singerText.getText().toString(),
                        finalizedLyrics);
            }
            else{
                //Message.message(mContext,"lyrics not found!");
            }
        }
        return super.onOptionsItemSelected(item);

    }

    public void saveData(String theTitle, String theSinger,String theLyrics){
        //first check if lyrics exist in database
        Cursor cursor = mContext.getContentResolver().query(
                SongContract.SongData.CONTENT_URI,
                null,
                SongContract.SongData.COL_TITLE + " = ? AND " + SongContract.SongData.COL_SINGER +" = ? ",
                new String []{theTitle,theSinger},
                null
        );
        if( cursor.moveToFirst()) {

            Message.message(mContext, "Song already in database!:)");
        }

        else{

            ContentValues newValue = new ContentValues();
            newValue.put(SongContract.SongData.COL_TITLE, theTitle);
            newValue.put(SongContract.SongData.COL_SINGER, theSinger);
            newValue.put(SongContract.SongData.COL_ALBUM, "");
            newValue.put(SongContract.SongData.COL_LYRICS, theLyrics);


            Uri insertUri = mContext.getContentResolver().insert(
                    SongContract.SongData.CONTENT_URI,
                    newValue
            );

            if(insertUri!=null){//great success
                Message.message(mContext,"Succesfully saved lyrics");
                mContext.getContentResolver().notifyChange(
                        SongContract.SongData.CONTENT_URI,null
                );
            }
            else{
                Message.message(mContext,"Problem occured during saving");
            }
        }
    }
}
