package com.example.learningdisabilitydetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    EditText usname,password;
    Button b1;
    String url;
    TextView t1;
    SharedPreferences sh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usname = findViewById(R.id.editTextTextPersonName);
        password =findViewById(R.id.editTextTextPassword);
        b1 = findViewById(R.id.button);
        t1 = findViewById(R.id.textView2);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),registernew.class);
                startActivity(i);
            }
        });

        SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url=sh.getString("url","")+"/user_login";


        b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String username = usname.getText().toString();
               String password1 = password.getText().toString();


               RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
               StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                       new Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {
//                                 Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                               // response
                               try {
                                   JSONObject jsonObj = new JSONObject(response);
                                   if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                       String lid=jsonObj.getString("lid");
                                       SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                       SharedPreferences.Editor ed=sh.edit();
                                       ed.putString("lid",lid);
                                       ed.commit();
                                       Intent i=new Intent(getApplicationContext(),home.class);
                                       startActivity(i);




                                   }



                                   else {
                                       Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                                   }

                               }    catch (Exception e) {
                                   Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                               }
                           }
                       },
                       new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {
                               // error
                               Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                           }
                       }
               ) {
                   @Override
                   protected Map<String, String> getParams() {
                       SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                       Map<String, String> params = new HashMap<String, String>();

//                       String id=sh.getString("uid","");
                       params.put("username",username);
                       params.put("password",password1);
//                params.put("mac",maclis);

                       return params;
                   }
               };

               int MY_SOCKET_TIMEOUT_MS=100000;

               postRequest.setRetryPolicy(new DefaultRetryPolicy(
                       MY_SOCKET_TIMEOUT_MS,
                       DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                       DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
               requestQueue.add(postRequest);

           }
       });
    }
}