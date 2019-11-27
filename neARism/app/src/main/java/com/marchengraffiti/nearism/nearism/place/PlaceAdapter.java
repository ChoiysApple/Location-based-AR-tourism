package com.marchengraffiti.nearism.nearism.place;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.marchengraffiti.nearism.nearism.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlaceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<PlaceItem> data;
    private int layout;

    public static String imgURL;
    ImageView gallaryImg;
    Bitmap bitmap;

    public PlaceAdapter(Context context, int layout, ArrayList<PlaceItem> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getGallaryImg(); //?
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        final PlaceItem placeItem = data.get(position);
        gallaryImg = convertView.findViewById(R.id.gallaryImg);
        imgURL = placeItem.getGallaryImg();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imgURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException ex) {

                }
            }
        };

        mThread.start();

        try {
            mThread.join();
            gallaryImg.setImageBitmap(bitmap);
        } catch (InterruptedException e) {

        }


        return convertView;
    }
}
