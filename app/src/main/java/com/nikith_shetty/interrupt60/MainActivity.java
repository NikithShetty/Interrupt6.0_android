package com.nikith_shetty.interrupt60;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;

import com.pushbots.push.Pushbots;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Register for Push Notifications
        Pushbots.sharedInstance().registerForRemoteNotifications();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapseToolbar);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        Palette.from(BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.logo_img_url))
                .generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorPrimary));
                        int vibrantDarkColor = palette.getVibrantColor(getResources().getColor(R.color.colorPrimaryDark));
                        collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                        collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
                    }
                });
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(String title, Fragment fragment){
            fragmentList.add(fragment);
            titleList.add(title);
        }

        @Override
        public String getPageTitle(int position){
            return titleList.get(position);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(HomeFragment.title, HomeFragment.newInstance(null));
        adapter.addFragment(EventsFragment.title, EventsFragment.newInstance(null));
        adapter.addFragment(ContactFragment.title, ContactFragment.newInstance(null));
        viewPager.setAdapter(adapter);
    }
}
