package com.example.bearbit.Main;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.bearbit.CircularImageView;
import com.example.bearbit.Login.LoginScreen;
import com.example.bearbit.Models.Product;
import com.example.bearbit.Models.User;
import com.example.bearbit.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    protected LinearLayout fullLayout;
    protected FrameLayout actContent;

    public User fetchedUser = new User();
    private Button button;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirestoreRecyclerAdapter adapter;
    private TextView calTextView;
    private TextView calInfoTextView;
    private TextView userNameTextView;
    private CircularImageView userImage;
    private View imageView;
    private LinearLayout colorTag;



    private Button addButton;
    private int currentCal;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mBrands = new ArrayList<>();
    private ArrayList<Integer> mValues = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        currentCal = 0;
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentCal = 0;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        getSupportActionBar().setTitle("Calorie Tracker");

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Location Permissions
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        recyclerView = findViewById(R.id.recycler_view);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();

        addButton = findViewById(R.id.addProductButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProduct.class);
                startActivity(intent);
            }
        });

        calTextView = findViewById(R.id.calories_textview);
        calTextView.setText(currentCal + " kcal");

        calInfoTextView = findViewById(R.id.calories_info_textview);
        calInfoTextView.setText(1800 - currentCal + " kcal");

        userNameTextView = navigationView.getHeaderView(0).findViewById(R.id.userNameTextView);
        userImage = navigationView.getHeaderView(0).findViewById(R.id.userImage);

        // Get logged user data
        getUser(new UserCallback() {
            @Override
            public void onCallback(Map<String,Object> userMap) {
                fetchedUser.setUid(user.getUid());
                fetchedUser.setName(userMap.get("name").toString());
                fetchedUser.setImage(userMap.get("image").toString());

                userNameTextView.setText(fetchedUser.getName());

                Glide.with(userImage).load(fetchedUser.getImage()).into(userImage);

            }
        });

    }

    // Ger User Products and use FirebaseRecyclerView to display
    private void fetch() {
        Query query = FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("products");


        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Product, ViewHolder>(options) {

            @Override
            public void onDataChanged() {
                System.out.println("Data Changed");
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                System.out.println(e);
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_listitem, group, false);

                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, final int position, final Product model) {
                holder.setName(model.getName());
                holder.setBrand(model.getBrand());
                holder.setCal(model.getCal());

                calTextView = findViewById(R.id.calories_textview);
                colorTag = holder.itemView.findViewById(R.id.colorTag);
                currentCal += model.getCal();
                calTextView.setText(currentCal + " kcal");
                calInfoTextView = findViewById(R.id.calories_info_textview);
                calInfoTextView.setText(1800 - currentCal + " kcal for goal");

                if (currentCal >= 0 && currentCal < 100) {
                    colorTag.setBackgroundResource(R.drawable.green_colored_label);
                }
                else if (currentCal >= 100 && currentCal < 300) {
                    colorTag.setBackgroundResource(R.drawable.yellow_colored_label);
                }
                else
                {
                    colorTag.setBackgroundResource(R.drawable.red_colored_label);
                }


                holder.rootCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ProductInfo.class);
                        intent.putExtra("qrCode", model.getQr());
                        intent.putExtra("parent", "main");
                        startActivityForResult(intent, 1);

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public void getUser(final UserCallback myCallback) {

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

    public interface UserCallback {
        void onCallback(Map<String,Object>  userMap);
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
            // This activity

        } else if (id == R.id.nav_gallery) {
            //Running Tracker
            finish();
            startActivity(new Intent(this, RunningTracker.class));
            overridePendingTransition(R.anim.fadeout, R.anim.fadein);

        } else if (id == R.id.nav_slideshow) {
            //Not Implemented
            Toast.makeText(MainActivity.this, "Not Implemented",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            //Logout
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, LoginScreen.class));
            overridePendingTransition(R.anim.fadeout, R.anim.fadein);
            Toast.makeText(MainActivity.this, "Logging Out",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public CardView rootCardView;
        public View itemView;

        public TextView nameTextView;
        public TextView brandTextView;
        public TextView calTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            rootCardView = itemView.findViewById(R.id.item_card_view);
            nameTextView = itemView.findViewById(R.id.todayText);
            brandTextView = itemView.findViewById(R.id.item_brand);
            calTextView = itemView.findViewById(R.id.item_value);
        }

        public void setName(String name) {
            nameTextView.setText(name);
        }

        public void setBrand(String brand) {
            brandTextView.setText(brand);
        }

        public void setCal(Long cal) {
            String display = cal + " kcal";
            calTextView.setText(display);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            overridePendingTransition(R.anim.fadeout, R.anim.fadein);
            this.finish();

        }
    }



}
