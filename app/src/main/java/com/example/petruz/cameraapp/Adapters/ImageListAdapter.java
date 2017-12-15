package com.example.petruz.cameraapp.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petruz.cameraapp.Fragments.StartupFragment;
import com.example.petruz.cameraapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Petruz on 11/12/17.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>
{
    private static final String LOGTAG = "IMAGE_LIST_ADAPTER";

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.image_cell, parent, false);

        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position)
    {
        holder.setup(position);
    }

    @Override
    public int getItemCount()
    {
        return StartupFragment.TEXTS_LENGTH;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv;
        private ImageView IVImage;

        public ImageViewHolder(View itemView)
        {
            super(itemView);

            this.tv = itemView.findViewById(R.id.TVTest);
            this.IVImage = itemView.findViewById(R.id.IVImage);
        }

        public void setup(int pos)
        {
            String currentText = "";
            File image = StartupFragment.IMAGE_FILE.listFiles()[pos];
            Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(image));

            File textFile = StartupFragment.TEXT_FILE.listFiles()[pos];
            StringBuilder text = new StringBuilder();

            try
            {
                String line;
                BufferedReader br = new BufferedReader(new FileReader(textFile));

                while ((line = br.readLine()) != null)
                {
                    text.append(line);
                    text.append('\n');
                    currentText += line;
                }

                tv.setText(currentText);
                br.close();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


            IVImage.setImageBitmap(myBitmap);

        }
    }
}
