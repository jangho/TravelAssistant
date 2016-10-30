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
    dialog_popup dialog_popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addtravelName = (TextView) findViewById(R.id.addtravelName);
        showDialog = (Button) findViewById(R.id.btn_start);

        dialog_popup = new dialog_popup(MainActivity.this);
        dialog_popup.setTitle("여행명 입력"); //다이얼로그 타이틀 설정

        dialog_popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                addtravelName.setText(dialog_popup.getTravelName()); // 커스텀 다이얼로그로부터 여행지 이름 얻어옴
                View view = findViewById(R.id.btn_finish);
                view.setVisibility(View.VISIBLE);
                LinearLayout layout = (LinearLayout) findViewById(R.id.travelNameLayout);
                layout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "여행지 이름을 입력하였습니다", Toast.LENGTH_SHORT).show();
            }
        });

        dialog_popup.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(getApplicationContext(), "입력을 취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_popup.show();
            }
        });
    }
}
