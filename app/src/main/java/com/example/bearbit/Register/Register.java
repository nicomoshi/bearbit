package com.example.bearbit.Register;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bearbit.Login.LoginScreen;
import com.example.bearbit.Main.MainActivity;
import com.example.bearbit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";

    private Button registerNextButton;
    private Button registerBackButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_register_screen);

        mAuth = FirebaseAuth.getInstance();

        registerNextButton = findViewById(R.id.registerNextButton);
        registerBackButton = findViewById(R.id.registerBackButton);
        emailEditText = findViewById(R.id.registerEmailEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        nameEditText = findViewById(R.id.registerNameEditText);

        // Register new user with firebase
        registerNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                // Register and save user data
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Map<String, Object> userMap = new HashMap<>();
                                    // Setting User Details to Database
                                    userMap.put("uid", user.getUid());
                                    userMap.put("name", nameEditText.getText().toString().trim());
                                    userMap.put("image", "");
                                    addNewUser(userMap);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        // Back to Login Screen
        registerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register.this.finish();
            }
        });


    }

    public void addNewUser(Map<String, Object> userMap) {
        // Check if user is logged in on google callback
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Add user data to Database
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(mAuth.getUid())
                    .set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    System.out.println("Success Login");
                }
            });
            updateUI(user);
            // User is signed in
        } else {
            // No user is signed in
        }
    }

    public void updateUI(FirebaseUser user) {
        if (user != null)
        {
            // Open MainActivity
            finish();
            Intent intent = new Intent(Register.this, MainActivity.class);
            Register.this.startActivity(intent);
            overridePendingTransition(R.anim.fadeout, R.anim.fadein);
        }
    }


}
