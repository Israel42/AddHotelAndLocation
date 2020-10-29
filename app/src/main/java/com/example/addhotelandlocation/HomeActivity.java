package com.example.addhotelandlocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomeActivity extends AppCompatActivity {
    MeowBottomNavigation navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navigation=findViewById(R.id.nav_view);
        navigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_baseline_bookmark_24));
        navigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_baseline_hotel_24));
        navigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_home_24));
        navigation.add(new MeowBottomNavigation.Model(4,R.drawable.ic_baseline_account_circle_24));
        navigation.show(3,true);
        navigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment=null;
                switch (model.getId()){
                    case 1:
                        fragment=new BookingFragemnt();
                        break;
                    case 2:
                        fragment=new HotelFragment();
                        break;
                    case 3:
                        fragment=new HomeFragment();
                        break;
                    case 4:
                        fragment=new ProfileFragemnt();
                        break;
                    default:
                }
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                assert fragment != null;
                fragmentTransaction.replace(R.id.frameLayout,fragment);
                fragmentTransaction.commit();
                return null;
            }
        });
    }
}