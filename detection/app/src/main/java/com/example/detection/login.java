package com.example.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crashlytics.buildtools.ndk.internal.dwarf.DebugLineEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class login extends AppCompatActivity {

    private EditText phone;
    private String Phone;
    TextView time;
    private String URL="http:// your ip /test_android/login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Phone = "";
        phone = findViewById(R.id.phone);
        time = findViewById(R.id.time);
        Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                Thread thread = new Thread(mutiThread);
                thread.start();
            }
        }, 0, 1000);

    }

    private Runnable mutiThread = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date curTime = new Date(System.currentTimeMillis());
            runOnUiThread(new Runnable() {
                public void run(){
                    String str = formatter.format(curTime);
                    time.setText("現在時間:" + str);
                }
            });

        }
    };

    public void Login(View view) {
        Phone = phone.getText().toString().trim();
        if(!phone.equals("")) {
            StringRequest stringRequest = new  StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.equals("failure")) {
                        Toast.makeText(login.this, "無法進入", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(login.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("phone", Phone);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "欄位不可為空", Toast.LENGTH_SHORT).show();
        }
    }
}