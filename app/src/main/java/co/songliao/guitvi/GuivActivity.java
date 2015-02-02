package co.songliao.guitvi;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import co.songliao.guitvi.adapter.PagerAdapter;

public class GuivActivity extends FragmentActivity
   {
    private final Handler handler = new Handler();

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private PagerSlidingTabStrip tabs;
    private int currentColor = 0xFF666666;
    private Drawable oldBackground = null;
       //did you know that, writing an phone app is a very complicated process.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guiv);


        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tabs.setDividerPadding(200);
        tabs.setViewPager(viewPager);


    }

       @Override
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
