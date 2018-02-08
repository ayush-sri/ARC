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
 * Created by Ayush Srivastava on 1/25/2018.
 */

public class ContentFragment extends Fragment {
    String data;
    /*long date = System.currentTimeMillis();
    SimpleDateFormat sdf =new SimpleDateFormat("dd MMM,yyyy");
    String dateString =sdf.format(date);
    private Bundle savedInstanceState;*/
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content,container,false);
    }
   /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
       TextView dt = (TextView)getActivity().findViewById(R.id.tarik);
       String text = savedInstanceState.getString("dateString");
        dt.setText(text);
    }*/

}
