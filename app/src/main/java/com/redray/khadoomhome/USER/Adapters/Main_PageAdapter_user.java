package com.redray.khadoomhome.USER.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.redray.khadoomhome.USER.Fragments.My_Bills_Fragment;
import com.redray.khadoomhome.USER.Fragments.My_Requests_Fragment;
import com.redray.khadoomhome.USER.Fragments.Orders_Fragment;
import com.redray.khadoomhome.all_users.fragments.Notifications_History_frag;


public class Main_PageAdapter_user extends FragmentPagerAdapter {


    private final String[] TITLES = {
            "Orders",
            "My Requests",
            "Bills",
            "Profile",
    };



    public Main_PageAdapter_user(FragmentManager fm ) {
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
                return new Orders_Fragment();
            case 1:
                return new My_Requests_Fragment();
            case 2:
                return new My_Bills_Fragment();
            case 3:
                return new Notifications_History_frag();
            default:
                return null;
        }

    }


}
