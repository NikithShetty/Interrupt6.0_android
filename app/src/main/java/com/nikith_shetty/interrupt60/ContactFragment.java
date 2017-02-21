package com.nikith_shetty.interrupt60;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    public static String title = "CONTACT US";


    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance(Bundle data) {
        if (data == null)
            return new ContactFragment();
        else {
            ContactFragment n = new ContactFragment();
            n.setArguments(data);
            return n;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        return view;
    }
}
