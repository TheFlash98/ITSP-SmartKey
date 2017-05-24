package com.example.ankitbohra.smartkey;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;


public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }



    private static final String url = "http://192.168.1.4:5000/signup?";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void action(View view)throws IOException {
        EditText enterName = (EditText) findViewById(R.id.name);

        EditText password = (EditText) findViewById(R.id.password);

        EditText confirm_password = (EditText) findViewById(R.id.confirm_password);

        TextView warning = (TextView) findViewById(R.id.warning);

        String string = enterName.getText().toString();

        if (!checkUniqueness(string)) {
            warning.setText("Choose a different username");
            return;
        }
        String user_name = enterName.getText().toString();
        String pass = password.getText().toString();
        String conf_pass = confirm_password.getText().toString();
        if (pass.equals(conf_pass)){
            warning.setText("Successful!");
            RequestBody body = RequestBody.create(JSON, jsonMaker(user_name,pass));
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
                public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                }
            });
        }
        else {
            warning.setText("Passwords don't match. Recheck!");

        }
    }

    public String jsonMaker(String username, String password){
        String json = "{\"email\":\"demo@gmail.com\","
                + "\"username\":\""+username+"\","
                + "\"phone\":\"123456789\","
                + "\"password\":\""+password+"\""
                + "}";

        return json;
    }


    public boolean checkUniqueness(String s){
        return true;
    }
}
