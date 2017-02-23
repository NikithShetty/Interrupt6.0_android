package com.nikith_shetty.interrupt60;

import android.content.Intent;
import android.database.Cursor;
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
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pushbots.push.Pushbots;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Intent callingIntent = null;
    Bundle postsListBundle = null;
    Bundle eventsListBundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Register for Push Notifications
        Pushbots.sharedInstance().registerForRemoteNotifications();

        callingIntent = getIntent();
        if(callingIntent.getExtras()!=null){
            postsListBundle = fetchPostsDataFromIntent(callingIntent);
            eventsListBundle = fetchEventsDataFromIntent(callingIntent);
        }else{
            postsListBundle = fetchPostsDataFromDB();
            eventsListBundle = fetchEventsDataFromDB();
        }

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

    private Bundle fetchPostsDataFromIntent(Intent in){
        Log.e(TAG, "fetchPostsDataFromIntent");
        PostsDB db = new PostsDB(getBaseContext());
        db.emptyDB();
        Gson gson = new Gson();
        Type type = new TypeToken<List<PostData>>() {}.getType();
        List<PostData> list = gson.fromJson(in.getStringExtra("posts"), type);
        Bundle postsData = new Bundle();
        postsData.putString("json", in.getStringExtra("posts"));
        Log.e(TAG, "in.getStringExtra(\"posts\") : " + in.getStringExtra("posts"));
        for (int i=0; i<list.size(); i++) {
            PostData item = list.get(i);
            db.insertData(item.getId(), item.getImgUrl(), item.getTitle(),
                    item.getContent(), item.getDisplayAs(), item.getWebLink(), item.getTimestamp());
        }
        return postsData;
    }

    private Bundle fetchPostsDataFromDB(){
        Log.e(TAG, "fetchPostsDataFromDB");
        Bundle postsData = new Bundle();
        PostsDB db = new PostsDB(getBaseContext());
        Cursor c = db.readAllData();
        List<PostData> data = new ArrayList<>();
        while (c.moveToNext()){
            data.add( PostData.createPost(c.getString(0), c.getString(1), c.getString(2),
                    c.getString(3), c.getString(4), c.getString(5), c.getString(6)));
        }
        if(data.size() == 0){
            postsData = null;
        }else {
            Gson gson = new Gson();
            Type type = new TypeToken<List<PostData>>() {}.getType();
            postsData.putString("json", gson.toJson(data, type));
            Log.e(TAG, "Posts from DB : " + gson.toJson(data, type));
        }
        return postsData;
    }

    private Bundle fetchEventsDataFromIntent(Intent in){
        Log.e(TAG, "fetchEventsDataFromIntent");
        EventsDB db = new EventsDB(getBaseContext());
        db.emptyDB();
        Gson gson = new Gson();
        Type type = new TypeToken<List<EventData>>() {}.getType();
        List<EventData> list = gson.fromJson(in.getStringExtra("events"), type);
        Bundle eventsData = new Bundle();
        eventsData.putString("json", in.getStringExtra("events"));
        Log.e(TAG, "in.getStringExtra(\"events\") : " + in.getStringExtra("events"));
        for (int i=0; i<list.size(); i++) {
            EventData item = list.get(i);
            db.insertData(item.getEvent_id(), item.getEvent_name(), item.getImgUrl(),
                    item.getEvent_desc(), item.getDateTime(), item.getVenue(),
                    item.getFee(), item.getContact());
        }
        return eventsData;
    }

    private Bundle fetchEventsDataFromDB(){
        Log.e(TAG, "fetchEventsDataFromDB");
        Bundle eventsData = new Bundle();
        EventsDB db = new EventsDB(getBaseContext());
        Cursor c = db.readAllData();
        List<EventData> data = new ArrayList<>();
        while (c.moveToNext()){
//            Log.e(TAG, "id : " + c.getString(0) + ", name : " + c.getString(1) +
//            ", imgurl : " + c.getString(2) + ", desc : " + c.getString(3) +
//            ", dateTime : " + c.getString(4) + ", venue : " + c.getString(5) +
//            ", fee : " + c.getString(6) + ", contact : " + c.getString(7));
            data.add( EventData.createEvent(c.getString(0), c.getString(1), c.getString(2),
                    c.getString(3), c.getString(4), c.getString(5), c.getString(6),
                    c.getString(7)));
        }
        if(data.size() == 0){
            eventsData = null;
        }else {
            Gson gson = new Gson();
            Type type = new TypeToken<List<EventData>>() {}.getType();
            eventsData.putString("json", gson.toJson(data, type));
            Log.e(TAG, "Events from DB : " + gson.toJson(data, type));
        }
        return eventsData;
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
        adapter.addFragment(HomeFragment.title, HomeFragment.newInstance(postsListBundle));
        adapter.addFragment(EventsFragment.title, EventsFragment.newInstance(eventsListBundle));
        adapter.addFragment(ContactFragment.title, ContactFragment.newInstance(null));
        viewPager.setAdapter(adapter);
    }
}
