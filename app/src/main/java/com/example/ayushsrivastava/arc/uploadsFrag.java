package com.example.ayushsrivastava.arc;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ayush Srivastava on 1/26/2018.
 */

public class uploadsFrag extends Fragment {
    ListView uploadlistview;
    DatabaseReference uploadsRef;
    List<uploadContent> uploadContentList;
    TextView noupload;
    Users user;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.uploads,container,false);
        uploadContentList = new ArrayList<>();
        uploadlistview = (ListView)view.findViewById(R.id.uploadedList);
        noupload = (TextView)view.findViewById(R.id.noUpload);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        final uploadlist adapter = new uploadlist(getActivity(),uploadContentList);
        uploadlistview.setAdapter(adapter);
        uploadlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final int pos = i;
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(Users.class);
                        createFileRef(user.getuBranch(),user.getSem(),pos);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                /*if (locatfile.mkdirs()) {

                    // Toast.makeText(view.getContext(), strRef.toString(), Toast.LENGTH_SHORT).show();
                }*/
            }

            private void createFileRef(final String branch, String sem, final int i) {
                final uploadContent up = uploadContentList.get(i);
                final int notifyid=11;
                final NotificationManager nm = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext());
                final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setCancelable(false);
                String path;
                final StorageReference strRef =FirebaseStorage.getInstance().getReference().child(branch).child(sem).child(up.getSubject()).child(up.getCategory()).child(up.getFname() + ".pdf");///CSE/5/BCS 5001/Books
                 File locatfile;//"/storage/emulated/0/","arc_icon/"+up.getFname()
                /*final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Save to : ");
                builder.setPositiveButton("Internal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      path[0] = String.valueOf(Environment.getExternalStorageDirectory());
                    }
                }).setNegativeButton("External", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        path[0]=System.getenv("SECONDARY_STORAGE");
                    }
                });
                builder.create().show();*/
                locatfile = new File(String.valueOf(Environment.getExternalStorageDirectory()),"ARC/");//"/storage/emulated/0/","arc_icon/"+up.getFname()
                if(locatfile.mkdirs()){
                    Toast.makeText(view.getContext(), "Directory Created", Toast.LENGTH_SHORT).show();
                }
                try {
                    String pre = up.getSubject().trim()+" "+up.getCategory()+" "+up.getFname();
                    String suf =".pdf";
                    final File file = new File(locatfile.getPath(),pre+suf);
                    //final File f = File.createTempFile(pre,suf,locatfile);
                    strRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            final DatabaseReference dwnldRef = FirebaseDatabase.getInstance().getReference("Downloads");
                            long date = System.currentTimeMillis();
                            SimpleDateFormat sdf =new SimpleDateFormat("dd/MMM/yyyy,hh:mm a");
                            final String dateString =sdf.format(date);
                            String dwnldon= "Downloaded on "+dateString;
                            downloadContent dt = new downloadContent(up.getFname(),file.getAbsolutePath(),dwnldon,up.getSubject(),up.getCategory(),FirebaseAuth.getInstance().getCurrentUser().getUid()+","+up.branch_sem,file.getName());
                            dwnldRef.child(dateString.replace("/","").trim()).setValue(dt);
                            builder.setProgress(0,0,false);
                            builder.setContentTitle("Download complete !");
                            builder.setOngoing(false);
                            nm.notify(notifyid,builder.build());
                            progressDialog.dismiss();
                            Toast.makeText(view.getContext(), "Downloaded at " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            final double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage(((int) progress) + "% downloading...");
                            progressDialog.show();
                            builder.setSmallIcon(R.drawable.arc);
                            builder.setContentTitle("Download status");
                            builder.setOngoing(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    builder.setProgress(100,(int)progress,false);
                                    nm.notify(notifyid,builder.build());
                                }
                            }).start();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            progressDialog.dismiss();
                            builder.setProgress(0,0,false);
                            builder.setContentTitle("Download Failed !");
                            builder.setOngoing(false);
                            nm.notify(notifyid,builder.build());
                            Toast.makeText(view.getContext(), "Something went wrong ! possible cause :\n File is not uploaded yet !\n Storage permission is not granted !", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), "Something went wrong, possible cause : turn on storage permission in settings > app permission", Toast.LENGTH_LONG).show();
                }
            }
        });

        uploadsRef = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("branch_sem").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Query query = uploadsRef.orderByChild("branch_sem").equalTo(dataSnapshot.getValue(String.class));
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        noupload.setVisibility(View.GONE);
                        uploadContent uploadedcontent = dataSnapshot.getValue(uploadContent.class);
                        uploadContentList.add(uploadedcontent);
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

        if(uploadContentList.isEmpty())
            noupload.setVisibility(View.VISIBLE);
//        uploadlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                uploadContent uploads = uploadContentList.get(i);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(uploads.getUrl()));
//                startActivity(intent);
//            }
//        });
        return view;
    }

}

/*    @Override
    public void onStart() {
        super.onStart();
//        uploadsRef = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
//        uploadsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                uploadContentList.clear();
//                for (DataSnapshot ndataSnapshot : dataSnapshot.getChildren()) {
//                    uploadContent uploadedcontent = ndataSnapshot.getValue(uploadContent.class);
//                    uploadContentList.add(uploadedcontent);
//                }
//                uploadlist adapter = new uploadlist(getActivity(),uploadContentList);
//                uploadlistview.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }*/
