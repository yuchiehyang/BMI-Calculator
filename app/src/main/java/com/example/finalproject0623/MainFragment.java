package com.example.finalproject0623;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "chloe";


    private TextView tv_register;
    private EditText etEmail,etPassword;
    private Button btLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private String newAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"onCreate_MainFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG,"onCreateView_MainFragment");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_register = view.findViewById(R.id.register);
        tv_register.setOnClickListener(this);
        btLogin = view.findViewById(R.id.login);
        btLogin.setOnClickListener(this);
        etEmail = view.findViewById(R.id.email);
        etPassword = view.findViewById(R.id.password);
        progressBar = view.findViewById(R.id.progressBar);

        //CHECKBOX: SAFE ARGS 2
        mAuth = FirebaseAuth.getInstance();
        newAdd = MainFragmentArgs.fromBundle(getArguments()).getMessage();
        Toast.makeText(getContext(),newAdd,Toast.LENGTH_LONG).show();
    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(getContext(),RegisterUser.class));
                break;
            case R.id.login:
                userLogin();
                break;
        }

    }

    private void userLogin() {
        String email = etEmail.getText().toString().trim();
        String passWord = etPassword.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Email is required!!");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter a valid email!!");
            etEmail.requestFocus();
            return;
        }

        if(passWord.isEmpty()){
            etPassword.setError("Password is required!!");
            etPassword.requestFocus();
            return;
        }

        if(passWord.length()<6){
            etPassword.setError("Min password is a 6 character");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //CHECKBOX: FIREBASE AUTH
        mAuth.signInWithEmailAndPassword(email,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userEmail = etEmail.getText().toString().trim();
                    if(!userEmail.isEmpty()){
                        startActivity(new Intent(getContext(), WholeAppActivity.class));
                    }else{
                        Toast.makeText(getContext(),"Failed to login! Please check your credentials!",Toast.LENGTH_LONG).show();
                    }
                    Log.v(TAG,userEmail);

                } else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Failed to login! Please check your credentials!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}