package co.songliao.guitvi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import co.songliao.guitvi.ListFragment;
import co.songliao.guitvi.SearchFragment;

/**
 * Created by Song on 2/1/15.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {

        super(fm);
    }

    private final String [] TITLES = {"List","Search"};
    @Override
    public CharSequence getPageTitle(int position){
        return TITLES[position];
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new ListFragment();
            case 1:
                return new SearchFragment();
            default:
                break;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }
}
