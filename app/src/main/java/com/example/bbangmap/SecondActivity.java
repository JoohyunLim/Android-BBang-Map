package com.example.bbangmap;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bbangmap.databinding.ActivityMainBinding;
import com.naver.maps.map.NaverMapSdk;
//MAIN
public class SecondActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private BottomNavigationView mBtmView;
    private int mMenuId;
    public static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("yamwqkg05h"));


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

            showSystemUI();

            BottomNavigationView navView = findViewById(R.id.nav_view);

            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_map, R.id.navigation_my_page)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);

            getSupportActionBar().setIcon(R.drawable.topbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        //GET USERNAME
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        Toast toast = Toast.makeText(getApplicationContext(), name+"님, 반갑습니다!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 130);
        toast.show();

    }

    void showSystemUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this); //아애 앱 종료하는 방법 Activity를 싹 끄게 하는 법
    }
}
