package com.example.bbangmap.ui.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bbangmap.InfoActivity;
import com.example.bbangmap.QnaActivity;
import com.example.bbangmap.R;

import com.example.bbangmap.databinding.FragmentMypageBinding;

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
/*
        final TextView textView = binding.textMyPage;
        myPageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

 */

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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}