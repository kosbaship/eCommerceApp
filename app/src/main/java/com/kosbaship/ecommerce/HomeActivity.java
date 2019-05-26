package com.kosbaship.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.kosbaship.ecommerce.Prevalent.Prevalent;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // (13 - B - 2 - b)
        // enable the logout button so we need to user the paper library
        Paper.init(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        //                      (13 - B)
        // (13 - B - 1)
        // st a title to the toolbar
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        //                                  (13 - C)
        // (13 - C - 1)
        // (13 - C - 2) go to LoginActivity.java
        // initialize the header view to display the user name after he logged in
        // and also the user image
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        // (13 - C - 3)
        // set the current user name for him
        //                  uninstall ur app and run to test it
        userNameTextView.setText(Prevalent.currentOnlineUser.getName());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // (13 - B - 2)
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // (13 - B - 2 - a)
        // modify switch statement
        switch (id){
            case R.id.nav_cart:
                // Handle the Cart action
                break;
            case R.id.nav_orders:

                break;
            case R.id.nav_categories:

                break;
            case R.id.nav_settings:

                break;
            case R.id.nav_logout:
                // (13 - B - 2 - c)
                //enable the logout button and then send the user to the main activity
                Paper.book().destroy();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                // to close all the existing activities we need to do some flags in this Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
