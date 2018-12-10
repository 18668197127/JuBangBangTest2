package com.example.administrator.jubangbangtest2.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.administrator.jubangbangtest2.R;
import com.example.administrator.jubangbangtest2.adapter.LocationAdapter;
import com.example.administrator.jubangbangtest2.data.LocationHint;

import java.util.ArrayList;
import java.util.List;

public class HuoyunSearchActivity extends AppCompatActivity implements LocationAdapter.AdapterCallback {

    private List<LocationHint> locationHintList=new ArrayList<>();
    private EditText searchEditText;
    private LocationAdapter locationAdapter;
    private LocationHint locationHint;
    private Button buttonConfirm;
    private LatLonPoint latLonPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huoyun_search);

        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        locationAdapter=new LocationAdapter(locationHintList,this);
        RecyclerView recyclerView=findViewById(R.id.search_location_recycler);
        LinearLayoutManager manager=new LinearLayoutManager(HuoyunSearchActivity.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(locationAdapter);

        searchEditText=findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String  content=s.toString().trim();
                InputtipsQuery inputquery = new InputtipsQuery(content, "杭州");
                inputquery.setCityLimit(true);//限制在当前城市
                Inputtips inputTips = new Inputtips(HuoyunSearchActivity.this, inputquery);
                inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
                    @Override
                    public void onGetInputtips(List<Tip> list, int i) {

                        if (!locationHintList.isEmpty()){
                            locationHintList.clear();
                        }
                        Log.i("LBS-Location", ""+list.get(0).getPoint()+list.get(0).getTypeCode()+" "+list.get(0).getPoiID()
                                +list.get(0).getName()+list.get(0).getDistrict()+list.get(0).getAddress()+list.get(0).getAdcode());
                        for (int y=0;y<list.size();y++){
                            locationHint=new LocationHint(list.get(y).getName(),list.get(y).getAddress(),list.get(0).getPoint());
                            locationHintList.add(locationHint);
                        }
                        if (locationHint!=null){
                            locationHint=null;
                        }
                        locationAdapter.notifyDataSetChanged();
                    }
                });
                inputTips.requestInputtipsAsyn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonConfirm=findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("data_return",searchEditText.getText().toString());
                intent.putExtra("latitude_return",latLonPoint.getLatitude());
                intent.putExtra("longitude_return",latLonPoint.getLongitude());
                setResult(2,intent);
                finish();
            }
        });

    }

    @Override
    public void onMethodCallback(LatLonPoint latLonPoint) {
        this.latLonPoint=latLonPoint;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
