package com.example.bbangmap.map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.bbangmap.R;
import com.example.bbangmap.databinding.FragmentMapBinding;
import com.example.bbangmap.map.database.AppDatabase;
import com.example.bbangmap.map.database.Bakery;
import com.example.bbangmap.map.database.BakeryDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private MapView mapView ;
    private AppDatabase db;
    private BakeryDao mBakeryDao;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();

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

        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.map_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        final FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }
        });


        //빵집DB
        db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "bakery-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        mBakeryDao = db.bakeryDao();

        //빵집추가
        addBakeryToDB();

        //빵집삭제
        //deleteBakeryFromDB(3);
        //mBakeryDao.deleteAll();

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
        //마커리스트에 추가
        mMarkerArray.add(marker);
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

        //빵집 리스트 마커 전부 추가
        List<Bakery> bakeryList = mBakeryDao.getAll();
        for (int i = 0;  i < bakeryList.size() ; i++) {
            Log.d("###########TEST", bakeryList.get(i).getName());
            Marker marker= new Marker();
            setMarker(marker, bakeryList.get(i).getName(), bakeryList.get(i).getLat(),bakeryList.get(i).getLng());
        }

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

    //BAKERY 삽입
    public void addBakeryToDB(){
        //  mBakeryDao.insert(new Bakery("디어필립", "경기 용인시 수지구 수지로296번길 51-5",37.293913587307586, 127.05516634679148));
        //  mBakeryDao.insert(new Bakery("본누벨베이커리", "경기도 용인시 수지구 수지로 20", 37.30483867346377, 127.07520066093477));
//        mBakeryDao.insert(new Bakery("오봉베르", "경기 수원시 영통구 센트럴파크로127번길 142", 37.293913587307586, 127.05516634679148));
//        mBakeryDao.insert(new Bakery("씨투베이커리 본점", "경기 용인시 수지구 성복2로 166", 37.317721439210494, 127.0684622693322));
//        mBakeryDao.insert(new Bakery("르디투어", "경기 수원시 영통구 이의동 1222-2", 37.3121372340001, 127.05494489737711));
//        mBakeryDao.insert(new Bakery("하구영베이커리", "경기 용인시 수지구 풍덕천로96번길 3-4 1층", 37.321315195444676, 127.09361943951407));
//        mBakeryDao.insert(new Bakery("올바른단팥빵&고로케", "경기 용인시 기흥구 보정로 115 우영프라자", 37.31990920240392, 127.11530384331465));
//        mBakeryDao.insert(new Bakery("아일랜드15", "경기 용인시 수지구 광교호수로378번길 25", 37.29636639148425, 127.08036898380995));
//        mBakeryDao.insert(new Bakery("델리봉봉", "경기 용인시 수지구 광교중앙로295번길 3", 37.29641146855036, 127.06829652117655));
//        mBakeryDao.insert(new Bakery("뺑오르방 광교카페거리점", "경기 수원시 영통구 센트럴파크로128번길 105", 37.29489629080948, 127.05667296040775));
//        mBakeryDao.insert(new Bakery("몽소베이커리앤카페", "경기 수원시 영통구 대학4로 9 리치프라자2 1층", 37.30033387016423, 127.04580794992314));
//        mBakeryDao.insert(new Bakery("고당팜베이커리", "경기 수원시 영통구 센트럴파크로 6", 37.285846787875485, 127.06369200837575));
//        mBakeryDao.insert(new Bakery("하얀풍차제과 매탄점", "경기 수원시 영통구 인계로189번길 7 종합상가", 37.266185367722784, 127.03815651355539));
//        mBakeryDao.insert(new Bakery("삐에스몽테 제빵소", "경기 수원시 권선구 오목천로 149", 37.24528578728469, 126.97673517421977));
//        mBakeryDao.insert(new Bakery("브레드쿠쿰", "경기 용인시 기흥구 흥덕4로 75", 37.27843452175131, 127.09009122777213));
//        mBakeryDao.insert(new Bakery("칼리오페", "경기 용인시 처인구 성산로170번길 23-1", 37.24813566780824, 127.19283833369533));
//        mBakeryDao.insert(new Bakery("옐로오븐", "경기 용인시 수지구 수지로 64", 37.30881456607982, 127.07613786358962));
//        mBakeryDao.insert(new Bakery("까미노빵집", "서울 강북구 4.19로 61", 37.64611799979539, 127.0078847553202));
//        mBakeryDao.insert(new Bakery("패멩베이커리", "서울 도봉구 도봉로116길 4", 37.64801175237896, 127.0356465450381));
//        mBakeryDao.insert(new Bakery("글림", "서울 도봉구 도봉로118길 6-8 1층", 37.64883904901911, 127.03516165273065));
//        mBakeryDao.insert(new Bakery("Bun418", "서울 강북구 오패산로 414", 37.637570703698344, 127.02634331990289));
//        mBakeryDao.insert(new Bakery("브래드마티스", "서울 성북구 숭인로 50", 37.61241878229841, 127.02726301155154));
//        mBakeryDao.insert(new Bakery("르뱅브래드", "경기 의정부시 신촌로63번길 20-4 1층", 37.751227793096916, 127.04184992023401));
    }

    //BAKERY 삭제
    public void deleteBakeryFromDB(int ID){
        Bakery deleteBakery = new Bakery("","",0,0);
        deleteBakery.setId(ID); //n번 아이디의 데이터 삭제
        mBakeryDao.delete(deleteBakery);
    }
}