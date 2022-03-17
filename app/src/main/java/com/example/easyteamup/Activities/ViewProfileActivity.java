package com.example.easyteamup.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.easyteamup.R;

public class ViewProfileActivity extends AppCompatActivity {

    private Intent getIntent;
    private TextView welcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getSupportActionBar().hide();

        getIntent = getIntent();
        String uid = getIntent.getStringExtra("uid");
        /*
            Get user object from uid
         */

        welcome = (TextView) findViewById(R.id.welcome);

    }
}