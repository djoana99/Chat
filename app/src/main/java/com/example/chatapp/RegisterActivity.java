package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.register));
        setSupportActionBar(toolbar);

        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        Button register = findViewById(R.id.register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(email.getText().toString()) ||
                        TextUtils.isEmpty(password.getText().toString()) ||
                        TextUtils.isEmpty(name.getText().toString()) ||
                        password.getText().toString().length() < 6) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.cant_register), Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    User user = new User(name.getText().toString(),
                                            email.getText().toString(),
                                            password.getText().toString(),
                                            "default");

                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                                        finish();
                                                    }
                                                }
                                            });
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, StartActivity.class));
        finish();
    }
}