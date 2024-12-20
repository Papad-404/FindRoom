package com.example.findroom;

import static com.example.findroom.R.id.navhome;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(navhome);

    }



    DashBoard dashBoard = new DashBoard();
    Home home = new Home();
    Notification notification = new Notification();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.navdashboard){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, dashBoard)
                    .commit();
            return true;

        }else if(item.getItemId() == R.id.navhome){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, home)
                    .commit();
            return true;

        }else if(item.getItemId() == R.id.navnotifications){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, notification)
                    .commit();
            return true;

        }

        return false;
    }
}