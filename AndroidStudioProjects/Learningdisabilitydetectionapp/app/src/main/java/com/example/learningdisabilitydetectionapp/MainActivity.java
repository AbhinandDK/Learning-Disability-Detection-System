package com.example.learningdisabilitydetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText ip;
    Button b5;
    SharedPreferences sh;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = findViewById(R.id.editTextTextPersonName4);

        sh  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip.setText(sh.getString("ip", "192.168.29.232"));
        b5 = findViewById(R.id.button6);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ipaddress = ip.getText().toString();
                url  = "http://"+ipaddress+":7500";
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("ip",ipaddress);
                ed.putString("url",url);
                ed.commit();
                Intent i=new Intent(getApplicationContext(),login.class);
                startActivity(i);
            }
        });


    }
}