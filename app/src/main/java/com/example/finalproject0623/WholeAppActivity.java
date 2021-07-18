package com.example.finalproject0623;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Calendar;

public class WholeAppActivity extends AppCompatActivity {
    private final String TAG = "chloe";
    private static final String KEY = "currentPage",KEYGender = "currentGender";
    private static final String KEYUserName="currentName",KEYAge="currentAge",KEYWeight = "currentWeight",KEYHeight = "currentHeight";
    private Integer mCurrentPage = 0;


    TextView mWeight,mHeight,mAge;
    ImageView mInAge,mDeAge,mInWeight,mDeWeight;
    SeekBar mSeekBarHeight;
    Button mCalculateBMI;
    RelativeLayout mMale,mFemale;
    EditText mKeyYourName;

    int oriWeight = 50;
    int oriAge = 30;
    int keyInProgress;
    String mHeightProgress = "160";
    String genderUser = "0";
    String showOriginWeight = "50";
    String showOriginAge = "30";
    String keyInName;


    BottomNavigationView navigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_enter);
        Log.v(TAG,"onCreate_WholeApp");

        if(savedInstanceState == null){

            //初始化再跳出已設立通知的訊息
            NotificationChannel();


        }

        //宣告介面元件
        mKeyYourName = findViewById(R.id.etKeyYourName);
        mMale = findViewById(R.id.male);
        mFemale = findViewById(R.id.female);

        mAge = findViewById(R.id.currentAge);
        mInAge = findViewById(R.id.incrementAge);
        mDeAge = findViewById(R.id.decrementAge);

        mWeight=findViewById(R.id.currentWeight);
        mInWeight =findViewById(R.id.incremetWeight);
        mDeWeight = findViewById(R.id.decrementWeight);

        mSeekBarHeight = findViewById(R.id.seekbarForHeight);
        mSeekBarHeight.setMax(250);
        mSeekBarHeight.setProgress(160);
        mSeekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                keyInProgress = progress;
                mHeightProgress = String.valueOf(keyInProgress);
                mHeight.setText(mHeightProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mHeight=findViewById(R.id.currentHeight);
        mCalculateBMI = findViewById(R.id.startCalculate);

        mMale.setOnClickListener(this::onClick);
        mFemale.setOnClickListener(this::onClick);
        mInWeight.setOnClickListener(this::onClick);
        mDeWeight.setOnClickListener(this::onClick);
        mInAge.setOnClickListener(this::onClick);
        mDeAge.setOnClickListener(this::onClick);
        mCalculateBMI.setOnClickListener(this::onClick);


        //CHECKBOX: 切fragment/activity (bottom navigation)
        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                if(item.getItemId()==R.id.nav_setting){
                    startActivity(new Intent(WholeAppActivity.this,SettingActivity.class) );
                    Log.v(TAG,"startActivity");
                }

                //若使用者點擊會直接刷新頁面(重開一個activity)
                if(item.getItemId()==R.id.nav_home){
                    finish();
                    startActivity(getIntent());
                }


                switch (item.getItemId()) {
                    case R.id.nav_familyMember:
                        fragment = new FamilyMemberFragment();
                        break;

                    //CHECKBOX:IMPLICIT INTENT(三選一)(跳到健康網站:國民健康署)
                    case R.id.nav_search:
                        openWeb();
                        break;
                }

                mCalculateBMI.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container,fragment).commit();
                return true;
            }
        });

        //CHECKBOX : 存取資料庫 1
        if(savedInstanceState !=null) {



            // 列印使用Bundle保存的currentPage信息
            Log.d(TAG,"saved currentPage: " + savedInstanceState.getInt(KEY,0));
            // 列印新建活動mCurrentPage的值，總為0
            Log.d(TAG,"init mCurrentPage: " + mCurrentPage);
            // ... update code




            keyInName = savedInstanceState.getString(KEYUserName);
            mKeyYourName.setText(keyInName);
            Log.v(TAG,"keyiNname"+keyInName);



            genderUser = savedInstanceState.getString(KEYGender);
            if(genderUser =="Male") {
                //fixme要一直呈現cream色
                Log.v(TAG,"genderUser: "+genderUser);
                mMale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.genderselect));
                mFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background));
            }else if (genderUser=="Female"){
                Log.v(TAG,"genderUser: "+genderUser);
                mFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.genderselect));
                mMale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background));
            }else if(genderUser.equals("0")){
                Log.v(TAG,"genderUser: "+genderUser);
            }else{
                Toast.makeText(WholeAppActivity.this,"gender Error",Toast.LENGTH_SHORT).show();
            }

            showOriginAge= savedInstanceState.getString(KEYAge);
            mAge.setText(showOriginAge);
            Log.v(TAG,showOriginAge);

            mHeightProgress= savedInstanceState.getString(KEYHeight);
            mHeight.setText(mHeightProgress);
            Log.v(TAG, mHeightProgress);

            showOriginWeight = savedInstanceState.getString(KEYWeight);
            mWeight.setText(showOriginWeight);
            Log.v(TAG,showOriginWeight);

        }

    }

    //CHECKBOX: ROTATE 儲存狀態(只新增一筆資料)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 使用key-value對的方式存儲臨時變數
        outState.putInt(KEY, mCurrentPage);
        outState.putString(KEYGender,genderUser);
        outState.putString(KEYAge,showOriginAge);
        outState.putString(KEYHeight,mHeightProgress);
        outState.putString(KEYWeight,showOriginWeight);
        outState.putString(KEYUserName,mKeyYourName.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getInt(KEY);
        savedInstanceState.getString(KEYGender);
        savedInstanceState.getString(KEYAge);
        savedInstanceState.getString(KEYHeight);
        savedInstanceState.getString(KEYWeight);
        savedInstanceState.getString(KEYUserName);

    }



    public void onClick(View v) {

        switch (v.getId()){
            case R.id.male:
                    mCurrentPage+=1;
                    mMale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.genderselect));
                    mFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background));
                    genderUser = "Male";
                break;

            case R.id.female:
                mCurrentPage+=1;
                mFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.genderselect));
                mMale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background));
                genderUser = "Female";
                break;

            case R.id.incremetWeight:

                oriWeight+=1;
                showOriginWeight = String.valueOf(oriWeight);
                mWeight.setText(showOriginWeight);
                break;

            case R.id.decrementWeight:

                oriWeight-=1;
                showOriginWeight = String.valueOf(oriWeight);
                mWeight.setText(showOriginWeight);
                break;

            case R.id.incrementAge:

                oriAge+=1;
                showOriginAge = String.valueOf(oriAge);
                mAge.setText(showOriginAge);
                break;

            case R.id.decrementAge:

                oriAge-=1;
                showOriginAge = String.valueOf(oriAge);
                mAge.setText(showOriginAge);
                break;

            case R.id.startCalculate:
                keyInName = mKeyYourName.getText().toString();
                if(mKeyYourName.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"Please key in your name First",Toast.LENGTH_SHORT).show();
                }else if(genderUser.equals("0")){
                    Toast.makeText(getApplicationContext(),"Select Your Gender First",Toast.LENGTH_SHORT).show();
                }else if(mHeightProgress.equals("0")){
                    Toast.makeText(getApplicationContext(),"Select Your Height First",Toast.LENGTH_SHORT).show();
                }else if(oriAge==0 || oriAge<0){
                    Toast.makeText(getApplicationContext(),"Age is Incorrect",Toast.LENGTH_SHORT).show();
                }else if(oriWeight==0|| oriWeight<0) {
                    Toast.makeText(getApplicationContext(),"Weight Is Incorrect",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(this,BMIActivity.class);
                    intent.putExtra("Gender", genderUser);
                    Log.v(TAG,"FINAL GENDER: "+genderUser);
                    intent.putExtra("Height", mHeightProgress);
                    intent.putExtra("Weight", showOriginWeight);
                    intent.putExtra("Age", showOriginAge);
                    intent.putExtra("KeyInName",keyInName);
                    startActivity(intent);
                }
        }


    }

    //CHECKBOX: 用系統通知 1 (定時間 20:00)(時間可以在Setting選擇開啟或關閉)
    private void NotificationChannel(){
        CharSequence name = "BMI_Measure_Channel";
        String description = "Channel for bmi reminder";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notifyBMI",name,importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,20);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,00);
        if(Calendar.getInstance().after(calendar)){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        Toast.makeText(this,"Reminder set",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(WholeAppActivity.this,NotifyBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
    }
    //CHECKBOX : IMPLICIT INTENT CONTENT
    private void openWeb() {
        Intent intent = new Intent();
        intent.setAction((Intent.ACTION_VIEW));
        intent.setData(Uri.parse("https://health99.hpa.gov.tw/"));
        startActivity(intent);
    }
}