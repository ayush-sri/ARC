package com.example.ayushsrivastava.arc;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.xml.transform.sax.SAXResult;

/**
 * Created by Ayush Srivastava on 1/25/2018.
 */

public class dataFragment extends android.support.v4.app.Fragment {
    View view;
    ViewPager viewPager;
    TabLayout tablayout;
    String branch,sem,subo;
    String sub [];
    int pos;
    private DatabaseReference subj;
    DatabaseReference subjects;
    @Override
    public void onStart() {
        super.onStart();
        sub = new String[6];
       final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbusers.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    Users user = dataSnapshot.getValue(Users.class);
                        branch = user.getuBranch();
                        sem = user.getSem();
                            subj = FirebaseDatabase.getInstance().getReference("Subjects").child(branch).child(sem);
                ;          subjects = FirebaseDatabase.getInstance().getReference("tabloc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        subj.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (int i = 1; i <= 6; i++) {
                                        if (dataSnapshot.hasChild(String.valueOf(i)))
                                            sub[i - 1] = dataSnapshot.child(String.valueOf(i)).getValue(String.class);
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


        /*ValueEventListener valueEventListener = subj.addValueEventListener(new ValueEventListener() {
            int i = 1;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    sub[i] = dt.child(String.valueOf(i)).getValue().toString();
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });*/
    }






    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
               /* sub.addValueEventListener(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                      String sub=dataSnapshot.getValue(String.class);
                        Toast.makeText(MainActivity.this, sub , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
        /*final DatabaseReference subjects = FirebaseDatabase.getInstance().getReference("Subjects");
        DatabaseReference usersem = FirebaseDatabase.getInstance().getReference("Users");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot usersem : dataSnapshot.getChildren()) {
                    Users user = usersem.getValue(Users.class);
                    if (firebaseAuth.getCurrentUser().getEmail().equals(user.getuEmail())) {
                        Toast.makeText(getActivity(), user.getSem(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),user.getuBranch(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

                                       @Override
                                       public void onCancelled(DatabaseError databaseError) {

                                       }
                                   });*/

        view = inflater.inflate(R.layout.sample,container,false);
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new sliderAdapter(getChildFragmentManager()));
        tablayout = (TabLayout)view.findViewById(R.id.sliding_tabs);
        /*subj.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 1; i <= 6; i++) {
                    if (dataSnapshot.hasChild(String.valueOf(i))) {
                        sub[i - 1] = dataSnapshot.child(String.valueOf(i)).getValue(String.class);
                        tablayout.addTab(tablayout.getTabAt(i-1).setText(sub[i-1]));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
       tablayout.post(new Runnable() {
            @Override
            public void run() {
        DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbusers.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                branch = user.getuBranch();
                sem = user.getSem();
                subj = FirebaseDatabase.getInstance().getReference("Subjects").child(branch).child(sem);
                subj.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int i = 1; i <= 6; i++) {
                            if (dataSnapshot.hasChild(String.valueOf(i))){
                                sub[i - 1] = dataSnapshot.child(String.valueOf(i)).getValue(String.class);
                                TabLayout.Tab tab;
                                    tab = tablayout.getTabAt(i-1);
                                    tab.setText(sub[i-1]);
                        }}
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

        /*for(int i=0;i<=5;i++)
        {
        }*/     tablayout.setupWithViewPager(viewPager);
                tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab){
                        subjects = FirebaseDatabase.getInstance().getReference("tabloc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        subjects.setValue(String.valueOf(tab.getPosition()+1));
                        /*DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dbusers.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                            @Override
                            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                Users user = dataSnapshot.getValue(Users.class);
                                branch = user.getuBranch();
                                sem = user.getSem();
                                subj = FirebaseDatabase.getInstance().getReference("Subjects").child(branch).child(sem);
                                subjects = FirebaseDatabase.getInstance().getReference("tabloc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                subjects = FirebaseDatabase.getInstance().getReference("tabloc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                subj.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (int i = 1; i <= 6; i++) {
                                            if (dataSnapshot.hasChild(String.valueOf(i))){
                                                sub[i - 1] = dataSnapshot.child(String.valueOf(i)).getValue(String.class);
                                                TabLayout.Tab tab;
                                                tab = tablayout.getTabAt(i-1);
                                                tab.setText(sub[i-1]);
                                            }}
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/
                        viewPager.setCurrentItem(tab.getPosition(),true);

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });
            }
        });


        return view;
    }

  /* private void ChangeName(TabLayout.Tab tab) {
       for (int i = 0; i < 6; i++) {
           tab = tablayout.getTabAt(i);
           tab.setText(sub[i]);
       }*/
   //}

    public static class sliderAdapter extends FragmentStatePagerAdapter {

        //String title[]={"tab1","tab2","tab3","tab4","tab5","tab6"};
        //@Override
        //public CharSequence getPageTitle( final int position) {
            /*DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            dbusers.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    title = new String[6];
                    Users user = dataSnapshot.getValue(Users.class);
                    String branch = user.getuBranch();
                    String sem = user.getSem();
                    subj = FirebaseDatabase.getInstance().getReference("Subjects").child(branch).child(sem);
                    subj.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                          *//* switch (position){
                               case 0: title[0]=dataSnapshot.child(String.valueOf(position+1)).getValue(String.class);break;
                               case 1: title[1]=dataSnapshot.child(String.valueOf(position+1)).getValue(String.class);break;
                               case 2: title[2]=dataSnapshot.child(String.valueOf(position+1)).getValue(String.class);break;
                               case 3: title[3]=dataSnapshot.child(String.valueOf(position+1)).getValue(String.class);break;
                               case 4: title[4]=dataSnapshot.child(String.valueOf(position+1)).getValue(String.class);break;
                               case 5: title[5]=dataSnapshot.child(String.valueOf(position+1)).getValue(String.class);break;
                               default:break;


                           }
*//*                             for (int i = 0; i <= 5; i++) {
                                if (dataSnapshot.hasChild(String.valueOf(i)))
                                     title[i]= dataSnapshot.child(String.valueOf(i+1)).getValue(String.class);
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
            });*/

           // return title[position];
        //}

        public sliderAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return new ContentFragment();
        }

        @Override
        public int getCount() {
            return 6;
        }

    }
}
