package com.ayra.pdamsurakarta.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ayra.pdamsurakarta.BuildConfig;
import com.ayra.pdamsurakarta.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MySingleton {

    @SuppressLint("StaticFieldLeak")
    private static volatile MySingleton instance;
    private RequestQueue requestQueue;
    private Context mContext;

    // Constructor
    private MySingleton(Context context) {
        this.mContext = context;
        requestQueue = getRequestQueue();
    }

    // Singleton
    public static synchronized MySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new MySingleton(context);
        }
        return instance;
    }

    // Create Request Queue
    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    // Encrypt Input With MD5
    private String MD5(String input) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder stringBuilder = new StringBuilder();
            for (byte mMessageDigest : messageDigest) {
                StringBuilder stringBuilder1 = new StringBuilder(Integer.toHexString(0xFF & mMessageDigest));
                while (stringBuilder1.length() < 2)
                    stringBuilder1.insert(0, "0");
                stringBuilder.append(stringBuilder1);
            }

            return stringBuilder.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.d(MySingleton.class.getSimpleName(), "MD5: ");
        }
        return "";
    }

    // API Connect
    public void connectApi(String url, String uuid, String ppid, String uData, Response.Listener<JSONObject> jsonObjectListener, Response.ErrorListener errorListener) {
        HttpTrustManager.nuke();
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";

            String ket = "siPoint Android";
            String time = new SimpleDateFormat(pattern, Locale.getDefault()).format(Calendar.getInstance().getTime());
            String apiKey = Constants.apiKey.apiKey;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uuid", MD5(uuid).toUpperCase());
            jsonObject.put("ppid", Base64.encodeToString(ppid.getBytes("UTF-8"), Base64.NO_WRAP | Base64.URL_SAFE));
            jsonObject.put("udata", Base64.encodeToString(uData.getBytes("UTF-8"), Base64.NO_WRAP | Base64.URL_SAFE));
            jsonObject.put("dtime", time);
            jsonObject.put("ket", ket);
            jsonObject.put("X-API-KEY", apiKey);
            jsonObject.put("signature", MD5("tele-android-" + MD5(uuid).toUpperCase() + ket + time + ppid + uData + apiKey + "-indonesia"));
            Log.d(MySingleton.class.getSimpleName(), "connectApi: $jsonObject");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, jsonObjectListener, errorListener);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(35000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            addRequestQueue(jsonObjectRequest);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    // Add To Request Queue
    private <T> void addRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    // Hit API Version
    public void version(Response.Listener<JSONObject> jsonObjectListener, Response.ErrorListener errorListener) {
        HttpTrustManager.nuke();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://103.245.181.220/dversion_ANDROID", null, jsonObjectListener, errorListener);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(35000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequestQueue(jsonObjectRequest);
    }

}
