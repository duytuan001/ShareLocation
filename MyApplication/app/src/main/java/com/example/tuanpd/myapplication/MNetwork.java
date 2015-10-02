package com.example.tuanpd.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;


public class MNetwork {

    private static final String UTF_8 = "UTF-8";
    private static final String POST = "POST";
    private static final String GET = "GET";

    public static final int REQ_GROUP_INFO = 0;
    public static final int REQ_GROUP_JOINT = 1;
    public static final int REQ_GROUP_LEAVE = 2;
    public static final int REQ_GROUP_CREATE = 3;
    public static final int REQ_GROUP_OPEN = 4;
    public static final int REQ_GROUP_CLOSE = 5;
    public static final int REQ_GROUP_JOINT_NEAR = 6;

    public static final int REQ_USER_DELETE = 7;
    public static final int REQ_USER_UPDATE_LOCATION = 8;

    public void handleGroupRequest(int req) {

    }

    public void handleGroupRequest(int req, LocationGroup group) {

    }

    public void handleUserRequest(int req, LocationUser user) {

    }

    public static boolean hasConnection(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }


    public String doRequest(String url, ContentValues params, String method) throws IOException {
        Uri.Builder builder = new Uri.Builder();
        for (Map.Entry<String, Object> entry : params.valueSet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        String query = builder.build().getEncodedQuery();


        HttpURLConnection conn = null;
        if (GET.equals(method)) {
            conn = (HttpURLConnection) new URL(url + "?" + query).openConnection();
        } else if (POST.equals(method)) {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
        }

        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod(method);
        conn.setDoInput(true);
//        conn.setChunkedStreamingMode();
        conn.connect();

        int response = conn.getResponseCode();
        InputStream is = conn.getInputStream();
        return readStream(is);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
