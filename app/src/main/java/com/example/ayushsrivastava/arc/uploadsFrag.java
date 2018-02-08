package com.example.ayushsrivastava.arc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * Created by Ayush Srivastava on 1/26/2018.
 */

public class uploadsFrag extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.uploads,container,false);
        return view;
    }
}
