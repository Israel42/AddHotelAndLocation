package com.example.addhotelandlocation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BookingActivity extends AppCompatActivity {
    ImageView imageView;
    TextView hotelname,ratting1,ratting2,review,price;
    Button selectroom;
    FirebaseDatabase database;
    DatabaseReference reference;
    String hoteldetailname,pricelow,pricehigh;
    public static final String TAG="Values";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
       imageView=findViewById(R.id.hotelimage2);
       hotelname=findViewById(R.id.hotelnamedetail);
       ratting1=findViewById(R.id.rating1);
       ratting2=findViewById(R.id.ratting2);
       review=findViewById(R.id.reviews);
       price=findViewById(R.id.pricepernight);
       selectroom=findViewById(R.id.selectroom);
       database=FirebaseDatabase.getInstance();
       hoteldetailname=getIntent().getStringExtra("File");
       reference=database.getReference().child("Hotels").child("HotelDetail");
       selectroom.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getApplicationContext(),SelectRoom.class);
               intent.putExtra("hotelnameselected",hoteldetailname);
               startActivity(intent);

           }
       });
       reference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (final DataSnapshot dataSnapshot:snapshot.getChildren()){
                     String key=dataSnapshot.getKey();
                     Log.d(TAG, "Key Value "+key);
                     Query query=reference.orderByChild("hotelname").equalTo(hoteldetailname);
                  query.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                              String key2=dataSnapshot1.getKey();
                              DatabaseReference refer=reference.child(key2);
                              DatabaseReference singleroom=refer.child("RoomTpe").child("SingleRoom");
                              DatabaseReference suitroom=refer.child("RoomTpe").child("SuitRoom");
                               singleroom.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    RoomDetail roomDetail=snapshot.getValue(RoomDetail.class);
                                      pricelow=String.valueOf(roomDetail.getRoomprice());
                                       price.setText(String.format("%sETB-%sETB", pricelow, pricehigh));

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });
                               suitroom.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       RoomDetail roomDetail=snapshot.getValue(RoomDetail.class);
                                       pricehigh=String.valueOf(roomDetail.getRoomprice());
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });

                           refer.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   Log.d(TAG, snapshot.getValue().toString());
                                   HotelDetail detail=snapshot.getValue(HotelDetail.class);
                                   hotelname.setText(detail.getHotelname());
                                   Picasso.get().load(detail.getImageurl()).fit().into(imageView);
                                   ratting1.setText(String.valueOf(detail.getRating()));
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });
                              Log.d(TAG, "KeyOf "+dataSnapshot1.getKey());
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
