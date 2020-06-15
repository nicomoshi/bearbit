package com.example.bearbit.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bearbit.Models.Product;
import com.example.bearbit.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class AddProduct extends AppCompatActivity {

    private Button qrButton;
    private Class<?> mClss;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirestoreRecyclerAdapter adapter;
    private TextView listInfo;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mBrands = new ArrayList<>();
    private ArrayList<Integer> mValues = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
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
        setContentView(R.layout.add_product);

        Toolbar toolbar = findViewById(R.id.addToolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        getSupportActionBar().setTitle(null);

        qrButton = findViewById(R.id.addProductButton);

        // Check QR Code
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    launchSimpleActivity(v);

            }
        });

        listInfo = findViewById(R.id.todayText);
        listInfo.setText("Products");

        recyclerView = findViewById(R.id.recycler_view);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();

    }



    private void fetch() {
        Query query = FirebaseFirestore.getInstance()
                .collection("products")
                .limit(50);

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

//        FirebaseRecyclerOptions<Model> options =
//                new FirebaseRecyclerOptions.Builder<Model>()
//

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

                return new AddProduct.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, final int position, final Product model) {
                holder.setName(model.getName());
                holder.setBrand(model.getBrand());
                holder.setCal(model.getCal());

                holder.rootCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddProduct.this, ProductInfo.class);
                        intent.putExtra("qrCode", model.getQr());
                        intent.putExtra("parent", "add");
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public CardView rootCardView;

        public TextView nameTextView;
        public TextView brandTextView;
        public TextView calTextView;

        public ViewHolder(View itemView) {
            super(itemView);
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

    public void launchSimpleActivity(View v) {
        launchActivity(SimpleScannerActivity.class);
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }






}