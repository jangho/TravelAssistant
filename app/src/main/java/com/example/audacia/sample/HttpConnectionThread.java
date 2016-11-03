package com.example.audacia.sample;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by n028tu on 2016-11-02.
 */
public class HttpConnectionThread extends AsyncTask<String, Void, String> {
    private Context mContext;
    private URL url;
    private String response = null;
    private static double baseLatitude, baseLongitude;
    double latitude;
    double longitude;
    private static boolean isFirst = false;


    HttpConnectionThread(Context context, String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
        }
        mContext = context;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if (unit == "meter") {
            dist = dist * 1609.344;
        }

        return (dist);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private boolean isOutbound() {
        if (isFirst) {
            double dist = distance(baseLatitude, baseLongitude, latitude, longitude, "kilometer");
            if (dist > 2.0)
                return true;
        }

        return false;
    }

    @Override
    protected String doInBackground(String... params) {
        if(!isFirst || isOutbound() ) {
            try {
                Properties prop = new Properties();
                prop.setProperty("msg", "hello");
                prop.setProperty("gps_x", Double.toString(latitude));
                prop.setProperty("gps_y", Double.toString(longitude));
                baseLatitude = latitude;
                baseLongitude = longitude;
                String encodedString = encodeString(prop);
                URL url = new URL("http://52.78.197.74:8080/test.jsp" + "?" + encodedString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);

                conn.connect();

                response = conn.getHeaderField("tnwls");

                //response = conn.getResponseMessage();
            } catch (IOException e) {

            }
          isFirst = true;
        }
            return response;

    }

    public static String encodeString(Properties params) {
        StringBuffer sb = new StringBuffer(256);
        Enumeration names = params.propertyNames();

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = params.getProperty(name);
            sb.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));

            if (names.hasMoreElements())
                sb.append("&");
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        //mContext
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    public void setLocation(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
