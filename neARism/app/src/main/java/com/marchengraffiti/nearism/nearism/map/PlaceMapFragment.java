package com.marchengraffiti.nearism.nearism.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.marchengraffiti.nearism.nearism.R;
import com.skt.Tmap.TMapView;

public class PlaceMapFragment extends Fragment {
    TMapView tMapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tmap_fragment, container, false);

        tMapView = (TMapView) view.findViewById(R.id.tmap);
        tMapView.setSKTMapApiKey("l7xxef96fe182e8243f489da89904f951211");

        return view;
    }
}
