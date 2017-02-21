package com.nikith_shetty.interrupt60;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
public class ContactFragment extends Fragment implements View.OnClickListener{

    public static String title = "CONTACT US";

    TextView myEmail, interruptEmail;

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

        myEmail = (TextView) view.findViewById(R.id.myEmail);
        myEmail.setText(Html.fromHtml(getString(R.string.myEmail)));
        myEmail.setOnClickListener(this);
        interruptEmail = (TextView) view.findViewById(R.id.interruptEmail);
        interruptEmail.setText(Html.fromHtml(getString(R.string.interruptEmail)));
        interruptEmail.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.equals(myEmail)){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:shettynikith3@gmail.com"));
            startActivity(Intent.createChooser(emailIntent, "Mail via"));
        }else if(view.equals(interruptEmail)){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:interrupt@dr-ait.com"));
            startActivity(Intent.createChooser(emailIntent, "Mail via"));
        }
    }
}
