package com.example.ankitbohra.smartkey;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void action(View view){
        EditText enterName = (EditText) findViewById(R.id.name);
        EditText password = (EditText) findViewById(R.id.password);
        EditText confirm_password = (EditText)findViewById(R.id.confirm_password);
        TextView warning = (TextView)findViewById(R.id.warning);
        String string = enterName.getText().toString();
        if(!checkUniqueness(string)){
            warning.setText("Choose a different username");
            return;
        }

        if(password.getText().toString().equals(confirm_password.getText().toString())){
            warning.setText("Successful!");
        }
        else{
            warning.setText("Passwords don't match. Recheck!");
        }
        return;
    }

    public boolean checkUniqueness(String s){
        return true;
    }
}
