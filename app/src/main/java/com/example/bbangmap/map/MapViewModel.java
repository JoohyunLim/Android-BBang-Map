package com.example.bbangmap.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {

   private MutableLiveData<String> mText;

    public MapViewModel() {
       mText = new MutableLiveData<>();
       mText.setValue("주변빵지도/지역선택/지도선택(현재위치,내가만든지도)");
    }
    public LiveData<String> getText() {
        return mText;
    }
}