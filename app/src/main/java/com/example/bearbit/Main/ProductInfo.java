package com.example.bearbit.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;
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

    private EditText brandEditText;
    private EditText nameEditText;
    private EditText calEditText;
    private EditText proteinEditText;
    private EditText carbsEditText;
    private EditText fatEditText;
    private EditText sugarEditText;

    private Long initCal;
    private double initCarbs;
    private double initProtein;
    private double initFat;
    private double initSugar;
    private Button addProduct;
    private Button erraseButton;
    private TextView gTextView;
    private long currentCal;
    private double currentCarbs;
    private double currentProtein;
    private double currentFat;
    private double currentSugar;
    private LinearLayout coloredTag;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String parent;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        final String qr = getIntent().getStringExtra("qrCode");
        parent = getIntent().getStringExtra("parent");



        System.out.println("CODE " + qr);

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
        coloredTag = findViewById(R.id.infoColorTag);

        brandEditText = findViewById(R.id.brandEditText);
        nameEditText = findViewById(R.id.nameEditText);
        calEditText = findViewById(R.id.calEditText);
        proteinEditText = findViewById(R.id.proteinEditText);
        carbsEditText = findViewById(R.id.carbsEditText);
        fatEditText = findViewById(R.id.fatEditText);
        sugarEditText = findViewById(R.id.sugarEditText);

        brandEditText.setVisibility(View.INVISIBLE);
        nameEditText.setVisibility(View.INVISIBLE);
        calEditText.setVisibility(View.INVISIBLE);
        proteinEditText.setVisibility(View.INVISIBLE);
        carbsEditText.setVisibility(View.INVISIBLE);
        fatEditText.setVisibility(View.INVISIBLE);
        sugarEditText.setVisibility(View.INVISIBLE);

        grams.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //grams.setText(String.valueOf((Double.parseDouble(carbs.getText().toString()) / Double.parseDouble(s.toString()))));
                try{
                    int x = Integer.parseInt(s.toString());

                    currentCal = x * fetchedProduct.cal / 100;
                    cal.setText(currentCal + " kcal");

                    currentProtein = x * fetchedProduct.protein / 100;
                    protein.setText(df2.format(currentProtein));

                    currentCarbs = x * fetchedProduct.carbs / 100;
                    carbs.setText(df2.format(currentCarbs));

                    currentFat = x * fetchedProduct.fat / 100;
                    fat.setText(df2.format(currentFat));

                    currentSugar = x * fetchedProduct.sugar / 100;
                    sugar.setText(df2.format(currentSugar));

                    if (currentCal >= 0 && currentCal < 100) {
                        coloredTag.setBackgroundResource(R.drawable.green_colored_label);
                    }
                    else if (currentCal >= 100 && currentCal < 300) {
                        coloredTag.setBackgroundResource(R.drawable.yellow_colored_label);
                    }
                    else
                    {
                        coloredTag.setBackgroundResource(R.drawable.red_colored_label);
                    }

                }
                catch (Exception e) {
                    currentProtein = initProtein;
                    protein.setText(String.valueOf(currentProtein));

                    currentCarbs = initCarbs;
                    carbs.setText(String.valueOf(currentCarbs));

                    currentFat = initFat;
                    fat.setText(String.valueOf(currentFat));

                    currentSugar = initSugar;
                    sugar.setText(String.valueOf(currentSugar));

                    if (currentCal >= 0 && currentCal < 100) {
                        coloredTag.setBackgroundResource(R.drawable.green_colored_label);
                    }
                    else if (currentCal >= 100 && currentCal < 300) {
                        coloredTag.setBackgroundResource(R.drawable.yellow_colored_label);
                    }
                    else
                    {
                        coloredTag.setBackgroundResource(R.drawable.red_colored_label);
                    }
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
                if (userProducts.isEmpty())
                {
                    brand.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    cal.setVisibility(View.INVISIBLE);
                    protein.setVisibility(View.INVISIBLE);
                    carbs.setVisibility(View.INVISIBLE);
                    fat.setVisibility(View.INVISIBLE);
                    sugar.setVisibility(View.INVISIBLE);
                    grams.setVisibility(View.INVISIBLE);

                    brandEditText.setVisibility(View.VISIBLE);
                    nameEditText.setVisibility(View.VISIBLE);
                    calEditText.setVisibility(View.VISIBLE);
                    proteinEditText.setVisibility(View.VISIBLE);
                    carbsEditText.setVisibility(View.VISIBLE);
                    fatEditText.setVisibility(View.VISIBLE);
                    sugarEditText.setVisibility(View.VISIBLE);

                }
                else
                {
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
                    initFat = fetchedProduct.fat;
                    initCarbs = fetchedProduct.carbs;
                    initProtein = fetchedProduct.protein;
                    initSugar = fetchedProduct.sugar;

                    currentCal = fetchedProduct.cal;
                    if (currentCal >= 0 && currentCal < 100) {
                        coloredTag.setBackgroundResource(R.drawable.green_colored_label);
                    }
                    else if (currentCal >= 100 && currentCal < 300) {
                        coloredTag.setBackgroundResource(R.drawable.yellow_colored_label);
                    }
                    else
                    {
                        coloredTag.setBackgroundResource(R.drawable.red_colored_label);
                    }

                }

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
                if (fetchedProduct.getQr() == null)
                {
                    if(!TextUtils.isEmpty(brandEditText.getText()) && !TextUtils.isEmpty(nameEditText.getText()) &&
                            !TextUtils.isEmpty(calEditText.getText()) && !TextUtils.isEmpty(proteinEditText.getText()) &&
                            !TextUtils.isEmpty(carbsEditText.getText()) && !TextUtils.isEmpty(fatEditText.getText()) &&
                            !TextUtils.isEmpty(sugarEditText.getText())) {
                        fetchedProduct.setQr(qr);
                        fetchedProduct.setBrand(brandEditText.getText().toString());
                        fetchedProduct.setName(nameEditText.getText().toString());
                        fetchedProduct.setCal(Long.valueOf(calEditText.getText().toString()));
                        fetchedProduct.setProtein(Double.valueOf(proteinEditText.getText().toString()));
                        fetchedProduct.setCarbs(Double.valueOf(carbsEditText.getText().toString()));
                        fetchedProduct.setFat(Double.valueOf(fatEditText.getText().toString()));
                        fetchedProduct.setSugar(Double.valueOf(sugarEditText.getText().toString()));
                        addProduct(fetchedProduct);
                        addNewProduct(fetchedProduct);
                        finish();
                    }
                    else {
                        Toast.makeText(ProductInfo.this, "Complete All Fields",Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    fetchedProduct.setCal(currentCal);
                    addProduct(fetchedProduct);
                    finish();
                }

            }
        });

        try{
            if(parent.equals("main"))
            {
                erraseButton.setVisibility(View.VISIBLE);
                addProduct.setVisibility(View.INVISIBLE);
                grams.setVisibility(View.INVISIBLE);
                gTextView.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception e) {

        }


    }

    private void addNewProduct(Product fetchedProduct) {

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

    private void addProduct(Product fetchedProduct) {

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
                                Log.w(TAG, "Not in database, Add new", task.getException());
                            }
                        }
                });

    }



    private interface MyCallback {
        void onCallback(List<Map<String,Object>>  userProducts);
    }



}