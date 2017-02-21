package com.nikith_shetty.interrupt60;


import android.content.Intent;
import android.net.Uri;
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
public class HomeFragment extends Fragment {

    public static String title = "HOME";
    private static String url = "http://interrupt.tk/postsData.php";
    private RecyclerView recyclerView;
    RecyclerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private List<PostData> list = new ArrayList<>();
    TextView noInternet;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Bundle data){
        if(data==null)
            return new HomeFragment();
        else{
            HomeFragment n = new HomeFragment();
            n.setArguments(data);
            return n;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.homeswiperefresh);
        noInternet = (TextView) view.findViewById(R.id.noInternet);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        adapter = new RecyclerAdapter(list);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
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

    private void makeNetworkCall() {
        swipeRefreshLayout.setRefreshing(true);
//        Log.e(title, "Inside makeNetworkCall");
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
                adapter.notifyDataSetChanged();
                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                noInternet.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                noInternet.setVisibility(View.VISIBLE);
            }
        });

        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }

    //RecyclerAdapter for the recycler view
    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

        private List<PostData> list;

        public class ViewHolder extends RecyclerView.ViewHolder {
            NetworkImageView rowImg;
            TextView rowHeader, rowSummary;
            CardView cardView;
            public ViewHolder(View itemView) {
                super(itemView);
                rowHeader = (TextView) itemView.findViewById(R.id.rowHeader);
                rowSummary = (TextView) itemView.findViewById(R.id.rowSummary);
                rowImg = (NetworkImageView) itemView.findViewById(R.id.rowImg);
                cardView = (CardView) itemView.findViewById(R.id.homeCard);
            }
        }

        public RecyclerAdapter(List<PostData> dataSet){
            list = dataSet;
        }

        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_card_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, int position) {
            final PostData data = list.get(position);
            int summaryLength = 50;
            if(data.getImgUrl().equals("null")){
                holder.rowImg.setVisibility(View.GONE);
            }else{
                fetchImgFromUrl(holder.rowImg, data.getImgUrl());
            }
            holder.rowHeader.setText(data.getTitle());
            if(!data.getContent().equals("null")) {
                if(data.getContent().length() > summaryLength){
                    holder.rowSummary.setText(data.getContent().substring(0, summaryLength) + "...");
                }else{
                    holder.rowSummary.setText(data.getContent());
                }
            }else {
                holder.rowSummary.setVisibility(View.GONE);
            }
            if(data.getDisplayAs()== PostData.DiplayAs.ACTIVITY){
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PostDispActivity.class);
                        Gson gson = new Gson();
                        Type type = new TypeToken<PostData>(){}.getType();
                        intent.putExtra("json", gson.toJson(data, type));
                        startActivity(intent);
                    }
                });
            }else if(data.getDisplayAs()== PostData.DiplayAs.WEBVIEW){
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (data.getWebLink()!=null && !data.getWebLink().equals("null"))
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getWebLink())));
                    }
                });
            }else if(data.getDisplayAs()== PostData.DiplayAs.NONE){
                //do nothing
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private void fetchImgFromUrl(NetworkImageView img, String url){
            ImageLoader imageLoader = ImageLoadRequest.getInstance(getContext()).getImageLoader();
            imageLoader.get(url,
                    ImageLoader.getImageListener(img, R.drawable.logo_img_url, 0));
            img.setImageUrl(url,
                    imageLoader);
        }
    }
}
