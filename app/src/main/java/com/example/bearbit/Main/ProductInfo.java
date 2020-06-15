package com.example.bearbit.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bearbit.Models.Product;
import com.example.bearbit.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.opencensus.tags.Tag;

public class ProductInfo extends AppCompatActivity {

    private Product fetchedProduct = new Product();
    private static final String TAG = "ProductInfo";

    private TextView brand;
    private TextView name;
    private TextView cal;
    private TextView protein;
    private TextView carbs;
    private TextView fat;
    private TextView sugar;
    private EditText grams;
    private Long initCal;
    private Button addProduct;
    private Button erraseButton;
    private TextView gTextView;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        String qr = getIntent().getStringExtra("qrCode");
        parent = getIntent().getStringExtra("parent");

        brand = findViewById(R.id.brandTextView);
        name = findViewById(R.id.nameTextBox);
        cal = findViewById(R.id.calTextBox);
        protein = findViewById(R.id.proteinTextBox);
        carbs = findViewById(R.id.carbsTextBox);
        fat = findViewById(R.id.fatTextBox);
        sugar = findViewById(R.id.sugarTextBox);
        grams = findViewById(R.id.gramsEditText);
        addProduct = findViewById(R.id.addNewProduct);
        erraseButton = findViewById(R.id.erraseButton);
        gTextView = findViewById(R.id.gTextView);



        grams.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //grams.setText(String.valueOf((Double.parseDouble(carbs.getText().toString()) / Double.parseDouble(s.toString()))));
                try{
                    int x = Integer.parseInt(s.toString());
                    cal.setText(String.valueOf(x * fetchedProduct.cal / 100) + " kcal");
                }
                catch (Exception e) {
                    cal.setText(String.valueOf(initCal) + " kcal");
                }



            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }


        });

        fetch(new MyCallback() {
            @Override
            public void onCallback(List<Map<String,Object>> userProducts) {
                fetchedProduct.setQr(userProducts.get(0).get("qr").toString());
                fetchedProduct.setName(userProducts.get(0).get("name").toString());
                fetchedProduct.setBrand(userProducts.get(0).get("brand").toString());
                fetchedProduct.setCarbs(Double.parseDouble(userProducts.get(0).get("carbs").toString()));
                fetchedProduct.setProtein(Double.parseDouble(userProducts.get(0).get("protein").toString()));
                fetchedProduct.setFat(Double.parseDouble(userProducts.get(0).get("fat").toString()));
                fetchedProduct.setSugar(Double.parseDouble(userProducts.get(0).get("sugar").toString()));
                fetchedProduct.setCal(Long.parseLong(userProducts.get(0).get("cal").toString()));
                brand.setText(fetchedProduct.brand);
                name.setText(fetchedProduct.name);
                cal.setText(fetchedProduct.cal.toString() + " kcal");
                protein.setText(String.valueOf(fetchedProduct.protein));
                carbs.setText(String.valueOf(fetchedProduct.carbs));
                fat.setText(String.valueOf(fetchedProduct.fat));
                sugar.setText(String.valueOf(fetchedProduct.sugar));
                initCal = fetchedProduct.cal;
            }
        }, qr);

        Toolbar toolbar = findViewById(R.id.infoToolBar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        erraseButton.setVisibility(View.INVISIBLE);



        erraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erraseProduct(fetchedProduct);
                setResult(RESULT_OK, null);
                finish();
            }
        });


        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(fetchedProduct);
                finish();
            }
        });

        if(parent.equals("main"))
        {
            erraseButton.setVisibility(View.VISIBLE);
            addProduct.setVisibility(View.INVISIBLE);
            grams.setVisibility(View.INVISIBLE);
            gTextView.setVisibility(View.INVISIBLE);
        }





    }

    private void addProduct(Product fetchedProduct) {
        // Create a new user with a first and last name
        Map<String, Object> product = new HashMap<>();
        product.put("qr", fetchedProduct.getQr());
        product.put("brand", fetchedProduct.getBrand());
        product.put("name", fetchedProduct.getName());
        product.put("cal", fetchedProduct.getCal());
        product.put("protein", fetchedProduct.getProtein());
        product.put("carbs", fetchedProduct.getCarbs());
        product.put("fat", fetchedProduct.getFat());
        product.put("sugar", fetchedProduct.getSugar());


        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("products")
                .document(fetchedProduct.getQr())
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Success Login");
                    }
                });
    }

    private void erraseProduct(Product fetchedProduct) {

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("products")
                .document(fetchedProduct.getQr())
                .delete();
    }

    private void fetch(final MyCallback myCallback, String qr) {

        Product product = new Product();
        final List<Map<String,Object>> userProducts = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .whereEqualTo("qr", qr)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    userProducts.add(document.getData());

                                }
                                myCallback.onCallback(userProducts);
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                });

    }



    private interface MyCallback {
        void onCallback(List<Map<String,Object>>  userProducts);
    }



}