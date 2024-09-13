package com.example.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class gasStandard extends AppCompatActivity {

    NavigationView navigationView;
    ImageView image;
    DrawerLayout drawerLayout;
    TextView text, text1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_standard);
        drawerLayout = findViewById(R.id.drawerlayout);
        image = findViewById(R.id.image);
        text = findViewById(R.id.text);
        text1 = findViewById(R.id.text1);
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
                            Intent intent = new Intent(gasStandard.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.showbtn){
                            Intent intent = new Intent(gasStandard.this, data.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.history){
                            Intent intent = new Intent(gasStandard.this, history.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.showhistory) {
                            Intent intent = new Intent(gasStandard.this, showhistory.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.gasStandard){
                            Intent intent = new Intent(gasStandard.this, gasStandard.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }else if(id==R.id.signout){
                            Intent intent = new Intent(gasStandard.this, login.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        text.setText("瓦斯警報器規定:濃度未達爆炸下限 1/100，不得警報以防止誤報；濃度介於爆炸下限 1/100 ~ 1/4 範圍內則須警報。丙烷警報標準:210ppm~5250ppm；丁烷:190ppm~4750ppm");
        text1.setText("一氧化碳警報器標準:CO 70ppm--在60至240分鐘之間警報；CO 150 PPM--在10至50分鐘之間警報；CO 400ppm--在4至15分鐘之間警報。");
    }
}