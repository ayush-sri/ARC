package com.example.ayushsrivastava.arc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by Ayush Srivastava on 3/5/2018.
 */

public class date extends Fragment {
    long date = System.currentTimeMillis();
    SimpleDateFormat sdf =new SimpleDateFormat("dd MMM,yyyy");
    String dateString =sdf.format(date);
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.date,container,false);
        TextView date = (TextView)view.findViewById(R.id.dt);
        date.setText(dateString);
        return view;
    }
}
