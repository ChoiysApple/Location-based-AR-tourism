package com.marchengraffiti.nearism.nearism.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseRead {
    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    public void ReadDB(final MyCallback myCallback) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("place_list");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    String[] info = {get.mapy, get.mapx, get.placeTitle};
                    String result = info[0] + "," + info[1] + "," + info[2];
                    arrayData.add(result);
                    arrayIndex.add(key);
                    //Log.d("FirebaseRead", result);
                    //Log.d("FirebaseRead", key);
                    myCallback.onCallback(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }*/
}
