package com.example.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    public static final String ChannelID="myChannel";
    public static final String ChannelName ="eles";
    private View view;
    private Context context;
    TextView temp_hum, fire, LPG, CO, SMOKE, time, current;
    String result1, result2, result4, result5, result6;
    int result3;
    int lpg, co, smoke;

    List<String> arr1, arr2, arr3, arr4, arr5;


    NavigationView navigationView;
    ImageView image;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();//創建頻道

        temp_hum = findViewById(R.id.temp_hum);
        fire = findViewById(R.id.fire);
        LPG = findViewById(R.id.lpg);
        CO = findViewById(R.id.co);
        time = findViewById(R.id.time);
        drawerLayout = findViewById(R.id.drawerlayout);
        image = findViewById(R.id.image);
        current = findViewById(R.id.current);
        navigationView = findViewById(R.id.navigation);
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
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.showbtn){
                            Intent intent = new Intent(MainActivity.this, data.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.history){
                            Intent intent = new Intent(MainActivity.this, history.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.showhistory) {
                            Intent intent = new Intent(MainActivity.this, showhistory.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.gasStandard){
                            Intent intent = new Intent(MainActivity.this, gasStandard.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.signout){
                            Intent intent = new Intent(MainActivity.this, login.class);
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

        /*temp_hum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });*/
    }

    public void send(View view) { //送出通知
        sendNotificationMsg();
    }

    public void createNotificationChannel(){ //初始化NotificationManager
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //創建NotificationChannel(String id,CharSequence name,int importance)對象
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ChannelID,ChannelName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
    }

    private void sendNotificationMsg() {
        // 1/100濃度的爆炸下限
        if(lpg>180) {
            builder = getNotificationChannelBuilder("lpg濃度過高");
            manager.notify(1, builder.build());
            Ring(context);
        }
        // 150ppm/(10~50分鐘)
        if(co>150) {
            builder = getNotificationChannelBuilder("co濃度過高");
            manager.notify(2, builder.build());
            Ring(context);
        }
        /*if(smoke>150) {
            builder = getNotificationChannelBuilder("smoke濃度過高");
            manager.notify(3, builder.build());
        }*/
    }
    //創建通知相關設定
    public NotificationCompat.Builder getNotificationChannelBuilder(String msg) {
        PendingIntent pendingIntent=PendingIntent.getActivity(this,
                0,new Intent(this,MainActivity.class)
                ,PendingIntent.FLAG_CANCEL_CURRENT);
        return builder =new NotificationCompat.Builder(this,ChannelID)
                .setContentTitle("氣體超標")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher_foreground)//必填
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
    }

    private Runnable mutiThread = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date curTime = new Date(System.currentTimeMillis());
            runOnUiThread(new Runnable() {
                public void run(){
                    String str = formatter.format(curTime);
                    current.setText("現在時間:" + str);
                }
            });
            connect();
            runOnUiThread(new Runnable() {
                public void run() {
                    String tout = result1 + "°C /" + result2 + "%";
                    temp_hum.setText(tout);
                    if(result3==0){
                        fire.setText("關");
                    }else{
                        fire.setText("開");
                    }
                    String lout = "         " + result4 + " ppm";
                    LPG.setText(lout);
                    String cout = "         " + result5 + " ppm";
                    CO.setText(cout);
                    String updatetime = "上次更新:" + result6;
                    time.setText(updatetime);
                }
            });
        }
    };

    private void connect() {
        try {
            arr1 = new ArrayList<String>();
            arr2 = new ArrayList<String>();
            arr3 = new ArrayList<String>();
            arr4 = new ArrayList<String>();
            arr5 = new ArrayList<String>();
            URL url = new URL("http:// your ip/jsonTypeOfData.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                String box = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    box += line + "\n";
                }
                bufferedReader.close();
                inputStream.close();
                JSONArray dataJson = new JSONArray(box.toString());
                int l = dataJson.length() - 1;
                for(int i=0; i<dataJson.length(); i++) {
                    JSONObject info = dataJson.getJSONObject(i);
                    String temperature = info.getString("temperature");
                    String humidity = info.getString("humidity");
                    String time = info.getString("updatetime");
                    lpg = info.getInt("lpg");
                    co = info.getInt("co");
                    int fire = info.getInt("fire");
                    result1 = temperature.toString();
                    result2 = humidity.toString();
                    result3 = fire;
                    result4 = String.valueOf(lpg);
                    result5 = String.valueOf(co);
                    result6 = String.valueOf(time);
                    arr1.add(String.valueOf(temperature)+"\n");
                    arr2.add(String.valueOf(humidity)+"\n");
                    arr3.add(String.valueOf(lpg));
                    arr4.add(String.valueOf(co));
                    arr5.add(String.valueOf(fire));
                }

                send(view);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //警鈴
    private static void Ring(Context context){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(context, uri);
        rt.play();
    }

    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("溫/濕度");

        String r = arr1.toString().replace("[", "").replace(",", "").replace("]", "");
        String r1 = arr2.toString().replace("[", "").replace(",", "").replace("]", "");


        builder.setMessage(r);
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}