package com.marchengraffiti.nearism.nearism.parsing;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FourSquare {
    GoogleMap mMap;
    Marker marker;
    BufferedReader br = null;
    URL url;
    String lat, lng, name, str, msg;
    double latitude, longitude;
    String[] mapValue;
    String query;

    public FourSquare(String query) {
        this.query = query;
    }

    public String getString() {
        return query;
    }
    //37.497601, 127.027422
    public void fourSquareParsing(final MyCallback myCallback) {
        String u = "https://api.foursquare.com/v2/venues/search?" +
                "client_id=SG3HXI2DCZ35RNXOP4SNGJQMM0W5JPTIMQM4TCC3KDHLU042&" +
                "client_secret=BVUHQ23DUVLGH4KRQ3IMMJSWDWJBTADFQOPSPZZSRE5DOEW2&" +
                "ll=37.450541, 127.129904&" +
                "query=" + query + "&v=20191128";

        new Thread() {
            public void run() {
                try {
                    url = new URL(u);
                    final HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

                    // GET 방식 request
                    urlconnection.setRequestMethod("GET");
                    br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
                    String result = "";
                    String line;
                    while ((line = br.readLine()) != null) {
                        result = result + line + "\n";
                    }

                    JSONObject jsonObject = new JSONObject(result);
                    String items = jsonObject.getString("response");
                    JSONObject jsonObject2 = new JSONObject(items);
                    JSONArray jsonArray = jsonObject2.getJSONArray("venues");

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject subJsonObject = jsonArray.getJSONObject(i);
                        name = subJsonObject.getString("name");
                        JSONObject subJsonObject2 = subJsonObject.getJSONObject("location");
                        lat = subJsonObject2.getString("lat");
                        lng = subJsonObject2.getString("lng");
                        str = name + "#" + lat + "#" + lng;
                        Log.d("parsing_task", str);
                        myCallback.onCallback(str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }
}
