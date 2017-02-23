package com.nikith_shetty.interrupt60;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static String TAG = "SplashActivity";
    private static String url = "http://interrupt.tk/postsData.php";
    private List<PostData> postList = new ArrayList<>();
    private List<EventData> eventList = new ArrayList<>();
    private int MY_PERMISSIONS_REQUEST_INTERNET = 5241526;

    private String gotPostsList = FETCH_STATUS.NULL;
    private String gotEventsList = FETCH_STATUS.NULL;

    private interface FETCH_STATUS{
        String NULL = "null";
        String NETERR = "network_error";
        String FETCH_SUCC = "fetched_data";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(checkInternetPermission()){
            //make network calls
            makeNetworkCalls();
        }else{
            getInternetPermission();
        }
    }

    private void makeNetworkCalls() {
        getPostsData();
        getEventsData();
        final Thread runner = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(!(gotEventsList.equals(FETCH_STATUS.NULL) ||
                            gotPostsList.equals(FETCH_STATUS.NULL))){
                        if(gotEventsList.equals(FETCH_STATUS.FETCH_SUCC) && gotPostsList.equals(FETCH_STATUS.FETCH_SUCC)){
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            Gson gson = new Gson();
                            //for posts data
                            Type type = new TypeToken<List<PostData>>(){}.getType();
                            intent.putExtra("posts", gson.toJson(postList, type));
                            //for events data
                            type = new TypeToken<List<EventData>>(){}.getType();
                            intent.putExtra("events", gson.toJson(eventList, type));
                            startActivity(intent);
                            finish();
                            return;
                        }else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                            return;
                        }
                    }
                }
            }
        });
        runner.start();

    }

    private boolean checkInternetPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }else
            return true;
    }

    private void getInternetPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                MY_PERMISSIONS_REQUEST_INTERNET);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],
                                           int[] grantResults){
        if(requestCode == MY_PERMISSIONS_REQUEST_INTERNET){
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Permission granted
                this.recreate();
            } else {
                //Permission Denied
                //Go back to main screen
                Toast.makeText(this, "No permission to access INTERNET", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getPostsData() {
        String url = "http://interrupt.tk/postsData.php";
        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Log.e(TAG, "response : " + response.toString());
                try {
                    for(int i=0; i<response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        postList.add(PostData.createPost(
                                obj.getString("id"),
                                obj.getString("imgurl"),
                                obj.getString("title"),
                                obj.getString("content"),
                                obj.getString("weblink"),
                                obj.getString("displayas"),
                                obj.getString("timestamp")
                        ));
//                        Log.e(TAG, ">>postList.toSting() : " + postList.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, ">>Splash postList.size() : " + postList.size());
                gotPostsList = FETCH_STATUS.FETCH_SUCC;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                gotPostsList = FETCH_STATUS.NETERR;
            }
        });

        Volley.newRequestQueue(SplashActivity.this).add(jsonRequest);
    }

    private void getEventsData() {
        String url = "http://interrupt.tk/buttons.php?type=JSON";
        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0; i<response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        eventList.add( EventData.createEvent(
                                obj.getString("event_id"),
                                obj.getString("event_name"),
                                obj.getString("img_url"),
                                obj.getString("event_desc"),
                                obj.getString("venue"),
                                obj.getString("fee"),
                                obj.getString("date_time"),
                                obj.getString("contact")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, ">>Splash eventList.size() : " + eventList.size());
                gotEventsList = FETCH_STATUS.FETCH_SUCC;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                gotEventsList = FETCH_STATUS.NETERR;
            }
        });

        Volley.newRequestQueue(SplashActivity.this).add(jsonRequest);
    }
}
