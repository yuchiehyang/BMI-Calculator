package com.example.finalproject0623;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class Splash extends Fragment {
    private final String TAG = "chloe";

    ImageView registerImageView;
    Button justStart;
    private WifiManager wifiManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        justStart = view.findViewById(R.id.switchEnter);
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerImageView = view.findViewById(R.id.imageView2);

        //ATTENTION : REGISTER (連接FIREBASE，需開啟網路(自動))
        registerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CHECKBOX: NAVIGATION 切FRAGMENT
                NavController navController = Navigation.findNavController(v);

                //CHECKBOX : SAFE ARGS 1
                String newTester ="Nice to meet you, new tester ^^";
                NavDirections action = SplashDirections.actionSplashToMainFragment2(newTester);
                navController.navigate(action);

                //CHECKBOX : 取得WIFI開啟權限(敏感權限)(API要求29以下)
                if(!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }
            }
        });

        //ATTENTION : 如果不想要登入可以直接CLICK進到測量頁
        justStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WholeAppActivity.class);
                startActivity(intent);
            }
        });
    }
}