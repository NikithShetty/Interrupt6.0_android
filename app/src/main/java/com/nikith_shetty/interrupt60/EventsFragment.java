package com.nikith_shetty.interrupt60;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    public static String title = "EVENTS";
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    String url = "http://interrupt.tk/buttons.php?type=JSON";
    private List<EventData> list = new ArrayList<>();
    TextView noInternt;

    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance(Bundle data){
        if(data==null)
            return new EventsFragment();
        else{
            EventsFragment n = new EventsFragment();
            n.setArguments(data);
            return n;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(title, "Inside onCreateView");
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.eventsSwiperefresh);
        noInternt = (TextView) view.findViewById(R.id.noInternetEvent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        adapter = new RecyclerAdapter(list);
        recyclerView = (RecyclerView) view.findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        if(list.size()==0) {
            makeNetworkCall();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                makeNetworkCall();
            }
        });
        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    private void makeNetworkCall() {
        swipeRefreshLayout.setRefreshing(true);
        Log.e(title, "Inside makeNetworkCall");
        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0; i<response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        //Log.e(title, "obj.getString(\"img_url\") : " + obj.getString("img_url"));
                        list.add( new EventData(
                                obj.getString("event_id"),
                                obj.getString("event_name"),
                                obj.getString("img_url"),
                                obj.getString("event_desc"),
                                obj.getString("venue"),
                                obj.getString("fee"),
                                obj.getString("date_time")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                noInternt.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                noInternt.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }


    //RecyclerAdapter for the recycler view
    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

        private List<EventData> list;
        private ImageLoader imageLoader;

        public class ViewHolder extends RecyclerView.ViewHolder {
            NetworkImageView rowImg;
            TextView rowTitle;
            CardView cardView;
            public ViewHolder(View itemView) {
                super(itemView);
                rowTitle = (TextView) itemView.findViewById(R.id.eventName);
                rowImg = (NetworkImageView) itemView.findViewById(R.id.eventImg);
                cardView = (CardView) itemView.findViewById(R.id.eventsCard);
            }
        }

        public RecyclerAdapter(List<EventData> dataSet){
            list = dataSet;
        }

        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.events_card_row, parent, false);
            return new RecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, final int position) {
            //load image from url
            String imgUrl = list.get(position).getImgUrl();
            if(!imgUrl.equals("null")) {
                imageLoader = ImageLoadRequest.getInstance(getContext()).getImageLoader();
                imageLoader.get(imgUrl,
                        ImageLoader.getImageListener(holder.rowImg, R.drawable.logo_img_url, 0));
                holder.rowImg.setImageUrl(imgUrl,
                        imageLoader);
            }else{
                holder.rowImg.setVisibility(View.GONE);
            }
            holder.rowTitle.setText(list.get(position).getEvent_name());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent data = new Intent(getActivity(), EventDescActivity.class);
                    Gson gson = new Gson();
                    Type type = new TypeToken<EventData>(){}.getType();
                    data.putExtra("json", gson.toJson(list.get(position), type));
//                    Log.e(title, "jsonList : " + gson.toJson(list.get(position), type));
                    startActivity(data);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
