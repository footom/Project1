package com.example.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.entity.UrlEncodedFormEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.protocol.HTTP;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class showhistory extends AppCompatActivity {

    NavigationView navigationView;
    ImageView image;
    DrawerLayout drawerLayout;
    TextView time, time1, text, text1, text2, text3, text4, text5, text6, t;
    ImageView button, button1;
    Button btn;
    DatePickerDialog.OnDateSetListener datepick, datepick1;
    Calendar calendar = Calendar.getInstance();
    TableLayout tableLayout;
    TableRow tableRow;
    private String URL = "http:// your ip /test_android/showDatehistory.php";
    String format, date, date1;
    String box = "";
    int l;
    ArrayList<String> arr, arr1, arr2, arr3, arr4, arr5, arr6;
    String r, r1, r2, r3, r4, r5, r6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showhistory);
        drawerLayout = findViewById(R.id.drawerlayout);
        image = findViewById(R.id.image);
        navigationView = findViewById(R.id.navigation);
        tableLayout = findViewById(R.id.tablelayout1);
        time = findViewById(R.id.text);
        time1 = findViewById(R.id.edit);
        //t = findViewById(R.id.t);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        btn = findViewById(R.id.btn);
        navigationView.setItemIconTintList(null);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.home) {
                            Intent intent = new Intent(showhistory.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        } else if (id == R.id.showbtn) {
                            Intent intent = new Intent(showhistory.this, data.class);
                            startActivity(intent);
                            finish();
                            return true;
                        } else if (id == R.id.history) {
                            Intent intent = new Intent(showhistory.this, history.class);
                            startActivity(intent);
                            finish();
                            return true;

                        } else if (id == R.id.showhistory) {
                            Intent intent = new Intent(showhistory.this, showhistory.class);
                            startActivity(intent);
                            finish();
                            return true;
                        } else if(id==R.id.gasStandard){
                            Intent intent = new Intent(showhistory.this, gasStandard.class);
                            startActivity(intent);
                            finish();
                            return true;
                        } else if (id == R.id.signout) {
                            Intent intent = new Intent(showhistory.this, login.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        datepick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                format = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.TAIWAN);
                time.setText(sdf.format(calendar.getTime()));
            }
        };

        datepick1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                format = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.TAIWAN);
                time1.setText(sdf.format(calendar.getTime()));
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(showhistory.this, datepick,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                tableLayout.removeView(tableRow);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(showhistory.this, datepick1,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                tableLayout.removeView(tableRow);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = time.getText().toString().trim();
                date1 = time1.getText().toString().trim();
                tableLayout.removeView(tableRow);
                //sendData();
                Thread thread = new Thread(mutiThread);
                thread.start();
            }
        });
    }

    private Runnable mutiThread = new Runnable() {
        @Override
        public void run() {
            try {
                arr = new ArrayList<String>();
                arr1 = new ArrayList<String>();
                arr2 = new ArrayList<String>();
                arr3 = new ArrayList<String>();
                arr4 = new ArrayList<String>();
                arr6 = new ArrayList<String>();
                URL url = new URL(URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.connect();
                sendData();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    JSONArray dataJson = new JSONArray(box.toString());
                    l = dataJson.length();
                    for (int i = 0; i < l; i++) {
                        JSONObject json = dataJson.getJSONObject(i);
                        String updatetime = json.getString("updatetime");
                        String temperature = json.getString("temperature");
                        String humidity = json.getString("humidity");
                        String lpg = json.getString("lpg");
                        String co = json.getString("co");
                        String fire = json.getString("fire");
                        arr.add(String.valueOf(updatetime)+"\n");
                        arr1.add(String.valueOf(temperature)+"\n");
                        arr2.add(String.valueOf(humidity)+"\n");
                        arr3.add(String.valueOf(lpg)+"\n");
                        arr4.add(String.valueOf(co)+"\n");
                        arr6.add(String.valueOf(fire)+"\n");
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    show();
                }
            });
        }
    };

    //傳資料至PHP
    private void sendData() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    box = "";
                    //Toast.makeText(showhistory.this, date, Toast.LENGTH_SHORT).show();
                    box += response+"\n";
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(showhistory.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("updatetime", date);
                    data.put("updatetime1", date1);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

    }

    private void show(){

        r = arr.toString().replace("[", "").replace(",", "").replace("]", "");
        r1 = arr1.toString().replace("[", "").replace(",", "").replace("]", "");
        r2 = arr2.toString().replace("[", "").replace(",", "").replace("]", "");
        r3 = arr3.toString().replace("[", "").replace(",", "").replace("]", "");
        r4 = arr4.toString().replace("[", "").replace(",", "").replace("]", "");
        r6 = arr6.toString().replace("[", "").replace(",", "").replace("]", "");

        text = new TextView(showhistory.this);
        text1 = new TextView(showhistory.this);
        text2 = new TextView(showhistory.this);
        text3 = new TextView(showhistory.this);
        text4 = new TextView(showhistory.this);
        text6 = new TextView(showhistory.this);

        tableRow = new TableRow(showhistory.this);

        text.setText(r);
        text.setGravity(Gravity.CENTER);
        text.setTextSize(18);

        text1.setText(r1);
        text1.setGravity(Gravity.CENTER);
        text1.setTextSize(18);

        text2.setText(r2);
        text2.setGravity(Gravity.CENTER);
        text2.setTextSize(18);

        text3.setText(r3);
        text3.setGravity(Gravity.CENTER);
        text3.setTextSize(18);

        text4.setText(r4);
        text4.setGravity(Gravity.CENTER);
        text4.setTextSize(18);

        text6.setText(r6);
        text6.setGravity(Gravity.CENTER);
        text6.setTextSize(18);

        tableRow.addView(text);
        tableRow.addView(text1);
        tableRow.addView(text2);
        tableRow.addView(text3);
        tableRow.addView(text4);
        tableRow.addView(text6);

        tableLayout.addView(tableRow);
        tableLayout.invalidate();
        tableLayout.refreshDrawableState();
    }
}