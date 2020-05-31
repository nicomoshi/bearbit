package com.example.bearbit.Register;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bearbit.Main.MainActivity;
import com.example.bearbit.R;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";

    private Button registerNextButton;
    private Button registerBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_register_screen);

        registerNextButton = findViewById(R.id.registerNextButton);
        registerNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                Register.this.startActivity(intent);
            }
        });

        registerBackButton = findViewById(R.id.registerBackButton);
        registerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Register.this.finish();
            }
        });


    }


}
