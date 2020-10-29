package com.example.addhotelandlocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {
    TextView username;
    ImageView hotel,location,resturant;
    TabLayout tabLayout;
    Animation slide;
    private static final int PERMISSION_CODE=99;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       slide= AnimationUtils.loadAnimation(getContext(),R.anim.slide_in);
       username=view.findViewById(R.id.username);
       hotel=view.findViewById(R.id.imagehotels);
       location=view.findViewById(R.id.location);
       resturant=view.findViewById(R.id.imagereastourant);
       tabLayout=view.findViewById(R.id.tablayout);
       username.setAnimation(slide);
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            CheckPermission();
        }
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),MainActivity.class));
            }
        });

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String User=auth.getCurrentUser().getUid();
        firebaseFirestore.collection("USERS").document(User).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               DocumentSnapshot documentSnapshot=task.getResult();
               if (documentSnapshot.exists()){
                   username.setText(String.format("Hi %s", documentSnapshot.get("FirstName")));
               }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "You Haven't Registered Yet", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(),RegistrationPage.class));
            }
        });

    }
    public boolean CheckPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==PERMISSION_CODE&&grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults.length>1){
            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }


}
