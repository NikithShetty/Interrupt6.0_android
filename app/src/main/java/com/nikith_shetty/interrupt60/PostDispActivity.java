package com.nikith_shetty.interrupt60;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class PostDispActivity extends AppCompatActivity {

    NetworkImageView imageView;
    TextView title, content;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_disp);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (NetworkImageView) findViewById(R.id.postImg);
        title = (TextView) findViewById(R.id.postTitle);
        content = (TextView) findViewById(R.id.postContent);

        Gson gson = new Gson();
        Type type = new TypeToken<PostData>(){}.getType();
        if(getIntent().getExtras()!=null){
            PostData data = gson.fromJson(getIntent().getExtras().getString("json"), type);
            if(!data.getImgUrl().equals("null")){
                imageLoader = ImageLoadRequest.getInstance(this).getImageLoader();
                imageLoader.get(data.getImgUrl(),
                        ImageLoader.getImageListener(imageView, R.drawable.logo_img_url, R.drawable.logo_img_url));
                imageView.setImageUrl(data.getImgUrl(), imageLoader);
            }else{
                imageView.setVisibility(View.GONE);
            }
            getSupportActionBar().setTitle(data.getTitle());
            title.setText(data.getTitle());
            content.setText(data.getContent());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
