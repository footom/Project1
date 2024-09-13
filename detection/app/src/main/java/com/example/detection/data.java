package com.example.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointD;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class data extends AppCompatActivity {
    public static final String tag ="TAG";
    LineChart linechart;
    int l;
    TextView t;
    LineDataSet line1, line2, line3;
    List<Entry> arrayList;
    List<Entry> arrayList1;
    List<Entry> arrayList2;
    List<String> arrayList3;
    private IAxisValueFormatter xAxisFormatter;

    NavigationView navigationView;
    ImageView image;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        linechart = findViewById(R.id.linechart);
        drawerLayout = findViewById(R.id.drawerlayout);
        image = findViewById(R.id.image);
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
                            Intent intent = new Intent(data.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.showbtn){
                            Intent intent = new Intent(data.this, data.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.history){
                            Intent intent = new Intent(data.this, history.class);
                            startActivity(intent);
                            finish();
                            return true;

                        }else if(id==R.id.showhistory) {
                            Intent intent = new Intent(data.this, showhistory.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.gasStandard){
                            Intent intent = new Intent(data.this, gasStandard.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.signout){
                            Intent intent = new Intent(data.this, login.class);
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
    }

    private Runnable mutiThread = new Runnable() {
        @Override
        public void run() {
            try {
                arrayList = new ArrayList<Entry>();
                arrayList1 = new ArrayList<Entry>();
                arrayList2 = new ArrayList<Entry>();
                arrayList3 = new ArrayList<String>();
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
                        box+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    JSONArray dataJson = new JSONArray(box.toString());
                    l = dataJson.length();
                    for(int i=0; i<dataJson.length(); i++) {
                        JSONObject json = dataJson.getJSONObject(i);
                        String lpg = json.getString("lpg");
                        String co = json.getString("co");
                        String smoke = json.getString("smoke");
                        String updatetime = json.getString("updatetime");
                        arrayList.add(new Entry(i, (float) Double.parseDouble(lpg)));
                        arrayList1.add(new Entry(i, (float) Double.parseDouble(co)));
                        //arrayList2.add(new Entry(i, (float) Double.parseDouble(smoke)));
                        arrayList3.add(String.valueOf(updatetime));
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                public void run() {
                    //t.setText(arrayList.toString());
                    showlinechart();
                }
            });
        }
    };

    private void showlinechart(){
        line1 = new LineDataSet(arrayList, "lpg");
        line2 = new LineDataSet(arrayList1, "co");
        //line3 = new LineDataSet(arrayList2, "smoke");

        //X軸預設
        XAxis xAxis = linechart.getXAxis();
        xAxis.setDrawAxisLine(true); //是否繪製軸線
        xAxis.setDrawGridLines(true); //設置x軸上每個點對應的線
        xAxis.setDrawLabels(true); //繪製標籤
        xAxis.setTextSize(10f);
        xAxis.setAxisMinimum(0f);
        xAxis.enableAxisLineDashedLine(10f, 10f, 0f); //設置數線顯示為虛線
        xAxisFormatter = new IndexAxisValueFormatter(arrayList3);
        xAxis.setValueFormatter((ValueFormatter) xAxisFormatter);
        xAxis.setLabelCount(5, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //Y軸預設
        YAxis rightAxis = linechart.getAxisRight();
        rightAxis.setEnabled(false); //關閉右邊Y軸刻度
        YAxis axisLeft = linechart.getAxisLeft();
        axisLeft.setTextSize(12f);
        axisLeft.setDrawTopYLabelEntry(false);
        axisLeft.setAxisMinimum(0f);
        axisLeft.setLabelCount(8, false);
        axisLeft.setGridLineWidth(1f);
        axisLeft.setAxisLineWidth(1f);
        axisLeft.setGranularity(0.1f);
        axisLeft.setDrawZeroLine(false);


        LimitLine l1 = new LimitLine(200f, "警示線");
        l1.setLineColor(Color.RED);
        l1.setLineWidth(4f);
        l1.setTextColor(Color.RED);
        l1.setTextSize(12f);
        axisLeft.addLimitLine(l1);

        line1.setValueTextSize(12f); //文字大小
        //line1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        line1.setCircleColor(Color.parseColor("#DC143C")); //圓點顏色
        line1.setDrawValues(false);
        line1.setLineWidth(3f);
        line1.setCircleRadius(4);
        line1.setHighlightLineWidth(2);
        line1.setColor(Color.parseColor("#DC143C")); //線條顏色
        line1.setCircleSize(4f);

        line2.setValueTextSize(12f); //文字大小
        //line2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        line2.setCircleColor(Color.parseColor("#00FFFF")); //圓點顏色
        line2.setDrawValues(false);
        line2.setLineWidth(3f);
        line2.setCircleRadius(4);
        line2.setHighlightLineWidth(2);
        line2.setColor(Color.parseColor("#00FFFF")); //線條顏色
        line2.setCircleSize(4f);

        /*line3.setValueTextSize(12f); //文字大小
        //line3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        line3.setCircleColor(Color.parseColor("#8FBC8F")); //圓點顏色
        line3.setDrawValues(false);
        line3.setLineWidth(3f);
        line3.setCircleRadius(4);
        line3.setHighlightLineWidth(2);
        line3.setColor(Color.parseColor("#8FBC8F")); //線條顏色
        line3.setCircleSize(4f);*/

        Legend legend = linechart.getLegend();
        legend.setXEntrySpace(20f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(20f);

        Description description = new Description();
        description.setText("單位:ppm");
        description.setTextSize(14);
        description.setEnabled(true);
        description.setPosition(180, 35);

        //預設沒資料
        linechart.setNoDataText("沒有數據");
        linechart.setNoDataTextColor(Color.BLACK);

        linechart.setDrawGridBackground(true);
        linechart.setDrawBorders(true); //圖表邊框
        linechart.setTouchEnabled(true); //是否可觸控
        linechart.setHighlightPerDragEnabled(true);
        linechart.getViewPortHandler().setMinMaxScaleX(5.0f, 1.5f);
        //linechart.getViewPortHandler().setMinMaxScaleY(5.0f, 5.0f);
        linechart.setScaleXEnabled(true);
        linechart.setScaleYEnabled(true);

        MakerView makerView = new MakerView(this, xAxisFormatter);
        makerView.setChartView(linechart);
        linechart.setMarker(makerView);

        linechart.setData(new LineData(line1, line2));
        linechart.setDescription(description);
        linechart.notifyDataSetChanged(); //刷新數據
        linechart.invalidate(); //重繪

    }
}