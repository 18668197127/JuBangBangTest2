package com.example.administrator.jubangbangtest2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.administrator.jubangbangtest2.R;

public class MyDialog extends Dialog {
    private Context mContext;
    private ImageButton imageButtonCancel;
    private Button buttonConfirm;
    private String message;

    private MyOnclickListener mMyOnclickListener;

    public MyDialog(Context context) {
        super(context);
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_item01);
        setCanceledOnTouchOutside(false);
        initView();
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width= 948;
        layoutParams.height= 885;
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.x=66;
        layoutParams.y=420;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }


    private void initView(){
        imageButtonCancel=findViewById(R.id.imagebutton_cancel);
        buttonConfirm=findViewById(R.id.button_confirm);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("测试自定义dialog的按钮点击");
                mMyOnclickListener.onYesClick(message);
                dismiss();
            }
        });
    }

    public interface MyOnclickListener{
        public void onYesClick(String message);
    }

    public void setMyOnclickListener(String message,MyOnclickListener myOnclickListener){
        this.message=message;
        this.mMyOnclickListener=myOnclickListener;
    }
}
