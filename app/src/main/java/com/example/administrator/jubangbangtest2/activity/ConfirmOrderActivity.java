package com.example.administrator.jubangbangtest2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.jubangbangtest2.R;
import com.example.administrator.jubangbangtest2.adapter.OrderDetailsAdapter;
import com.example.administrator.jubangbangtest2.data.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmOrderActivity";

    private List<OrderDetail> orderDetailList = new ArrayList<>();
    private List<String> text02List = new ArrayList<>();
    private OrderDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        initText02List();
        initOrderDetails(text02List);
        RecyclerView recyclerView=findViewById(R.id.recyclerview_order_details);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter=new OrderDetailsAdapter(orderDetailList);
        recyclerView.setAdapter(adapter);
        initData();

        Button button=findViewById(R.id.button_pay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ConfirmOrderActivity.this,PaymentSuccess.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initText02List() {
        Intent intent=getIntent();
        text02List.add("小型面包车");
        text02List.add("2018-11-09 09:50");
        text02List.add("浙江杭州滨江区江南星座1楼");
        text02List.add("浙江杭州江干区伊萨卡国际城");
        text02List.add("赵丽颖");
        text02List.add("15945645645");
        text02List.add("冯绍峰");
        text02List.add("18654578954");
    }

    private void initOrderDetails(List<String> list) {
        OrderDetail orderDetai01 = new OrderDetail(R.drawable.icon_car_order, "订服务车型:", list.get(0));
        orderDetailList.add(orderDetai01);
        OrderDetail orderDetai02 = new OrderDetail(R.drawable.icon_time_order, "用车时间:", list.get(1));
        orderDetailList.add(orderDetai02);
        OrderDetail orderDetai03 = new OrderDetail(R.drawable.icon_origin_order, "用车起点:", list.get(2));
        orderDetailList.add(orderDetai03);
        OrderDetail orderDetai04 = new OrderDetail(R.drawable.icon_des_order, "用车终点:", list.get(3));
        orderDetailList.add(orderDetai04);
        OrderDetail orderDetai05 = new OrderDetail(R.drawable.icon_con_order, "发货联系人:", list.get(4));
        orderDetailList.add(orderDetai05);
        OrderDetail orderDetai06 = new OrderDetail(R.drawable.icon_pone_order, "发货联系电话:", list.get(5));
        orderDetailList.add(orderDetai06);
        OrderDetail orderDetai07 = new OrderDetail(R.drawable.icon_con_order, "取货联系人:", list.get(6));
        orderDetailList.add(orderDetai07);
        OrderDetail orderDetai08 = new OrderDetail(R.drawable.icon_pone_order, "取货联系电话:", list.get(7));
        orderDetailList.add(orderDetai08);
    }

    private void initData(){
        Intent intent=getIntent();
        orderDetailList.get(2).setText02(intent.getStringExtra("begin"));
        orderDetailList.get(3).setText02(intent.getStringExtra("end"));
        orderDetailList.get(4).setText02(intent.getStringExtra("e1"));
        orderDetailList.get(5).setText02(intent.getStringExtra("e2"));
        orderDetailList.get(6).setText02(intent.getStringExtra("e3"));
        orderDetailList.get(7).setText02(intent.getStringExtra("e4"));
        adapter.notifyDataSetChanged();

        Log.i(TAG, "initData:测试 "+intent.getStringExtra("price"));
        TextView textView1=findViewById(R.id.text_price_1);
        textView1.setText(intent.getStringExtra("price")+"元");
        TextView textView2=findViewById(R.id.text_price_2);
        textView2.setText(intent.getStringExtra("price")+"元");
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
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
