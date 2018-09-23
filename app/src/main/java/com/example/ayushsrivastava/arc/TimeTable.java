package com.example.ayushsrivastava.arc;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;

/**
 * Created by Ayush Srivastava on 1/26/2018.
 */

public class TimeTable extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timetable,container,false);
        Button btn_gettimetable = (Button)view.findViewById(R.id.btn_gettimetable);
        Button btn_getSyllabus = (Button)view.findViewById(R.id.btn_getSyllabus);
        Button btn_feedback = (Button)view.findViewById(R.id.feedback);
        final TextView feedback_tv = (TextView)view.findViewById(R.id.feedback_tv);
        final EditText feedback_edit =(EditText)view.findViewById(R.id.feedback_edit);
        final LinearLayout feedback_layout = (LinearLayout)view.findViewById(R.id.feedback_layout);
        Button send = (Button)view.findViewById(R.id.send);
        final TextView broadcast = (TextView)view.findViewById(R.id.broadcast_message_tv);
        final DatabaseReference message = FirebaseDatabase.getInstance().getReference("Notify").child("broadcasts");
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.child("branch_sem").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                message.child(dataSnapshot.getValue(String.class)).child("broadcast").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        broadcast.setText(dataSnapshot.getValue(String.class));
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
        btn_getSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                userRef.child("branch_sem").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        StorageReference getTimetable = FirebaseStorage.getInstance().getReference("Syllabus").child(dataSnapshot.getValue(String.class));
                        File locatfile;//"/storage/emulated/0/","arc_icon/"+up.getFname()
                        locatfile = new File(String.valueOf(Environment.getExternalStorageDirectory()), "arc_icon/");//"/storage/emulated/0/","arc_icon/"+up.getFname()
                        if (locatfile.mkdirs()) {
                            Toast.makeText(view.getContext(), "Directory Created", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            final int notifyid=12;
                            final NotificationManager nm = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            final NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext());
                            final ProgressDialog dialog = new ProgressDialog(view.getContext());
                            dialog.setCancelable(false);
                            String pre = "Syllabus"+dataSnapshot.getValue(String.class);
                            String suf = ".pdf";
                            final File file = new File(locatfile.getPath(), pre + suf);
                            getTimetable.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    dialog.dismiss();
                                    builder.setProgress(0,0,false);
                                    builder.setContentTitle("Download complete !");
                                    builder.setOngoing(false);
                                    nm.notify(notifyid,builder.build());
                                    Toast.makeText(view.getContext(), "Done !", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    builder.setProgress(0,0,false);
                                    builder.setContentTitle("Download Failed !");
                                    builder.setOngoing(false);
                                    nm.notify(notifyid,builder.build());
                                    Toast.makeText(view.getContext(), "Something went wrong, possible cause : File is not uploaded yet !", Toast.LENGTH_LONG).show();
                                }
                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    final double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
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
                                    dialog.setMessage("Downloading syllabus");
                                    dialog.show();
                                }
                            });
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "Something went wrong, possible cause : turn on storage permission in settings > app permission", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        btn_gettimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        Users user = dataSnapshot.getValue(Users.class);
                        StorageReference getTimetable = FirebaseStorage.getInstance().getReference("Timetable").child(user.getBranch_sem()).child(user.getuGroup());
                        File locatfile;//"/storage/emulated/0/","arc_icon/"+up.getFname()
                        locatfile = new File(String.valueOf(Environment.getExternalStorageDirectory()), "ARC/");//"/storage/emulated/0/","arc_icon/"+up.getFname()
                        if (locatfile.mkdirs()) {
                            Toast.makeText(view.getContext(), "Directory Created", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            final int notifyid=12;
                            final NotificationManager nm = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            final NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext());
                            final ProgressDialog dialog = new ProgressDialog(view.getContext());
                            dialog.setCancelable(false);
                            String pre = "Time table "+user.getuGroup();
                            String suf = ".jpg";
                            final File file = new File(locatfile.getPath(), pre + suf);
                            getTimetable.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    dialog.dismiss();
                                    builder.setProgress(0,0,false);
                                    builder.setContentTitle("Download complete !");
                                    builder.setOngoing(false);
                                    nm.notify(notifyid,builder.build());
                                    Toast.makeText(view.getContext(), "Done !", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    builder.setProgress(0,0,false);
                                    builder.setContentTitle("Download Failed !");
                                    builder.setOngoing(false);
                                    nm.notify(notifyid,builder.build());
                                    Toast.makeText(view.getContext(), "Something went wrong, possible cause : File is not uploaded yet !", Toast.LENGTH_LONG).show();
                                }
                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    dialog.setMessage("Downloading TimeTable");
                                    final double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
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
                                    dialog.show();
                                }
                            });
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "Something went wrong, possible cause : turn on storage permission in settings > app permission", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback_tv.setText("Report :");
                feedback_edit.setHint("Write your issue");

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!feedback_edit.getText().toString().isEmpty())
                {
                    final DatabaseReference feedback = FirebaseDatabase.getInstance().getReference("feedback");
                    final DatabaseReference feedbackReply = FirebaseDatabase.getInstance().getReference("feedbackReply");
                    userRef.child("uname").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            feedback.child(dataSnapshot.getValue(String.class)).setValue(feedback_edit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if(feedback_tv.getText().equals("Report :"))
                                        feedbackReply.child(dataSnapshot.getValue(String.class)).child("reply").setValue("Thanks for the feedback,we'll come back to you asap");
                                        feedback_edit.setHint("Share your experience with arc_icon");
                                        feedback_tv.setText("Feedback : ");
                                        Toast.makeText(view.getContext(), "Your feedback has been recorded", Toast.LENGTH_SHORT).show();
                                    }
                                    else Toast.makeText(view.getContext(), "Something went wrong ! possible cause : network interruption !", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        return view;
    }
}
