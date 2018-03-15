package com.redray.khadoomhome.PROVIDER.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.redray.khadoomhome.PROVIDER.fragments.Accepted_Orders_frag;
import com.redray.khadoomhome.PROVIDER.fragments.Done_Orders_frag;
import com.redray.khadoomhome.PROVIDER.fragments.New_Orders_frag;
import com.redray.khadoomhome.PROVIDER.fragments.Refused_Orders_frag;



public class Main_PageAdapter_provider extends FragmentPagerAdapter {


    private final String[] TITLES = {
            "Orders",
            "My Requests",
            "Bills",
            "Profile",
    };




    public Main_PageAdapter_provider(FragmentManager fm) {
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
                return new New_Orders_frag();
            case 1:
                return new Accepted_Orders_frag();
            case 2:
                return new Refused_Orders_frag();
            case 3:
                return new Done_Orders_frag();
            default:
                return null;
        }

        //	return SuperAwesomeCardFragment.newInstance(position);

    }


}
