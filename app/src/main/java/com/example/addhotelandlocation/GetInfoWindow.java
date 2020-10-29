package com.example.addhotelandlocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GetInfoWindow implements GoogleMap.InfoWindowAdapter {
    Context context;
LayoutInflater inflater;
HotelDetail details;

    public GetInfoWindow(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View mview=inflater.inflate(R.layout.custom_view,null);
        details=(HotelDetail) marker.getTag();
        final TextView hotelname,address,Booking;
        RatingBar ratingBar;
        hotelname=mview.findViewById(R.id.hotelname);
        address=mview.findViewById(R.id.address);
        ratingBar=mview.findViewById(R.id.rating);
        Booking=mview.findViewById(R.id.bookingforgetinfo);
         hotelname.setText(details.getHotelname());
         String title=hotelname.getText().toString();
         marker.setTitle(title);
        address.setText(details.getAddress());
        ratingBar.setRating(details.getRating());
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hotelname1=hotelname.getText().toString();
                Intent intent=new Intent(mview.getContext(),BookingActivity.class);
                intent.putExtra("hotelname",hotelname1);
                mview.getContext().startActivity(intent);
            }
        });
        return mview;
    }
}
