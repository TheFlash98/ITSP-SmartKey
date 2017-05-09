package com.example.ankitbohra.smartkey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SMARTKEYitsp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartkeyitsp);
    }

    public void signIn(View view){
        Intent signing_in = new Intent(SMARTKEYitsp.this , signin.class);
        startActivity(signing_in);
    }

    public void signUp(View view){
        Intent signing_up = new Intent(SMARTKEYitsp.this , SignUpActivity.class);
        startActivity(signing_up);
    }
}
