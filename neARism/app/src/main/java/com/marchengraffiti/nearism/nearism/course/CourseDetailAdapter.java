package com.marchengraffiti.nearism.nearism.course;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marchengraffiti.nearism.nearism.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CourseDetailAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<CourseDetailItem> data;
    private int layout;

    ImageView detailimg;
    public static String imgURL;
    Bitmap bitmap;

    public CourseDetailAdapter(Context context, int layout, ArrayList<CourseDetailItem> data) {
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
        return data.get(position).getSubname();
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

        TextView subname = convertView.findViewById(R.id.subname);
        TextView overview = convertView.findViewById(R.id.overview);
        detailimg = convertView.findViewById(R.id.detailimg);

        final CourseDetailItem courseDetailItem = data.get(position);
        subname.setText(courseDetailItem.getSubname());
        overview.setText(courseDetailItem.getOverview());
        imgURL = courseDetailItem.getDetailimg();

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
            detailimg.setImageBitmap(bitmap);
        } catch (InterruptedException e) {

        }


        return convertView;
    }
}
