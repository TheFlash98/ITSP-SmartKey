package com.example.ankitbohra.smartkey;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class signin extends AppCompatActivity {
    Button b1,b2;
    EditText ed1,ed2;

    TextView tx1;

    private static final String url = "http://192.168.0.9:5000/login";
    private static final String checkRightsUrl = "http://192.168.0.9:5000/checkrights";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    public void action(View view){
        EditText enterName = (EditText)findViewById(R.id.editText);
        EditText enterPassword = (EditText)findViewById(R.id.editText2);

        final String username = enterName.getText().toString();
        String password = enterPassword.getText().toString();

        String json = jsonMaker(username,password);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new com.squareup.okhttp.Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException{
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                //TextView textView = (TextView)findViewById(R.id.demo);
                //Log.d("Random",response.body().string());
                String reply = response.body().toString();
                //Log.d("Random",reply);
                try {
                    //JSONObject object = (JSONObject) new JSONTokener(response.body().string()).nextValue();
                    //String query = object.getString("results");
                    if(response.code()==200){
                       // Log.d("Random","Bingo!");
                    }
                    String query ="";
                    JSONObject jsonRootObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonRootObject.optJSONArray("results");
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        query = jsonObject.optString("param").toString();
                    }
                    //Log.d("Random","DoneTillHere");
                    //String s = (String)reader.nextValue();
                    //Log.d("Random",query);
                    if(query.equals("Grant Access")){
                        checkRights(username);
                    }

                    //JSONObject sys = reader.getJSONObject("results");
                    //String parameters = sys.getString("parameters");
                    //Log.d("Random",parameters);
                }
                catch (org.json.JSONException e){
                    Log.d("Random",e.getMessage());
                }


                //Intent intent = new Intent(signin.this , LoggedIn.class);
                //startActivity(intent);
            }
        });
    }

    String jsonMaker(String username, String password){
        String json = "{\"username\":"+"\""+username+"\","
                       +"\"password\":"+"\""+password+"\""
                       +"}" ;
        return json;
    }

    public void checkRights(final String username){
        String json = jsonMaker2(username);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new com.squareup.okhttp.Request.Builder()
                .url(checkRightsUrl)
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
                    Log.d("Random","DoneTillHere");
                    Log.d("Random",query);
                    if(query.equals("Admin")){
                        Intent intent = new Intent(signin.this, LoggedIn.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                    else if(query.equals("Not Admin")){
                        Intent intent = new Intent(signin.this, userlogin.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                }
                catch (org.json.JSONException e){
                    Log.d("Random",e.getMessage());
                }
            }
        });

    }

    public String jsonMaker2(String username){
        String json = "{"
                + "\"username\":\""+username+"\""
                +"}";
        return json;
    }
}
