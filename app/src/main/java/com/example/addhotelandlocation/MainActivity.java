package com.example.addhotelandlocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;


import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {
    GoogleMap map;
    LatLng mOrigin;
    LazyLoader lazyLoader;
    LinearLayout linearLayout;
    Location location;
    LocationManager manager;
    Button find;
    RippleBackground rippleBackground;
    GeoLocation userLocation;
    Marker m;
    Animation fabshow,fabhide;
    MarkerOptions markerOptions=new MarkerOptions();
    List<HotelDetail> hotelDetails1=new ArrayList<>();
    HotelDetail hotelDetail;
  FloatingActionButton floatingActionButton,findhotel,findrestourant,findmylocation;
  boolean fabopen=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
         rippleBackground=findViewById(R.id.ripple);
        lazyLoader=findViewById(R.id.lazydots);
        linearLayout=findViewById(R.id.linearmap);
        floatingActionButton=findViewById(R.id.fabmenu);
        findhotel=findViewById(R.id.findhotel);
        findhotel.setOnClickListener(this);
        findrestourant=findViewById(R.id.findrestourant);
        findmylocation=findViewById(R.id.mylocation);
        fabhide= AnimationUtils.loadAnimation(this,R.anim.fab_hide);
        fabshow=AnimationUtils.loadAnimation(this,R.anim.fab_show);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (!fabopen) {
                        showFABMenu();
                    }else{
                        closeFABMenu();
                    }


            }
        });
        findmylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location!=null){
                    onLocationChanged(location);
                }
            }
        });
        lazyLoader=new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        lazyLoader.setAnimDuration(500);
        lazyLoader.setFirstDelayDuration(100);
        lazyLoader.setSecondDelayDuration(200);
        lazyLoader.setInterpolator(new LinearInterpolator());
     }


    private void closeFABMenu() {
        fabopen=false;
        findhotel.animate().translationY(0).translationX(0);
        findrestourant.animate().translationY(0).translationX(0);
    }
    private void showFABMenu() {
        fabopen=true;
        findhotel.setVisibility(View.VISIBLE);
        findhotel.animate().translationY(-200)
        .translationX(-60);
        findrestourant.setVisibility(View.VISIBLE);
        findrestourant.animate().translationY(-60)
                .translationX(-200);


    }

    @SuppressLint("WrongConstant")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        String provider = manager.getBestProvider(new Criteria(), true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (location!=null){
            onLocationChanged(location);
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,50,this);
        GetInfoWindow infoWindow=new GetInfoWindow(getApplicationContext());
        map.setInfoWindowAdapter(infoWindow);


    }

    @Override
    public void onLocationChanged(Location location) {
        mOrigin=new LatLng(location.getLatitude(),location.getLongitude());
        userLocation=new GeoLocation(location.getLatitude(),location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin,16));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {
        rippleBackground.startRippleAnimation();
        linearLayout.setVisibility(View.VISIBLE);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Hotels").child("HotelLocation");
       final GeoFire geofire=new GeoFire(reference);
       final GeoQuery geoQuery= geofire.queryAtLocation(userLocation,5);
       geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
           @Override
           public void onKeyEntered(final String key, final GeoLocation location) {
             final DatabaseReference  databaseReference=FirebaseDatabase.getInstance().getReference().child("Hotels").child("HotelDetail").child(key);
          databaseReference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  final HotelDetail detail=snapshot.getValue(HotelDetail.class);
                  LatLng latLng=new LatLng(location.latitude,location.longitude);
                  markerOptions.position(latLng);
                  Marker marker1=map.addMarker(markerOptions);
                  rippleBackground.stopRippleAnimation();
                  linearLayout.setVisibility(View.INVISIBLE);
                  marker1.setTag(detail);
                  marker1.getPosition();
                  map.setInfoWindowAdapter(new GetInfoWindow(getApplicationContext()));
                  map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                      @Override
                      public void onInfoWindowClick(Marker marker) {
                          Intent intent=new Intent(getApplicationContext(),BookingActivity.class);
                          intent.putExtra("File",marker.getTitle());
                          startActivity(intent);

                      }
                  });
                  Log.d("Value", "onDataChange: "+detail.getHotelname()+detail.getAddress()+detail.getImageurl());
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
           }

           @Override
           public void onKeyExited(String key) {

           }

           @Override
           public void onKeyMoved(String key, GeoLocation location) {

           }

           @Override
           public void onGeoQueryReady() {

           }

           @Override
           public void onGeoQueryError(DatabaseError error) {

           }
       });
    }
}