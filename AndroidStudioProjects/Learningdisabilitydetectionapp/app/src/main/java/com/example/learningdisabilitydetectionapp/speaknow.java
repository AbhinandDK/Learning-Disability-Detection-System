package com.example.learningdisabilitydetectionapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class speaknow extends AppCompatActivity implements View.OnClickListener {
    ImageView im;
    ImageButton imbtn;
    Button b1;
    String stat="";
    private final int REQ_CODE = 100;
    String svalue="";
   TextView textView, tv_score, tv_att;
   String[] path, pers_name;
   int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaknow);
       textView =(TextView)findViewById(R.id.textView);
       tv_att =(TextView)findViewById(R.id.textView1);
       tv_score =(TextView)findViewById(R.id.textView2);
        im = (ImageView)findViewById(R.id.imageView2);
        imbtn = (ImageButton)findViewById(R.id.imageButton2);
        b1 =(Button)findViewById(R.id.quitbtn);

        SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1.setOnClickListener(this);
        imbtn.setOnClickListener(this);

        pos=Integer.valueOf(sh.getString("speech_cnt", ""));
        tv_score.setText("Correct:"+sh.getString("correct", ""));
        tv_att.setText("Attended:"+sh.getString("attended", ""));
        load_pic();

    }

    public void load_pic(){
        final SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String hu = sh.getString("url", "");
        final String url = hu + "/and_view_objects";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js= jsonObj.getJSONArray("data");
                                path=new String[js.length()];
                                pers_name=new String[js.length()];

                                int ln=js.length();
                                if (pos<ln-1){
                                    b1.setText("Next");
                                    b1.setVisibility(View.INVISIBLE);
                                }
                                else if(pos==ln-1){
                                    b1.setText("Finish");
                                    b1.setVisibility(View.INVISIBLE);
                                }
                                else {

                                    SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    String ip = sh.getString("url", "");
                                    final String url = ip + "/and_insert_speech_marks";
                                    RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
                                    StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String s) {

                                            try {
                                                JSONObject json = new JSONObject(s);
                                                String res = json.getString("status");

                                                if (res.equals("ok")) {
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

                                for(int i=0;i<js.length();i++)
                                {
                                    JSONObject u=js.getJSONObject(i);
                                    path[i]=u.getString("image");
                                    pers_name[i]=u.getString("name");
                                }

                                String pic_url=hu + path[pos];
                                Picasso.with(getApplicationContext()).load(pic_url).into(im);

                                SharedPreferences.Editor ed=sh.edit();
                                ed.putString("ans", pers_name[pos]);
                                ed.commit();

                            }
                            else {
                                Toast.makeText(getApplicationContext(), "No objects", Toast.LENGTH_LONG).show();
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

                params.put("lid", sh.getString("lid", ""));

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
    public void onClick(View v) {
        if(v==imbtn){
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
            try {
                startActivityForResult(intent, REQ_CODE);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        "Sorry your device not supported",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if(v==b1){
            String val=b1.getText().toString();
            if(val.equalsIgnoreCase("Next")){
                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor ed=sh.edit();
                int ps=Integer.valueOf(sh.getString("speech_cnt", ""))+1;
                ed.putString("speech_cnt", String.valueOf(ps));
                ed.commit();
                Intent i = new Intent(getApplicationContext(),speaknow.class);
                startActivity(i);
            }
            if(val.equalsIgnoreCase("Finish")){

                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String ip = sh.getString("url", "");
                final String url = ip + "/and_insert_speech_marks";
                RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {


//                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                        try {
                            JSONObject json = new JSONObject(s);
                            String res = json.getString("status");

                            if (res.equals("ok")) {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if ((resultCode == RESULT_OK) && (data!=null))
                {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    svalue=result.get(0);

                    final SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    final String hu = sh.getString("url", "");
                    final String url = hu + "/and_check_ans";

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObj = new JSONObject(response);
                                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                            SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor ed=sh.edit();
                                            int ps=Integer.valueOf(sh.getString("speech_cnt", ""))+1;
                                            ed.putString("speech_cnt", String.valueOf(ps));
                                            int ps1=Integer.valueOf(sh.getString("correct", ""))+1;
                                            ed.putString("correct", String.valueOf(ps1));
                                            int ps2=Integer.valueOf(sh.getString("attended", ""))+1;
                                            ed.putString("attended", String.valueOf(ps2));
                                            ed.commit();
                                            Intent i = new Intent(getApplicationContext(),speaknow.class);
                                            startActivity(i);
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Incorrect Answer", Toast.LENGTH_LONG).show();
                                            SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor ed=sh.edit();
                                            int ps=Integer.valueOf(sh.getString("speech_cnt", ""))+1;
                                            ed.putString("speech_cnt", String.valueOf(ps));
                                            int ps2=Integer.valueOf(sh.getString("attended", ""))+1;
                                            ed.putString("attended", String.valueOf(ps2));
                                            ed.commit();
                                            Intent i = new Intent(getApplicationContext(),speaknow.class);
                                            startActivity(i);
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

                            params.put("lid", sh.getString("lid", ""));
                            params.put("pos", pos+"");
                            params.put("ans", svalue);

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
                break;
            }
        }
    }
}
