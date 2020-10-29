package com.example.addhotelandlocation;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kenilt.loopingviewpager.scroller.AutoScroller;
import com.kenilt.loopingviewpager.widget.LoopingViewPager;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginPage extends AppCompatActivity {
    LoopingViewPager viewPager;
    ViewpageAdapter madapter;
    LinearLayout linearLayout;
    EditText phonenumber;
    Button login,signup;
    AutoScroller autoScroller;


    CountryCodePicker countryCodePicker;
    Lifecycle lifecycle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linearLayout = findViewById(R.id.linear);
        viewPager = findViewById(R.id.viewpager);
        phonenumber = findViewById(R.id.phonenumber);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if (user!=null){
            startActivity(new Intent(LoginPage.this,HomeActivity.class));
            finish();
        }
        countryCodePicker = findViewById(R.id.countrycode);
       madapter = new ViewpageAdapter(getApplicationContext());
        viewPager.setPageTransformer(false, new FadePageTransform());
        viewPager.setAdapter(madapter);
      autoScroller=new AutoScroller(viewPager,lifecycle,5000);
      login.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              login.setVisibility(View.INVISIBLE);
              Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in);
             linearLayout.setVisibility(View.VISIBLE);
             linearLayout.setAnimation(animation);
             signup.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     String phone,ccode,phonenum;
                     phone=phonenumber.getText().toString();
                     ccode=countryCodePicker.getSelectedCountryCode();
                     phonenum=ccode+phone;
                     if (phone.isEmpty()||phone.length()<9){
                         phonenumber.setError("Please Add Phone Number");
                         phonenumber.setFocusable(true);
                         return;
                     }
                     Intent intent=new Intent(getApplicationContext(),VerifyPhone.class);
                     intent.putExtra("PHONE",phonenum);
                     startActivity(intent);
                     finish();
                 }
             });
          }
      });

    }
    private static class FadePageTransform implements ViewPager.PageTransformer{

        @Override
        public void transformPage(@NonNull View view, float position) {
            if (position <= -1.0F || position >= 1.0F) {
                view.setAlpha(0.0F);
                view.setVisibility(View.GONE);
            } else if (position == 0.0F) {
                view.setAlpha(1.0F);
                view.setVisibility(View.VISIBLE);
            } else {

                view.setAlpha(1.0F - Math.abs(position));
                view.setTranslationX(-position * (view.getWidth() / 2));
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}
