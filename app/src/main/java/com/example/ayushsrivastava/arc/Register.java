package com.example.ayushsrivastava.arc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent i = getIntent();
        final EditText name = (EditText)findViewById(R.id.name);
        final EditText Rollnum=(EditText)findViewById(R.id.rollNum);
        final EditText email =(EditText)findViewById(R.id.email);
        final EditText passwd = (EditText)findViewById(R.id.passwd);
        final Button register = (Button)findViewById(R.id.register);
        hideKeyboard(passwd);
        hideKeyboard(name);
        hideKeyboard(Rollnum);
        hideKeyboard(email);
        final Spinner Branch = (Spinner) findViewById(R.id.branch);
        final Spinner semester = (Spinner) findViewById(R.id.sem);
        final Spinner grp = (Spinner) findViewById(R.id.group);
        final String[] Brnch = {"Branch", "CSE", "ECE", "Mech", "Civil"};
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
                    register.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String n = name.getText().toString().trim();
                            String Pass = passwd.getText().toString().trim();
                            String Roll = Rollnum.getText().toString().trim();
                            String em = email.getText().toString().trim();
                            if(n.isEmpty()) {
                                name.setError("Please enter your username !");
                                name.requestFocus();
                            }
                            else if(Pass.isEmpty()) {
                                passwd.setError("Enter password !");
                                passwd.requestFocus();
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
                            else
                            {
                                Intent i;
                                i = new Intent(view.getContext(),MainActivity.class);
                                startActivity(i);
                                finish();
                            }

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


}

