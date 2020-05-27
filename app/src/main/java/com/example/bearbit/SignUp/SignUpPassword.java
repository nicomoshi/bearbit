package com.example.bearbit.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bearbit.R;

public class SignUpPassword extends AppCompatActivity {

    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_password_fragment_layout);

        nextButton = findViewById(R.id.signInPasswordNextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPassword.this, SignUpAge.class);
                SignUpPassword.this.startActivity(intent);
            }
        });


    }
}