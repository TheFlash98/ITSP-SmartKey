package com.example.ankitbohra.smartkey;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    Button btnDatePicker, btnTimePicker, next;
    EditText txtDate, txtTime;
    int mYear, mMonth, mDay, mHour, mMinute;
    int startYear, startMonth, startDay, startHour, startMinute;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        next = (Button)findViewById(R.id.next);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        next.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");

    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            startDay = dayOfMonth;
                            startMonth = monthOfYear+1;
                            startYear = year;

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                            startHour = hourOfDay;
                            startMinute = minute;
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        }

        if(v == next){
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            intent.putExtra("username",username);
            intent.putExtra("start-year",startYear);
            intent.putExtra("start-month",startMonth);
            intent.putExtra("start-day",startDay);
            intent.putExtra("start-hour",startHour);
            intent.putExtra("start-minute",startMinute);
            startActivity(intent);
        }
    }
/**
    public void selectDate(View view){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void selectTime(View view){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void next(View view){
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        intent.putExtra("username",username);
        intent.putExtra("start-year",mYear);
        intent.putExtra("start-month",mMonth);
        intent.putExtra("start-day",mDay);
        intent.putExtra("start-hour",mHour);
        intent.putExtra("start-minute",mMinute);
        startActivity(intent);
    }
    **/

}
