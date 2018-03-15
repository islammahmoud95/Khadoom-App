package com.redray.khadoomhome;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.redray.khadoomhome.PROVIDER.Activities.Get_All_Services;
import com.redray.khadoomhome.PROVIDER.Activities.Get_Bills_prov_activity;
import com.redray.khadoomhome.PROVIDER.Activities.Get_Technicals;
import com.redray.khadoomhome.PROVIDER.Activities.Update_Profile_prov_Activity;
import com.redray.khadoomhome.PROVIDER.Adapters.Main_PageAdapter_provider;
import com.redray.khadoomhome.Technical.Adapter.Main_PageAdapter_tech;
import com.redray.khadoomhome.USER.Activities.Change_Country_Activity;
import com.redray.khadoomhome.USER.Activities.Update_Profile_Activity;
import com.redray.khadoomhome.USER.Adapters.Main_PageAdapter_user;
import com.redray.khadoomhome.all_users.Activites.About_Us;
import com.redray.khadoomhome.all_users.Activites.Account_Type;
import com.redray.khadoomhome.all_users.Activites.Change_Password;
import com.redray.khadoomhome.all_users.Activites.Contact_US;
import com.redray.khadoomhome.all_users.Activites.Lang_Choose_activity;
import com.redray.khadoomhome.all_users.Activites.Notifications_History_Act;
import com.redray.khadoomhome.all_users.Activites.Privacy_Activity;
import com.redray.khadoomhome.all_users.Activites.Remove_Account;
import com.redray.khadoomhome.tickets.Activities.Main_Get_Tickets;
import com.redray.khadoomhome.utilities.Check_Con;
import com.redray.khadoomhome.utilities.CustomBadgeProvider;
import com.redray.khadoomhome.utilities.SessionManager;
import com.redray.khadoomhome.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    KProgressHUD dialog_bar;

    SessionManager sessionManager;

    String server_token, user_email, user_pic, user_name, user_id;

    RelativeLayout main_reg_layout;

    private static final int PROFILE_LANGUAGE = 1005;
    private static final int PROFILE_Password = 1006;
    private static final int PROFILE_remove_ACC = 1007;
    private static final int PROFILE_SETTING = 1008;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    CustomBadgeProvider provider;

    Toolbar toolbar;

    //counter parms
    String Notify_parm = "0";
    String Bills_parm = "0";
    String Request_parm = "0";
    String New_Orders_parm = "0";
    String Finished_Done_parm = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Handle Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        main_reg_layout = findViewById(R.id.main_reg_layout);

        sessionManager = new SessionManager(getApplicationContext());

        dialog_bar = KProgressHUD.create(MainActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.plz_wait))
                // .setDetailsLabel("Downloading data")
                .setCancellable(false).setAnimationSpeed(2).setDimAmount(0.6f).setCornerRadius(25);


        user_id = sessionManager.getUserDetails().get("USER_ID");
        user_name = sessionManager.getUserDetails().get("USER_NAME");
        user_pic = sessionManager.getUserDetails().get("User_Pic");
        user_email = sessionManager.getUserDetails().get("Email");
        server_token = sessionManager.getUserDetails().get("Server_token");


        if (sessionManager.getUser_Type().equals("1")) {

            nav_USER(savedInstanceState, toolbar);

        } else if (sessionManager.getUser_Type().equals("2")) {

            nav_PROVIDER(savedInstanceState, toolbar);

        } else if (sessionManager.getUser_Type().equals("3")) {

            nav_Technical(savedInstanceState, toolbar);
        }


        bottomNavigation = findViewById(R.id.BottomNavigation);

        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(1);


        if (null != bottomNavigation) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/neosans-regular.ttf");
            bottomNavigation.setDefaultTypeface(typeface);
            bottomNavigation.setDefaultSelectedIndex(0);

            switch (sessionManager.getUser_Type()) {
                case "1":

                    Main_PageAdapter_user adapter_user = new Main_PageAdapter_user(getSupportFragmentManager());
                    pager.setAdapter(adapter_user);

                    bottomNavigation.inflateMenu(R.menu.user_bar_menu);

                    provider = (CustomBadgeProvider) bottomNavigation.getBadgeProvider();
                    provider.show(R.id.my_Requests, Integer.parseInt(Request_parm));
                    provider.show(R.id.bills, Integer.parseInt(Bills_parm));

                    break;

                case "2":

                    Main_PageAdapter_provider adapter_provider = new Main_PageAdapter_provider(getSupportFragmentManager());
                    pager.setAdapter(adapter_provider);

                    bottomNavigation.inflateMenu(R.menu.provider_bar_menu);

                    provider = (CustomBadgeProvider) bottomNavigation.getBadgeProvider();
                    provider.show(R.id.Finished, Integer.parseInt(Finished_Done_parm));

                    break;

                case "3":

                    Main_PageAdapter_tech adapter_tech = new Main_PageAdapter_tech(getSupportFragmentManager());
                    pager.setAdapter(adapter_tech);

                    bottomNavigation.inflateMenu(R.menu.technical_bar_menu);

                    provider = (CustomBadgeProvider) bottomNavigation.getBadgeProvider();
                    provider.show(R.id.Finished, Integer.parseInt(Finished_Done_parm));
                    provider.show(R.id.Bills, Integer.parseInt(Bills_parm));

                    break;

            }


            bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
                @Override
                public void onMenuItemSelect(@IdRes int itemId, int position, boolean fromUser) {

                    pager.setCurrentItem(position);

                    if (provider.hasBadge(itemId)) {
                        provider.remove(itemId);
                    }
                }

                @Override
                public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {

                }
            });
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                switch (position) {
                    case 0:
                        bottomNavigation.setSelectedIndex(0, true);
                        pager.setCurrentItem(0);

                        break;

                    case 1:
                        bottomNavigation.setSelectedIndex(1, true);
                        pager.setCurrentItem(1);

                        break;

                    case 2:
                        bottomNavigation.setSelectedIndex(2, true);
                        pager.setCurrentItem(2);

                        break;

                    case 3:
                        bottomNavigation.setSelectedIndex(3, true);
                        pager.setCurrentItem(3);

                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        Check_Session();


    }

    ViewPager pager;
    BottomNavigation bottomNavigation;

    @Override
    protected void onResume() {
        super.onResume();
        Counters_sections();
    }


    PrimaryDrawerItem request_drawer;
    PrimaryDrawerItem bill_drawer;
    PrimaryDrawerItem notify_drawer;
    PrimaryDrawerItem order_new_drawer;


    public void nav_USER(final Bundle savedInstanceState, final Toolbar toolbar) {

        final IProfile profile;
        if (user_pic.equals("")) {
            profile = new ProfileDrawerItem().withName(user_name).withEmail(user_email).withIcon(R.drawable.profile_pic);

        } else {
            profile = new ProfileDrawerItem().withName(user_name).withEmail(user_email).withIcon(user_pic);

        }

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.nav_header)
                .addProfiles(
                        profile,

                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName(getString(R.string.chng_lang))
                                .withIcon(R.drawable.nav_lang).withIdentifier(PROFILE_LANGUAGE),

                        new ProfileSettingDrawerItem().withName(getString(R.string.chng_pass))
                                .withIcon(R.drawable.password_reg).withIdentifier(PROFILE_Password),

                        new ProfileSettingDrawerItem().withName(getString(R.string.remov_acc))
                                .withIcon(R.drawable.nav_remove).withIdentifier(PROFILE_remove_ACC),

                        new ProfileSettingDrawerItem().withName(getString(R.string.upd_profil))
                                .withIcon(R.drawable.company_name_reg).withIdentifier(PROFILE_SETTING)

                )

                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override

                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {

                        if (profile instanceof IDrawerItem && (profile).getIdentifier() == PROFILE_LANGUAGE) {
//                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman")
//                                    .withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));

                            Intent edit_prof = new Intent(getApplicationContext(), Lang_Choose_activity.class);
                            startActivity(edit_prof);


                        } else if (profile instanceof IDrawerItem && (profile).getIdentifier() == PROFILE_Password) {
//                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman")
//                                    .withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));

                            Intent edit_prof = new Intent(getApplicationContext(), Change_Password.class);
                            startActivity(edit_prof);

                        } else if (profile instanceof IDrawerItem && (profile).getIdentifier() == PROFILE_remove_ACC) {

                            Intent edit_prof = new Intent(getApplicationContext(), Remove_Account.class);
                            startActivity(edit_prof);


                        } else if (profile instanceof IDrawerItem && (profile).getIdentifier() == PROFILE_SETTING) {

                            Intent edit_prof = new Intent(getApplicationContext(), Update_Profile_Activity.class);
                            startActivity(edit_prof);

                        }


                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withAlternativeProfileHeaderSwitching(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withAccountHeader(R.layout.material_drawer_compact_persistent_header)
                .withProfileImagesClickable(false)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                // .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(

//                        new PrimaryDrawerItem().withName(R.string.make_order).withIcon(R.drawable.address_logo)
//                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(1)
//                                .withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.chos_country).withIcon(R.drawable.country_reg)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(1)
                                .withSelectable(false),

                        request_drawer = new PrimaryDrawerItem().withName(R.string.my_req).withIcon(R.drawable.nav_myreq)
                                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge(Request_parm)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(2)
                                .withSelectable(false),

                        bill_drawer = new PrimaryDrawerItem().withName(R.string.my_bills).withIcon(R.drawable.nav_bills)
                                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge(Bills_parm)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(3)
                                .withSelectable(false),

                        notify_drawer = new PrimaryDrawerItem().withName(R.string.notifications).withIcon(R.drawable.nav_notify)
                                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge(Notify_parm)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(4)
                                .withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.tickts).withIcon(R.drawable.nav_ticket)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray))
                                .withIdentifier(5)
                                .withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.abut_us).withIcon(R.drawable.nav_about)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(6)
                                .withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.contct_us).withIcon(R.drawable.phone_reg)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(7)
                                .withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.share).withIcon(R.drawable.nav_share)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(8)
                                .withSelectable(false),


                        new PrimaryDrawerItem().withName(R.string.privcy).withIcon(R.drawable.password_reg)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(9)
                                .withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.log_out).withIcon(R.drawable.nav_logout)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(10)
                                .withSelectable(false)


                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(MainActivity.this, Change_Country_Activity.class);
                            } else if (drawerItem.getIdentifier() == 2) {

                                bottomNavigation.setSelectedIndex(1, true);
                                pager.setCurrentItem(1);
                                bottomNavigation.getBadgeProvider().remove(R.id.my_Requests);

                                request_drawer.withBadge("0");
                                result.updateItem(request_drawer);



                            } else if (drawerItem.getIdentifier() == 3) {

                                bottomNavigation.setSelectedIndex(2, true);
                                pager.setCurrentItem(2);
                                bottomNavigation.getBadgeProvider().remove(R.id.bills);

                                bill_drawer.withBadge("0");
                                result.updateItem(bill_drawer);

                                // intent = new Intent(MainActivity.this, ContactListActivity.class);
                            } else if (drawerItem.getIdentifier() == 4) {

                                bottomNavigation.setSelectedIndex(3, true);
                                pager.setCurrentItem(3);
                                bottomNavigation.getBadgeProvider().remove(R.id.notifiactions);

                                notify_drawer.withBadge("0");
                                result.updateItem(notify_drawer);

                            } else if (drawerItem.getIdentifier() == 5) {

                                intent = new Intent(MainActivity.this, Main_Get_Tickets.class);

                            } else if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(MainActivity.this, About_Us.class);
                            } else if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(MainActivity.this, Contact_US.class);
                            } else if (drawerItem.getIdentifier() == 8) {

                                try {
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_SUBJECT, "Khadoom");
                                    String sAux = "\nLet me recommend you this application\n\n";
                                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.redray.khadoomhome \n\n";
                                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                                    startActivity(Intent.createChooser(i, "choose one"));
                                } catch (Exception e) {
                                    //e.toString();
                                }


                            } else if (drawerItem.getIdentifier() == 9) {
                                intent = new Intent(MainActivity.this, Privacy_Activity.class);
                            } else if (drawerItem.getIdentifier() == 10) {


                                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

                                    logout();
                                    dialog_bar.show();

                                } else {
                                    Snackbar snackbar = Snackbar
                                            .make(main_reg_layout, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                                            .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                                }
                                            });

                                    snackbar.show();
                                }


                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }

//                            if (drawerItem instanceof Badgeable) {
//                                Badgeable badgeable = (Badgeable) drawerItem;
//                                if (badgeable.getBadge() != null) {
//                                    //note don't do this if your badge contains a "+"
//                                    //only use toString() if you set the test as String
//                                    int badge = Integer.valueOf(badgeable.getBadge().toString());
//                                    if (badge > 0) {
//                                        badgeable.withBadge(String.valueOf(badge - 1));
//                                        result.updateItem(drawerItem);
//                                    }
//                                }
//                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
//                .withShowDrawerUntilDraggedOpened(true)
                .build();

        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        //RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);
        //   new RecyclerViewCacheUtil<IDrawerItem>().withCacheSize(2).apply(result.getRecyclerView(), result.getDrawerItems());


        result.setSelection(21, false);
    }

    public void nav_PROVIDER(final Bundle savedInstanceState, final Toolbar toolbar) {

        final IProfile profile;
        if (user_pic.equals("")) {
            profile = new ProfileDrawerItem().withName(user_name).withEmail(user_email).withIcon(R.drawable.profile_pic);

        } else {
            profile = new ProfileDrawerItem().withName(user_name).withEmail(user_email).withIcon(user_pic);

        }

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.nav_header)
                .addProfiles(
                        profile,

                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName(getString(R.string.chng_lang))
                                .withIcon(R.drawable.nav_lang).withIdentifier(PROFILE_LANGUAGE),

                        new ProfileSettingDrawerItem().withName(getString(R.string.chng_pass))
                                .withIcon(R.drawable.password_reg).withIdentifier(PROFILE_Password),

                        new ProfileSettingDrawerItem().withName(getString(R.string.remov_acc))
                                .withIcon(R.drawable.nav_remove).withIdentifier(PROFILE_remove_ACC),

                        new ProfileSettingDrawerItem().withName(getString(R.string.upd_profil))
                                .withIcon(R.drawable.company_name_reg).withIdentifier(PROFILE_SETTING)


                )

                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override

                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {

                        if (profile instanceof IDrawerItem && (profile).getIdentifier() == PROFILE_LANGUAGE) {
//                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman")
//                                    .withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));

                            Intent edit_prof = new Intent(getApplicationContext(), Lang_Choose_activity.class);
                            startActivity(edit_prof);


                        } else if (profile instanceof IDrawerItem && (profile).getIdentifier() == PROFILE_Password) {
//                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman")
//                                    .withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));

                            Intent edit_prof = new Intent(getApplicationContext(), Change_Password.class);
                            startActivity(edit_prof);

                        } else if (profile instanceof IDrawerItem && (profile).getIdentifier() == PROFILE_remove_ACC) {

                            Intent edit_prof = new Intent(getApplicationContext(), Remove_Account.class);
                            startActivity(edit_prof);


                        } else if (profile instanceof IDrawerItem && (profile).getIdentifier() == PROFILE_SETTING) {
//                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman")
//                                    .withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));

                            Intent edit_prof = new Intent(getApplicationContext(), Update_Profile_prov_Activity.class);
                            startActivity(edit_prof);

                        }


                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withAlternativeProfileHeaderSwitching(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withAccountHeader(R.layout.material_drawer_compact_persistent_header)
                .withProfileImagesClickable(false)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                // .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(


                        new PrimaryDrawerItem().withName(R.string.servics).withIcon(R.drawable.wrench)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(1)
                                .withSelectable(false),

                        bill_drawer = new PrimaryDrawerItem().withName(R.string.my_bills).withIcon(R.drawable.nav_bills)
                                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge(Bills_parm)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(2).withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.technicans).withIcon(R.drawable.username_reg)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(3).withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.tickts).withIcon(R.drawable.nav_ticket)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(4).withSelectable(false),

                        notify_drawer = new PrimaryDrawerItem().withName(R.string.notifications).withIcon(R.drawable.nav_notify)
                                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge(Notify_parm)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(5).withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.abut_us).withIcon(R.drawable.nav_about)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(6).withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.contct_us).withIcon(R.drawable.phone_reg)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(7).withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.share).withIcon(R.drawable.nav_share)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(8).withSelectable(false),


                        new PrimaryDrawerItem().withName(R.string.privcy).withIcon(R.drawable.password_reg)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(9).withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.log_out).withIcon(R.drawable.nav_logout)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(10).withSelectable(false)


                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(MainActivity.this, Get_All_Services.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(MainActivity.this, Get_Bills_prov_activity.class);

                                bill_drawer.withBadge("0");
                                result.updateItem(bill_drawer);

                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(MainActivity.this, Get_Technicals.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(MainActivity.this, Main_Get_Tickets.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(MainActivity.this, Notifications_History_Act.class);

                                notify_drawer.withBadge("0");
                                result.updateItem(notify_drawer);

                            } else if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(MainActivity.this, About_Us.class);
                            } else if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(MainActivity.this, Contact_US.class);
                            } else if (drawerItem.getIdentifier() == 8) {

                                try {
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_SUBJECT, "Khadoom");
                                    String sAux = "\nLet me recommend you this application\n\n";
                                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.redray.khadoomhome \n\n";
                                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                                    startActivity(Intent.createChooser(i, "choose one"));
                                } catch (Exception e) {
                                    //e.toString();
                                }

                            } else if (drawerItem.getIdentifier() == 9) {

                                intent = new Intent(MainActivity.this, Privacy_Activity.class);

                            } else if (drawerItem.getIdentifier() == 10) {

                                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {

                                    logout();
                                    dialog_bar.show();

                                } else {
                                    Snackbar snackbar = Snackbar
                                            .make(main_reg_layout, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                                            .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                                }
                                            });

                                    snackbar.show();
                                }
                            }

                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }

                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
//                .withShowDrawerUntilDraggedOpened(true)
                .build();

        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        //RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);
        //   new RecyclerViewCacheUtil<IDrawerItem>().withCacheSize(2).apply(result.getRecyclerView(), result.getDrawerItems());


        result.setSelection(21, false);
    }

    public void nav_Technical(final Bundle savedInstanceState, final Toolbar toolbar) {

        final IProfile profile;
        if (user_pic.equals("")) {
            profile = new ProfileDrawerItem().withName(user_name).withEmail(user_email).withIcon(R.drawable.profile_pic);

        } else {
            profile = new ProfileDrawerItem().withName(user_name).withEmail(user_email).withIcon(user_pic);

        }

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.nav_header)
                .addProfiles(
                        profile,

                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName(getString(R.string.chng_lang))
                                .withIcon(R.drawable.nav_lang).withIdentifier(PROFILE_LANGUAGE),

                        new ProfileSettingDrawerItem().withName(getString(R.string.chng_pass))
                                .withIcon(R.drawable.password_reg).withIdentifier(PROFILE_Password)


                )

                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override

                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {

                        if (profile instanceof IDrawerItem && (profile).getIdentifier() == PROFILE_LANGUAGE) {
//                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman")
//                                    .withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));

                            Intent edit_prof = new Intent(getApplicationContext(), Lang_Choose_activity.class);
                            startActivity(edit_prof);


                        } else if (profile instanceof IDrawerItem && (profile).getIdentifier() == PROFILE_Password) {
//                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman")
//                                    .withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));

                            Intent edit_prof = new Intent(getApplicationContext(), Change_Password.class);
                            startActivity(edit_prof);

                        }


                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withAlternativeProfileHeaderSwitching(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withAccountHeader(R.layout.material_drawer_compact_persistent_header)
                .withProfileImagesClickable(false)
                .build();


        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                // .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(

                        order_new_drawer = new PrimaryDrawerItem().withName(R.string.new_orders).withIcon(R.drawable.address_logo)
                                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge(New_Orders_parm)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(1)
                                .withSelectable(false),

                        notify_drawer = new PrimaryDrawerItem().withName(R.string.notifications).withIcon(R.drawable.nav_notify)
                                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge(Notify_parm)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(2)
                                .withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.abut_us).withIcon(R.drawable.nav_about)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(3).withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.contct_us).withIcon(R.drawable.phone_reg)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(4).withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.share).withIcon(R.drawable.nav_share)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(5).withSelectable(false),


                        new PrimaryDrawerItem().withName(R.string.privcy).withIcon(R.drawable.password_reg)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(6).withSelectable(false),

                        new PrimaryDrawerItem().withName(R.string.log_out).withIcon(R.drawable.nav_logout)
                                .withTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textbox_gray)).withIdentifier(7).withSelectable(false)


//                        new ExpandableBadgeDrawerItem().withName("Collapsable Badge")
//                                .withIcon(R.mipmap.ic_launcher).withIdentifier(18).withSelectable(false)
//                                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700))
//                                .withBadge("100").withSubItems(
//                                new SecondaryDrawerItem().withName("CollapsableItem")
//                                        .withLevel(2).withIcon(R.mipmap.ic_launcher).withIdentifier(2000),
//                                new SecondaryDrawerItem().withName("CollapsableItem 2")
//                                        .withLevel(2).withIcon(R.mipmap.ic_launcher).withIdentifier(2001)
//                        ),
//                        new DividerDrawerItem(),
//                        new SwitchDrawerItem().withName("Switch").withIcon(R.mipmap.ic_launcher).withChecked(true)
//                                .withOnCheckedChangeListener(onCheckedChangeListener),
//                        new ToggleDrawerItem().withName("Toggle").withIcon(R.mipmap.ic_launcher).withChecked(true)
//                                .withOnCheckedChangeListener(onCheckedChangeListener)


                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {

                                bottomNavigation.setSelectedIndex(0, true);
                                pager.setCurrentItem(0);
                                bottomNavigation.getBadgeProvider().remove(R.id.new_orders);

                                order_new_drawer.withBadge("0");
                                result.updateItem(order_new_drawer);

                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(MainActivity.this, Notifications_History_Act.class);

                                notify_drawer.withBadge("0");
                                result.updateItem(notify_drawer);

                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(MainActivity.this, About_Us.class);

                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(MainActivity.this, Contact_US.class);
                                intent.putExtra("Nav_btn", true);
                            } else if (drawerItem.getIdentifier() == 5) {
                                try {
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_SUBJECT, "Khadoom");
                                    String sAux = "\nLet me recommend you this application\n\n";
                                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.redray.khadoomhome \n\n";
                                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                                    startActivity(Intent.createChooser(i, "choose one"));
                                } catch (Exception e) {
                                    //e.toString();
                                }
                            } else if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(MainActivity.this, Privacy_Activity.class);
                            } else if (drawerItem.getIdentifier() == 7) {


                                if (Check_Con.getInstance(getApplicationContext()).isOnline()) {
                                    logout();
                                    dialog_bar.show();
                                } else {
                                    Snackbar snackbar = Snackbar
                                            .make(main_reg_layout, R.string.no_connect, Snackbar.LENGTH_INDEFINITE)
                                            .setAction(R.string.connect_snackbar, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                                }
                                            });

                                    snackbar.show();
                                }
                            }


                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
//                .withShowDrawerUntilDraggedOpened(true)
                .build();

        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        //RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);
        //   new RecyclerViewCacheUtil<IDrawerItem>().withCacheSize(2).apply(result.getRecyclerView(), result.getDrawerItems());


        result.setSelection(21, false);
    }


    public void logout() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("tessssssst", response);


                        try {
                            // JSON Object
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {

                                sessionManager.logoutUser();
                                Intent go_to_login = new Intent(getApplicationContext(), Account_Type.class);
                                go_to_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(go_to_login);

                                Toast.makeText(getApplicationContext(), R.string.suc_logout, Toast.LENGTH_LONG).show();

                                // to clear all notifications
                                NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                nMgr.cancelAll();

                            } else {

                                StringBuilder s = new StringBuilder(150);
                                String street;
                                JSONArray st = obj.getJSONArray("Errors");
                                for (int i = 0; i < st.length(); i++) {
                                    street = st.getString(i);
                                    s.append(street);
                                    s.append("\n");
                                    Log.i("teeest", s.toString());
                                    // loop and add it to array or arraylist
                                }


                                Utility.dialog_error(MainActivity.this, s.toString());

                            }


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            // _errorMsg.setText(e.getMessage());

                            e.printStackTrace();

                        }

                        dialog_bar.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog_bar.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();

            }

        })

        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("userType", sessionManager.getUser_Type());

                Log.e("Params", params.toString());

                return params;
            }

            // TO GET COOKIE FROM HEADER and save it to prefrences
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Map<String, String> responseHeaders = response.headers;
                Log.d("cookies_login", responseHeaders.get("Set-Cookie"));
                sessionManager.setCookie(responseHeaders.get("Set-Cookie"));

                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                headers.put("Cookie", sessionManager.getCookie());

                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private Request.Priority mPriority_check_session = Request.Priority.HIGH;
    //this method to check is session with web service lost and get last notifications
    public void Check_Session() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "checkSession",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("api_res", response);

                        try {

                            // JSON Object

                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {

                                JSONObject data = obj.getJSONObject("Data");
                                Boolean session_stat = data.getBoolean("sessionStatus");
                                if (!session_stat.equals(true)) {
                                    // that's mean session lost

                                    sessionManager.logoutUser();
                                    Intent go_to_login = new Intent(getApplicationContext(), Account_Type.class);
                                    go_to_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(go_to_login);

                                }


                            } else {

                                StringBuilder s = new StringBuilder(100);
                                String street;
                                JSONArray st = obj.getJSONArray("Errors");
                                for (int i = 0; i < st.length(); i++) {
                                    street = st.getString(i);
                                    s.append(street);
                                    s.append("\n");
                                    Log.i("teeest", s.toString());
                                    // loop and add it to array or arraylist
                                }

                                dialog_bar.dismiss();

                                Utility.dialog_error(MainActivity.this, s.toString());

                            }


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            // _errorMsg.setText(e.getMessage());

                            e.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog_bar.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();
                Log.e("errror", error.getMessage() + "");

            }

        })

        {

            @Override
            public Priority getPriority() {
                return mPriority_check_session;
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                // params.put("login", loginJsonObject.toString());

                //   params.put("lang", sessionManager.getLang());
                params.put("userType", sessionManager.getUser_Type());

                Log.e("Params", params.toString());

                return params;
            }

            // TO GET COOKIE FROM HEADER and save it to prefrences
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Map<String, String> responseHeaders = response.headers;
                Log.d("cookies_login", responseHeaders.get("Set-Cookie"));
                sessionManager.setCookie(responseHeaders.get("Set-Cookie"));

                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                headers.put("Cookie", sessionManager.getCookie());

                return headers;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        requestQueue.add(stringRequest);
    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter("MyData"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getExtras().getString("send_count") != null){
                Counters_sections();
            }
        }
    };


    private Request.Priority mPriority_Counters_sections = Request.Priority.LOW;

    //this method to edit order or bill details parts and close order done (finished)
    public void Counters_sections() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.URL + "countUnseen",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("api_res", response);
                        try {

                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("Status");
                            if (status) {

                                JSONObject data = obj.getJSONObject("Data");
                                if (status.equals(true)) {
                                    // that's mean session lost

                                    Bills_parm = data.getString("bills");
                                    Notify_parm = data.getString("notification");
                                    Request_parm = data.getString("acceptedOrders");

                                    New_Orders_parm = data.getString("newOrders");
                                    Finished_Done_parm = data.getString("finishedOrders");


                                    if (sessionManager.getUser_Type().equals("1")) {

                                        request_drawer.withBadge(Request_parm);
                                        result.updateItem(request_drawer);

                                        bill_drawer.withBadge(Bills_parm);
                                        result.updateItem(bill_drawer);

                                        notify_drawer.withBadge(Notify_parm);
                                        result.updateItem(notify_drawer);
                                        Log.d("dsfdsfdsf", Finished_Done_parm);


                                        // bottom requests - bills if 0 remove it
                                        if (Request_parm.equals("0")){
                                            bottomNavigation.getBadgeProvider().remove(R.id.my_Requests);
                                        }else {
                                            provider.show(R.id.my_Requests, Integer.parseInt(Request_parm));
                                        }

                                        if (Bills_parm.equals("0")){
                                            bottomNavigation.getBadgeProvider().remove(R.id.bills);
                                        }else {
                                            provider.show(R.id.bills, Integer.parseInt(Bills_parm));
                                        }



                                    } else if (sessionManager.getUser_Type().equals("2")) {


                                        bill_drawer.withBadge(Bills_parm);
                                        result.updateItem(bill_drawer);

                                        notify_drawer.withBadge(Notify_parm);
                                        result.updateItem(notify_drawer);

                                        // bottom (done) if 0 remove it
                                        if (Finished_Done_parm.equals("0")){
                                            bottomNavigation.getBadgeProvider().remove(R.id.Finished);
                                        }else {
                                            provider.show(R.id.Finished, Integer.parseInt(Finished_Done_parm));
                                        }


                                    } else if (sessionManager.getUser_Type().equals("3")) {


                                        Log.d("dsfdsfdsf", Bills_parm);
                                        order_new_drawer.withBadge(Bills_parm);
                                        result.updateItem(order_new_drawer);

                                        notify_drawer.withBadge(Notify_parm);
                                        result.updateItem(notify_drawer);


                                        // bottom requests - bills   bills if 0 remove it
                                        if (Finished_Done_parm.equals("0")){
                                            bottomNavigation.getBadgeProvider().remove(R.id.Finished);
                                        }else {
                                            provider.show(R.id.Finished, Integer.parseInt(Finished_Done_parm));
                                        }

                                        if (Bills_parm.equals("0")){
                                            bottomNavigation.getBadgeProvider().remove(R.id.Bills);
                                        }else {
                                            provider.show(R.id.Bills, Integer.parseInt(Bills_parm));
                                        }

                                    }// close user types

                                } // if response true


                            } else {

                                StringBuilder s = new StringBuilder(100);
                                String street;
                                JSONArray st = obj.getJSONArray("Errors");
                                for (int i = 0; i < st.length(); i++) {
                                    street = st.getString(i);
                                    s.append(street);
                                    s.append("\n");
                                    Log.i("teeest", s.toString());
                                    // loop and add it to array or arraylist
                                }

                                dialog_bar.dismiss();

                                Utility.dialog_error(MainActivity.this, s.toString());

                            }

                            dialog_bar.dismiss();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            // _errorMsg.setText(e.getMessage());

                            e.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog_bar.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_down, Toast.LENGTH_LONG).show();
                Log.e("errror", error.getMessage() + "");

            }

        })

        {
            @Override
            public Priority getPriority() {
                return mPriority_Counters_sections;
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("userType", sessionManager.getUser_Type());

                Log.e("Params", params.toString());

                return params;
            }

            // TO GET COOKIE FROM HEADER and save it to prefrences
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Map<String, String> responseHeaders = response.headers;
                Log.d("cookies_login", responseHeaders.get("Set-Cookie"));
                sessionManager.setCookie(responseHeaders.get("Set-Cookie"));

                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                headers.put("Cookie", sessionManager.getCookie());

                return headers;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        requestQueue.add(stringRequest);
    }

}
