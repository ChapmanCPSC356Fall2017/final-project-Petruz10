package com.example.petruz.cameraapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.petruz.cameraapp.Fragments.StartupFragment;
import com.example.petruz.cameraapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.LongAccumulator;

public class AddTextActivity extends AppCompatActivity
{
    private static final String LOGTAG = "ADD_TEXT_ACTIVTY";

    private ImageView IV;
    private EditText ETaddText;
    private String imageText;

    //public static File TEXT_FILE;
    //public static int TEXTS_LENGTH;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);

        IV = findViewById(R.id.IVNewImage);
        ETaddText  = findViewById(R.id.ETAddText);

        ETaddText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                imageText = editable.toString();
            }
        });

        viewImage();
    }

    private void viewImage()
    {
        File image = StartupFragment.IMAGE_FILE.listFiles()[StartupFragment.IMAGES_LENGTH-1];
        Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(image));

        IV.setImageBitmap(myBitmap);
    }

    public void finishActivity(View view) throws IOException
    {

        Log.d(LOGTAG, imageText);

        createTextFile();
       // Uri textUri = FileProvider.getUriForFile(this.getApplicationContext(), "com.example.petruz.cameraapp.fileprovider", textFile);

       // saveTextIntent.putExtra(MediaStore.EXTRA_OUTPUT, textUri);

       // setFile();
        finish();
    }

    public void setFile()
    {
      /*  StartupFragment.TEXT_FILE = getApplicationContext().getFilesDir();
        StartupFragment.TEXTS_LENGTH = StartupFragment.TEXT_FILE.listFiles().length;*/


         /*   StringBuilder text = new StringBuilder();
            try {


                BufferedReader br = new BufferedReader(new FileReader(StartupFragment.TEXT_FILE.listFiles()[1]));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                    Log.d(LOGTAG, line);
                }
                br.close() ;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/

            //Log.d(LOGTAG, line);

    }

    private File createTextFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEXT_" + timeStamp;

        File storageDir = getApplicationContext().getFilesDir();

        File file = new File(getApplicationContext().getFilesDir(), imageFileName+".txt");
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(imageFileName+".txt", Context.MODE_PRIVATE);
            outputStream.write(imageText.getBytes());
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.d(LOGTAG, storageDir.toString());

        return file;
    }
}
