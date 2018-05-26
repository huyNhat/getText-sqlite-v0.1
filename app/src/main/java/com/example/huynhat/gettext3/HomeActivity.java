package com.example.huynhat.gettext3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;


import com.example.huynhat.gettext3.Utils.SectionPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by huynhat on 2017-11-14.
 */

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    //Fragments

    //widgets
    private TabLayout mTabLayout;
    public ViewPager mViewPager;
    BottomNavigationViewEx myBottomNav;
    MenuItem prevMenuItem;

    //Variables
    public SectionPagerAdapter mPagerAdapater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mViewPager = (ViewPager) findViewById(R.id.viewPager_container);

        myBottomNav = (BottomNavigationViewEx) findViewById(R.id.bottomNavBar);
        myBottomNav.enableAnimation(false);
        myBottomNav.enableShiftingMode(false);
        myBottomNav.enableItemShiftingMode(false);



        myBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.ic_sell:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.ic_wish:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.ic_profile:
                        mViewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(prevMenuItem!= null){
                    prevMenuItem.setChecked(false);
                }else {
                    myBottomNav.getMenu().getItem(0).setChecked(false);
                }

                myBottomNav.getMenu().getItem(position).setChecked(true);
                prevMenuItem= myBottomNav.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        verifyPermission();

    }

    private void setUpViewPager(ViewPager viewPager){

        mPagerAdapater = new SectionPagerAdapter(getSupportFragmentManager());
        mPagerAdapater.addFragment(new FragmentHomeSearch());
        mPagerAdapater.addFragment(new FragmentSell());
        mPagerAdapater.addFragment(new FragmentWish());
        mPagerAdapater.addFragment(new FragmentProfile());

        viewPager.setAdapter(mPagerAdapater);

    }

    private void verifyPermission(){
        String[] persmissons ={Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                persmissons[0])== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                persmissons[1])== PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this.getApplicationContext(),
                persmissons[2])== PackageManager.PERMISSION_GRANTED){

            setUpViewPager(mViewPager);

        }
        else{
            ActivityCompat.requestPermissions(HomeActivity.this,
                    persmissons, REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermission();
    }
}
