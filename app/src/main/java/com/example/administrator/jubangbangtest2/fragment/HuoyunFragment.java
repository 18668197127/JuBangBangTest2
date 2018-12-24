package com.example.administrator.jubangbangtest2.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.TruckPath;
import com.amap.api.services.route.TruckRouteRestult;
import com.amap.api.services.route.WalkRouteResult;
import com.example.administrator.jubangbangtest2.R;
import com.example.administrator.jubangbangtest2.activity.ConfirmOrderActivity;
import com.example.administrator.jubangbangtest2.activity.HuoyunSearchActivity;
import com.example.administrator.jubangbangtest2.adapter.CarTypeViewAdapter;
import com.example.administrator.jubangbangtest2.dialog.MyDialog;

import java.util.ArrayList;
import java.util.List;

import static com.amap.api.services.route.RouteSearch.DRIVING_SINGLE_NO_HIGHWAY_SAVE_MONEY_AVOID_CONGESTION;
import static com.amap.api.services.route.RouteSearch.TRUCK_AVOID_CONGESTION__SAVE_MONEY_NO_HIGHWAY;

public class HuoyunFragment extends Fragment {
    //这个是货运生命周期的Tag
    private static final String TAG = "HuoyunFragment";




//    private String [] carStringList=new String []{"小型面包车","中型面包车","小型货车","中型货车"};
    private ArrayList<String> carStringList =new ArrayList<>();
    private ArrayList<Fragment> carTypeFragmentList = new ArrayList<Fragment>();
    private CarTypeViewAdapter carTypeViewAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout1;

    //尝试在fragment中获取控件实例
    private EditText begin;
    private EditText end;
    private Button order;
    private ImageButton check;
    private TextView price;
    private LatLonPoint latLonPointBegin;
    private LatLonPoint latLonPointEnd;
    private float totalDistance=0;
    private int priceInt=0;

    //用于判断check图片是否点击选中
    private boolean checkFlag=false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        Log.i(TAG, "onCreate: ");
    }

    @Nullable



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.huoyun_fragment,container,false);
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        setTab1();

        if (carStringList.size()==4&&carTypeFragmentList.size()==4){
            setVp();
        }
        Log.i(TAG, "onActivityCreated: ");

        //初始化begin
        begin=getView().findViewById(R.id.begin_location);

        begin.setFocusable(false);//让EditText失去焦点，然后获取点击事件

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),HuoyunSearchActivity.class);
//                startActivity(intent);
                startActivityForResult(intent,1);
            }
        });
        //初始化end
        end=getView().findViewById(R.id.end_location);

        end.setFocusable(false);//让EditText失去焦点，然后获取点击事件

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),HuoyunSearchActivity.class);
//                startActivity(intent);
                startActivityForResult(intent,2);
            }
        });

        order=getView().findViewById(R.id.button_order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (begin.getText().length()>=2&&end.getText().length()>=2){
                    Log.i(TAG, "onClick: 立即用车生效"+" "+begin.getText()+" "+end.getText());
                    final MyDialog myDialog=new MyDialog(getActivity(),R.style.Dialog_Msg);

                    myDialog.setMyOnclickListener("这是外部实现的点击事件", new MyDialog.MyOnclickListener() {
                        @Override
                        public void onYesClick(String message) {
                            System.out.println("测试:"+message);
                            EditText e1=myDialog.findViewById(R.id.dialog_edit_01);
                            EditText e2=myDialog.findViewById(R.id.dialog_edit_02);
                            EditText e3=myDialog.findViewById(R.id.dialog_edit_03);
                            EditText e4=myDialog.findViewById(R.id.dialog_edit_04);
                            System.out.println(message+e1.getText());
                            Intent intent=new Intent(getActivity(),ConfirmOrderActivity.class);
                            intent.putExtra("begin",begin.getText().toString());
                            intent.putExtra("end",end.getText().toString());
                            intent.putExtra("e1",e1.getText().toString());
                            intent.putExtra("e2",e2.getText().toString());
                            intent.putExtra("e3",e3.getText().toString());
                            intent.putExtra("e4",e4.getText().toString());
                            intent.putExtra("price",priceInt+"");
                            startActivity(intent);

                        }
                    });
                    myDialog.show();
                }
            }
        });
        check=getView().findViewById(R.id.image_check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFlag){
                    check.setBackgroundResource(R.drawable.huoyun_check_false);
                    checkFlag=false;
                }else {
                    check.setBackgroundResource(R.drawable.huoyun_check_true);
                    checkFlag=true;
                }
            }
        });
        price=getView().findViewById(R.id.text_price);



        System.out.println("测试");
    }

    @Override
    public void onStart() {
        super.onStart();
//        setVp();
        Log.i(TAG, "onStart: ");
    }

    //    private void setTab1(){
//        tabLayout1=getView().findViewById(R.id.huoyun_tablayout);
//        for (int i=0;i<carStringList.length;i++){
//            tabLayout1.addTab(tabLayout1.newTab());
//        }
//        for (int i=0;i<carStringList.length;i++){
//            tabLayout1.getTabAt(i).setText(carStringList[i]);
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        removeDatea();
        removeVp();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==2){
                    String returnData=data.getStringExtra("data_return");
//                    Toast.makeText(getContext(),"数据返回",Toast.LENGTH_SHORT);
                    begin.setText(returnData);
                    latLonPointBegin=new LatLonPoint(data.getDoubleExtra("latitude_return",1),data.getDoubleExtra("longitude_return",1));
                    if (latLonPointBegin!=null&&latLonPointEnd!=null&&begin.getText()!=null&&end.getText()!=null){
                        RouteSearch routeSearch = new RouteSearch(getActivity());
                        routeSearch.setOnTruckRouteSearchListener(new RouteSearch.OnTruckRouteSearchListener() {
                            @Override
                            public void onTruckRouteSearched(TruckRouteRestult truckRouteRestult, int i) {
                                List<TruckPath> truckPaths=truckRouteRestult.getPaths();
                                if (totalDistance!=0){
                                    totalDistance=0;
                                }
                                for (int x=0;x<truckPaths.size();x++){
                                    totalDistance=totalDistance+truckPaths.get(x).getDistance();
                                }
                                totalDistance=totalDistance/truckPaths.size();
                                int totalDistanceInt=Math.round(totalDistance/1000);
                                System.out.println("测试"+totalDistance+","+truckPaths.size());
                                System.out.println("测试"+totalDistanceInt+","+truckPaths.size());
                                priceInt=totalDistanceInt*2+20;
                                price.setText(priceInt+"");
                            }
                        });
                        System.out.println("测试"+latLonPointBegin+","+latLonPointEnd);
                        RouteSearch.FromAndTo fromAndTo=new RouteSearch.FromAndTo(latLonPointBegin,latLonPointEnd);
                        RouteSearch.TruckRouteQuery query = new RouteSearch.TruckRouteQuery(fromAndTo,TRUCK_AVOID_CONGESTION__SAVE_MONEY_NO_HIGHWAY, null, RouteSearch.TRUCK_SIZE_LIGHT);
                        routeSearch.calculateTruckRouteAsyn(query);
                    }
                }
                break;
            case 2:
                if (resultCode==2){
                    String returnData=data.getStringExtra("data_return");
//                    Toast.makeText(getContext(),"数据返回",Toast.LENGTH_SHORT);
                    end.setText(returnData);
                    latLonPointEnd=new LatLonPoint(data.getDoubleExtra("latitude_return",1),data.getDoubleExtra("longitude_return",1));
                    if (latLonPointBegin!=null&&latLonPointEnd!=null&&begin.getText()!=null&&end.getText()!=null){
                        RouteSearch routeSearch = new RouteSearch(getActivity());
                        routeSearch.setOnTruckRouteSearchListener(new RouteSearch.OnTruckRouteSearchListener() {
                            @Override
                            public void onTruckRouteSearched(TruckRouteRestult truckRouteRestult, int i) {
                                List<TruckPath> truckPaths=truckRouteRestult.getPaths();
                                if (totalDistance!=0){
                                    totalDistance=0;
                                }
                                for (int x=0;x<truckPaths.size();x++){
                                    totalDistance=totalDistance+truckPaths.get(x).getDistance();
                                }
                                totalDistance=totalDistance/truckPaths.size();
                                int totalDistanceInt=Math.round(totalDistance/1000);
                                System.out.println("测试"+totalDistance+","+truckPaths.size());
                                System.out.println("测试"+totalDistanceInt+","+truckPaths.size());
                                priceInt=totalDistanceInt*2+20;
                                price.setText(priceInt+"");
                            }
                        });
                        System.out.println("测试"+latLonPointBegin+","+latLonPointEnd);
                        RouteSearch.FromAndTo fromAndTo=new RouteSearch.FromAndTo(latLonPointBegin,latLonPointEnd);
                        RouteSearch.TruckRouteQuery query = new RouteSearch.TruckRouteQuery(fromAndTo,TRUCK_AVOID_CONGESTION__SAVE_MONEY_NO_HIGHWAY, null, RouteSearch.TRUCK_SIZE_LIGHT);
                        routeSearch.calculateTruckRouteAsyn(query);
                    }
                }
                break;
            default:
        }
    }

    private void setData(){
        carStringList.add("小型面包车");
        carStringList.add("中型面包车");
        carStringList.add("小型货车");
        carStringList.add("中型货车");
        CarTypeFragment carTypeFragment1=CarTypeFragment.newInstance(1);
        CarTypeFragment carTypeFragment2=CarTypeFragment.newInstance(2);
        CarTypeFragment carTypeFragment3=CarTypeFragment.newInstance(3);
        CarTypeFragment carTypeFragment4=CarTypeFragment.newInstance(4);
        carTypeFragmentList.add(carTypeFragment1);
        carTypeFragmentList.add(carTypeFragment2);
        carTypeFragmentList.add(carTypeFragment3);
        carTypeFragmentList.add(carTypeFragment4);
    }

    private void removeDatea(){
        carStringList.clear();
        carTypeFragmentList.clear();
    }

    private void setVp(){
        carTypeViewAdapter=new CarTypeViewAdapter(getChildFragmentManager(), carStringList,carTypeFragmentList);
        viewPager= getView().findViewById(R.id.vp_cartype);
        viewPager.setAdapter(carTypeViewAdapter);
        tabLayout1=getView().findViewById(R.id.huoyun_tablayout);
        tabLayout1.setupWithViewPager(viewPager);
//        tabLayout1.setTabsFromPagerAdapter(carTypeViewAdapter);
    }

    private void removeVp(){
        carTypeViewAdapter=null;
        viewPager=null;
        tabLayout1=null;
    }

    public void setBegin(){
        begin.setText("你好");
    }

}
