package com.nikith_shetty.interrupt60;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
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
    private int MY_PERMISSIONS_REQUEST_INTERNET = 5241526;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(checkInternetPermission()){
            //make network calls
            getPostsData();
        }else{
            getInternetPermission();
        }
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
                finish();
            }
        }
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
                        postList.add(PostData.createPost(
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
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                Gson gson = new Gson();
                Type type = new TypeToken<List<PostData>>(){}.getType();
                intent.putExtra("json", gson.toJson(postList, type));
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });

        Volley.newRequestQueue(SplashActivity.this).add(jsonRequest);
    }
}
