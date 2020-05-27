package com.example.bearbit.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bearbit.Main.MainActivity;
import com.example.bearbit.R;
import com.example.bearbit.SignUp.SignUpName;

public class LoginScreen extends AppCompatActivity {

    private static final String TAG = "LoginScreen";

    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                LoginScreen.this.startActivity(intent);
            }
        });


    }


}
