package com.example.administrator.jubangbangtest2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ActivityTest extends AppCompatActivity {

    private static final String TAG = "ActivityTest";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Log.i(TAG, "onCreate: ");
    }
}
