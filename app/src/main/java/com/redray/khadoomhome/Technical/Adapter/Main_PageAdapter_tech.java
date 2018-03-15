package com.redray.khadoomhome.Technical.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.redray.khadoomhome.Technical.Fragment.Finished_Orders_tech_frag;
import com.redray.khadoomhome.Technical.Fragment.My_Bills_tech_Fragment;
import com.redray.khadoomhome.Technical.Fragment.New_Orders_tech_frag;


public class Main_PageAdapter_tech extends FragmentPagerAdapter {


    private final String[] TITLES = {
            "new Orders",
            "My Requests",
            "Bills",
    };




    public Main_PageAdapter_tech(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new New_Orders_tech_frag();
            case 1:
                return new Finished_Orders_tech_frag();
            case 2:
                return new My_Bills_tech_Fragment();
            default:
                return null;
        }

        //	return SuperAwesomeCardFragment.newInstance(position);

    }


}
