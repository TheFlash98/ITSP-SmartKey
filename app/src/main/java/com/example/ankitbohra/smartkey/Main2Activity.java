package com.example.ankitbohra.smartkey;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity implements
        View.OnClickListener {

    Button btnDate, btnTime, add;
    EditText txtD, txtT;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int endYear, endMonth, endDay, endHour, endMinute;
    int startyear, startmonth, startday, starthour, startminute ;
    String username;
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String tempAccessUrl = "http://192.168.0.9:5000/tempaccess";
    int i = 0;

    android.os.Handler mhandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        startyear = bundle.getInt("start-year");
        startmonth = bundle.getInt("start-month");
        startday=bundle.getInt("start-day");
        starthour=bundle.getInt("start-hour");
        startminute=bundle.getInt("start-minute");
        Log.d("Random",startmonth+" is startmonth");


        btnDate=(Button)findViewById(R.id.btn_date);
        btnTime=(Button)findViewById(R.id.btn_time);
        add = (Button)findViewById(R.id.add);
        txtD=(EditText)findViewById(R.id.in_date);
        txtT=(EditText)findViewById(R.id.in_time);

        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        add.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == btnDate) {

            // Get Current Date
            i++;
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtD.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            endDay = dayOfMonth;
                            endYear = year;
                            endMonth = monthOfYear+1;

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            Log.d("Random","Mhour is "+mHour);
        }
        if (v == btnTime) {

            // Get Current Time
            i++;
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtT.setText(hourOfDay + ":" + minute);
                            endHour = hourOfDay;
                            endMinute = minute;
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if(v == add){
            String ssid="";
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo;

            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                ssid = wifiInfo.getSSID();
            }
            if(i>=2){
                String json = "{"
                        +"\"username\":\""+username+"\","
                        +"\"ssid\":"+ssid+","
                        +"\"start-year\":"+startyear+","
                        +"\"end-year\":"+endYear+","
                        +"\"start-month\":"+startmonth+","
                        +"\"end-month\":"+endMonth+","
                        +"\"start-date\":"+startday+","
                        +"\"end-date\":"+endDay+","
                        +"\"start-hours\":"+starthour+","
                        +"\"end-hours\":"+endHour+","
                        +"\"start-minute\":"+startminute+","
                        +"\"end-minute\":"+endMinute
                        +"}";
                Log.d("Random",json);

                RequestBody body = RequestBody.create(JSON, json);
                Request request = new com.squareup.okhttp.Request.Builder()
                        .url(tempAccessUrl)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(com.squareup.okhttp.Request request, IOException throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onResponse(final com.squareup.okhttp.Response response) throws IOException {
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
                            if(query.equals("Invalid Username")){
                                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main2Activity.this);
                                alertDialogBuilder.setMessage("Username is invalid");
                                alertDialogBuilder.setPositiveButton("Okay",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                Toast.makeText(Main2Activity.this,"Try Again!"+action,Toast.LENGTH_LONG).show();
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();*/
                            }
                            else if(query.equals("Invalid Time Span!")){

                            }
                            else if (query.equals("The end time is from the past.")){
                                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main2Activity.this);
                                alertDialogBuilder.setMessage("The end time is from the past.");
                                alertDialogBuilder.setPositiveButton("Okay",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                Toast.makeText(Main2Activity.this,"Try Again!"+action,Toast.LENGTH_LONG).show();
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();*/
                            }

                            //JSONObject sys = reader.getJSONObject("results");
                            //String parameters = sys.getString("parameters");
                            //Log.d("Random",parameters);
                        }
                        catch (org.json.JSONException e){
                            Log.d("Random",e.getMessage());
                        }
                        Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {

                                int serverResponseCode = response.code();
                                switch (serverResponseCode)
                                {
                                    case 200:
                                    {
                                        AlertDialog alertDialog;
                                        alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                        alertDialog.setTitle("Super :)");
                                        alertDialog.setMessage("Poza a fost trimisa cu success.");
                                        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int id) {

                                                finish();

                                            } });
                                        alertDialog.show();
                                        serverResponseCode = -1;

                                        break;
                                    }
                                    default:
                                    {
                                        AlertDialog alertDialog;
                                        alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                        alertDialog.setTitle("Eroare :(");
                                        alertDialog.setMessage("Eroare la trimiterea pozei.");
                                        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int id) {

                                                finish();

                                            } });
                                        alertDialog.show();

                                        break;
                                    }
                                }


                            }
                        };

                    }
                });
            }

        }

    }

 }




