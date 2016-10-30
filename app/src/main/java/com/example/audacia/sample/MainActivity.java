package com.example.audacia.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Daum map api key
    static final String API_KEY = "847e6978474f073d697d46982b459a1f";

    TextView addtravelName;
    Button showDialog;
    DialogPopup DialogPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addtravelName = (TextView) findViewById(R.id.addtravelName);
        showDialog = (Button) findViewById(R.id.btn_start);

        DialogPopup = new DialogPopup(MainActivity.this);
        DialogPopup.setTitle("여행명 입력"); //다이얼로그 타이틀 설정

        DialogPopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                addtravelName.setText(DialogPopup.getTravelName()); // 커스텀 다이얼로그로부터 여행지 이름 얻어옴
                View view = findViewById(R.id.btn_finish);
                view.setVisibility(View.VISIBLE);
                LinearLayout layout = (LinearLayout) findViewById(R.id.travelNameLayout);
                layout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "여행지 이름을 입력하였습니다", Toast.LENGTH_SHORT).show();
            }
        });

        DialogPopup.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(getApplicationContext(), "입력을 취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPopup.show();
            }
        });
    }


}
