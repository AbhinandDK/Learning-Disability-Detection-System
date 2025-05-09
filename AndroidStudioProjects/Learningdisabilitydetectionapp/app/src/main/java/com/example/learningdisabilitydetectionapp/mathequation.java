package com.example.learningdisabilitydetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class mathequation extends AppCompatActivity {
    ImageView eq;
    RadioButton opA,opB,opC,opD;
    Button b7;
    String url;
    SharedPreferences sh;
    String corr="", sel_answ="";
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathequation);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ip","");
        url=sh.getString("url","")+"/load_discalculia_question";

        eq=findViewById(R.id.imageView5);
        opA=findViewById(R.id.radioButton) ;
        opB=findViewById(R.id.radioButton2);
        opC=findViewById(R.id.radioButton3);
        opD=findViewById(R.id.radioButton4);
        b7=findViewById(R.id.button7);
        pos=Integer.valueOf(sh.getString("speech_cnt", ""));

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(opA.isChecked()){
                    sel_answ=opA.getText().toString();
                }
                if(opB.isChecked()){
                    sel_answ=opB.getText().toString();
                }
                if(opC.isChecked()){
                    sel_answ=opC.getText().toString();
                }
                if(opD.isChecked()){
                    sel_answ=opD.getText().toString();
                }
                String val=b7.getText().toString();
                if(val.equalsIgnoreCase("Next")) {
                    if (corr.equalsIgnoreCase(sel_answ)) {
                        Toast.makeText(getApplicationContext(), "Correct Answer", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor ed = sh.edit();
                        int ps = Integer.valueOf(sh.getString("speech_cnt", "")) + 1;
                        ed.putString("speech_cnt", String.valueOf(ps));
                        int ps1 = Integer.valueOf(sh.getString("correct", "")) + 1;
                        ed.putString("correct", String.valueOf(ps1));
                        int ps2 = Integer.valueOf(sh.getString("attended", "")) + 1;
                        ed.putString("attended", String.valueOf(ps2));
                        ed.commit();
                        Intent i = new Intent(getApplicationContext(), mathequation.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect Answer", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor ed = sh.edit();
                        int ps = Integer.valueOf(sh.getString("speech_cnt", "")) + 1;
                        ed.putString("speech_cnt", String.valueOf(ps));
                        int ps2 = Integer.valueOf(sh.getString("attended", "")) + 1;
                        ed.putString("attended", String.valueOf(ps2));
                        ed.commit();
                        Intent i = new Intent(getApplicationContext(), mathequation.class);
                        startActivity(i);
                    }
                }
                else {
                    if (corr.equalsIgnoreCase(sel_answ)) {
                        Toast.makeText(getApplicationContext(), "Correct Answer", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor ed = sh.edit();
                        int ps = Integer.valueOf(sh.getString("speech_cnt", "")) + 1;
                        ed.putString("speech_cnt", String.valueOf(ps));
                        int ps1 = Integer.valueOf(sh.getString("correct", "")) + 1;
                        ed.putString("correct", String.valueOf(ps1));
                        int ps2 = Integer.valueOf(sh.getString("attended", "")) + 1;
                        ed.putString("attended", String.valueOf(ps2));
                        ed.commit();
                        Intent i = new Intent(getApplicationContext(), mathequation.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect Answer", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor ed = sh.edit();
                        int ps = Integer.valueOf(sh.getString("speech_cnt", "")) + 1;
                        ed.putString("speech_cnt", String.valueOf(ps));
                        int ps2 = Integer.valueOf(sh.getString("attended", "")) + 1;
                        ed.putString("attended", String.valueOf(ps2));
                        ed.commit();
                        Intent i = new Intent(getApplicationContext(), mathequation.class);
                        startActivity(i);
                    }
                    String ip = sh.getString("url", "");
                    String url2 = ip + "/submit_score";
                    RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest postrequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {

                            try {
                                JSONObject json = new JSONObject(s);
                                String res = json.getString("status");

                                if (res.equals("ok")) {
                                    Toast.makeText(getApplicationContext(), "Score submitted", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), home.class);
                                    startActivity(i);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                            Toast.makeText(getApplicationContext(), "Error........", Toast.LENGTH_SHORT).show();

                        }
                    }) {

                        @Override
                        public Map<String, String> getParams() {

                            Map<String, String> params = new HashMap<String, String>();
                            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            String cor = sh.getString("correct", "");
                            String att = sh.getString("attended", "");
                            params.put("lid", sh.getString("lid", ""));
                            params.put("correct", cor);
                            params.put("attended", att);
//                }
                            return (params);

                        }
                    };
                    requestqueue.add(postrequest);


                }



            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);


                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {



                                opA.setText(jsonObj.getString("option_A"));
                                opB.setText(jsonObj.getString("option_B"));
                                opC.setText(jsonObj.getString("option_C"));
                                opD.setText(jsonObj.getString("option_D"));

                                corr=jsonObj.getString("correct_answer");
                                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String url=sh.getString("url", "")+jsonObj.getString("equation");
                                Picasso.with(getApplicationContext()).load(url).into(eq);

                                int ln=Integer.parseInt(jsonObj.getString("ln"));
                                Toast.makeText(getApplicationContext(), pos+"", Toast.LENGTH_SHORT).show();
                                if (pos<ln-1){
                                    b7.setText("Next");
//                                    b7.setVisibility(View.INVISIBLE);
                                }
                                else if(pos==ln-1){
                                    b7.setText("Finish");
//                                    b7.setVisibility(View.INVISIBLE);
                                }
                                else {

                                    String ip = sh.getString("url", "");
                                    String url2 = ip + "/submit_score";
                                    RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
                                    StringRequest postrequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String s) {

                                            try {
                                                JSONObject json = new JSONObject(s);
                                                String res = json.getString("status");

                                                if (res.equals("ok")) {
                                                    Toast.makeText(getApplicationContext(), "Score submitted", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(getApplicationContext(), home.class);
                                                    startActivity(i);

                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {

                                            Toast.makeText(getApplicationContext(), "Error........", Toast.LENGTH_SHORT).show();

                                        }
                                    }) {

                                        @Override
                                        public Map<String, String> getParams() {

                                            Map<String, String> params = new HashMap<String, String>();
                                            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            String cor = sh.getString("correct", "");
                                            String att = sh.getString("attended", "");
                                            params.put("lid", sh.getString("lid", ""));
                                            params.put("correct", cor);
                                            params.put("attended", att);
//                }
                                            return (params);

                                        }
                                    };
                                    requestqueue.add(postrequest);


                                }




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

////                       String id=sh.getString("uid","");
//                params.put("username",username);
                params.put("qn_cnt", sh.getString("speech_cnt", ""));
////                params.put("mac",maclis);

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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),home.class);
        startActivity(i);
        super.onBackPressed();
    }
}