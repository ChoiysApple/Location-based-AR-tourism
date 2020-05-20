package com.marchengraffiti.nearism.nearism.map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.marchengraffiti.nearism.nearism.R;
import com.marchengraffiti.nearism.nearism.firebase.FirebaseRead;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;
import com.marchengraffiti.nearism.nearism.place.placesActivity;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

public class MapFragment extends Fragment {
    TMapView tMapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tmap_fragment, container, false);

        tMapView = (TMapView) view.findViewById(R.id.tmap);
        tMapView.setSKTMapApiKey("l7xxef96fe182e8243f489da89904f951211");

        new task().execute();

        return view;
    }

    private class task extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params){
            FirebaseRead firebaseRead = new FirebaseRead();
            firebaseRead.ReadDB(new MyCallback() {
                @Override
                public void onCallback(String value) {
                    publishProgress(value);
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            String[] mapValue = values[0].split(",");

            Double latitude = Double.valueOf(mapValue[0]);
            Double longitude = Double.valueOf(mapValue[1]);
            String msg = mapValue[2];

            Log.d("mapLog", latitude + " / " + longitude + " [" + msg + "]\n");

            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.white);
            Bitmap pin = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.redpin);

            TMapPoint point = new TMapPoint(latitude, longitude);
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            markerItem1.setIcon(pin);
            markerItem1.setTMapPoint(point);
            markerItem1.setName(msg);
            //markerItem1.setEnableClustering(true);

            markerItem1.setCalloutTitle(msg);
            markerItem1.setCanShowCallout(true);
            markerItem1.setAutoCalloutVisible(true);
            markerItem1.setCalloutRightButtonImage(bitmap);
            tMapView.addMarkerItem(msg, markerItem1);
            //tMapView.setEnableClustering(true);

            tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
                @Override
                public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                    String name = tMapMarkerItem.getName();
                    TMapPoint tp = tMapMarkerItem.getTMapPoint();
                    double lat = tp.getLatitude();
                    double lng = tp.getLongitude();

                    Intent intent = new Intent(getContext(), placesActivity.class);
                    intent.putExtra("title", name);
                    intent.putExtra("lat", lat + "");
                    intent.putExtra("lng", lng + "");
                    startActivity(intent);

                    Log.d("TmapLog", name + " / " + lat + ", " + lng);
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }


}
