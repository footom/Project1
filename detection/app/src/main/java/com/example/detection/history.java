package com.example.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toolbar;

import com.github.mikephil.charting.data.Entry;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class history extends AppCompatActivity {

    NavigationView navigationView;
    ImageView image;
    DrawerLayout drawerLayout;
    TextView text, text1, text2, text3, text4;
    TableLayout tableLayout;
    SwipeRefreshLayout swipe;
    TableRow tableRow;
    ArrayList<String> arr, arr1, arr2, arr3, arr4;
    //TextView t;
    int l;
    String r, r1, r2, r3, r4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        drawerLayout = findViewById(R.id.drawerlayout);
        image = findViewById(R.id.image);
        navigationView = findViewById(R.id.navigation);
        swipe = findViewById(R.id.swipe);
        //t = findViewById(R.id.t);
        /*time = findViewById(R.id.time);
        LPG = findViewById(R.id.lpg);
        CO = findViewById(R.id.co);
        SMOKE = findViewById(R.id.smoke);
        FIRE = findViewById(R.id.fire);*/
        tableLayout = findViewById(R.id.tablelayout);
        navigationView.setItemIconTintList(null);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if(id==R.id.home){
                            Intent intent = new Intent(history.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.showbtn){
                            Intent intent = new Intent(history.this, data.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.history){
                            Intent intent = new Intent(history.this, history.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.showhistory) {
                            Intent intent = new Intent(history.this, showhistory.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.gasStandard){
                            Intent intent = new Intent(history.this, gasStandard.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.signout){
                            Intent intent = new Intent(history.this, login.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
        Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                Thread thread = new Thread(mutiThread);
                thread.start();
            }
        }, 0, 1000);

        //Thread thread = new Thread(mutiThread);
        //thread.start();


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                tableLayout.removeView(tableRow);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //finish();
                        //Intent intent = new Intent(history.this, history.class);
                        //startActivity(intent);
                        show();
                        swipe.setRefreshing(false);
                    }
                }, 1000);
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
                //arr4 = new ArrayList<String>();
                URL url = new URL("http:// your ip/test_android/history.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    String box = "";
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        box += line + "\n";
                    }
                    //bufferedReader.close();
                    inputStream.close();
                    JSONArray dataJson = new JSONArray(box.toString());
                    l = dataJson.length();
                    for (int i = l-1; i >= 0; i--) {
                        JSONObject json = dataJson.getJSONObject(i);
                        String updatetime = json.getString("updatetime");
                        String lpg = json.getString("lpg");
                        String co = json.getString("co");
                        String fire = json.getString("fire");
                        arr.add(String.valueOf(updatetime)+"\n");
                        arr1.add(String.valueOf(lpg)+"\n");
                        arr2.add(String.valueOf(co)+"\n");
                        arr3.add(String.valueOf(fire)+"\n");
                    }
                    //show();

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void show(){
        r = arr.toString().replace("[", "").replace(",", "").replace("]", "");
        r1 = arr1.toString().replace("[", "").replace(",", "").replace("]", "");
        r2 = arr2.toString().replace("[", "").replace(",", "").replace("]", "");
        r3 = arr3.toString().replace("[", "").replace(",", "").replace("]", "");
        //r4 = arr4.toString().replace("[", "").replace(",", "").replace("]", "");

        text = new TextView(history.this);
        text1 = new TextView(history.this);
        text2 = new TextView(history.this);
        text3 = new TextView(history.this);
        text4 = new TextView(history.this);

        tableRow = new TableRow(history.this);
        text.setText(r);
        text.setGravity(Gravity.CENTER);
        text.setTextSize(16);
        text1.setText(r1);
        text1.setGravity(Gravity.CENTER);
        text1.setTextSize(16);
        text2.setText(r2);
        text2.setGravity(Gravity.CENTER);
        text2.setTextSize(16);
        text3.setText(r3);
        text3.setGravity(Gravity.CENTER);
        text3.setTextSize(16);
        /*text4.setText(r4);
        text4.setGravity(Gravity.CENTER);
        text4.setTextSize(16);*/
        tableRow.addView(text);
        tableRow.addView(text1);
        tableRow.addView(text2);
        tableRow.addView(text3);
        //tableRow.addView(text4);
        tableLayout.addView(tableRow);
        tableLayout.invalidate();
        tableLayout.refreshDrawableState();
    }
}