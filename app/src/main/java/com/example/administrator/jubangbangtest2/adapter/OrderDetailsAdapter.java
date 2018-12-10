package com.example.administrator.jubangbangtest2.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.administrator.jubangbangtest2.R;
import com.example.administrator.jubangbangtest2.data.OrderDetail;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private List<OrderDetail> mOrderDetailList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView orderImage;
        TextView orderText01;
        TextView orderText02;

        public ViewHolder(View view){
            super(view);
            orderImage=view.findViewById(R.id.order_detail_image);
            orderText01=view.findViewById(R.id.order_detail_text01);
            orderText02=view.findViewById(R.id.order_detail_text02);
        }
    }

    public OrderDetailsAdapter(List<OrderDetail> orderDetailList){
        mOrderDetailList=orderDetailList;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_detail_item,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        OrderDetail orderDetail=mOrderDetailList.get(i);
        viewHolder.orderImage.setImageResource(orderDetail.getImageId());
        viewHolder.orderText01.setText(orderDetail.getText01());
        viewHolder.orderText02.setText(orderDetail.getText02());
    }

    @Override
    public int getItemCount() {
        return mOrderDetailList.size();
    }
}
