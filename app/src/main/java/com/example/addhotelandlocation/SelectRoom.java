package com.example.addhotelandlocation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SelectRoom extends AppCompatActivity {
    List<RoomDetail>roomDetails=new ArrayList<>();
    RecyclerView recyclerView;
    SelectedroomAdapter selectedroomAdapter;
    String hotelname;
    FirebaseDatabase database;
    DatabaseReference Reference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectroom);
        hotelname=getIntent().getStringExtra("hotelnameselected");
        database=FirebaseDatabase.getInstance();
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        selectedroomAdapter=new SelectedroomAdapter(roomDetails,getApplicationContext(),hotelname);
        Reference=database.getReference().child("Hotels").child("HotelDetail");
        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Query query=Reference.orderByChild("hotelname").equalTo(hotelname);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                String key=dataSnapshot1.getKey();
                                DatabaseReference reference1=Reference.child(key).child("RoomTpe");
                                reference1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        roomDetails.clear();
                                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                                            RoomDetail roomDetail=snapshot1.getValue(RoomDetail.class);
                                            roomDetails.add(roomDetail);
                                        }
                                        recyclerView.setAdapter(selectedroomAdapter);
                                        selectedroomAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
