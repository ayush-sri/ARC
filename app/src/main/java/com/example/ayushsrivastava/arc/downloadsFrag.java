package com.example.ayushsrivastava.arc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Ayush Srivastava on 1/26/2018.
 */

public class downloadsFrag extends Fragment {
    ListView downloadListView;
    DatabaseReference downloadref;
    List<downloadContent> downloadContentList;
    TextView nodwnloads;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.downloads, container, false);
        downloadContentList = new ArrayList<>();
        nodwnloads = (TextView)view.findViewById(R.id.noDownload);
        Object clipboardService = getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager) clipboardService;
        downloadListView = (ListView) view.findViewById(R.id.downloadedlist);
        final downloadlist adapter = new downloadlist(getActivity(), downloadContentList);
        downloadListView.setAdapter(adapter);
        downloadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                downloadContent dt = downloadContentList.get(i);
                ClipData data = ClipData.newPlainText("Source Text",dt.getFullfname());
                clipboardManager.setPrimaryClip(data);
                Toast.makeText(view.getContext(), "Copied name to clipboard", Toast.LENGTH_SHORT).show();

            }

        });

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        downloadref = FirebaseDatabase.getInstance().getReference("Downloads");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("branch_sem").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                Query query = downloadref.orderByChild("branch_sem").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()+","+Snapshot.getValue(String.class));
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        nodwnloads.setVisibility(View.INVISIBLE);
                        downloadContent content = dataSnapshot.getValue(downloadContent.class);
                        downloadContentList.add(content);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if(downloadContentList.isEmpty())
            nodwnloads.setVisibility(View.VISIBLE);
        return view;
    }

}
