package com.example.petruz.cameraapp.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.petruz.cameraapp.Fragments.StartupFragment;
import com.example.petruz.cameraapp.R;

import java.io.File;

public class AddTextActivity extends AppCompatActivity
{
    private ImageView IV;
    private EditText ETaddText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);

        IV = findViewById(R.id.IVNewImage);
        ETaddText  = findViewById(R.id.ETAddText);

        viewImage();
    }

    private void viewImage()
    {
        File image = StartupFragment.IMAGE_FILE.listFiles()[StartupFragment.IMAGES_LENGTH-1];
        Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(image));

        IV.setImageBitmap(myBitmap);
    }

    public void finishActivity(View view)
    {
        finish();
    }
}
