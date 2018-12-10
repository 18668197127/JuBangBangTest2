package com.example.administrator.jubangbangtest2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.services.core.LatLonPoint;
import com.example.administrator.jubangbangtest2.R;
import com.example.administrator.jubangbangtest2.data.LocationHint;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{
    private List<LocationHint> mLocationHintList;
    private AdapterCallback mAdapterCallback;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View locationView;
        TextView locationName;
        TextView locationAddress;

        public ViewHolder(View view){
            super(view);
            locationView=view;
            locationName=view.findViewById(R.id.location_hint_name);
            locationAddress=view.findViewById(R.id.location_hint_address);
        }
    }

    public LocationAdapter(List<LocationHint> locationHintList,Context context){
        mLocationHintList=locationHintList;
        this.mAdapterCallback = ((AdapterCallback) context);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_location_item,viewGroup,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                LocationHint locationHint=mLocationHintList.get(position);
                String locationNameBack=locationHint.getLocationName();
                String locationAddressBack=locationHint.getLocationAddress();
                EditText editText=v.getRootView().findViewById(R.id.search_edit_text);
                editText.setText(locationNameBack+"("+locationAddressBack+")");
                LatLonPoint latLonPoint=locationHint.getLatLonPoint();
                mAdapterCallback.onMethodCallback(latLonPoint);
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(LocationAdapter.ViewHolder viewHolder, int i) {
        LocationHint locationHint=mLocationHintList.get(i);
        viewHolder.locationName.setText(locationHint.getLocationName());
        viewHolder.locationAddress.setText(locationHint.getLocationAddress());
    }

    @Override
    public int getItemCount() {
        return mLocationHintList.size();
    }

    public static interface AdapterCallback {
        void onMethodCallback(LatLonPoint latLonPoint);
    }
}
