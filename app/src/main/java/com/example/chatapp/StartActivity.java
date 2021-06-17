package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(StartActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);

        login.setOnClickListener(v -> {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            finish();
        });

        register.setOnClickListener(v -> {
            startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            finish();
        });

    }

}