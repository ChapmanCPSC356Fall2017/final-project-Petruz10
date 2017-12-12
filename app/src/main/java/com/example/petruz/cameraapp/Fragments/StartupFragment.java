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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Petruz on 27/11/17.
 */

public class StartupFragment extends Fragment
{
    private static String LOGTAG = "STARTUP_CAMERA";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;

 //   private TextureView textureView;
    /*
     * TODO:
     * skapa en recycler view där bilderna visas i detta fragment
     * on activity result, öppna en ny activity där man kan lägga till text osv osv
     */


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        for (File f : storageDir.listFiles())
        {
            Log.i(LOGTAG, f.getName());
        }
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
        takePicture();
    }

    protected void takePicture()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null)
        {
            // Create the File where the photo should go
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(LOGTAG, "there was an error" + ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                Uri photoUri = FileProvider.getUriForFile(this.getContext(), "com.example.petruz.cameraapp.fileprovider", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",         /* suffix */
                storageDir             /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
