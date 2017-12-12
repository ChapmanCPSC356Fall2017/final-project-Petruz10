package com.example.petruz.cameraapp.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petruz.cameraapp.Activities.AddTextActivity;
import com.example.petruz.cameraapp.Adapters.ImageListAdapter;
import com.example.petruz.cameraapp.MainActivity;
import com.example.petruz.cameraapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Petruz on 27/11/17.
 */

public class StartupFragment extends Fragment
{
    private static final String LOGTAG = "STARTUP_CAMERA";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;

    private ImageListAdapter adapter;

    public static int IMAGES_LENGTH;
    public static File IMAGE_FILE;

    public static int TEXTS_LENGTH;
    public static File TEXT_FILE;

    /*
     * TODO:
     * on activity result, öppna en ny activity där man kan lägga till text osv osv
     * visa den nya bilden direkt
     * koppla bild och text
     */


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        AddTextActivity a = new AddTextActivity();

        setFiles();

    }

    private void setFiles()
    {
        IMAGE_FILE = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        IMAGES_LENGTH = IMAGE_FILE.listFiles().length;

        TEXT_FILE = getContext().getFilesDir();
        TEXTS_LENGTH = StartupFragment.TEXT_FILE.listFiles().length;

       /* for (File f: IMAGE_FILE.listFiles())
        {
            f.delete();
        }*/

       /* for (File ff: TEXT_FILE.listFiles())
        {
            ff.delete();
        }*/


        Log.d(LOGTAG, "img length "+IMAGES_LENGTH);
        Log.d(LOGTAG, "text length "+TEXTS_LENGTH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_startup, container, false);

        RecyclerView imageListView = v.findViewById(R.id.rvImages);

        this.adapter = new ImageListAdapter();
        imageListView.setAdapter(adapter);

        imageListView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException ex)
            {
                Log.e(LOGTAG, "there was an error" + ex.getMessage());
            }

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

      //  mCurrentPhotoPath = image.getAbsolutePath();
        Log.d(LOGTAG, storageDir.toString());
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(LOGTAG, "onActivityResult()");
        //setFiles();

        Intent intent = new Intent(this.getActivity(), AddTextActivity.class);
        startActivity(intent);
    }
}
