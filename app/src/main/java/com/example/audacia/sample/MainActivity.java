package com.example.audacia.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Daum map api key
    static final String API_KEY = "847e6978474f073d697d46982b459a1f";

    TextView addtravelName;
    Button showDialog;
    DialogPopup dialog_popup;

    //카메라관련 코드 시작1
    Button btn_Camera;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private Uri fileUri;

    public static final int MEDIA_TYPE_IMAGE = 1;
    //카메라관련 코드 끝1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addtravelName = (TextView) findViewById(R.id.addtravelName);
        showDialog = (Button) findViewById(R.id.btn_start);

        //카메라관련 코드 시작2
        btn_Camera = (Button) findViewById(R.id.bottom3);
        findViewById(R.id.bottom3).setOnClickListener(mBtnCameraClick);
        //카메라관련 코드 끝2

        dialog_popup = new DialogPopup(MainActivity.this);
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

        // TravelListView로 이동
        Button btn_go = (Button) findViewById(R.id.bottom4);
        btn_go.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //TravelListView로 가는 인텐트를 생성
                        Intent intent = new Intent(v.getContext(), TravelListView.class);
                        //액티비티 시작!
                        startActivity(intent);
                    }
                }
        );
    }


    //카메라관련 코드 시작3
    Button.OnClickListener mBtnCameraClick = new Button.OnClickListener() {

        public void onClick(View v) {
            // 사진 촬영할때.
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    };


    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.e("com", "result_ok");

                // Image captured and saved to fileUri specified in the Intent
                if (data != null) {
                    Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();

                } else {
                    Log.e("onActivityResult", fileUri.getPath());
                }

            } else if (resultCode == RESULT_CANCELED) {
                Log.e("com", "result_canceled");

                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }
}
