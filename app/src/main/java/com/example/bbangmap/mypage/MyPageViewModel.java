package com.example.bbangmap.mypage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyPageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyPageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("별표빵집/지도관리/빵집추가신청");
    }

    public LiveData<String> getText() {
        return mText;
    }
}