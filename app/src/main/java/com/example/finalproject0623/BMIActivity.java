package com.example.finalproject0623;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BMIActivity extends AppCompatActivity {

    private static final String KEY = "currentPage";
    private static final String TAG = "chloe";
    private Integer mCurrentPage = 0;



    TextView mBMI,mCategory,mGender;
    Button mAgain;
    ImageView resultView;
    String bmi,height,weight,keyInName,age;
    int intAge,intBMI;
    float floatHeight,floatWeight,floatBMI;
    RelativeLayout background;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiactivity);


        getSupportActionBar().setTitle("Your BMI Result");
        Intent intent = getIntent();
        mBMI = findViewById(R.id.bmiDisplay);
        mCategory = findViewById(R.id.bmiCategoryDispaly);
        mAgain = findViewById(R.id.again);

        mAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BMIActivity.this, WholeAppActivity.class);
                startActivity(intent1);
            }
        });

        resultView = findViewById(R.id.healthState);
        mGender = findViewById(R.id.genderDisplay);
        background = findViewById(R.id.contentLayout);

        height = intent.getStringExtra("Height");
        weight = intent.getStringExtra("Weight");
        keyInName = intent.getStringExtra("KeyInName");
        age = intent.getStringExtra("Age");
        intAge = Integer.parseInt(age);

        floatHeight = Float.parseFloat(height);
        floatHeight=floatHeight/100;
        floatWeight=Float.parseFloat(weight);

        //BMI Function
        floatBMI = floatWeight/(floatHeight*floatHeight);
        Log.v(TAG,floatWeight+"&"+floatHeight);
        intBMI = (int)floatBMI;
        bmi = Integer.toString(intBMI);
        mGender.setText(intent.getStringExtra("Gender"));
        mBMI.setText(bmi);

        healthStateDisplay();

        //CHECKBOX : 存取資料庫 1
        if(savedInstanceState ==null){
            showInRecyclerView();
        }

    }

    //CHECKBOX : 存取資料庫 2 (將輸入資料存到FIRESTORE DATABASE)
    private void showInRecyclerView() {

        CollectionReference memberListRef = FirebaseFirestore.getInstance()
                .collection("FamilyMemberBook");

        memberListRef.add(new Member(keyInName, intAge, intBMI));
        mCurrentPage += 1;
        Toast.makeText(this, "New Data added", Toast.LENGTH_SHORT).show();
    }

    private void healthStateDisplay() {
        if(floatBMI<18.5) {
            mCategory.setText("Thinness");
            background.setBackgroundColor(Color.RED);
            resultView.setImageResource(R.drawable.slim);
        }else if(floatBMI<24 && floatBMI>=18.5) {
            mCategory.setText("Normal");
            resultView.setImageResource(R.drawable.healthy);
        } else if(floatBMI<27 && floatBMI>=24) {
            mCategory.setText("OverWight");
            resultView.setImageResource(R.drawable.alert);
        } else if(floatBMI<30 && floatBMI>=27 ) {
            mCategory.setText("Obesity I");
            resultView.setImageResource(R.drawable.fat);
        } else if(floatBMI <35 && floatBMI>=30) {
            mCategory.setText("Obesity II");
            resultView.setImageResource(R.drawable.doctor);
        } else {
            mCategory.setText("Obesity III");
            resultView.setImageResource(R.drawable.ambulance);
        }
    }


    //CHECKBOX: ROTATE 儲存狀態(只新增一筆資料)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 使用key-value對的方式存儲臨時變數
        outState.putInt(KEY, mCurrentPage);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getInt(KEY);
    }
}