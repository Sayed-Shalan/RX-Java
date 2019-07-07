package com.sayed.rxjava.ui.home;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sayed.rxjava.R;
import com.sayed.rxjava.databinding.ActivityMainBinding;
import com.sayed.rxjava.other.ViewPagerAdapter;
import com.sayed.rxjava.ui.base.BaseActivity;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {

    //Dec Data
    ActivityMainBinding binding;
    ArrayList<Fragment> fragmentsList;
    MoviesFragment moviesFragment;
    FavouritesFragment favouritesFragment;
    ViewPagerAdapter pagerAdapter;

    //ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);

        initToolbarWithNoBack(binding.toolbar);//init toolbar
        initData(); //init Data
        setUpViewPager();
    }



    //Init Data
    private void initData() {

        moviesFragment=new MoviesFragment();
        favouritesFragment=new FavouritesFragment();
        fragmentsList=new ArrayList<>();
    }

    //set up view pager
    private void setUpViewPager() {
        fragmentsList.add(moviesFragment);
        fragmentsList.add(favouritesFragment);

        pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),fragmentsList);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(1);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        //set tabs titles
        binding.tabLayout.getTabAt(0).setText(getResources().getString(R.string.movies));
        binding.tabLayout.getTabAt(1).setText(getResources().getString(R.string.favourites));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
