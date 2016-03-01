package com.cyruszhang.cluboard.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyruszhang.cluboard.R;
import com.cyruszhang.cluboard.SampleDispatchActivity;
import com.cyruszhang.cluboard.fragment.ClubCatalogFragment;
import com.cyruszhang.cluboard.fragment.HomeFragment;
import com.cyruszhang.cluboard.parse.Club;
import com.parse.ParseInstallation;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by zhangxinyuan on 1/27/16.
 */
public class Home extends AppCompatActivity {
    public static final int MENU_ITEM_LOGOUT = 1001;
    public static final int MENU_ITEM_REFRESH = 1002;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    SwipeRefreshLayout swipeRefresh;
    ParseQueryAdapter<Club> clubsQueryAdapter;

    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Retrieve current user from Parse.com
        final ParseUser currentUser = ParseUser.getCurrentUser();
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", currentUser);
        installation.saveInBackground();
        // Convert currentUser into String
        String struser = currentUser.getUsername();

        // Find drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle(toolbar);
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        // Find NavigationView
        nvDrawer = (NavigationView) findViewById(R.id.home_navigation_view);
        // Find name field
        View navHeader = nvDrawer.getHeaderView(0);
        TextView drawerName = (TextView) navHeader.findViewById(R.id.drawer_nav_header_name);
        drawerName.setText(struser);
        setupDrawerContent(nvDrawer);

        // TODO: setChecked
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.add(R.id.main_fragment_placeholder, new HomeFragment());
        // Complete the changes added above
        ft.commit();

//        // add new club floating button
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.welcome_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Home.this, NewClub.class);
//                startActivity(intent);
//            }
//        });
    }

    private ActionBarDrawerToggle setupDrawerToggle(Toolbar toolbar) {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, MENU_ITEM_LOGOUT, 102, "Logout");
        MenuItem refresh = menu.add(0, MENU_ITEM_REFRESH, 103, "Refresh");
        refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refresh.setIcon(R.drawable.ic_action_refresh);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // TODO: but why
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (id) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                //go to setting page
                Snackbar.make(coordinatorLayout,
                        "You selected settings", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(Home.this, Setting.class);
                startActivity(intent);
                return true;
            case R.id.action_about:
                Snackbar.make(coordinatorLayout,
                        "You selected About", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case MENU_ITEM_LOGOUT:
                logout();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Logout current user
        ParseUser.logOut();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Intent intentLogout = new Intent(Home.this,
                    SampleDispatchActivity.class);
            intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentLogout);
        } else {
            finish();
        }
        Snackbar.make(coordinatorLayout,
                "You are logged out", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_all_clubs:
                fragmentClass = ClubCatalogFragment.class;
                break;
//            case R.id.nav_followed:
//                break;
//            case R.id.nav_bookmark:
//                break;
            case R.id.nav_new_club:
                Intent intent = new Intent(Home.this, NewClub.class);
                startActivity(intent);
                return;
//            case R.id.nav_manage_clubs:
//                break;
//            case R.id.nav_setting:
//                break;
//            case R.id.nav_logout:
//                break;
//            case R.id.nav_about:
//                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment_placeholder, fragment);
        //TODO: go back to one stack won't change the title
//        // stack check
//        if (!menuItem.isChecked() && menuItem.getItemId() != R.id.nav_home) {
//            transaction.addToBackStack(null);
//        }
        // Commit the transaction
        transaction.commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }
}
