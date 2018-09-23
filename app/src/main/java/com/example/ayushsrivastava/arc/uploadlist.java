package com.example.ayushsrivastava.arc;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Ayush Srivastava on 6/16/2018.
 */
public class uploadlist extends ArrayAdapter {
    private Activity context;
    private List<uploadContent> uploadContentList;

    public uploadlist(Activity context, List<uploadContent> uploadContentList) {
        super(context, R.layout.uploadcont, uploadContentList);
        this.context = context;
        this.uploadContentList = uploadContentList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View ListViewItem = inflater.inflate(R.layout.uploadcont, null, false);
        TextView uploadfname = (TextView) ListViewItem.findViewById(R.id.upld);
        TextView uplddate = (TextView) ListViewItem.findViewById(R.id.uplddate);
        uploadContent content = uploadContentList.get(position);
        uploadfname.setText(content.getSubject()+":"+content.getFname()+" in "+content.getCategory());
        uplddate.setText(content.getTime());
        return ListViewItem;

    }
}
