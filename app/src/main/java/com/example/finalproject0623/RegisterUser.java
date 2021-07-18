package com.example.finalproject0623;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "chloe";
    private TextView tv_banner;
    private EditText etName,etEmail,etPassword;
    private ProgressBar progressBar2;
    private Button btRegister;
    private FirebaseAuth mAuth;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //CHECKBOX :敏感權限
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_banner = findViewById(R.id.banner);
        tv_banner.setOnClickListener(this);
        btRegister = findViewById(R.id.registerUser);
        btRegister.setOnClickListener(this);
        etName = findViewById(R.id.newUserName);
        etEmail = findViewById(R.id.newEmail_address);
        etPassword = findViewById(R.id.newPassword);

        progressBar2 = findViewById(R.id.progressBar2);

        //CHECKBOX :取得FIREBASE資料
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.banner:
                //CHECKBOX : 切 Activity
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
        }
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(name.isEmpty()){
            etName.setError("Name is required!!");
            etName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            etEmail.setError("Email is required!!");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please provide valid email!!");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPassword.setError("Password is required!!");
            etPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            etPassword.setError("Min password length should be 6 character!!");
            etPassword.requestFocus();
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //ATTENTION : FIREBASE設定取得 NAME & EMAIL(User.class)
                            User user = new User(name,email);
                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this,"Successfully registered !",Toast.LENGTH_LONG).show();
                                        progressBar2.setVisibility((View.GONE));
                                    }else{
                                        Toast.makeText(RegisterUser.this,"Failed to register!! Try Again",Toast.LENGTH_LONG).show();
                                        progressBar2.setVisibility((View.GONE));
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterUser.this,"Failed to register!! Try Again2",Toast.LENGTH_LONG).show();
                            progressBar2.setVisibility((View.GONE));
                        }
                    }
                });
    }
}