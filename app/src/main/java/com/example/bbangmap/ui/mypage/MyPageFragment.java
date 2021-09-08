package com.example.bbangmap.ui.mypage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bbangmap.InfoActivity;
import com.example.bbangmap.MainActivity;
import com.example.bbangmap.QnaActivity;
import com.example.bbangmap.R;

import com.example.bbangmap.SecondActivity;
import com.example.bbangmap.SendMail;
import com.example.bbangmap.databinding.FragmentMypageBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class MyPageFragment extends Fragment {

    private MyPageViewModel myPageViewModel;
    private FragmentMypageBinding binding;
    static final String[] LIST_MENU = {"즐겨찾는 빵집", "내가 만든 빵집지도", "스크랩한 빵집지도"} ;
    static final String[] LIST_MENU2 = {"빵맵소개","문의하기"} ;
    static final String[] LIST_MENU3 = {"로그아웃"} ;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPageViewModel =
                new ViewModelProvider(this).get(MyPageViewModel.class);

        binding = FragmentMypageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView name = binding.name;
        name.setText(SecondActivity.name);


        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;
        ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU2) ;
        ArrayAdapter adapter3 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU3) ;
        ListView listview = (ListView) root.findViewById(R.id.listView);
        listview.setAdapter(adapter);
        ListView listview2 = (ListView) root.findViewById(R.id.listView2);
        listview2.setAdapter(adapter2);
        ListView listview3 = (ListView) root.findViewById(R.id.listView3);
        listview3.setAdapter(adapter3);


        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String strText = (String) parent.getItemAtPosition(position) ;
                if(strText == "빵맵소개"){
              //      Toast toast = Toast.makeText(getActivity().getApplicationContext(), "빵맵소개로 이동", Toast.LENGTH_SHORT);
              //      toast.setGravity(Gravity.TOP, 0, 130);
              //      toast.show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), InfoActivity.class);
                    startActivity(intent);
                }else if(strText == "문의하기"){
                    Intent intent = new Intent(getActivity().getApplicationContext(), QnaActivity.class);
                    startActivity(intent);
                }
            }
        }) ;

        listview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String strText = (String) parent.getItemAtPosition(position) ;
                if(strText == "로그아웃"){
                    showAlertDialogButtonClicked(v);



                }
            }
        }) ;

        return root;
    }

    //로그아웃 확인 팝업
    public void showAlertDialogButtonClicked(View view) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("로그아웃 하시겠습니까?");

        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                //로그아웃하기
                 Toast toast = Toast.makeText(getActivity().getApplicationContext(), "정상적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT);
                 toast.setGravity(Gravity.TOP, 0, 130);
                 toast.show();
                 logOut();

                 dialog.dismiss();
            }
        });

        builder.setNegativeButton("아니오", null);
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void logOut(){
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}