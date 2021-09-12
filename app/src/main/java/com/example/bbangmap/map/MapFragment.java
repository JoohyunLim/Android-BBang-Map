package com.example.bbangmap.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.bbangmap.R;
import com.example.bbangmap.databinding.FragmentMapBinding;
import com.example.bbangmap.map.database.AppDatabase;
import com.example.bbangmap.map.database.Bakery;
import com.example.bbangmap.map.database.BakeryDao;
import com.example.bbangmap.map.BottomSheetActivity;
import com.example.bbangmap.mypage.QnaActivity;
import com.example.bbangmap.mypage.SendMail;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ZoomControlView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback, Overlay.OnClickListener {

    private FragmentMapBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private MapView mapView ;
    private AppDatabase db;
    private BakeryDao mBakeryDao;
    private ArrayList<Bakery> globalBakeryList;
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

//        //저장빵집DB
//        db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "bakery-db")
//                .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
//                .build();
//        mBakeryDao = db.bakeryDao();


        return root;
    }

    private void setMarker(Marker marker,  String name, String address, double lat, double lng)
    {
        //아이콘 지정
        marker.setCaptionMinZoom(14);
        marker.setCaptionText(name);
        marker.setSubCaptionText(address);
        marker.setSubCaptionTextSize(0);
        marker.setIcon(OverlayImage.fromResource(R.drawable.icon));
        marker.setHeight(90);
        marker.setWidth(90);
        //마커의 투명도
        marker.setAlpha(1f);
        //마커 위치
        marker.setPosition(new LatLng(lat, lng));
        //마커 표시
        mMarkerArray.add(marker);
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

        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                View bottomView = (View) getActivity().findViewById(R.id.bottom_sheet);
                bottomView.setVisibility(View.INVISIBLE);
            }
        });

        globalBakeryList = new ArrayList<Bakery>();
        setBakeryList();

        for (int i = 0; i < globalBakeryList.size(); i++) {
            Marker marker = new Marker();
            setMarker(marker, globalBakeryList.get(i).getName(), globalBakeryList.get(i).getAddress(),
                    globalBakeryList.get(i).getLat(),globalBakeryList.get(i).getLng()
            );
        }

        for(Marker marker : mMarkerArray){
            marker.setOnClickListener(this);
        }

        //빵집 리스트 마커 전부 추가
//        List<Bakery> bakeryList = mBakeryDao.getAll();
//        for (int i = 0;  i < bakeryList.size() ; i++) {
//            //Log.d("###########TEST", bakeryList.get(i).getName());
//            Marker marker= new Marker();
//            setMarker(marker, bakeryList.get(i).getName(), bakeryList.get(i).getLat(),bakeryList.get(i).getLng());
//        }
    }

    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if(overlay instanceof Marker){
            //정보창 이름,주소 세팅
            TextView nameView =  (TextView) getActivity().findViewById(R.id.nameView);
            TextView addressView =  (TextView) getActivity().findViewById(R.id.addressView);
            nameView.setText(((Marker) overlay).getCaptionText());
            addressView.setText(((Marker) overlay).getSubCaptionText());

            editInfoForm();

            View bottomView = (View) getActivity().findViewById(R.id.bottom_sheet);
            bottomView.setVisibility(View.VISIBLE);

            //카메라 포커스 이동
            CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(
                    new LatLng(((Marker) overlay).getPosition().latitude, ((Marker) overlay).getPosition().longitude), 16)
                    .animate(CameraAnimation.Fly, 700);
            naverMap.moveCamera(cameraUpdate);

            return true;
        }
        return false;
    }

    //오류수정 요청하기 버튼
    public void editInfoForm(){
        Button editTextButton = (Button) getActivity().findViewById(R.id.editTextButton);
        editTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] problems = new String[] {"주소가 잘못되었습니다.", "폐업한 가게입니다.", "기타"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("어떤 문제가 있나요?");
                builder. setSingleChoiceItems(problems, -1, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), problems[which], Toast.LENGTH_SHORT).show(); }
                });
                builder.setPositiveButton("제출", null);
                AlertDialog dialog1 = builder.create();
                dialog1.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override public void onShow(DialogInterface arg0) {
                        dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    }
                });
                dialog1.show();
            }
        });
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

//    //BAKERY 삽입
//    public void addBakeryToDB(){
////mBakeryDao.insert(new Bakery("디어필립", "경기도 용인시 수지구 수지로296번길 51-5", 37.321672038986314, 127.09446862858634));
//   }

//    //BAKERY 삭제
//    public void deleteBakeryFromDB(int ID){
//        Bakery deleteBakery = new Bakery("","",0,0);
//        deleteBakery.setId(ID); //n번 아이디의 데이터 삭제
//        mBakeryDao.delete(deleteBakery);
//    }

    public void setBakeryList(){
        globalBakeryList.add(new Bakery("디어필립", "경기도 용인시 수지구 수지로296번길 51-5", 37.321672038986314, 127.09446862858634));
        globalBakeryList.add(new Bakery("본누벨베이커리", "경기도 용인시 수지구 수지로 20", 37.30483867346377, 127.07520066093477));
        globalBakeryList.add(new Bakery("오봉베르", "경기도 수원시 영통구 센트럴파크로127번길 142", 37.293913587307586, 127.05516634679148));
        globalBakeryList.add(new Bakery("씨투베이커리 본점", "경기도 용인시 수지구 성복2로 166", 37.317721439210494, 127.0684622693322));
        globalBakeryList.add(new Bakery("르디투어", "경기도 수원시 영통구 이의동 1222-2", 37.3121372340001, 127.05494489737711));
        globalBakeryList.add(new Bakery("하구영베이커리", "경기도 용인시 수지구 풍덕천로96번길 3-4 1층", 37.321315195444676, 127.09361943951407));
        globalBakeryList.add(new Bakery("올바른단팥빵&고로케", "경기도 용인시 기흥구 보정로 115 우영프라자", 37.31990920240392, 127.11530384331465));
        globalBakeryList.add(new Bakery("블랑제리", "경기도 용인시 수지구 성복1로 106 벽산프라자", 37.31673654796295, 127.06813381164015));
        globalBakeryList.add(new Bakery("크라상점 수지상현점", "경기도 용인시 수지구 만현로 110 B동 105호", 37.30665240759278, 127.0848815051436));
        globalBakeryList.add(new Bakery("아일랜드15", "경기도 용인시 수지구 광교호수로378번길 25", 37.29636639148425, 127.08036898380995));
        globalBakeryList.add(new Bakery("델리봉봉", "경기도 용인시 수지구 광교중앙로295번길 3", 37.29641146855036, 127.06829652117655));
        globalBakeryList.add(new Bakery("뺑오르방 광교카페거리점", "경기도 수원시 영통구 센트럴파크로128번길 105", 37.29489629080948, 127.05667296040775));
        globalBakeryList.add(new Bakery("아우어베이커리 광교앨리웨이점", "경기도 수원시 영통구 원천동 광교호수공원로 80", 37.274719783120375, 127.06188953775724));
        globalBakeryList.add(new Bakery("밀도 광교앨리웨이점", "경기도 수원시 영통구 하동 광교호수공원로 80", 37.27508044657123, 127.0607312488967));
        globalBakeryList.add(new Bakery("브라우니70 본점", "경기도 용인시 수지구 성복동 수지로 119", 37.31287856634796, 127.07888155970471));
        globalBakeryList.add(new Bakery("몽소베이커리앤카페", "경기도 수원시 영통구 대학4로 9 리치프라자2 1층", 37.30033387016423, 127.04580794992314));
        globalBakeryList.add(new Bakery("고당팜베이커리", "경기도 수원시 영통구 센트럴파크로 6", 37.285846787875485, 127.06369200837575));
        globalBakeryList.add(new Bakery("하얀풍차제과 매탄점", "경기도 수원시 영통구 인계로189번길 7 종합상가", 37.266185367722784, 127.03815651355539));
        globalBakeryList.add(new Bakery("삐에스몽테 제빵소", "경기도 수원시 권선구 오목천로 149", 37.24528578728469, 126.97673517421977));
        globalBakeryList.add(new Bakery("브레드쿠쿰", "경기도 용인시 기흥구 흥덕4로 75", 37.27843452175131, 127.09009122777213));
        globalBakeryList.add(new Bakery("칼리오페", "경기도 용인시 처인구 성산로170번길 23-1", 37.24813566780824, 127.19283833369533));
        globalBakeryList.add(new Bakery("옐로오븐", "경기도 용인시 수지구 수지로 64", 37.30881456607982, 127.07613786358962));
        globalBakeryList.add(new Bakery("까미노빵집", "서울특별시 강북구 4.19로 61", 37.64611799979539, 127.0078847553202));
        globalBakeryList.add(new Bakery("히피스베이글", "서울특별시 강북구 우이동 삼양로 528", 37.65004735195512, 127.01362770925707));
        globalBakeryList.add(new Bakery("보나뻬띠브레드", "서울특별시 도봉구 쌍문동 삼양로 574-29 1층", 37.65457317600603, 127.01375597521117));
        globalBakeryList.add(new Bakery("패멩베이커리", "서울특별시 도봉구 도봉로116길 4", 37.64801175237896, 127.0356465450381));
        globalBakeryList.add(new Bakery("글림", "서울특별시 도봉구 도봉로118길 6-8 1층", 37.64883904901911, 127.03516165273065));
        globalBakeryList.add(new Bakery("Bun418", "서울특별시 강북구 오패산로 414", 37.637570703698344, 127.02634331990289));
        globalBakeryList.add(new Bakery("나폴레옹과자점 본점", "서울특별시 성북구 성북동 성북로 7", 37.58892060060636, 127.00518662905147));
        globalBakeryList.add(new Bakery("오보록", "서울특별시 성북구 성북동 성북로 63", 37.59285887557508, 127.00151448139837));
        globalBakeryList.add(new Bakery("블랑제메종 북악", "서울특별시 성북구 성북동 성북로 156", 37.59470703220529, 126.99240233843328));
        globalBakeryList.add(new Bakery("성북동빵공장", "서울특별시 성북구 성북동 대사관로 40", 37.59583654066862, 126.98843484890078));
        globalBakeryList.add(new Bakery("슬로우브레드파파", "서울특별시 성북구 보문로30나길 29", 37.590682515157674, 127.0203006055725));
        globalBakeryList.add(new Bakery("브래드마티스", "서울특별시 성북구 숭인로 50", 37.61241878229841, 127.02726301155154));
        globalBakeryList.add(new Bakery("르뱅브래드", "경기도 의정부시 신촌로63번길 20-4 1층", 37.751227793096916, 127.04184992023401));
        globalBakeryList.add(new Bakery("러스트 베이커리", "서울특별시 영등포구 문래동 경인로79길 15", 37.513159290625275, 126.8937028765799));
        globalBakeryList.add(new Bakery("인포메이션카페", "서울특별시 강남구 역삼1동 역삼로 121", 37.4940369637935, 127.03264422389768));
        globalBakeryList.add(new Bakery("베이커리 무이", "서울특별시 용산구 원효로1가 28-1", 37.53964273135676, 126.96686455694795));
        globalBakeryList.add(new Bakery("베떼엠", "경기도 성남시 분당구 수내동 33-2", 37.377319667665446, 127.11579384775956));
        globalBakeryList.add(new Bakery("비파티세리", "서울특별시 강남구 신사동 압구정로14길 36", 37.5219794805256, 127.0232459031536));
        globalBakeryList.add(new Bakery("에뚜왈 연남점", "서울특별시 마포구 연남동 성미산로 170", 37.56410951155832, 126.92456518465477));
        globalBakeryList.add(new Bakery("빵길따라 연남동", "서울특별시 마포구 연남동 227-2", 37.561810131625684, 126.92692387472106));
        globalBakeryList.add(new Bakery("앙토낭카렘", "경기도 성남시 분당구 서현동 306-2", 37.37392851963139, 127.13682156921074));
        globalBakeryList.add(new Bakery("데조로의집", "경기도 성남시 분당구 삼평동 719", 37.40394703532899, 127.11620055805162));
        globalBakeryList.add(new Bakery("김영모과자점 도곡타워점", "서울특별시 강남구 도곡2동 언주로30길 10", 37.488150246604505, 127.05287061162215));
        globalBakeryList.add(new Bakery("팡도리노", "서울특별시 중랑구 망우동 용마산로115길 127", 37.59717817992555, 127.0932093238178));
        globalBakeryList.add(new Bakery("초이고야", "서울특별시 서초구 방배동 방배로20길 11", 37.48585794281395, 126.99565851777467));
        globalBakeryList.add(new Bakery("소울브레드", "서울특별시 서초구 우면동 59", 37.471308168334794, 127.02425087239124));
        globalBakeryList.add(new Bakery("루엘드파리", "서울특별시 서초구 서초중앙로 18 서초쌍용플래티넘 112호", 37.483975726750465, 127.01721601970715));
        globalBakeryList.add(new Bakery("타르틴 베이커리 서울 한남점", "서울특별시 용산구 한남동 한남대로18길 22", 37.53463961802338, 127.00879353445592));
        globalBakeryList.add(new Bakery("꼼다비뛰드", "서울특별시 강남구 역삼동 강남대로110길 62", 37.50334691903085, 127.02951363147028));
        globalBakeryList.add(new Bakery("브레드숨", "서울특별시 동작구 노량진1동 노량진로18길 37", 37.51233571154324, 126.95178546259604));
        globalBakeryList.add(new Bakery("효자베이커리", "서울특별시 종로구 통인동 필운대로 54", 37.58091954586318, 126.96865952755645));
        globalBakeryList.add(new Bakery("뺑드에코", "서울특별시 성동구 성수동2가 연무장길 44 2층", 37.54260909019213, 127.05401432800868));
        globalBakeryList.add(new Bakery("폴앤폴리나", "서울특별시 서대문구 연희동 연희로11길 56", 37.56886384707141, 126.92901544741055));
        globalBakeryList.add(new Bakery("아티장베이커스 한남점", "서울특별시 용산구 한남동 한남대로18길 26", 37.53454794677909, 127.00861223858425));
        globalBakeryList.add(new Bakery("오월의 종", "서울특별시 용산구 한남동 이태원로 229", 37.53576353486479, 126.99920017189761));
        globalBakeryList.add(new Bakery("어글리 베이커리", "서울특별시 마포구 망원1동 월드컵로13길 73 1층", 37.55500894075755, 126.90604320161343));
        globalBakeryList.add(new Bakery("투떰즈업", "서울특별시 마포구 망원1동 월드컵로19길 71", 37.5543219497524, 126.90717888319982));
        globalBakeryList.add(new Bakery("파롤앤랑그", "서울특별시 마포구 연남동 성미산로29안길 8", 37.564950100634796, 126.92283223307035));
        globalBakeryList.add(new Bakery("피터팬 1978", "서울특별시 서대문구 연희동 90-5", 37.5693571103465, 126.93170160863491));
        globalBakeryList.add(new Bakery("독일빵집", "서울특별시 서대문구 연희동 132-20", 37.5676048146701, 126.93000261924131));
        globalBakeryList.add(new Bakery("금양식방", "서울특별시 서대문구 연희동 717-31 1층", 37.576823313477085, 126.93091149278153));
        globalBakeryList.add(new Bakery("뉘블랑쉬", "서울특별시 서대문구 연희동 연희로15길 52 지층", 37.56969530661221, 126.92989352702283));
        globalBakeryList.add(new Bakery("만동제과", "서울특별시 서대문구 연희로 32", 37.56150580384586, 126.9272526779984));
        globalBakeryList.add(new Bakery("바이레인", "서울특별시 성동구 성수동1가 왕십리로14길 12", 37.549085247137036, 127.0450917683186));
        globalBakeryList.add(new Bakery("빵의정석", "서울특별시 성동구 성수동1가 서울숲2길 45", 37.54640450806095, 127.04343352195345));
        globalBakeryList.add(new Bakery("에르제", "서울특별시 성동구 성수2가3동 277-56", 37.54398108136744, 127.05896713136775));
        globalBakeryList.add(new Bakery("태극당", "서울특별시 중구 장충동 동호로24길 7", 37.55952115769714, 127.00503389245561));
        globalBakeryList.add(new Bakery("라운드앤드", "서울특별시 중구 정동 정동길 35 두비빌딩", 37.56602555703956, 126.97235492872899));
        globalBakeryList.add(new Bakery("르빵 명동성당점", "서울특별시 중구 명동2가 명동길 74", 37.56412148757096, 126.98664253235471));
        globalBakeryList.add(new Bakery("쟝블랑제리 이수점", "서울특별시 동작구 동작대로23길 8", 37.484348370397115, 126.98131278184597));
        globalBakeryList.add(new Bakery("쟝블랑제리", "서울특별시 관악구 낙성대역길 8", 37.47706045254908, 126.96192799579167));
        globalBakeryList.add(new Bakery("아티장베이커스 본점", "서울특별시 강남구 논현로105길 8", 37.506053480717505, 127.03363034355266));
        globalBakeryList.add(new Bakery("아베베베이커리", "제주특별자치도 제주시 동문로 10", 33.51279413077327, 126.52793292526506));;
        globalBakeryList.add(new Bakery("어머니빵집", "제주특별자치도 제주시 연동 도령로 103", 33.491593428733474, 126.48890790603807));
        globalBakeryList.add(new Bakery("르에스까르고", "제주특별자치도 제주시 월랑로2길 29", 33.488387042817735, 126.48159332927656));
        globalBakeryList.add(new Bakery("애월빵공장&카페", "제주특별자치도 제주시 애월읍 금성5길 44-9", 33.448549889099276, 126.30142618872887));
        globalBakeryList.add(new Bakery("타임투베이크", "경기도 안양시 만안구 안양동 성결대학로23번길 65 1층", 37.38080553546558, 126.92995885890849));
        globalBakeryList.add(new Bakery("하츠베이커리", "서울특별시 송파구 중대로 210", 37.5004194637202, 127.12712816303407));
        globalBakeryList.add(new Bakery("하츠베이커리 x 센터커피점", "경기도 안양시 동안구 관양2동 관악대로 450", 37.40409567032979, 126.97246781521271));
        globalBakeryList.add(new Bakery("이성당 롯데몰수지점", "경기도 용인시 수지구 성복동 61-6", 37.31316049237615, 127.081154800505));
        globalBakeryList.add(new Bakery("몽꺄도", "서울특별시 중구 신당동 동호로18길 13", 37.55823705904145, 127.00918022481774));
        globalBakeryList.add(new Bakery("블랑제리 더 플라자", "서울특별시 중구 소공로 119 더 플라자 호텔 LL층", 37.56472571735351, 126.97798315484417));
        globalBakeryList.add(new Bakery("디어브레드 안암본점", "서울특별시 성북구 안암동 개운사1길 29", 37.5872985596481, 127.0286883112576));
        globalBakeryList.add(new Bakery("수더분", "서울특별시 마포구 공덕동 백범로 152", 37.54549358831956, 126.94663480663294));
        globalBakeryList.add(new Bakery("서울앵무새", "서울특별시 성동구 성수동1가 서울숲9길 3", 37.54828944762756, 127.04353266547022));
        globalBakeryList.add(new Bakery("심세정", "서울특별시 중구 흥인동 퇴계로 409-11", 37.566029531369324, 127.0170499563962));
        globalBakeryList.add(new Bakery("보보찰리베이커리 제기동점", "서울특별시 동대문구 용신동 왕산로 110", 37.577973571801195, 127.03552925405849));
        globalBakeryList.add(new Bakery("빵선생", "경기도 과천시 중앙로 389", 37.44880129936233, 126.99758135009726));
        globalBakeryList.add(new Bakery("베이커스퍼센트", "서울특별시 종로구 누하동 필운대로2길 3", 37.57824243267839, 126.96943148227327));
        globalBakeryList.add(new Bakery("몽쥬 빠티세리", "서울특별시 성동구 이태원2동 왕십리로 410 E동 104호", 37.539551056973366, 126.99537709036635));
        globalBakeryList.add(new Bakery("크루아상 148", "서울특별시 강서구 공항대로 237 1층 112호", 37.55940554600925, 126.83370009460359));
        globalBakeryList.add(new Bakery("팡오뉴", "서울특별시 강서구 등촌로 81 지하1층", 37.53723929627493, 126.86316079435876));
        globalBakeryList.add(new Bakery("fleuve", "서울특별시 강서구 강서로15길 26 1층", 37.53231905050768, 126.84453287573088));
        globalBakeryList.add(new Bakery("갓브레드", "서울특별시 강서구 마곡중앙5로 6 보타닉푸르지오씨티 지하2층", 37.56711880708186, 126.82701764242582));
        globalBakeryList.add(new Bakery("앵글드", "서울특별시 강서구 양천로10길 38 103동 상가 101호", 37.57142170361582, 126.80800989060147));
        globalBakeryList.add(new Bakery("고산빵명장", "경기도 의정부시 산꽃길 5", 37.72853633636647, 127.11268882118803));
        globalBakeryList.add(new Bakery("그린베이커리", "대전광역시 유성구 테크노4로 98-8 평원오피스텔 101호", 36.42645449315943, 127.38920923919534));
        globalBakeryList.add(new Bakery("르뺑99-1", "대전광역시 유성구 온천북로33번길 22-3", 36.35788939901003, 127.34727923711706));
        globalBakeryList.add(new Bakery("연선흠베이커리카페", "대전광역시 유성구 지족동 1048-5", 36.37852986363704, 127.30532874681778));
        globalBakeryList.add(new Bakery("파이한모금", "대전광역시 동구 백룡로5번길 59 1층", 36.33863745608623, 127.44917645487754));
        globalBakeryList.add(new Bakery("무슈뱅상", "부산광역시 수영구 광남로48번길 19", 35.14638596800597, 129.11295534423215));
        globalBakeryList.add(new Bakery("파리휘셀과자점", "부산광역시 부산진구 개금본동로 22", 35.159282605805, 129.02448851092404));
        globalBakeryList.add(new Bakery("루반도르 파티세리", "부산광역시 동구 수정2동 중앙대로 375-1", 35.129059191276035, 129.04870091384257));
        globalBakeryList.add(new Bakery("나폴레옹키오스크", "서울특별시 중구 태평로1가 세종대로21길 52", 37.56877189277412, 126.97601262834775));
        globalBakeryList.add(new Bakery("고메코나베이커리", "강원도 강릉시 포남2동 강릉대로457번길 4-1", 37.77419492983039, 128.91292160570768));
        globalBakeryList.add(new Bakery("52블럭", "강원도 강릉시 정원로 52 유성빌딩 1층", 37.76234233164716, 128.87615894063788));
        globalBakeryList.add(new Bakery("빵앗간", "강원도 강릉시 하평길 74 1층", 37.77033433509351, 128.91666519040805));
        globalBakeryList.add(new Bakery("24FRAME", "강원도 강릉시 난설헌로 73 2층", 37.78348521730433, 128.90197952875351));
        globalBakeryList.add(new Bakery("만동제과", "강원도 강릉시 금성로 6 1층", 37.755117787423714, 128.89948705970505));
        globalBakeryList.add(new Bakery("바로방", "강원도 강릉시 경강로 2092", 37.75430333260466, 128.89589578846977));
        globalBakeryList.add(new Bakery("강릉빵다방", "강원도 강릉시 남강초교1길 24", 37.769008282541385, 128.91807330449475));
    }

}