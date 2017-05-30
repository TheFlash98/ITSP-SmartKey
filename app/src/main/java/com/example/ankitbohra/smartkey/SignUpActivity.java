package com.example.ankitbohra.smartkey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }



    private static final String signUpUrl = "http://192.168.0.9:5000/signup";
    private static final String makeAdminUrl = "http://192.168.0.9:5000/makeadmin";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void action(View view)throws IOException {
        EditText enterName = (EditText) findViewById(R.id.name);

        EditText password = (EditText) findViewById(R.id.password);

        EditText confirm_password = (EditText) findViewById(R.id.confirm_password);

        TextView warning = (TextView) findViewById(R.id.warning);

        String string = enterName.getText().toString();

        final String user_name = enterName.getText().toString();
        String pass = password.getText().toString();
        String conf_pass = confirm_password.getText().toString();
        Log.d("Random",jsonMaker(user_name,pass));
        if (pass.equals(conf_pass)){
            RequestBody body = RequestBody.create(JSON, jsonMaker(user_name,pass));
            Request request = new com.squareup.okhttp.Request.Builder()
                    .url(signUpUrl)
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
                        Log.d("Random","DoneTillHere");
                        //String s = (String)reader.nextValue();
                        Log.d("Random",query);
                        if(query.equals("Choose a different username")){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
                            alertDialogBuilder.setMessage("Username already taken.");
                            alertDialogBuilder.setPositiveButton("Okay",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Toast.makeText(SignUpActivity.this,"Try Again!",Toast.LENGTH_LONG).show();
                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                        else{
                            Intent intent = new Intent(SignUpActivity.this,SMARTKEYitsp.class);
                            intent.putExtra("username",user_name);
                            startActivity(intent);
                        }

                        //JSONObject sys = reader.getJSONObject("results");
                        //String parameters = sys.getString("parameters");
                        //Log.d("Random",parameters);
                    }
                    catch (org.json.JSONException e){
                        Log.d("Random",e.getMessage());
                    }
                }
            });


        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Passwords Don't Match");
            alertDialogBuilder.setPositiveButton("Okay",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Toast.makeText(SignUpActivity.this,"Try Again!",Toast.LENGTH_LONG).show();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    public String jsonMaker(String username, String password){
        String json = "{"
                + "\"username\":\""+username+"\","
                + "\"password\":\""+password+"\""
                + "}";

        return json;
    }

    public void makeAdmin(final String username){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        String ssid = "";
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }
        String json = jsonMaker2(username,ssid);
        Log.d("Random",json);
        RequestBody body = RequestBody.create(JSON, json);
        final Request request = new com.squareup.okhttp.Request.Builder()
                .url(makeAdminUrl)
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
                String reply = response.body().toString();
                //Log.d("Random",reply);
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
                    Log.d("Random","DoneTillHere");
                    //String s = (String)reader.nextValue();
                    Log.d("Random",query);
                    if(query.equals("No New")){
                        Intent intent = new Intent(SignUpActivity.this,userlogin.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                    else if (query.equals("New Admin")){
                        Intent intent = new Intent(SignUpActivity.this,LoggedIn.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }

                    //JSONObject sys = reader.getJSONObject("results");
                    //String parameters = sys.getString("parameters");
                    //Log.d("Random",parameters);
                }
                catch (org.json.JSONException e){
                    Log.d("Random",e.getMessage());
                }
            }
        });

    }

    public String jsonMaker2(String username, String ssid){
        String json = "{"
                + "\"username\":\""+username+"\","
                + "\"ssid\":"+ssid
                + "}";

        return json;
    }

}
