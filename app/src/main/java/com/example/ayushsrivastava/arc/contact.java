package com.example.ayushsrivastava.arc;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ayush Srivastava on 1/26/2018.
 */

public class contact extends Fragment {
    ListView userlist;
    DatabaseReference dbusers,Uinfo;
    List<Users> usersList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users,container,false);
        userlist =(ListView)view.findViewById(R.id.userList);
        usersList = new ArrayList<>();
        final UserList adapter = new UserList(getActivity(),usersList);
        userlist.setAdapter(adapter);
        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(view.getContext(), "Hello !", Toast.LENGTH_SHORT).show();
            }
        });
       // Uinfo = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //getBranch(Uinfo);
        //getSem(Uinfo);
        dbusers = FirebaseDatabase.getInstance().getReference("Users");
        dbusers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("branch_sem").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CreateList(dataSnapshot.getValue(String.class),adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;

    }

    private void CreateList(String branch_sem, final UserList adapter) {
        Query query = dbusers.orderByChild("branch_sem").equalTo(branch_sem);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Users user = dataSnapshot.getValue(Users.class);
                usersList.add(user);
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

//    private void getBranch(DatabaseReference uinfo) {
//
//        uinfo.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Users user = dataSnapshot.getValue(Users.class);
//                sem =
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        dbusers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                usersList.clear();
//              for(DataSnapshot usersnapshot : dataSnapshot.getChildren()){
//                 Users user = usersnapshot.getValue(Users.class);
//                 usersList.add(user);
//             }
//             UserList adapter = new UserList(getActivity(),usersList);
//                userlist.setAdapter(adapter);
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
