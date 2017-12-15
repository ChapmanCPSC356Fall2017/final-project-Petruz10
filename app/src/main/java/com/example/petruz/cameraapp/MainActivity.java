package com.example.petruz.cameraapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.petruz.cameraapp.Fragments.StartupFragment;

public class MainActivity extends AppCompatActivity {

    StartupFragment start;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = new StartupFragment();

        showFragment(start);
    }

    private void showFragment(Fragment frag)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_fragContainer, frag)
                .commit();
    }

    public void openCamera(View view)
    {
        start.showCamera();
    }



}
