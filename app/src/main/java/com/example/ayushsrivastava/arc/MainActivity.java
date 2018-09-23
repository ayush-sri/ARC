package com.example.ayushsrivastava.arc;

import android.*;
import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.ayushsrivastava.arc.NetworkStateChangeReciever.IS_NETWORK_AVAILABLE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private TextView username,emailadd,comp;
    private String networkstatus;
    public String usr,unt,subn,cat;
    private Snackbar snackbar,snackbar2;
    private FrameLayout Layout;
    private ImageView userImage;
    Boolean cr;
    Double total;
    FirebaseAuth firebaseAuth;
    private Spinner Category,Unit;
    private EditText subject,completed,due;
    String sem,branch;
    private View view;
    Boolean stat;
    ProgressDialog progressDialog;
    DatabaseReference uploadRef,subj;
    double val;
    Bitmap bitmap;
    private ProgressDialog dialog;
    private static final int GALLERY_INTENT=234;
    private StorageReference storageReference,images;
    DatabaseReference usersimg,tabloc;
    LinearLayout bg;
    View headerView;
    @Override
    protected void onStart() {
        super.onStart();
        //final Firebase sub = new Firebase("https://arc-artificial-cr-a.firebaseio.com/Subjects/ME/1/SUB 1");
        final DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbusers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                if (user != null) {
                    tabloc = FirebaseDatabase.getInstance().getReference("tabloc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    tabloc.setValue("1");
                    usr = user.getUname();
                    username.setText(usr);
                    cr = user.getCr();
                    if (!user.getImgurl().equals("default")){
                       Picasso.get().load(user.getImgurl()).transform(new CircularTransform()).resize(90, 90).centerCrop().into(userImage);
                        new getImagefromURL(bg).execute(user.getImgurl());
                        /*Picasso.get().load(user.getImgurl()).into(new Target() {
                           @Override
                           public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                               blur(bitmap,bg);
                               //bg.setBackground(new BitmapDrawable(getResources(),bitmap));
                           }

                           @Override
                           public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                           }

                           @Override
                           public void onPrepareLoad(Drawable placeHolderDrawable) {

                           }
                       });*/
                    }
                       progressDialog.dismiss();
            }}
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 8:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "That does it !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "You can allow it manually in settings", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this,ArcNotificationService.class));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Layout =(FrameLayout) findViewById(R.id.content_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("just a sec !....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbusers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                if (user != null) {
                    usr = user.getUname();
                    username.setText(usr);
                    cr = user.getCr();
                    if (!user.getImgurl().equals("default")) {
                        Picasso.get().load(user.getImgurl()).transform(new CircularTransform()).fit().into(userImage);
                        new getImagefromURL(bg).execute(user.getImgurl());
                       /* Picasso.get().load(user.getImgurl()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                blur(bitmap,bg);
                                //bg.setBackground(new BitmapDrawable(getResources(),bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });*/

                    }progressDialog.dismiss();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*DatabaseReference subj = FirebaseDatabase.getInstance().getReference("1");
        subj.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
           Subject subject = dataSnapshot.getValue(Subject.class);
                Toast.makeText(MainActivity.this, subject.getSUB6(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},8);
            }
        }
        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReciever.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE,false);
                networkstatus =isNetworkAvailable?"connected":"disconnected";
                if (networkstatus.equals("disconnected") ) {
                    //Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                    snackbar =Snackbar.make(Layout, "No internet connection", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();/*setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(networkstatus.equals("connected"))
                                {
                                    snackbar.dismiss();
                                    snackbar2 = Snackbar.make(relativeLayout,"Conection Etablished !",Snackbar.LENGTH_LONG);
                                    ((TextView)snackbar2.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.GREEN);
                                    snackbar2.show();
                                }
                                else if (networkstatus.equals("disconnected"))
                                    Toast.makeText(getApplicationContext(), "Please check your network !", Toast.LENGTH_SHORT).show();
                            }
                        }).setActionTextColor(Color.GREEN).show();*/
                }
                else if(networkstatus.equals("connected"))
                {
                    snackbar2 = Snackbar.make(Layout,"Connection Established !",Snackbar.LENGTH_LONG);
                    ((TextView)snackbar2.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.GREEN);
                    snackbar2.show();
                }

            }
        },intentFilter);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(this,MainActivityLogin.class);
            startActivity(intent);
            finish();
        }
        //Snackbar.make(v,"No internet connection !",Snackbar.LENGTH_INDEFINITE).setAction("Retry",null).show();
        //Toast.makeText(this, "Hello, Toast.LENGTH_SHORT).show();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        username =(TextView)headerView.findViewById(R.id.username);
        userImage =(ImageView)headerView.findViewById(R.id.imageView);
        bg = (LinearLayout)headerView.findViewById(R.id.bgimage);
        emailadd = (TextView)headerView.findViewById(R.id.emailPro);
        emailadd.setText(firebaseAuth.getCurrentUser().getEmail().toString());
       /* usersimg.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.getValue(String.class).equals("default"))
                    Picasso.get().load(dataSnapshot.getValue(String.class)).transform(new CircularTransform()).resize(90,90).centerCrop().into(userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MainActivity.this,profileactivity.class);
                startActivity(profileIntent);
            }
        });
        uploadRef = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState!=null) {
            savedInstanceState.getString("dateString");
        }
        setFragment();//init

    }



               /* sub.addValueEventListener(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                      String sub=dataSnapshot.getValue(String.class);
                        Toast.makeText(MainActivity.this, sub , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });*/

    /*private void ReitrieveUser(Firebase mRef, final String mail) {
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if( fragment instanceof dataFragment) {
            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to Exit? ")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.super.onBackPressed();
                            finish();
                        }
                    }).create().show();
        }
        else{
            setFragment();

        }
    }
        /*new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to Exit? ")
                .setNegativeButton(android.R.string.no,null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                        finish();
                    }
                }).create().show();*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.Notification:
                if(cr) {
                   openNotificationDialogue();
                }
                else
                    Toast.makeText(this,"Only CR can use this option !", Toast.LENGTH_SHORT).show();
                break;
            case   R.id.subject :
                if(cr) {
                    openDialogue();
                    break;
                }
                else
                    Toast.makeText(this,"Only CR can use this option !", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.topic :
                if(cr) {
                    openTopicDialogue();
                    break;
                }
                else
                    Toast.makeText(this,"Only CR can use this option !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.timeTable:
                if(cr){
                    getImage();
                }
                else
                    Toast.makeText(this, "Only CR can use this option !", Toast.LENGTH_SHORT).show();
                break;
            case   R.id.abt :
                Intent aboutintent = new Intent(this,about.class);
                startActivity(aboutintent);
                break;
            case R.id.syllabus:
                if(cr)
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(Intent.createChooser(intent, "Pick a File"), 5);
                }
                else
                    Toast.makeText(this, "Only CR can use this option !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.marks:
                inflateEntryDialog();
                break;
            case R.id.update:
                if(cr) {
                    new AlertDialog.Builder(this)
                            .setTitle("Sure?")
                            .setMessage("All user sems will be updated sure to continue ? ")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final ProgressDialog updateprogress = new ProgressDialog(MainActivity.this);
                                    updateprogress.setCancelable(false);
                                    updateprogress.setMessage("updating sem !");
                                    updateprogress.show();
                                    final DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    userref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            final Users userthis = dataSnapshot.getValue(Users.class);
                                            String val = userthis.getSem();
                                            final int newval = Integer.parseInt(val) + 1;
                                            FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                                        Users user = dt.getValue(Users.class);
                                                        if (user.getuGroup().equals(userthis.getuGroup()) && user.getSem().equals(userthis.getSem()) && user.getBranch_sem().equals(userthis.getBranch_sem()) && user.getuBranch().equals(userthis.getuBranch())) {
                                                            FirebaseDatabase.getInstance().getReference("Users").child(dt.getKey()).child("sem").setValue(String.valueOf(newval)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    userref.child("sem").setValue(String.valueOf(newval));
                                                                    userref.child("branch_sem").setValue(userthis.getuBranch()+String.valueOf(newval));
                                                                    userref.child("uGroup").setValue(String.valueOf(Integer.parseInt(userthis.getuGroup())+10));
                                                                    updateprogress.dismiss();
                                                                    if(task.isSuccessful()) Toast.makeText(MainActivity.this, "Sem updated to "+String.valueOf(newval), Toast.LENGTH_SHORT).show();
                                                                    else
                                                                        Toast.makeText(MainActivity.this, "Something went wrong try again later !", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            FirebaseDatabase.getInstance().getReference("Users").child(dt.getKey()).child("branch_sem").setValue(userthis.getuBranch()+String.valueOf(newval));
                                                            FirebaseDatabase.getInstance().getReference("Users").child(dt.getKey()).child("uGroup").setValue(String.valueOf(Integer.parseInt(user.getuGroup())+10));
                                                        }

                                                    }

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
                            }).create().show();
                }
                else Toast.makeText(this, "Only CR can use this option", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                Intent profile = new Intent(this,profileactivity.class);
                startActivity(profile);
                break;
                default: break;
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    private void inflateEntryDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View ItemView = inflater.inflate(R.layout.marksfrag,null);
        final DatabaseReference subject_ref = FirebaseDatabase.getInstance().getReference("Subjects");
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final Spinner subject= (Spinner)ItemView.findViewById(R.id.subject_spin);
        final EditText marks = (EditText)ItemView.findViewById(R.id.marks_edit);
        final Spinner category = (Spinner)ItemView.findViewById(R.id.subject_category);
        final String subject_string [ ] = new String[7];
        subject_string[0]="Subject";
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Users user = dataSnapshot.getValue(Users.class);
                for(int i=1;i<=6;i++){
                    final int finali = i;
                    subject_ref.child(user.getuBranch()).child(user.getSem()).child(String.valueOf(finali)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            subject_string[finali] = dataSnapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String [] subject_category = {"Category","UT1","UT2","UT3","UT4","PS1","PS2","PS3","PS4","ST1","ST2","ST3","ST4","TA"};
        ArrayAdapter<String>category_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,subject_category);
        ArrayAdapter<String>subject_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,subject_string);
        subject.setAdapter(subject_adapter);
        category.setAdapter(category_adapter);
        builder.setView(ItemView);
        builder.setTitle("Marks").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String val_string;
                if(!subject.getSelectedItem().toString().equals("Subject")&&!category.getSelectedItem().toString().equals("Category")) {
                    if (!marks.getText().toString().isEmpty()) {
                        switch(category.getSelectedItem().toString()){
                            case "UT1":
                                val_string = marks.getText().toString();
                                val =  Double.parseDouble(val_string)/4;
                                break;

                            case "UT2":
                                val_string = marks.getText().toString();
                                val =  Double.parseDouble(val_string)/4;
                                break;
                            case "UT3":
                                val_string = marks.getText().toString();
                                val =  Double.parseDouble(val_string)/4;
                                break;

                            case "UT4":
                                val_string = marks.getText().toString();
                                val =  Double.parseDouble(val_string)/4;
                                break;

                            case "PS1":
                                val_string = marks.getText().toString();
                                val =  Double.parseDouble(val_string)/16.66;
                                break;

                            case "PS2":
                                val_string = marks.getText().toString();
                                val =  Double.parseDouble(val_string)/16.66;
                                break;

                            case "PS3":
                                val_string = marks.getText().toString();
                                val =  Double.parseDouble(val_string)/16.66;
                                break;

                            case "PS4":
                                val_string = marks.getText().toString();
                                val = Double.parseDouble(val_string)/16.66;
                                break;

                            case "ST1":
                                val_string = marks.getText().toString();
                                val = Double.parseDouble(val_string)/5;
                                break;
                            case "ST2":
                                val_string = marks.getText().toString();
                                val = Double.parseDouble(val_string)/5;
                                break;
                            case "ST3":
                                val_string = marks.getText().toString();
                                val = Double.parseDouble(val_string)/5;
                                break;
                            case "ST4":
                                val_string = marks.getText().toString();
                                val = Double.parseDouble(val_string)/5;
                                break;
                            case "TA":
                                val_string = marks.getText().toString();
                                val = Double.parseDouble(val_string)/2.5;
                                break;
                            default:break;
                        }

                        DatabaseReference marks  = FirebaseDatabase.getInstance().getReference("progressreport")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(subject.getSelectedItem().toString())
                                .child("marks")
                                .child(category.getSelectedItem().toString());
                        marks.setValue(String.valueOf(new DecimalFormat("##.##").format(val))).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Toast.makeText(MainActivity.this, "Done !", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(MainActivity.this, "something went wrong try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
                        total=0.00;
                       FirebaseDatabase.getInstance().getReference("progressreport")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(subject.getSelectedItem().toString())
                                .child("marks").addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                   total = total + Double.parseDouble(dt.getValue(String.class));
                               }
                               FirebaseDatabase.getInstance().getReference("progressreport")
                                       .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                       .child(subject.getSelectedItem().toString())
                                       .child("Total").setValue(String.valueOf(total));
                           }

                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });
                    } else
                        Toast.makeText(MainActivity.this, "Cancelled,Nothing entered !", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(MainActivity.this, "Please select subject and category !", Toast.LENGTH_SHORT).show();

            }
        });
        builder.create().show();


    }

    private void getImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 4);
    }

    private void openNotificationDialogue() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        view = inflater.inflate(R.layout.notification,null);
        final EditText notification = (EditText)view.findViewById(R.id.notice);
        builder.setView(view);
        builder.setTitle("Broadcast")
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference userbranchsem = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("branch_sem");
                userbranchsem.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DatabaseReference notify = FirebaseDatabase.getInstance().getReference("Notify").child("broadcasts").child(dataSnapshot.getValue(String.class)).child("broadcast");
                        if(!notification.getText().toString().isEmpty())
                        notify.setValue(notification.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful())
                                    Toast.makeText(MainActivity.this, "Something went wrong , try again later", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(MainActivity.this, "Sent !", Toast.LENGTH_SHORT).show();
                            }
                        });
                        else Toast.makeText(MainActivity.this, "You should enter something to broadcast !", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.create().show();


    }

    private Fragment fragment=null;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (id == R.id.nav_Home){
            setTitle("ARC : Overview");
            fragment = new dataFragment();
            FragmentManager frt=getSupportFragmentManager();
            frt.beginTransaction().replace(R.id.content_main,new dataFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_uploads) {
            fragment = new uploadsFrag();
            setTitle("Uploads by CR");
            FragmentManager frt=getSupportFragmentManager();
            frt.beginTransaction().replace(R.id.content_main,new uploadsFrag()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_downloads) {
            setTitle("Your Downloads");
            fragment = new downloadsFrag();
            FragmentManager frt=getSupportFragmentManager();
            frt.beginTransaction().replace(R.id.content_main,new downloadsFrag()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_user) {
            setTitle("Users");
            fragment = new contact();
            FragmentManager frt=getSupportFragmentManager();
            frt.beginTransaction().replace(R.id.content_main,new contact()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_time) {
            fragment = new TimeTable();
            setTitle("Broadcasts & Extras");
            FragmentManager frt=getSupportFragmentManager();
            frt.beginTransaction().replace(R.id.content_main,new TimeTable()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_share) {
            Intent i =new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            String sharebody = "Hello engineers ! check out this awesome app ,ARC : stands for artificial reperesentative of class,where all the stuff which sometimes we miss are collected at one place i.e, class notes,syllabus,important notices,and much more.All the stuff is uploaded by the respective class representative of the class. Just register and forget about loosing your notes or stuff !";
            //i.putExtra(Intent.EXTRA_SUBJECT,sharebody);
            i.putExtra(Intent.EXTRA_TEXT,sharebody);
            startActivity(Intent.createChooser(i,"Share Using :"));
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(id == R.id.nav_logout)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to Logout ? ")
                    .setNegativeButton(android.R.string.no,null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            firebaseAuth.signOut();
                            Intent in = new Intent(getApplicationContext(),MainActivityLogin.class);
                            startActivity(in);
                            finish();
                        }
                    }).create().show();

        }
        return true;
    }
    public void setFragment()
    {
        fragment = new dataFragment();
        setTitle("Arc : Overview");
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_Home);
        if(fragment!=null) {
            FragmentTransaction frt = getSupportFragmentManager().beginTransaction();
            FragmentTransaction replace = frt.replace(R.id.content_main, fragment);
            replace.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    public void openDialogue() {
        editContentAlert alert = new editContentAlert();
        alert.show(getSupportFragmentManager(), "alert");
    }

    private void openTopicDialogue() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        view = inflater.inflate(R.layout.uploadcontent,null);
        Category = (Spinner)view.findViewById(R.id.Category);
        Unit = (Spinner)view.findViewById(R.id.Unit);
        String [] category ={"Select Category","Books","Notes","Problem Sets : Solved","Problem Sets : Unsolved","Unit Test Papers"};
        String [] unit ={"Select Unit","Unit 1","Unit 2","Unit 3","Unit 4","Unit 5"};
        dialog = new ProgressDialog(view.getContext());
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_dropdown_item_1line,category);
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_dropdown_item_1line,unit);
        Category.setAdapter(categoryAdapter);
        Unit.setAdapter(unitAdapter);
        subject=(EditText)view.findViewById(R.id.subjectpref);
        builder.setView(view);
        builder.setTitle("ARC");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseAuth = FirebaseAuth.getInstance();
                if(Category.getSelectedItem().toString().equals("Select Category") || Unit.getSelectedItem().toString().equals("Select Unit") || subject.getText().toString().isEmpty() )
                    Toast.makeText(MainActivity.this, "please Select the required references !", Toast.LENGTH_SHORT).show();
                else {
                    DatabaseReference subref = FirebaseDatabase.getInstance().getReference("Users");
                    subn = subject.getText().toString();
                    unt = Unit.getSelectedItem().toString();
                    cat = Category.getSelectedItem().toString();
                    subref.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Users users = dataSnapshot.getValue(Users.class);
                            branch = users.getuBranch();
                            sem = users.getSem();
                            subj = FirebaseDatabase.getInstance().getReference("Subjects").child(branch).child(sem);
                            subj.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    stat=false;
                                    for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                        if (dt.getValue(String.class).equals(subject.getText().toString()))
                                            stat = true;

                                    }
                                    if (stat) {
                                        storageReference = FirebaseStorage.getInstance().getReference().child(branch).child(sem).child(subn).child(cat).child(unt + ".pdf");
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("application/pdf");
                                        startActivityForResult(Intent.createChooser(intent, "Pick a File"), GALLERY_INTENT);
                                    }
                                    else if(!stat){
                                        Toast.makeText(MainActivity.this, "Enter a valid subject", Toast.LENGTH_SHORT).show();
                                    }
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
                        /*storageReference = FirebaseStorage.getInstance().getReference();
                        reference=storageReference.child(branch).child(sem).child(subn).child(cat).child(unt+".pdf");
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        startActivityForResult(Intent.createChooser(intent,"Pick a File"),GALLERY_INTENT);*/
                }
            }

        });
        builder.setCancelable(false);
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case GALLERY_INTENT :
                    final Uri uri = data.getData();
                    if (uri != null) {
                        dialog.setCancelable(false);
                        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                dialog.dismiss();
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf =new SimpleDateFormat("dd/MMM/yyyy,hh:mm a");
                                final String dateString =sdf.format(date);
                                final String dt = "uploaded on "+dateString;
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Users users = dataSnapshot.getValue(Users.class);
                                        CreateList(unt,taskSnapshot.getDownloadUrl().toString(),dt,subject.getText().toString(),cat,users.getBranch_sem(),dateString);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                Toast.makeText(MainActivity.this, "Uploaded Successfully !", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Something went wrong please try again ! possible cause : turn on storage in settings > permissions", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                dialog.setMessage(((int)progress)+"% uploaded");
                                dialog.show();
                            }
                        });
                    }
                    break;
                case 4:
                    final ProgressDialog imguploading = new ProgressDialog(this);
                    imguploading.setCancelable(false);
                    final Uri selectedImage = data.getData();
                    if(selectedImage!=null) {
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                Users user = dataSnapshot.getValue(Users.class);
                                StorageReference images = FirebaseStorage.getInstance().getReference("Timetable").child(user.getBranch_sem()).child(user.getuGroup());
                                images.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                        imguploading.dismiss();//FirebaseDatabase.getInstance().getReference("Imageurl").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(taskSnapshot.getDownloadUrl());
                                        Toast.makeText(MainActivity.this, "time table added !", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        imguploading.setMessage("Adding Time Table..");
                                        imguploading.show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else{
                        Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    final StorageReference Syllabusreference = FirebaseStorage.getInstance().getReference("Syllabus");
                    final Uri syllabusuri = data.getData();
                    if (syllabusuri != null) {
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final ProgressDialog syllabusdiag = new ProgressDialog(MainActivity.this);
                                Users users = dataSnapshot.getValue(Users.class);
                                Syllabusreference.child(users.getBranch_sem()).putFile(syllabusuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                        syllabusdiag.dismiss();
                                        Toast.makeText(MainActivity.this, "Syllabus Added !", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        syllabusdiag.dismiss();
                                        Toast.makeText(MainActivity.this, "Something went wrong please try again ! possible cause : turn on storage in settings > permissions", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        syllabusdiag.setMessage("Adding Syllabus....");
                                        syllabusdiag.show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                    break;
                default: break;
            }
        }
    }

    private void CreateList(final String unit, String taskSnapshot, String dt, final String subject, final String cat, String branch_sem, String date) {
        uploadContent content = new uploadContent(unit,taskSnapshot,dt,subject,cat,branch_sem);
        DatabaseReference Notify = FirebaseDatabase.getInstance().getReference("Notify").child("uploads").child(branch_sem).child("upload");
        Notify.setValue(subject+" "+cat+","+unit+" check your uploads !");
        uploadRef.child(date.replace("/","").trim()).setValue(content);
    }
    private void blur(Bitmap bitmap, LinearLayout headerView) {
        float radius=20;
        Bitmap overlay = Bitmap.createBitmap((int)(headerView.getMeasuredWidth()),(int)(headerView.getMeasuredHeight()),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-headerView.getLeft(),-headerView.getTop());
        /*canvas.scale(1/scalefactor,1/scalefactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        */canvas.drawBitmap(bitmap,0,0,null);//-80,-200
        RenderScript rs = RenderScript.create(headerView.getContext());
        Allocation overlayAlloc = Allocation.createFromBitmap(rs,overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs,overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        headerView.setBackground(new BitmapDrawable(getResources(),overlay));
        rs.destroy();
    }
   private static class CircularTransform implements Transformation {

       @Override
       public Bitmap transform(Bitmap source) {
           int size = Math.min(source.getWidth(), source.getHeight());

           int x = (source.getWidth() - size) / 2;
           int y = (source.getHeight() - size) / 2;

           Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
           if (squaredBitmap != source) {
               source.recycle();
           }

           Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

           Canvas canvas = new Canvas(bitmap);
           Paint paint = new Paint();
           BitmapShader shader = new BitmapShader(squaredBitmap,
                   BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
           paint.setShader(shader);
           paint.setAntiAlias(true);

           float r = size / 2f;
           canvas.drawCircle(r, r, r, paint);

           squaredBitmap.recycle();
           return bitmap;
       }

       @Override
       public String key() {
           return "circle";
       }
   }
   public class getImagefromURL extends AsyncTask<String,Void,Bitmap>
   {
       LinearLayout header;
       public getImagefromURL(LinearLayout headerlayout) {
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
           blur(bitmap,header);
       }
   }
}

