package com.example.metricconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//hide the default action bar

        //below is the handler where we call run function after 2000 milliseconds.
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();//this will close first activity
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);//this will start homeactivity
            }
        }, 2000);
    }
}