package com.example.finalproject0623;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class NotificationOpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //CHECKBOX: 用系統通知 4 (點擊notice後會回到APP)
        super.onCreate(savedInstanceState);
        Intent intenBacktoKeyInData = new Intent(NotificationOpenActivity.this, WholeAppActivity.class);
        startActivity(intenBacktoKeyInData);
    }
}