package com.example.petruz.cameraapp.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.petruz.cameraapp.Activities.CameraActivity;
import com.example.petruz.cameraapp.MainActivity;
import com.example.petruz.cameraapp.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Petruz on 27/11/17.
 */

public class StartupFragment extends Fragment
{
    private static String LOGTAG = "STARTUP_CAMERA";

 //   private TextureView textureView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_startup, container, false);

        return v;
    }

    public void showCamera()
    {
        Intent myIntent = new Intent(this.getActivity(), CameraActivity.class);
        this.getActivity().startActivity(myIntent);

    }

}
