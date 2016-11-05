package com.example.audacia.sample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera_Finish extends AppCompatActivity {

    TextView addtravelName;
    DialogPopup dialog_popup;

    //카메라관련 코드 시작1
    Button btn_Camera;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final String KEY_PHOTO_PATH = "photoPath";
    private String photoName = null;
    private File photoFile = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //카메라관련 코드 끝1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera__finish);

        addtravelName = (TextView)findViewById(R.id.addtravelName);
        Intent intent = getIntent();
        String travelName = intent.getExtras().getString("travelName");
        addtravelName.setText(travelName);

        //카메라관련 코드 시작2
        // Check if anything in savedInstanceState Bundle.
        // If so, Activity is being recreated after screen orientation change,
        //   so get saved information from Bundle.
        if (savedInstanceState != null) {
            photoName = savedInstanceState.getString(KEY_PHOTO_PATH);
        }

        btn_Camera = (Button) findViewById(R.id.bottom3);
        findViewById(R.id.bottom3).setOnClickListener(mBtnCameraClick);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        //카메라관련 코드 끝2

    }

    //카메라관련 코드 시작3
    Button.OnClickListener mBtnCameraClick = new Button.OnClickListener() {

        public void onClick(View v) {
            // 사진 촬영할때.
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Check to make sure there is an installed app that can handle this Intent.
            // If not, this app would crash when Intent is sent.
            if (intent.resolveActivity(getPackageManager()) != null) {

                // Create the File where the photo should go
                photoFile = null;
                photoFile = getFileName();

                // Continue only if the File was successfully created.
                // Add the file name to the Intent, using Intent.putExtra().
                // MediaStore will use our file name to store the image.
                // After the picture is taken, MediaStore will return to this Activity
                //    in the onActivityResult() method.
                if (photoFile != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
            } else {
                // Log an error message
                Log.e("Photo", "No app installed to handle taking a photo");
            }
            // start the image capture Intent
            //startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    };

    private File getFileName() {

        // Create a folder to store photos
        String folderCount = String.valueOf(HttpConnectionThread.count);
        String folderName = this.getString(R.string.app_name)+folderCount;
        File appFolder = new File(Environment.getExternalStorageDirectory(), folderName);
        // Create appFolder, if it does nor already exist
        appFolder.mkdirs();

        // Create an image file name.
        // Use time stamp as part of the file name so that each new file name is unique.
        // (In your own app, use whatever file naming you choose.)
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";

        photoFile = new File(appFolder, imageFileName);
        photoName = photoFile.getAbsolutePath();

        return photoFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO) {
            // Get location information
            Location location = getLocation();

            Double textLat = location.getLatitude();
            Double textLong = location.getLongitude();

            HttpConnectionThread thread = new HttpConnectionThread
                    (getApplicationContext(), "http://52.78.197.74:8080/test.jsp");

            thread.setLocation(textLat, textLong);
            thread.execute();

            if (location == null) {
                textLat = 0.0;
                textLong = 0.0;
            } else {

                if (photoName != null) {
                    // Encode and store the location in the Exif information of the photo file.
                    loc2Exif(photoName, location);
                    Toast.makeText(Camera_Finish.this, Double.toString(textLat) + ", " + Double.toString(textLong), Toast.LENGTH_SHORT).show();
                }
            }
        }
        // Check resultCode
        if (resultCode == RESULT_OK) {

            // Instead of using "data", read in the file that was stored by MediaStore.
            // "data" may contain a thumbnail image.
            // Pass our own saved file name to BitmapFactory to read the file.

            //loadPhoto();
        } else {
            Log.e("Photo", "Problem taking photo");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

    }
    private Location getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location == null)
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null)
            location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        return location;
    }

    // Convert location to Exif format,
    //    then save the data tag into the stored JPG image file.
    public void loc2Exif(String fileName, Location location) {
        try {
            ExifInterface exif = new ExifInterface(fileName);
            if (location == null) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, dec2Tag(location.getLatitude()));
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, dec2Tag(location.getLongitude()));

                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, dec2Tag(location.getLatitude()));
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, dec2Tag(location.getLongitude()));

                if (location.getLatitude() > 0d)
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
                else
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");

                if (location.getLongitude() > 0d)
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
                else
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            SimpleDateFormat fmt_Exif = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            exif.setAttribute(ExifInterface.TAG_DATETIME, fmt_Exif.format(new Date(location.getTime())));

            // Save the tag data into the JPEG file.
            exif.saveAttributes();

        } catch (IOException e) {
            Log.e("Photo", "IO Exception: " + e.getMessage());
        }
    }

    // Format latitude or longitude as a String of degrees, minutes, seconds
    private String dec2Tag(double coordinate) {

        // Get absolute value of the coordinate (if negative, make it positive).
        coordinate = Math.abs(coordinate);  // -105.9876543 -> 105.9876543

        // Place degrees into String.
        String stringCoord = Integer.toString((int) coordinate) + "/1,";  // 105/1,

        // Place minutes into String.
        coordinate = (coordinate % 1) * 60;  // .987654321 * 60 = 59.259258
        stringCoord = stringCoord + Integer.toString((int) coordinate) + "/1,";  // 105/1,59/1,

        // Place seconds into String.
        coordinate = (coordinate % 1) * 60000;  // .259258 * 60000 = 15555
        stringCoord = stringCoord + Integer.toString((int) coordinate) + "/1000";  // 105/1,59/1,15555/1000

        return stringCoord;
    }


    // Save information in Bundle in case screen orientation changes when picture is taken.
    // Orientation change causes Activity to be recreated, and any variables lost unless saved.
    @Override
    protected void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
        // Save (key, value) pair for photo path.
        saveState.putString(KEY_PHOTO_PATH, photoName);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    //카메라관련 코드 끝3
}
