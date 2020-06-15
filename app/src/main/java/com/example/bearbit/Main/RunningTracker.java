package com.example.bearbit.Main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.bearbit.CircularImageView;
import com.example.bearbit.Location;
import com.example.bearbit.LocationService;
import com.example.bearbit.Models.User;
import com.example.bearbit.R;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class RunningTracker extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback{


    SupportMapFragment mapFragment;
    GoogleMap mMap;
    Marker marker;
    LocationBroadcastReceiver receiver;
    public User fetchedUser = new User();
    private TextView userNameTextView;
    private CircularImageView userImage;
    private final Handler handler = new Handler();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_tracker);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        getSupportActionBar().setTitle("Running Tracker");


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        receiver = new LocationBroadcastReceiver();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //Req Location Permission
                startLocService();
            }
        } else {
            //Start the Location Service
            startLocService();
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userNameTextView = navigationView.getHeaderView(0).findViewById(R.id.userNameTextView);
        userImage = navigationView.getHeaderView(0).findViewById(R.id.userImage);

        getUser(new MainActivity.UserCallback() {
            @Override
            public void onCallback(Map<String,Object> userMap) {
                fetchedUser.setUid(userMap.get("uid").toString());
                fetchedUser.setName(userMap.get("name").toString());
                fetchedUser.setImage(userMap.get("image").toString());

                userNameTextView.setText(fetchedUser.getName());

                Glide.with(userImage).load(fetchedUser.getImage()).into(userImage);

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        Fragment fragment;

        if (id == R.id.nav_home) {
            // Handle the camera action
            finish();
            startActivity(new Intent(this, MainActivity.class));
            //Todo

        } else if (id == R.id.nav_gallery) {
            //Todo



        } else if (id == R.id.nav_slideshow) {
            //Todo

        } else if (id == R.id.nav_share) {
            //Todo

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void startLocService() {
        IntentFilter filter = new IntentFilter("ACT_LOC");
        registerReceiver(receiver, filter);
        Intent intent = new Intent(RunningTracker.this, LocationService.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //startLocService();
                } else {
                    Toast.makeText(this, "Give me permissions", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ACT_LOC")) {
                double lat = intent.getDoubleExtra("latitude", 0f);
                double longitude = intent.getDoubleExtra("longitude", 0f);
                if (mMap != null) {
                    LatLng latLng = new LatLng(lat, longitude);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = false;
                    Bitmap icon= BitmapFactory.decodeResource(getResources(), R.drawable.red_marker, options);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(icon));

                    if (marker != null)
                        marker.setPosition(latLng);
                    else
                        marker = mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                }
                Toast.makeText(RunningTracker.this, "Latitude is: " + lat + ", Longitude is " + longitude, Toast.LENGTH_LONG).show();
            }
        }
    }

    private Timer timer = null;

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                RunningTracker.this.handler.post(new Runnable() {
                    public void run() {
                        RunningTracker.this.onTick();
                    }
                });
            }
        }, 0, 500);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private Location l = null;

    public void onTick() {
        if (workout != null) {
            workout.onTick();
            updateView();

            if (mTracker != null) {
                Location l2 = mTracker.getLastKnownLocation();
                if (l2 != null && !l2.equals(l)) {
                    l = l2;
                }
            }
        }
    }

    private void doStop() {
        if (timer != null) {
            workout.onStop(workout);
            stopTimer(); // set timer=null;
            mTracker.stopForeground(true); // remove notification
            Intent intent = new Intent(RunActivity.this, DetailActivity.class);
            /*
             * The same activity is used to show details and to save
             * activity they show almost the same information
             */
            intent.putExtra("mode", "save");
            intent.putExtra("ID", mTracker.getActivityId());
            RunActivity.this.startActivityForResult(intent, workout.isPaused() ? 1 : 0);
        }
    }

    public void getUser(final MainActivity.UserCallback myCallback) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Map<String,Object> userMap = new HashMap<>();
                        userMap = task.getResult().getData();
                        myCallback.onCallback(userMap);
                    }
                });

    }
}