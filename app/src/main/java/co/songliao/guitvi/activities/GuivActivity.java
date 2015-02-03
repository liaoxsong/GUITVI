package co.songliao.guitvi.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;

import co.songliao.guitvi.R;
import co.songliao.guitvi.adapter.PagerAdapter;

public class GuivActivity extends FragmentActivity
   {
    private final Handler handler = new Handler();

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private TitlePageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guiv);


        viewPager = (ViewPager)findViewById(R.id.viewpager);
        indicator = (TitlePageIndicator) findViewById(R.id.titleindicator);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(listener);
        indicator.setViewPager(viewPager);
        indicator.setOnPageChangeListener(listener);

    }

       ViewPager.OnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener(){
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }
           @Override
           public void onPageSelected(int position) {
               switch (position){
                   case 0:
                       Toast.makeText(getApplicationContext(), "on page selected 0", Toast.LENGTH_SHORT).show();

                   case 1:
                       Toast.makeText(getApplicationContext(), "on page selected 1" , Toast.LENGTH_SHORT).show();

                   default:
                       break;
               }
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       };
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guiv, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

}
