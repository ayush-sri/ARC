package com.example.ayushsrivastava.arc;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.UnknownServiceException;
import java.text.SimpleDateFormat;

/**
 * Created by Ayush Srivastava on 1/25/2018.
 */

public class ContentFragment extends Fragment {
    /*long date = System.currentTimeMillis();
    SimpleDateFormat sdf =new SimpleDateFormat("dd MMM,yyyy");
    String dateString =sdf.format(date);
    private Bundle savedInstanceState;*/
   /* public ContentFragment()
    {

    }
    public static ContentFragment newInstance(String title)
    {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putCharSequence("title",title);
        fragment.setArguments(args);
        return fragment;
    }
    public CharSequence getTitle()
    {
        Bundle args = getArguments();
        return args.getCharSequence("title","Nothing");
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.content,container,false);
        //LinearLayout compdue =(LinearLayout)view.findViewById(R.id.compdue);
        LinearLayout completeLyout =(LinearLayout)view.findViewById(R.id.completed_layout);
        LinearLayout duelayout = (LinearLayout)view.findViewById(R.id.due_layout);
        final Button btn_savecomp = (Button)view.findViewById(R.id.btn_compsave);
        final Button btn_savedue = (Button)view.findViewById(R.id.btn_duesave);
        final EditText completed = (EditText)view.findViewById(R.id.complete_edit);
        final EditText due = (EditText)view.findViewById(R.id.due_edit);
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference tabloc = FirebaseDatabase.getInstance().getReference("tabloc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        tabloc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot Snapshot) {
                        Users thisUser = Snapshot.getValue(Users.class);
                        if(thisUser!=null)
                        {
                        DatabaseReference contentdesc = FirebaseDatabase.getInstance().getReference("content")
                                .child(thisUser.getuBranch())
                                .child(thisUser.getuGroup())
                                .child(dataSnapshot.getValue(String.class));
                        if(contentdesc!=null){
                        contentdesc.child("completed").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                completed.setText(dataSnapshot.getValue(String.class));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        contentdesc.child("due").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                due.setText(dataSnapshot.getValue(String.class));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });}}

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
        completed.setEnabled(false);
        completed.setScroller(new Scroller(view.getContext()));
        completed.setVerticalScrollBarEnabled(true);
        completed.setMovementMethod(new ScrollingMovementMethod());
        completed.setInputType(EditorInfo.TYPE_NULL);
        due.setEnabled(false);
        due.setScroller(new Scroller(view.getContext()));
        due.setVerticalScrollBarEnabled(true);
        due.setInputType(EditorInfo.TYPE_NULL);
        due.setMovementMethod(new ScrollingMovementMethod());
        final DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        completeLyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dbusers.child("cr").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                     if(dataSnapshot.getValue(Boolean.class)){
                         completed.setEnabled(true);
                         btn_savecomp.setVisibility(View.VISIBLE);
                         completed.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                         completed.setSingleLine(false);
                         ((InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(completed,InputMethodManager.SHOW_FORCED);
                     }}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        duelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dbusers.child("cr").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue(Boolean.class)){
                            due.setEnabled(true);
                            btn_savedue.setVisibility(View.VISIBLE);
                            due.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                            due.setSingleLine(false);
                            ((InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(due,InputMethodManager.SHOW_FORCED);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        btn_savecomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                btn_savecomp.setVisibility(View.INVISIBLE);
                completed.setEnabled(false);
                DatabaseReference tabloc = FirebaseDatabase.getInstance().getReference("tabloc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                tabloc.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot Snapshot) {
                                Users thiUser = Snapshot.getValue(Users.class);
                                DatabaseReference contentdesc = FirebaseDatabase.getInstance().getReference("content").child(thiUser.getuBranch()).child(thiUser.getuGroup()).child(dataSnapshot.getValue(String.class)).child("completed");
                                contentdesc.setValue(completed.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(view.getContext(), "Saved !", Toast.LENGTH_SHORT).show();
                                        else Toast.makeText(view.getContext(), "Something went wrong ! posssible cause : no network connection", Toast.LENGTH_SHORT).show();

                                    }
                                });
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
            }
        });
        btn_savedue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                btn_savedue.setVisibility(View.INVISIBLE);
                due.setEnabled(false);
                final DatabaseReference tabloc = FirebaseDatabase.getInstance().getReference("tabloc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                tabloc.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot Snapshot) {
                                Users thiUser = Snapshot.getValue(Users.class);
                                DatabaseReference contentdesc = FirebaseDatabase.getInstance().getReference("content").child(thiUser.getuBranch()).child(thiUser.getuGroup()).child(dataSnapshot.getValue(String.class)).child("due");
                                contentdesc.setValue(completed.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(view.getContext(), "Saved !", Toast.LENGTH_SHORT).show();
                                        else Toast.makeText(view.getContext(), "Something went wrong ! posssible cause : no network connection", Toast.LENGTH_SHORT).show();

                                    }
                                });
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
            }
        });
        return view;
    }


   /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
       TextView dt = (TextView)getActivity().findViewById(R.id.tarik);
       String text = savedInstanceState.getString("dateString");
        dt.setText(text);
    }*/

}
