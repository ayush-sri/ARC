package com.example.ayushsrivastava.arc;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class Register extends AppCompatActivity {
    private Button register,update;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progessDialogue;
    private CheckBox chkbk;
    private EditText name,Rollnum,passwd,email;
    FirebaseDatabase database;
    private DatabaseReference ref;

    Firebase users;//password,RollNum,Branchbase,Group,Email,Status;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progessDialogue = new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        database= FirebaseDatabase.getInstance();
        ref =database.getReference();
        chkbk = (CheckBox)findViewById(R.id.ctvReg);
        users = new Firebase("https://arc-artificial-cr-a.firebaseio.com/Users");
       /* Email = new Firebase("https://arc-artificial-cr-a.firebaseio.com/Email");
        password =new Firebase("https://arc-artificial-cr-a.firebaseio.com/Password");
        RollNum = new Firebase("https://arc-artificial-cr-a.firebaseio.com/RollNum");
        Branchbase = new Firebase("https://arc-artificial-cr-a.firebaseio.com/Branch");
        Group = new Firebase("https://arc-artificial-cr-a.firebaseio.com/Group");
        Status = new Firebase("https://arc-artificial-cr-a.firebaseio.com/Status");*/
        //final Button register;
        name = (EditText)findViewById(R.id.name);
        Rollnum=(EditText)findViewById(R.id.rollNum);
        email =(EditText)findViewById(R.id.Email);
        passwd = (EditText)findViewById(R.id.Passwd);
        register = (Button)findViewById(R.id.Register);
        final Button show =(Button)findViewById(R.id.Show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show.getText().equals("SHOW")){
                    show.setText("HIDE");
                    passwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwd.setSelection(passwd.length());
                }
                else if(show.getText().equals("HIDE"))
                {
                    show.setText("SHOW");
                    passwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwd.setSelection(passwd.length());
                }
            }
        });
        hideKeyboard(passwd);
        hideKeyboard(name);
        hideKeyboard(Rollnum);
        hideKeyboard(email);
        final Spinner Branch = (Spinner) findViewById(R.id.branch);
        final Spinner semester = (Spinner) findViewById(R.id.sem);
        final Spinner grp = (Spinner) findViewById(R.id.group);
        final String[] Brnch = {"Branch", "CSE", "ECE", "ME", "CE","EE"};
        final String[] sem = {"Sem","1", "2", "3", "4", "5", "6", "7", "8"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Brnch);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sem);
        Branch.setAdapter(arrayAdapter);
        semester.setAdapter(arrayAdapter2);
                    semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String[] group = {"Group"};
                            ArrayAdapter<String> first;
                            if (semester.getSelectedItem().equals(sem[0])) {
                                first = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, group);
                                grp.setAdapter(first);
                            } else if (semester.getSelectedItem().equals(sem[1])) {
                                group = new String[]{"11", "12", "13", "14", "15", "16"};
                                first = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, group);
                                grp.setAdapter(first);
                            } else if (semester.getSelectedItem().equals(sem[2])) {
                                group = new String[]{"21", "22", "23", "24", "25", "26"};
                                first = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, group);
                                grp.setAdapter(first);
                            } else if (semester.getSelectedItem().equals(sem[3])) {
                                group = new String[]{"31", "32", "33", "34", "35", "36"};
                                first = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, group);
                                grp.setAdapter(first);
                            } else if (semester.getSelectedItem().equals(sem[4])) {
                                group = new String[]{"41", "42", "43", "44", "45", "46"};
                                first = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, group);
                                grp.setAdapter(first);
                            }
                            if (semester.getSelectedItem().equals(sem[5])) {
                                group = new String[]{"51", "52", "53", "54", "55", "56"};
                                first = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, group);
                                grp.setAdapter(first);
                            }
                            if (semester.getSelectedItem().equals(sem[6])) {
                                group = new String[]{"61", "62", "63", "64", "65", "66"};
                                first = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, group);
                                grp.setAdapter(first);
                            }
                            if (semester.getSelectedItem().equals(sem[7])) {
                                group = new String[]{"71", "72", "73", "74", "75", "76"};
                                first = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, group);
                                grp.setAdapter(first);
                            }
                            if (semester.getSelectedItem().equals(sem[8])) {
                                group = new String[]{"81", "82", "83", "84", "85", "86"};
                                first = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, group);
                                grp.setAdapter(first);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    passwd.addTextChangedListener(new TextWatcher() {
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
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
                    register.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            final boolean cr = CheckStatus();
                            final String n = name.getText().toString().trim();
                            final String Pass = passwd.getText().toString().trim();
                            final String Roll = Rollnum.getText().toString().trim();
                            final String em = email.getText().toString().trim();
                            final String sems = semester.getSelectedItem().toString();
                            final String branch = Branch.getSelectedItem().toString();
                            final String group = grp.getSelectedItem().toString();
                            final String imgurl = "default";
                            if(n.isEmpty()) {
                                name.setError("Please enter your username !");
                                name.requestFocus();
                            }
                            else if(Pass.isEmpty()) {
                                passwd.setError("Enter password !");
                                passwd.requestFocus();
                                if (Pass.length() < 8) {
                                    passwd.setError("Password Should be atleast 8 characters long !");
                                    passwd.requestFocus();
                                }

                            }
                            else if(Roll.isEmpty())
                            {
                                Rollnum.setError("Can't left this empty");
                                Rollnum.requestFocus();
                            }
                            else if(em.isEmpty())
                            {
                                email.setError("Can't left this empty");
                                email.requestFocus();
                            }
                            else if(Branch.getSelectedItem().equals(Brnch[0])) {
                                Toast.makeText(Register.this, "Please Select Branch", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else if(semester.getSelectedItem().equals(sem[0]))
                            {
                                Toast.makeText(Register.this, "Please Select Semester & Group", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {

                                if (Roll.length() < 15 ) {
                                    Rollnum.setError("Invalid Roll Number !");
                                    Rollnum.requestFocus();
                                }
                               /* users.push().setValue(name.getText().toString());
                                Email.push().setValue(email.getText().toString());
                                password.push().setValue(passwd.getText().toString());
                                RollNum.push().setValue(Rollnum.getText().toString());
                                Branchbase.push().setValue(Branch.getSelectedItem().toString());
                                Group.push().setValue(grp.getSelectedItem().toString());
                                Status.push().setValue(CheckStatus());*/
                                else {
                                    progessDialogue.show();
                                    progessDialogue.setMessage("Please wait !");
                                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), passwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()) {
                                                Users user = new Users(n,em,group,branch,Roll,Pass,cr,sems,branch+sems,imgurl);
                                                /*user.setUname(n);
                                                user.setuEmail(em);
                                                user.setRollNum(Roll);
                                                user.setUpassword(Pass);
                                                user.setuBranch(branch);
                                                user.setuGroup(group);
                                                user.setSem(sems);
                                                user.setCr(cr);
                                                user.setBranch_sem(branch+sems);
                                                user.setImgurl(imgurl);*/
                                                users.child(firebaseAuth.getUid()).setValue(user);
                                                progessDialogue.dismiss();
                                                Toast.makeText(Register.this, "Registered Successfully !", Toast.LENGTH_LONG).show();
                                                Intent back = new Intent(view.getContext(),MainActivityLogin.class);
                                                startActivity(back);
                                                finish();
                                            }
                                            else
                                            {
                                                progessDialogue.dismiss();
                                                if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                    Toast.makeText(Register.this, "Already Registered !", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                Toast.makeText(Register.this, "Some Error Occurred !", Toast.LENGTH_LONG).show();
                                            }}
                                    });
                                }
                            }


                                /*Intent i;
                                i = new Intent(view.getContext(),MainActivity.class);
                                startActivity(i);
                                finish();*/
                            }

                        });
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
    @Override
    public void onBackPressed()
    {

        new AlertDialog.Builder(this)
                .setTitle("Leave")
                .setMessage("Are you sure you want to leave? ")
                .setNegativeButton(android.R.string.no,null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Register.super.onBackPressed();
                        Intent ii;
                        ii = new Intent(Register.this,MainActivityLogin.class);
                        startActivity(ii);
                    }
                }).create().show();
    }
    Boolean CheckStatus()
    {
        if(chkbk.isChecked())
          return true;
        else return false;
    }


}

