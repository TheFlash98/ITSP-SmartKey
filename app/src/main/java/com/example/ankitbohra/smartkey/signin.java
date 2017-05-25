package com.example.ankitbohra.smartkey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    String username,password;
    CheckBox saveLoginCheckBox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    Boolean saveLogin;

    TextView tx1;
    int counter = 3;

    private static final String url = "http://192.168.1.4:5000/login";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        b1 = (Button)findViewById(R.id.button);
        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);

        b2 = (Button)findViewById(R.id.button2);
        tx1 = (TextView)findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);


    }


    public void action(View view){
        EditText enterName = (EditText)findViewById(R.id.editText);
        EditText enterPassword = (EditText)findViewById(R.id.editText2);

        String username = enterName.getText().toString();
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
                        Log.d("Random","Bingo!");
                    }
                    String query ="";
                    JSONObject jsonRootObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonRootObject.optJSONArray("results");
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        query = jsonObject.optString("parameters").toString();
                    }
                    Log.d("Random","DoneTillHere");
                    //String s = (String)reader.nextValue();
                    Log.d("Random",query);
                    if(query.equals("Grant Access")){
                        Intent intent = new Intent(signin.this,LoggedIn.class);
                        startActivity(intent);
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
}
