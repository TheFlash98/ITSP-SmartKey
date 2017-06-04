package com.example.ankitbohra.smartkey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class AddAdminActivity extends AppCompatActivity {
    String username = "";
    public static final String URL = MyApp.url;
    private static final String addAdminUrl = URL+"makeadmin";
    private static final String removeAdminUrl = URL+"removeadmin";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String ssid = "";
    AddAdminActivity obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addadmin);

        obj = AddAdminActivity.this;
    }

    public void insertadmin(View view){
        final EditText editText = (EditText)findViewById(R.id.addadmin);
        String user_name = editText.getText().toString();
        Log.d("Random",user_name);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;

        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }
        String json = jsonMaker(user_name, ssid);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new com.squareup.okhttp.Request.Builder()
                .url(addAdminUrl)
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
                        Log.d("Random",query);
                    }

                    if(query.equals("User Not Found")){
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                generateDialog("User not found.","Try Again!");
                            }
                        });
                    }
                    else if(query.equals("Already Admin")){
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                generateDialog("This User is already an Admin of this lock","Try Again!");
                            }
                        });
                    }
                    else{
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddAdminActivity.this);
                                alertDialogBuilder.setMessage("Added Successfully");
                                alertDialogBuilder.setPositiveButton("Okay",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                               editText.setText("");
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
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

    public void deleteadmin(View view){
        final EditText editText = (EditText)findViewById(R.id.addadmin);
        String user_name = editText.getText().toString();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;

        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }
        String json = jsonMaker(user_name, ssid);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new com.squareup.okhttp.Request.Builder()
                .url(removeAdminUrl)
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
                    if(query.equals("User Not Found")){
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                generateDialog("User does not exist.","Try Again!");
                            }
                        });
                    }
                    else if(query.equals("Admin Not Found")){
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                generateDialog("This User is not an Admin","Try Again!");
                            }
                        });
                    }
                    else{
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddAdminActivity.this);
                                alertDialogBuilder.setMessage("Deleted Successfully");
                                alertDialogBuilder.setPositiveButton("Okay",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                editText.setText("");
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
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

    public String jsonMaker(String username, String ssid){
        String json = "{"
                + "\"username\":\""+username+"\","
                + "\"ssid\":"+ssid
                + "}";
        Log.d("Random",json);
        return json;
    }

    public void generateDialog(String s, final String error){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddAdminActivity.this);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(AddAdminActivity.this,error,Toast.LENGTH_LONG).show();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
