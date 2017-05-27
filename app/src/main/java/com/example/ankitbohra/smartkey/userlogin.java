package com.example.ankitbohra.smartkey;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class userlogin extends AppCompatActivity {
    String username = "";
    private static final String operateLockUrl = "http://192.168.0.9:5000/operatelock";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        Bundle bundle = getIntent().getExtras();
        String user_name = bundle.getString("username");

        username = user_name;
    }

    public void lock(View view){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        String ssid = "";
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }
        String json = jsonMaker(username, ssid);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new com.squareup.okhttp.Request.Builder()
                .url(operateLockUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                try {
                    //JSONObject object = (JSONObject) new JSONTokener(response.body().string()).nextValue();
                    //String query = object.getString("results");
                    if(response.code()==200){
                        Log.d("Random","Bingo!");
                    }
                    String query ="";
                    JSONObject jsonRootObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonRootObject.optJSONArray("results");
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        query = jsonObject.optString("param").toString();
                    }
                    if(query.equals("Open Lock")){
                        Intent intent = new Intent(userlogin.this, LockOpen.class);
                        startActivity(intent);
                    }

                }
                catch (org.json.JSONException e){
                    Log.d("Random",e.getMessage());
                }
            }
        });
    }

    public String jsonMaker(String username, String ssid){
        String json = "{"
                + "\"username\":\""+username+"\","
                + "\"ssid\":"+ssid
                + "}";

        return json;
    }
}

