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

public class AdminLoginActivity extends AppCompatActivity {
    String username = "";
    public static final String URL = MyApp.url;
    private static final String operateLockUrl = URL+"operatelock";
    private static final String tempUserAccessUrl = URL+"tempaccess";
    private static final String addAdminUrl = URL+"makeadmin";
    private static final String removeAdminUrl = URL+"removeadmin";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String ssid = "";
    int i=0,j=0;
    AdminLoginActivity obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        obj = AdminLoginActivity.this;

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
                    if(i==1){
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminLoginActivity.this);
                                alertDialogBuilder.setMessage("The lock is already open.");
                                alertDialogBuilder.setPositiveButton("Okay",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                return;
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        });
                    }
                    else if(query.equals("Operate Lock")){
                        i = 1;
                        j = 0;
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                generateDialog("Operated Lock Successfully","Lock is Open");
                            }
                        });
                    }
                    else if(query.equals("You cannot operate the lock")){
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                generateDialog("You don't have the access rights to this lock","Contact the Admin");
                            }
                        });
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
                    if(j==1){
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminLoginActivity.this);
                                alertDialogBuilder.setMessage("The lock is already closed.");
                                alertDialogBuilder.setPositiveButton("Okay",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                return;
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        });
                    }
                    else if(query.equals("Operate Lock")){
                        j = 1;
                        i = 0;
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                generateDialog("Lock Operated Successfully","Lock is Closed");
                            }
                        });
                    }
                    else if(query.equals("You cannot operate the lock")){
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                generateDialog("You don't have the access rights to this lock","Contact the Admin");
                            }
                        });
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminLoginActivity.this);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(AdminLoginActivity.this,Error,Toast.LENGTH_LONG).show();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void tempAccess(View view){
        Intent intent = new Intent(AdminLoginActivity.this,AddTempActivity.class);
            startActivity(intent);
    }

    public void adminaccess(View view){
        Intent intent = new Intent(AdminLoginActivity.this,AddAdminActivity.class);
            startActivity(intent);
    }


}