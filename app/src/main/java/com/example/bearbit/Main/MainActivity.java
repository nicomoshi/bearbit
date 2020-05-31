package com.example.bearbit.Main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.bearbit.R;
import com.google.android.material.navigation.NavigationView;

import android.util.Log;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.view.MenuItem;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    protected LinearLayout fullLayout;
    protected FrameLayout actContent;


    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mBrands = new ArrayList<>();
    private ArrayList<Integer> mValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Log.d(TAG, "onCreate: started.");



        Log.d(TAG, "onCreate: started.");

        initItems();
    }

    private void initItems() {
        Log.d(TAG, "initItems: preparing items.");

        mBrands.add("GoldenVale");
        mNames.add("Cornflakes");
        mValues.add(300);

        mBrands.add("GoldenVale");
        mNames.add("Cornflakes");
        mValues.add(60);

        mBrands.add("GoldenVale");
        mNames.add("Cornflakes");
        mValues.add(200);

        mBrands.add("GoldenVale");
        mNames.add("Cornflakes");
        mValues.add(20);

        mBrands.add("GoldenVale");
        mNames.add("Cornflakes");
        mValues.add(100);

        mBrands.add("GoldenVale");
        mNames.add("Cornflakes");
        mValues.add(10);

        mBrands.add("GoldenVale");
        mNames.add("Cornflakes");
        mValues.add(20);

        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mBrands, mNames, mValues);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;

        if (id == R.id.nav_home) {
            // Handle the camera action

           //Todo

        } else if (id == R.id.nav_gallery) {
            //Todo
            Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) {
            //Todo
            Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            //Todo
            Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
