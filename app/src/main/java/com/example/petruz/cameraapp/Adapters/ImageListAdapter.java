package com.example.petruz.cameraapp.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petruz.cameraapp.Fragments.StartupFragment;
import com.example.petruz.cameraapp.R;

import java.io.File;

/**
 * Created by Petruz on 11/12/17.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>
{

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
        return StartupFragment.IMAGES_LENGTH;
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
            File image = StartupFragment.IMAGE_FILE.listFiles()[pos];

            tv.setText("text test");
            Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(image));
            IVImage.setImageBitmap(myBitmap);
        }
    }
}
