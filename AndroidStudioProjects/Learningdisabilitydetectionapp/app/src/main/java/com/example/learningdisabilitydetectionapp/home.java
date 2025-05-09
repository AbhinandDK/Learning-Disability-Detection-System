package com.example.learningdisabilitydetectionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learningdisabilitydetectionapp.databinding.ActivityHomeBinding;

public class home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.changepass){
                    Intent i=new Intent(getApplicationContext(),changepassword.class);
                    startActivity(i);
                }
                if(id==R.id.logout){
                    Intent i=new Intent(getApplicationContext(),login.class);
                    startActivity(i);
                }
                if(id==R.id.feedback) {
                    Intent i = new Intent(getApplicationContext(),feedback.class);
                    startActivity(i);
                }
                if (id == R.id.viewobj) {
                    SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor ed=sh.edit();
                    ed.putString("speech_cnt","0");
                    ed.putString("attended","0");
                    ed.putString("correct","0");
                    ed.commit();
                    Intent ij=new Intent(getApplicationContext(), speaknow.class);
                    startActivity(ij);
                }

//                if (id == R.id.viewspell) {
//                    SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    SharedPreferences.Editor ed=sh.edit();
//                    ed.putString("speech_cnt","0");
//                    ed.putString("attended","0");
//                    ed.putString("correct","0");
//                    ed.commit();
//                    Intent ij=new Intent(getApplicationContext(), scrambled.class);
//                    startActivity(ij);
//                }

                if(id==R.id.viewtest) {
                    SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor ed=sh.edit();
                    ed.putString("speech_cnt","0");
                    ed.putString("attended","0");
                    ed.putString("correct","0");
                    ed.commit();
                    Intent i = new Intent(getApplicationContext(),mathequation.class);
                    startActivity(i);
                }
                if(id==R.id.result) {

                    Intent i = new Intent(getApplicationContext(),suggestion.class);
                    startActivity(i);
                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}