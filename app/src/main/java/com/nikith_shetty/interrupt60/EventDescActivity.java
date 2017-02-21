package com.nikith_shetty.interrupt60;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class EventDescActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "EventDescActivity";
    NetworkImageView img;
    TextView title, desc, venue, date, fee, contact;
    ImageLoader imageLoader;
    EventData eventData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_desc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img = (NetworkImageView) findViewById(R.id.eventDescImg);
        title = (TextView) findViewById(R.id.eventDescTitle);
        desc = (TextView) findViewById(R.id.eventDesc);
        venue = (TextView) findViewById(R.id.eventDescVenue);
        date = (TextView) findViewById(R.id.eventDescDate);
        fee = (TextView) findViewById(R.id.eventDescFee);
        contact = (TextView) findViewById(R.id.eventDescContact);

        Bundle data = getIntent().getExtras();
        Gson gson = new Gson();
        Type type = new TypeToken<EventData>() {
        }.getType();
        if (data != null) {
            eventData = gson.fromJson((String) data.getCharSequence("json"), type);
            Log.e(TAG, "received data : " + eventData.toString());
            title.setText(eventData.getEvent_name());
            getSupportActionBar().setTitle(eventData.getEvent_name());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                desc.setText(Html.fromHtml(eventData.getEvent_desc(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                desc.setText(Html.fromHtml(eventData.getEvent_desc()));
            }
            venue.setText(eventData.getVenue());
            date.setText(eventData.getDateTime());
            fee.setText(eventData.getFee());
            contact.setText(Html.fromHtml("<u>" + eventData.getContact() + "</u>"));
            contact.setOnClickListener(this);

            if (eventData.getImgUrl() != "null") {
                imageLoader = ImageLoadRequest.getInstance(this).getImageLoader();
                imageLoader.get(eventData.getImgUrl(),
                        ImageLoader.getImageListener(img, R.drawable.logo_img_url, 0));
                img.setImageUrl(eventData.getImgUrl(), imageLoader);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onClick(View view) {
        if (view.equals(contact)) {
            if (eventData != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + eventData.getContact()));
                startActivity(intent);
            }
        }
    }
}
