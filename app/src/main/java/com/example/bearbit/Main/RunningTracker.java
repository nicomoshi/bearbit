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
import com.example.bearbit.LocationService;
import com.example.bearbit.Models.User;
import com.example.bearbit.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.time.Duration;
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
    Double latitude;
    Double longitude;
    Double initialLat;
    Double initialLon;
    Double finalLat;
    Double finalLon;
    Button startButton;
    Button stopButton;
    private CardView resultsCardView;
    private TextView timerTextView;
    private TextView distanceTextView;
    private boolean status;
    private TextView caloriesTextView;
    private TextView paceTextView;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    TextView timerText;
    long starttime = 0;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    // Timer Handler
    final Handler h = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            long millis = System.currentTimeMillis() - starttime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds     = seconds % 60;

            timerText.setText(String.format("%d:%02d", minutes, seconds));
            return false;
        }
    });
    //runs without timer be reposting self
    Handler h2 = new Handler();
    Runnable run = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - starttime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds     = seconds % 60;


            h2.postDelayed(this, 500);
        }
    };

    //tells handler to send a message
    class firstTask extends TimerTask {

        @Override
        public void run() {
            h.sendEmptyMessage(0);
        }
    };

    //tells activity to run on ui thread
    class secondTask extends TimerTask {

        @Override
        public void run() {
            RunningTracker.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    long millis = System.currentTimeMillis() - starttime;
                    int seconds = (int) (millis / 1000);
                    int minutes = seconds / 60;
                    seconds     = seconds % 60;

                }
            });
        }
    };


    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_tracker);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        Toolbar toolbar = findViewById(R.id.toolbar);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        timerTextView = findViewById(R.id.timeTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        paceTextView = findViewById(R.id.paceTextView);

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

        userNameTextView = navigationView.getHeaderView(0).findViewById(R.id.userNameTextView);
        userImage = navigationView.getHeaderView(0).findViewById(R.id.userImage);

        status = false;

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

        timerText = findViewById(R.id.text);




        resultsCardView = findViewById(R.id.resultsCardView);

        resultsCardView.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude == null) {
                    Toast.makeText(RunningTracker.this, "Tracking Location", Toast.LENGTH_LONG).show();
                }
                else {
                    resultsCardView.setVisibility(View.INVISIBLE);
                    starttime = System.currentTimeMillis();
                    status = true;
                    timer = new Timer();
                    timer.schedule(new firstTask(), 0, 500);
                    timer.schedule(new secondTask(), 0, 500);
                    h2.postDelayed(run, 0);
                    stopButton.setVisibility(View.VISIBLE);
                    startButton.setVisibility(View.INVISIBLE);
                    initialLat = latitude;
                    initialLon = longitude;
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = false;
                timer.cancel();
                timer.purge();
                h2.removeCallbacks(run);
                startButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                finalLat = latitude;
                finalLon = longitude;
                String duration = timerText.getText().toString();
                String[] tokens = duration.split(":");
                int hours, minutes, seconds;
                if (tokens.length > 2) {
                    hours = Integer.parseInt(tokens[0]);
                    minutes = Integer.parseInt(tokens[1]);
                    seconds = Integer.parseInt(tokens[2]);
                }
                else
                {
                    hours = 0;
                    minutes = Integer.parseInt(tokens[0]);
                    seconds = Integer.parseInt(tokens[1]);
                }

                int durationHours = hours + minutes / 60 + seconds / 3600;
                double distance = getDistance(initialLat, initialLon, finalLat, finalLon);
                double pace;
                if (durationHours > 0.2)
                {
                    pace = distance / durationHours;
                }
                else
                {
                  pace = 0;
                }
                double calories = pace * 70;

                resultsCardView.setVisibility(View.VISIBLE);
                timerTextView.setText(timerText.getText());
                distanceTextView.setText(df2.format(distance));
                paceTextView.setText(df2.format(pace));
                caloriesTextView.setText(df2.format(Math.round(calories)));
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
            overridePendingTransition(R.anim.fadeout, R.anim.fadein);
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
        timer.cancel();
        timer.purge();
        h2.removeCallbacks(run);
        unregisterReceiver(receiver);
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ACT_LOC")) {
                latitude = intent.getDoubleExtra("latitude", 0f);
                longitude = intent.getDoubleExtra("longitude", 0f);
                if (mMap != null) {
                    LatLng latLng = new LatLng(latitude, longitude);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = false;
                    Bitmap icon;
                    MarkerOptions markerOptions;
                    if (status) {
                        icon= BitmapFactory.decodeResource(getResources(), R.drawable.green_marker, options);
                        markerOptions = new MarkerOptions();
                        markerOptions.position(latLng)
                                .icon(BitmapDescriptorFactory.fromBitmap(icon));
                    }
                    else {
                        icon= BitmapFactory.decodeResource(getResources(), R.drawable.red_marker, options);
                        markerOptions = new MarkerOptions();
                        markerOptions.position(latLng)
                                .icon(BitmapDescriptorFactory.fromBitmap(icon));
                    }

                    if (marker != null)
                    {
                        marker.setPosition(latLng);
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                    }
                    else {
                        marker = mMap.addMarker(markerOptions);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    }

                }

            }
        }
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double p = 0.017453292519943295;    // Math.PI / 180
        double a = 0.5 - Math.cos((lat2 - lat1) * p)/2 +
                Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                        (1 - Math.cos((lon2 - lon1) * p))/2;

        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
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