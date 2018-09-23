package com.example.ayushsrivastava.arc;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
//import com.squareup.picasso.Picasso;

public class profileactivity extends AppCompatActivity {

    DatabaseReference currentuser;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileactivity);
        final DatabaseReference subject_ref = FirebaseDatabase.getInstance().getReference("Subjects");
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final CollapsingToolbarLayout profile = (CollapsingToolbarLayout) findViewById(R.id.profile);
        FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.b1);
//        LinearLayout head = (LinearLayout) findViewById(R.id.head);
//        LinearLayout LL = (LinearLayout) findViewById(R.id.spin);
        final TextView total_marks = (TextView)findViewById(R.id.total_marks);
        final Spinner subject = (Spinner) findViewById(R.id.subject_spinner);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressreport);
        progressBar.setMax(40);
        /*if (progressBar.getProgress() <= 20)
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        */final String subject_string[] = new String[7];
        subject_string[0] = "SUBJECT";
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Users user = dataSnapshot.getValue(Users.class);
                for (int i = 1; i <= 6; i++) {
                    final int finali = i;
                    subject_ref.child(user.getuBranch()).child(user.getSem()).child(String.valueOf(finali)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            subject_string[finali] = dataSnapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ArrayAdapter<String> subject_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, subject_string);
        subject.setAdapter(subject_adapter);
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setCurrentProgress(subject,progressBar,total_marks);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*if(subject.getSelectedItem().toString().equals("BCS 5001")){
            Toast.makeText(this, subject_string[1], Toast.LENGTH_SHORT).show();
        }
        else if(subject.getSelectedItem().toString().equals(subject_string[2]))
        {
            setCurrentProgress(subject_string[2],progressBar);

        }
        else if(subject.getSelectedItem().toString().equals(subject_string[3])){
            setCurrentProgress(subject_string[3],progressBar);

        }
        else if (subject.getSelectedItem().toString().equals(subject_string[4])){
            setCurrentProgress(subject_string[4],progressBar);

        }
        else if (subject.getSelectedItem().toString().equals(subject_string[5])){
            setCurrentProgress(subject_string[5],progressBar);

        }
        else if (subject.getSelectedItem().toString().equals(subject_string[6])){
            setCurrentProgress(subject_string[6],progressBar);
        }*/
        final Button b2 = (Button) findViewById(R.id.b2);
        final Button b3 = (Button) findViewById(R.id.b3);
        final EditText changeusername = (EditText) findViewById(R.id.changeusername);
        final TextView loading = (TextView) findViewById(R.id.loading);
        TextView openprogresstracker = (TextView) findViewById(R.id.tracker);
        final ImageView dp = (ImageView) findViewById(R.id.profile_pic);
        final Button change = (Button) findViewById(R.id.change);
        final Button show = (Button) findViewById(R.id.show);
        final Button changeuser = (Button) findViewById(R.id.changeUsername);
        final EditText changepassword = (EditText) findViewById(R.id.changepassword);
        final TextView course = (TextView) findViewById(R.id.atcp1);
        final TextView branch = (TextView) findViewById(R.id.atcp2);
        final TextView sem = (TextView) findViewById(R.id.atcp3);
        final TextView group = (TextView) findViewById(R.id.atcp4);
        final EditText et1 = (EditText) findViewById(R.id.et1);
        et1.setEnabled(false);
        final EditText et2 = (EditText) findViewById(R.id.et2);
        et2.setEnabled(false);

        /*String [] Courses = {"B.Tech","BCA","Diploma"};
        String [] Branch ={"CSE","ECE","Mech","Civil"};
        String [] year = {"1","2","3","4"};
        String [] group = {"11","12","13","14","15","16","21","22","23","24","25","26","31","32","33","34","35","36","41","42","43","44","45","46","51","52","53","54","55","56","61","62","63","64","65","66","71","72","73","74","75","76","81","82","83","84","85","86"};
        Spinner atcp1 = (Spinner) findViewById(R.id.atcp1);
        Spinner atcp2 = (Spinner) findViewById(R.id.atcp2);
        Spinner atcp3 = (Spinner) findViewById(R.id.atcp3);
        Spinner atcp4 = (Spinner) findViewById(R.id.atcp4);
        ArrayAdapter <String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,Courses);
        ArrayAdapter <String> arrayAdapter2 = new ArrayAdapter <String>(this,android.R.layout.simple_dropdown_item_1line,Branch);
        ArrayAdapter <String> arrayAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,year);
        ArrayAdapter <String> arrayAdapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,group);
        atcp1.setAdapter(arrayAdapter);
        atcp1.setTag("B.tech");
        atcp2.setAdapter(arrayAdapter2);
        atcp2.setTag("CSE");
        atcp3.setAdapter(arrayAdapter3);
        atcp3.setTag("1");
        atcp4.setAdapter(arrayAdapter4);
        atcp4.setTag("11");*//*
*/
        //CollapseToolbar begins here
        final Toolbar toolbaar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(toolbaar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbaar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (change.getText().equals("Reset Password")) {
                    changepassword.setVisibility(View.VISIBLE);
                    change.setText("Save Password");
                } else if (change.getText().equals("Save Password")) {
                    change.setText("Reset Password");
                    show.setVisibility(View.INVISIBLE);
                    changepassword.setVisibility(View.INVISIBLE);
                    if (!changepassword.getText().toString().isEmpty()) {
                        changepassword.setVisibility(View.INVISIBLE);
                        if (changepassword.getText().toString().length() < 8) {
                            changepassword.setVisibility(View.VISIBLE);
                            changepassword.requestFocus();
                            changepassword.setError("at least 8 characters are required !");
                        } else {
                            FirebaseAuth.getInstance().getCurrentUser().updatePassword(changepassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    hideKeyboard(changepassword);
                                    if (!task.isSuccessful()) {
                                        changepassword.setVisibility(View.INVISIBLE);
                                        Toast.makeText(profileactivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(profileactivity.this, "password updated", Toast.LENGTH_SHORT).show();
                                        changepassword.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("upassword").setValue(changepassword.getText().toString());

                        }
                    } else {
                        Toast.makeText(profileactivity.this, "Cancelled,Nothing Entered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b2.getText().equals("EDIT")) {
                    b2.setText("SAVE");
                    et1.setEnabled(true);
                    et1.requestFocus();
                    et1.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(et1, InputMethodManager.SHOW_FORCED);
                } else if (b2.getText().equals("SAVE")) {
                    b2.setText("EDIT");
                    et1.setEnabled(false);
                    et1.getBackground().setColorFilter(getResources().getColor(R.color.colprim), PorterDuff.Mode.SRC_ATOP);
                    if (!et1.getText().toString().isEmpty()) {
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uEmail").setValue(et1.getText().toString());
                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(et1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(profileactivity.this, "Email updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        et1.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        Toast.makeText(profileactivity.this, "cancelled,nothing entered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b3.getText().equals("EDIT")) {
                    b3.setText("SAVE");
                    et2.setEnabled(true);
                    et2.requestFocus();
                    et2.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(et2, InputMethodManager.SHOW_FORCED);
                } else if (b3.getText().equals("SAVE")) {
                    b3.setText("EDIT");
                    et2.setEnabled(false);
                    et2.getBackground().setColorFilter(getResources().getColor(R.color.colprim), PorterDuff.Mode.SRC_ATOP);
                    if (!(et2.getText().toString().length() < 15)) {
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("rollNum").setValue(et2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(profileactivity.this, "Roll number updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("rollNum").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                et2.setText(dataSnapshot.getValue(String.class));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(profileactivity.this, "Invalid Roll number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });/*
        LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et2.setEnabled(false);
            }
        });*/
        changepassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    show.setVisibility(View.GONE);
                } else {
                    show.setVisibility(View.VISIBLE);
                }
                if (charSequence.length() < 8) {
                    changepassword.setError("enter at least 8 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (show.getText().equals("SHOW")) {
                    show.setText("HIDE");
                    changepassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    changepassword.setSelection(changepassword.length());
                } else if (show.getText().equals("HIDE")) {
                    show.setText("SHOW");
                    changepassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    changepassword.setSelection(changepassword.length());
                }
            }
        });
        currentuser = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        currentuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loading.setVisibility(View.VISIBLE);
                Users user = dataSnapshot.getValue(Users.class);
                if (user != null) {
                    profile.setTitle(user.getUname());
                    et1.setText(user.getuEmail());
                    et2.setText(user.getRollNum());
                    branch.setText(user.getuBranch());
                    sem.setText(user.getSem());
                    group.setText(user.getuGroup());
                    if (!user.getImgurl().equals("default")) {
                        Picasso.get().load(user.getImgurl()).fit().into(dp);
                        //new getImagefromURL(dp).execute(user.getImgurl());
                        loading.setVisibility(View.INVISIBLE);
                    } else{
                        Drawable drawable =getApplicationContext().getResources().getDrawable(R.drawable.defaultimg);
                        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                        dp.setImageBitmap(bitmap);
                        loading.setText("No profile pic uploaded yet !");
                    }
                } else {
                    Toast.makeText(profileactivity.this, "Restart the app to configure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
        changeuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeuser.getText().equals("Change Username")) {
                    changeuser.setText("Save changes");
                    changeusername.setVisibility(View.VISIBLE);
                } else if (changeuser.getText().equals("Save changes")) {
                    changeuser.setText("Change Username");
                    changeusername.setVisibility(View.INVISIBLE);
                    hideKeyboard(changeusername);
                    if (!changeusername.getText().toString().isEmpty()) {
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uname").setValue(changeusername.getText().toString());
                        profile.setTitle(changeusername.getText().toString());
                    } else {
                        Toast.makeText(profileactivity.this, "cancelled,nothing entered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        openprogresstracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setCurrentProgress(Spinner s, final ProgressBar progressBar, final TextView total_marks) {
        if(!s.getSelectedItem().toString().equals("SUBJECT")){
        String Total="Total";
        DatabaseReference marksRef = FirebaseDatabase.getInstance().getReference("progressreport").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        marksRef.child(s.getSelectedItem().toString()).child(Total).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long val = Math.round(Double.parseDouble(dataSnapshot.getValue(String.class)));
                    progressBar.setProgress(Integer.parseInt(String.valueOf(val)));
                    if(progressBar.getProgress()<17)
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                    else if(progressBar.getProgress()>17)
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(80,210,194)));
                    else if(progressBar.getProgress()>35)
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(214,103,205)));
                    total_marks.setText(String.valueOf(val));
                }
                else Toast.makeText(profileactivity.this, "The value at the selected item is not set !", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });}
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final TextView loading = (TextView)findViewById(R.id.loading);
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case 1:
                    final ProgressDialog imguploading = new ProgressDialog(this);
                    imguploading.setCancelable(false);
                    Uri selectedImage = data.getData();
                    if(selectedImage!=null) {
                       StorageReference images = FirebaseStorage.getInstance().getReference("Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                       images.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                ImageView dp = (ImageView)findViewById(R.id.profile_pic);
                                imguploading.dismiss();
                                DatabaseReference usersimg =FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgurl");
                                usersimg.setValue(taskSnapshot.getDownloadUrl().toString());
                                //FirebaseDatabase.getInstance().getReference("Imageurl").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(taskSnapshot.getDownloadUrl());
                                Toast.makeText(profileactivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                Picasso.get().load(taskSnapshot.getDownloadUrl()).fit().into(dp);
                                //new getImagefromURL(dp).execute(taskSnapshot.getDownloadUrl().toString());
                                loading.setVisibility(View.INVISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                imguploading.setMessage("updating your pic..");
                                imguploading.show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default: break;
            }
        }
    }
    public void hideKeyboard(EditText et) {
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }
  /*  public class getImagefromURL extends AsyncTask<String,Void,Bitmap>
    {
        ImageView header;
        public getImagefromURL(ImageView headerlayout) {
            this.header = headerlayout;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urlDisplay = url[0];
            bitmap=null;
            try {
                InputStream src = new java.net.URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(src);

            }
            catch (Exception e){
                e.printStackTrace();

            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            header.setImageBitmap(bitmap);

        }
    }*/
}
/*Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);


  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case 1:
                    final ProgressDialog imguploading = new ProgressDialog(this);
                    imguploading.setCancelable(false);
                    Uri selectedImage = data.getData();
                    if(selectedImage!=null) {
                        images.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                imguploading.dismiss();
                                usersimg =FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgurl");
                                usersimg.setValue(taskSnapshot.getDownloadUrl().toString());
                                //FirebaseDatabase.getInstance().getReference("Imageurl").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(taskSnapshot.getDownloadUrl());
                                Toast.makeText(MainActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                Picasso.get().load(taskSnapshot.getDownloadUrl()).transform(new CircularTransform()).resize(90, 90).centerCrop().into(userImage);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                imguploading.setMessage("updating your pic..");
                                imguploading.show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default: break;
            }
        }
    }*/