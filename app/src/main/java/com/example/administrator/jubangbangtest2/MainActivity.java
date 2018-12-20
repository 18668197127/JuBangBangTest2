package com.example.administrator.jubangbangtest2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.administrator.jubangbangtest2.fragment.HuoyunFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    //用于地图项目的日志Tag
    private static final String TAG = "MainActivity";

    //设置Spinner的数据和适配器
    private Spinner spinner;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;


    private TabLayout tabLayout1;
    private String [] tabString=new String []{"货运","跑腿","找帮手","保洁","维修"};
    //标题左上角按钮用于打开滑动侧栏
    private ImageButton titleButton;
    //DrawerLayout控件的声明
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private ImageButton navImageButton;
    private HuoyunFragment huoyunFragment;

    private EditText beginLocation;

    //判断货运Fragment是否加载,未加载,隐藏的旗帜flag
    private boolean huoyunflag=false;

    private Bundle bundle;

    //声明高德地图类
    public MapView mapView;
    public AMap aMap;
    private LatLng latLng=null;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (latLng==null&&aMapLocation.getLongitude()!=0&&aMapLocation.getLatitude()!=0){
                latLng=new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                Marker marker=aMap.addMarker(new MarkerOptions().position(latLng).title("杭州滨江").snippet("江南星座")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location)).draggable(true));
            }
            Log.i("LBS", "onLocationChanged: "+aMapLocation.getLatitude()+","+aMapLocation.getLongitude());
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle=savedInstanceState;

        //活动布局显示在状态栏上并使状态栏透明
        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //权限获取
        List<String> permissionList=new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!permissionList.isEmpty()){
            String [] permissions=permissionList.toArray(new String [permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }

        setContentView(R.layout.activity_main);
        //初始化滑动侧栏按钮和滑动侧栏布局
        titleButton=findViewById(R.id.title_button);
        navImageButton=findViewById(R.id.nav_image_button);
        mDrawerLayout=findViewById(R.id.drawer_layout);
        //设置打开滑动页面右侧是否阴影
//        mDrawerLayout.setScrimColor(Color.TRANSPARENT);


        navView=findViewById(R.id.nav_view);
        //icon设置原图默认颜色
        navView.setItemIconTintList(null);

        //添加Spinner的初始化
        dataList=new ArrayList<String>();
        dataList.add("北京");
        dataList.add("上海");
        dataList.add("广州");
        dataList.add("深圳");
        dataList.add("杭州");

        spinner=findViewById(R.id.title_spinner);
        adapter = new ArrayAdapter<String>(this,R.layout.custom_spiner_text_item,dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //高德地图相关的初始化方法
        initMap(savedInstanceState);


        //按钮点击事件打开滑动侧栏
        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //滑动菜单底部图片按钮点击事件
        navImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });

        //滑动侧栏菜单点击事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        //滑动侧栏头部点击事件
        navView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(navView);
                Toast.makeText(MainActivity.this,"HeaderView is clicked!",Toast.LENGTH_SHORT);
            }
        });



        //货运Fragment初始化
        huoyunFragment=new HuoyunFragment();
//        beginLocation=findViewById(R.id.begin_location);

        tabLayout1=findViewById(R.id.tablayout1);
        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("tablayout01", "onTabSelected: "+tab.getText());
                if (tab.getText()=="货运"){
                    setHuoyunFragment(huoyunFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("tablayout02", "onTabSelected: "+tab.getText());
                if (tab.getText()=="货运"){
                    removeFragment(huoyunFragment);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i("tablayout03", "onTabSelected: "+tab.getText());
                if (tab.getText()=="货运"){
                    setHuoyunFragment(huoyunFragment);
                }
            }
        });


        //TabLayout1初始化
        setTab1();

        Log.i(TAG, "onCreate: ");

    }

    //初始化TabLayout方法
    private void setTab1(){
        for (int i=0;i<tabString.length;i++){
            tabLayout1.addTab(tabLayout1.newTab(),false);
        }
        for (int i=0;i<tabString.length;i++){
            tabLayout1.getTabAt(i).setText(tabString[i]);
        }

    }
    //加载货运fragment在主页面下方
    private void setHuoyunFragment(Fragment fragment){
        huoyunflag=true;
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.huoyun_layout,fragment);
        transaction.addToBackStack(null);
        transaction.show(fragment);

        transaction.commit();
//        beginLocation=getSupportFragmentManager().findFragmentById(R.id.huoyun_layout).getView().findViewById(R.id.begin_location);
//        beginLocation=findViewById(R.id.begin_location);
//        System.out.println(beginLocation.toString());
    }
    private void removeFragment(Fragment fragment){
        huoyunflag=false;
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    //高德地图相关的初始化
    public void initMap(Bundle savedInstanceState){
        //显示地图
        mapView = (MapView) findViewById(R.id.map_01);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        //定位蓝点
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        //Mark初步实现(1.当前地点Mark   2.显示位置信息    3.拖拽事件:显示地点在发货栏)
//        LatLng latLng = new LatLng(39.906901,116.397972);
//        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
        //自定义蓝标的图标
//        BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.icon_map_location));
//        BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location);
//        myLocationStyle.myLocationIcon(bitmapDescriptor);
        myLocationStyle.interval(8000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(16);
        aMap.moveCamera(mCameraUpdate);

        //获取的几个地图类有待研究!!!
//        CameraPosition cameraPosition=aMap.getCameraPosition();
//        Location location=aMap.getMyLocation();
//        String  contentApprovalNumber=aMap.getMapContentApprovalNumber();
//        cameraPosition
//        location
//        myLocationStyle
//        mapView
//        aMap
//        location.getLatitude();
//        location.getLongitude();
//        Log.i(TAG, "map:"+location.getLatitude()+"  "+location.getLongitude());
//        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
//        Marker marker=aMap.addMarker(new MarkerOptions().position(cameraPosition.target).title("杭州滨江").snippet("江南星座"));

//        aMap.setLocationSource(this);//通过aMap对象设置定位数据源的监听
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮



        //定位
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

//        Marker marker=aMap.addMarker(new MarkerOptions().position(latLng).title("杭州滨江").snippet("江南星座"));

        //marker的拖动
        AMap.OnMarkerDragListener markerDragListener=new AMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                LatLng markerLatLng= marker.getPosition();
//                beginLocation.setText(markerLatLng.toString());
                Log.i("LBS", "onMarkerDragStart: "+markerLatLng.toString()+"  位置");
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng markerLatLng= marker.getPosition();
//                beginLocation=getSupportFragmentManager().findFragmentById(R.id.huoyun_layout).getView().findViewById(R.id.begin_location);
                beginLocation=findViewById(R.id.begin_location);
                beginLocation.setText(markerLatLng.toString());
                Log.i("LBS", "onMarkerDragEnd: "+markerLatLng.toString()+"  位置");
//                huoyunFragment.setBegin();
            }
        };
        aMap.setOnMarkerDragListener(markerDragListener);
    }

    //动态请求权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"请同意所以请求才能运行程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        initMap(bundle);
                    }
                }else {
                    Toast.makeText(this,"发生权限请求错误,程序关闭",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState:");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeFragment(huoyunFragment);
    }
}
