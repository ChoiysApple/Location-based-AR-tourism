package com.marchengraffiti.nearism.nearism.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseRead {
    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    public void ReadCourseList(final MyCallback myCallback) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("course_list");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot deeperSnapshot : snapshot.getChildren()) {
                        String key = snapshot.getKey();
                        CourseData get = deeperSnapshot.getValue(CourseData.class);
                        String[] info = {get.subnum, get.detailimg, get.subname, get.overview, get.subid};
                        String result = info[0] + "#" + info[1] + "#" + info[2] + "#" + info[3] + "#" + key + "#" + info[4];
                        arrayData.add(result);
                        arrayIndex.add(key);
                        Log.d("CourseRead", "RESULT : " + result);
                        Log.d("CourseRead", "KEY : " + key);
                        myCallback.onCallback(result);
                    }
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
