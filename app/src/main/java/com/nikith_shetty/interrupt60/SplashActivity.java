package com.nikith_shetty.interrupt60;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static String TAG = "SplashActivity";
    private static String url = "http://interrupt.tk/postsData.php";
    private List<PostData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private boolean checkForInternet(){
        return false;
    }

    private void getPostsData() {
        String url = "http://interrupt.tk/postsData.php";
        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Log.e(title, "response : " + response.toString());
                try {
                    for(int i=0; i<response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        int display = PostData.DiplayAs.NONE;
                        if(obj.get("displayas").equals("ACTIVITY"))
                            display = PostData.DiplayAs.ACTIVITY;
                        else if(obj.get("displayas").equals("WEBVIEW"))
                            display = PostData.DiplayAs.WEBVIEW;
                        list.add(PostData.createPost(
                                obj.getString("imgurl"),
                                obj.getString("title"),
                                obj.getString("content"),
                                obj.getString("weblink"),
                                display
                        ));
                        //Log.e(title, ">>list.toSting() : " + list.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.e(title, ">>list.size() : " + list.size());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        Volley.newRequestQueue(SplashActivity.this).add(jsonRequest);
    }
}
