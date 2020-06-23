package com.marchengraffiti.nearism.nearism.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marchengraffiti.nearism.nearism.ArImageActivity;
import com.marchengraffiti.nearism.nearism.MainActivity;
import com.marchengraffiti.nearism.nearism.R;
import com.marchengraffiti.nearism.nearism.course.CourseMainActivity;
import com.marchengraffiti.nearism.nearism.firebase.FirebaseRead;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;
import com.marchengraffiti.nearism.nearism.parsing.FourSquare;
import com.marchengraffiti.nearism.nearism.place.placesActivity;
import com.marchengraffiti.nearism.nearism.tflite.ClassifierActivity;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    TMapView tMapView;
    String[] parsing_split;
    double lat, lng;
    String name;
    int flag = 0;

    FloatingActionButton locationFab1, locationFab2, locationFab3, fab1, fab2, fab3;

    List<MarkerItem> markerList = new ArrayList<MarkerItem>();
    List<String> list = new ArrayList<String>();
    List<Double> latlist = new ArrayList<Double>();
    List<Double> lnglist = new ArrayList<Double>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tmap_fragment, container, false);

        tMapView = (TMapView) view.findViewById(R.id.tmap);
        tMapView.setSKTMapApiKey("l7xxef96fe182e8243f489da89904f951211");
        tMapView.setCenterPoint(127.009545,37.571300, false);

        fab3 = view.findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), ArImageActivity.class);
                startActivity(i);
            }
        });

        fab2 = view.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ClassifierActivity.class);
                startActivity(intent);
            }
        });

        fab1 = view.findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), CourseMainActivity.class);
                startActivity(i);
            }
        });


        locationFab1 = view.findViewById(R.id.locationFab1); // 음식점
        locationFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                new parsing_task().execute();
            }
        });

        locationFab2 = view.findViewById(R.id.locationFab2); // 카페
        locationFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                new parsing_task().execute();
            }
        });

        locationFab3 = view.findViewById(R.id.locationFab3); // 호텔
        locationFab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 3;
                new parsing_task().execute();
            }
        });

        new task().execute();

        final AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.toolField);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, list));

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                AutoCompleteTextView autoComplete = (AutoCompleteTextView)view.findViewById(R.id.toolField);

                // 열려있는 키패드 닫기
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoComplete.getWindowToken(), 0);

                for(int i=0; i<list.size(); i++) {
                    if(String.valueOf(autoComplete.getText()).equals(list.get(i))) {
                        tMapView.setCenterPoint(lnglist.get(i), latlist.get(i), false);
                    }
                }
            }
        });

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

            MarkerItem markerItem = new MarkerItem(latitude, longitude, msg);
            markerList.add(markerItem);
            if (markerList.size() == 56) {
                for (int i = 0; i < markerList.size(); i++) {
                    list.add(markerList.get(i).getMsg());
                    latlist.add(markerList.get(i).getLat());
                    lnglist.add(markerList.get(i).getLng());
                }
            }

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

                    tMapView.setZoomLevel(18);
                    tMapView.setMapPosition(TMapView.POSITION_NAVI);
                    tMapView.setMapType(tMapView.MAPTYPE_STANDARD);
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    public class parsing_task extends AsyncTask<Void, String, Void> {
        String query;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            if (flag == 1) {
                query = "restaurant";
            }
            if (flag == 2) {
                query = "cafe";
            }
            if (flag == 3) {
                query = "inn";
            }

            FourSquare f = new FourSquare(query);
            f.fourSquareParsing(new MyCallback() {
                @Override
                public void onCallback(String value) {
                    publishProgress(value);
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            parsing_split = values[0].split("#");
            lat = Double.valueOf(parsing_split[1]);
            lng = Double.valueOf(parsing_split[2]);
            name = parsing_split[0];
            Log.d("parsing_task", lat + " " + lng + " " + name);

            LatLng position = new LatLng(lat, lng);

            if (flag == 1) {
                Bitmap pin = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bluepin);

                TMapPoint point = new TMapPoint(lat, lng);
                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                markerItem1.setIcon(pin);
                markerItem1.setTMapPoint(point);
                markerItem1.setName(name);
                //markerItem1.setEnableClustering(true);

                markerItem1.setCalloutTitle(name);
                markerItem1.setCanShowCallout(true);
                markerItem1.setAutoCalloutVisible(true);
                tMapView.addMarkerItem(name, markerItem1);
            }

            if (flag == 2) {
                Bitmap pin = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.greenpin);

                TMapPoint point = new TMapPoint(lat, lng);
                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                markerItem1.setIcon(pin);
                markerItem1.setTMapPoint(point);
                markerItem1.setName(name);
                //markerItem1.setEnableClustering(true);

                markerItem1.setCalloutTitle(name);
                markerItem1.setCanShowCallout(true);
                markerItem1.setAutoCalloutVisible(true);
                tMapView.addMarkerItem(name, markerItem1);
            }

            if (flag == 3) {
                Bitmap pin = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.purplepin);

                TMapPoint point = new TMapPoint(lat, lng);
                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                markerItem1.setIcon(pin);
                markerItem1.setTMapPoint(point);
                markerItem1.setName(name);
                //markerItem1.setEnableClustering(true);

                markerItem1.setCalloutTitle(name);
                markerItem1.setCanShowCallout(true);
                markerItem1.setAutoCalloutVisible(true);
                tMapView.addMarkerItem(name, markerItem1);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    public void autoComplete(final List<MarkerItem> markerList) {

        for(int i=0; i<markerList.size(); i++) {
            list.add(markerList.get(i).getMsg());
            latlist.add(markerList.get(i).getLat());
            lnglist.add(markerList.get(i).getLng());
        }

        //Log.d("testList", "latlist : " + String.valueOf(latlist));
        //Log.d("testList", "lnglist : " + String.valueOf(lnglist));


    }

}
