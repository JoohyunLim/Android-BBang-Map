package com.example.bbangmap.ui.map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bbangmap.AddActivity;
import com.example.bbangmap.MainActivity;
import com.example.bbangmap.R;
import com.example.bbangmap.databinding.FragmentMapBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private MapView mapView ;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapView =  (MapView) root.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        final FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }
        });
/*
        final TextView textView = binding.textMap;
        mapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
 */
        return root;
    }

    private void setMarker(Marker marker,  String name, double lat, double lng)
    {
        //아이콘 지정
        marker.setCaptionMinZoom(14);
        marker.setCaptionText(name);
        marker.setIcon(OverlayImage.fromResource(R.drawable.icon));
        marker.setHeight(90);
        marker.setWidth(90);
        //마커의 투명도
        marker.setAlpha(1f);
        //마커 위치
        marker.setPosition(new LatLng(lat, lng));
        //마커 표시
        marker.setMap(naverMap);
    }
    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    /*
    @Override
    public void onStop(){
        super.onStop();
        mapView.onStop();
    }
    */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        naverMap.setMaxZoom(18);
        naverMap.setMinZoom(6);
        naverMap.getUiSettings().setLocationButtonEnabled(true);
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);


        //용인, 수원
        Marker marker0 = new Marker();
        setMarker(marker0, "오봉베르", 37.293913587307586, 127.05516634679148);
        Marker marker1 = new Marker();
        setMarker(marker1, "디어필립", 37.321681442560546, 127.09447682669928);
        Marker marker2 = new Marker();
        setMarker(marker2, "본누벨베이커리", 37.30483867346377, 127.07520066093477);
        Marker marker3 = new Marker();
        setMarker(marker3, "씨투베이커리 본점", 37.317721439210494, 127.0684622693322);
        Marker marker4 = new Marker();
        setMarker(marker4, "르디투어", 37.3121372340001, 127.05494489737711);
        Marker marker5 = new Marker();
        setMarker(marker5, "하구영베이커리", 37.321315195444676, 127.09361943951407);
        Marker marker6 = new Marker();
        setMarker(marker6, "올바른단팥빵&고로케", 37.31990920240392, 127.11530384331465);
        Marker marker7 = new Marker();
        setMarker(marker7, "아일랜드15", 37.29636639148425, 127.08036898380995);
        Marker marker8 = new Marker();
        setMarker(marker8, "델리봉봉", 37.29641146855036, 127.06829652117655);
        Marker marker9 = new Marker();
        setMarker(marker9, "뺑오르방 광교카페거리점", 37.29489629080948, 127.05667296040775);
        Marker marker10 = new Marker();
        setMarker(marker10, "몽소베이커리앤카페", 37.30033387016423, 127.04580794992314);
        Marker marker11 = new Marker();
        setMarker(marker11, "고당팜베이커리", 37.285846787875485, 127.06369200837575);
        Marker marker12 = new Marker();
        setMarker(marker12, "하얀풍차제과 매탄점", 37.266185367722784, 127.03815651355539);
        Marker marker13 = new Marker();
        setMarker(marker13, "삐에스몽테 제빵소", 37.24528578728469, 126.97673517421977);
        Marker marker14 = new Marker();
        setMarker(marker14, "브레드쿠쿰", 37.27843452175131, 127.09009122777213);
        Marker marker15 = new Marker();
        setMarker(marker15, "칼리오페", 37.24813566780824, 127.19283833369533);
        Marker marker16 = new Marker();
        setMarker(marker16, "옐로오븐", 37.30881456607982, 127.07613786358962);

        //강북
        Marker marker20 = new Marker();
        setMarker(marker20, "까미노빵집", 37.64611799979539, 127.0078847553202);
        Marker marker21 = new Marker();
        setMarker(marker21, "패멩베이커리", 37.64801175237896, 127.0356465450381);
        Marker marker22 = new Marker();
        setMarker(marker22, "글림", 37.64883904901911, 127.03516165273065);
        Marker marker23 = new Marker();
        setMarker(marker23, "Bun418", 37.637570703698344, 127.02634331990289);
        Marker marker24 = new Marker();
        setMarker(marker24, "브래드마티스", 37.61241878229841, 127.02726301155154);

        //의정부, 기타
        Marker marker100 = new Marker();
        setMarker(marker100, "르뱅브래드", 37.751227793096916, 127.04184992023401);
        Marker marker101 = new Marker();
        setMarker(marker101, "팡도리노베이커리", 37.59724420471355, 127.09323142805576);

    }

    public void onRequestPermissionResult(int requestCode,
                                          @NonNull String[] permissions, @NonNull int[] grantResults){
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)){
            if(!locationSource.isActivated()){
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults
        );
    }

}