package com.example.ankitbohra.smartkey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AddTempActivity extends AppCompatActivity {

    public static final String URL = MyApp.url;
    private static final String validUserUrl = URL+"validuser";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    AddTempActivity obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtemp);

        obj = AddTempActivity.this;
    }

    public void tempUserAdd(View view){
        final EditText editText = (EditText) findViewById(R.id.temp_user);
        final String user_name = editText.getText().toString();
        RequestBody body = RequestBody.create(JSON, jsonMaker(user_name));
        Request request = new com.squareup.okhttp.Request.Builder()
                .url(validUserUrl)
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
                    if(query.equals("Not Ok")){
                        obj.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddTempActivity.this);
                                alertDialogBuilder.setMessage("Username Not Found");
                                alertDialogBuilder.setPositiveButton("Okay",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                Toast.makeText(AddTempActivity.this,"Try Again!",Toast.LENGTH_LONG).show();
                                                editText.setText("");
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        });

                    }
                    else if(query.equals("OK")){
                        Intent intent = new Intent(AddTempActivity.this,StartTimeActivity.class);
                        intent.putExtra("username",user_name);
                        startActivity(intent);
                    }

                }
                catch (org.json.JSONException e){
                    Log.d("Random",e.getMessage());
                }
            }
        });


    }

    public String jsonMaker(String username){
        String json = "{"
                +"\"username\":\""+username+"\""
                +"}";
        return json;
    }
}
