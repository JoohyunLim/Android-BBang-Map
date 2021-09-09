package com.example.bbangmap.add;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bbangmap.databinding.FragmentAddBinding;

public class AddFragment extends Fragment {

    private AddViewModel addViewModel;
    private FragmentAddBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel =
                new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageButton button1 = binding.imageButton;
        final ImageButton button2 = binding.imageButton2;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "택배빵집", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msearch.shopping.naver.com/search/all?query=%ED%83%9D%EB%B0%B0%EB%B9%B5%EC%A7%91&frm=NVSHSRC&prevQuery=%EB%B9%B5")); startActivity(intent);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "편의점빵집", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cu.bgfretail.com/product/product.do?category=product&depth2=5&depth3=3")); startActivity(intent);
                startActivity(intent);
            }
        });

/*
        final TextView textView = binding.textAdd;
        addViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
 */
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}