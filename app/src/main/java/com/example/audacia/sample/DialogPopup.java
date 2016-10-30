package com.example.audacia.sample;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View.OnTouchListener;
import android.content.Context;
import android.view.*;
import android.widget.*;


public class DialogPopup extends Dialog implements OnTouchListener {
    private EditText travelName;
    private Button addOK, addCancel;
    private String _travelName;

    public DialogPopup(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_popup);

        travelName = (EditText)findViewById(R.id.travelName);
        addOK = (Button)findViewById(R.id.addOK);
        addCancel = (Button)findViewById(R.id.addCancel);

        addOK.setOnTouchListener(this);
        addCancel.setOnTouchListener(this);
    }

    public String getTravelName(){
        return _travelName;
    }

    public boolean onTouch(View v, MotionEvent event){
        if(v == addOK){
            _travelName = travelName.getText().toString();
            dismiss();
        }
        else if(v == addCancel)
            cancel();

        return false;
    }

}








