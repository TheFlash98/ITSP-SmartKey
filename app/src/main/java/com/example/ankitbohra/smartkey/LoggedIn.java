package com.example.ankitbohra.smartkey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class LoggedIn extends AppCompatActivity {
    String username = "";
    private static final String operateLockUrl = "http://192.168.0.9:5000/operatelock";
    private static final String tempUserAccessUrl = "http://192.168.0.9:5000/tempaccess";
    private static final String addAdminUrl = "http://192.168.0.9:5000/makeadmin";
    private static final String removeAdminUrl = "http://192.168.0.9:5000/removeadmin";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String ssid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        Bundle bundle = getIntent().getExtras();
        String user_name = bundle.getString("username");

        username = user_name;
    }

    public void openlock(View view){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        String ssid = "";
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }
        String json = jsonMaker(username, ssid,"open");
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
                    if(query.equals("Operate Lock")){
                        generateDialog("Operated Lock Successfully","Lock is Open");
                    }
                    else if(query.equals("You cannot operate the lock")){
                        generateDialog("You don't have the access rights to this lock","Contact the Admin");
                    }

                }
                catch (org.json.JSONException e){
                    Log.d("Random",e.getMessage());
                }
            }
        });
    }

    public void closelock(View view){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        String ssid = "";
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }
        String json = jsonMaker(username, ssid,"close");
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
                    if(query.equals("Operate Lock")){
                        generateDialog("Lock Operated Successfully","Lock is Closed");
                    }

                }
                catch (org.json.JSONException e){
                    Log.d("Random",e.getMessage());
                }
            }
        });
    }

    public String jsonMaker(String username, String ssid,String action){
        String json = "{"
                + "\"username\":\""+username+"\","
                + "\"ssid\":"+ssid+","
                + "\"action\":\""+action+"\""
                + "}";

        return json;
    }

    public void generateDialog(String s, final String Error){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoggedIn.this);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(LoggedIn.this,Error,Toast.LENGTH_LONG).show();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void tempAccess(View view){
        Intent intent = new Intent(LoggedIn.this,addtemp.class);
            startActivity(intent);
    }

    public void adminaccess(View view){
        Intent intent = new Intent(LoggedIn.this,addadmin.class);
            startActivity(intent);
    }


}