package com.example.administrator.jubangbangtest2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.jubangbangtest2.R;

public class CarTypeFragment extends Fragment {

    int number=0;
    private static final String TAG = "CarTypeFragment";

    public static CarTypeFragment newInstance(int i){
        CarTypeFragment fragment=new CarTypeFragment();
        Bundle args=new Bundle();
        args.putInt("number",i);
        fragment.setArguments(args);
        Log.i(TAG, "newInstance: "+i);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.huoyun_cartype_fragment,container,false);
        Log.i(TAG, "onCreateView: "+number);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: "+number);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        number=getArguments().getInt("number");
        Log.i(TAG, "onAttach: "+number);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: "+number);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: "+number);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: "+number);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: "+number);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: "+number);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: "+number);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: "+number);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: "+number);
    }
}
