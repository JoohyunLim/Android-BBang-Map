package com.example.bbangmap.loginScreen;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bbangmap.R;
import com.example.bbangmap.SecondActivity;
import com.example.bbangmap.databinding.ActivityMainBinding;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements ISessionCallback
{

    private ActivityMainBinding binding;
    //private KaKaoCallBack kaKaoCallBack;


    //private BottomNavigationView mBtmView;
    //private boolean LoginState = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //    NaverMapSdk.getInstance(this).setClient(
    //            new NaverMapSdk.NaverCloudPlatformClient("yamwqkg05h"));
    //    Log.d("GET_KEYHASH", getKeyHash(MainActivity.this));

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());



       // setContentView(R.layout.login);
       // Button loginButton = (Button) this.findViewById(R.id.loginButton);

        //kaKaoCallBack = new KaKaoCallBack();
        //Session.getCurrentSession().addCallback(kaKaoCallBack);
        Session.getCurrentSession().addCallback(this);
        Session.getCurrentSession().checkAndImplicitOpen();


        hideSystemUI();
        setContentView(R.layout.login);



     //   loginButton.setOnClickListener(new View.OnClickListener() {
         //   @Override
        //    public void onClick(View v) {
          //      Toast.makeText(getApplicationContext(),"????????? ????????? ?????? ??????!",Toast.LENGTH_SHORT).show();
                //loginButton.performClick();
          //  }
      //  });


        /*
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
            }
        });

         */
/*
        //Login ?????????
        if(LoginState == true){

        showSystemUI();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_map, R.id.navigation_my_page)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        getSupportActionBar().setIcon(R.drawable.topbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        } */
    }

    public void kakaoError(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(this);
    }


    public void onSessionOpened() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int result = errorResult.getErrorCode();

                if (result == ApiErrorCode.CLIENT_ERROR_CODE) kakaoError("???????????? ????????? ??????????????????. ?????? ????????? ?????????.");
                else kakaoError("????????? ?????? ????????? ??????????????????.");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                kakaoError("????????? ???????????????. ?????? ????????? ?????????.");
            }

            @Override
            public void onSuccess(MeV2Response result) {
                Log.d("????????? ?????? : ",result.getNickname()); //?????????

                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("name", result.getNickname());
                startActivity(intent);
                finish();

                //Log.d("????????? ?????? : ",result.getKakaoAccount().getEmail()); //?????????
                //Log.d("????????? ?????? : ",result.getThumbnailImagePath()); //????????? ??????
            }
        });
    }

    @Override
    public void onSessionOpenFailed (KakaoException e){
        kakaoError("????????? ?????? ????????? ??????????????????. ????????? ????????? ??????????????????.");
    }


/*
    //??????????????????API???????????????KEYHASH?????????
    public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    } */
    void hideSystemUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
