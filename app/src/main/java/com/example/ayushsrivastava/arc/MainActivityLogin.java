package com.example.ayushsrivastava.arc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityLogin extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            Intent i;
            i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        RelativeLayout ll1 = (RelativeLayout)findViewById(R.id.ll1);
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.passwd);
        TextView frgtpsswd = (TextView) findViewById(R.id.frgt);
        Button lgn = (Button) findViewById(R.id.login);
        Button reg = (Button) findViewById(R.id.reg);
        final CheckBox cr = (CheckBox) findViewById(R.id.ctv);
        username.setCursorVisible(false);
        hideKeyboard(username);
        hideKeyboard(password);
        final EditText psswd = password;
        final EditText user = username;
        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0)
                {
                    if(user.isInTouchMode())
                        user.setCursorVisible(true);
                    user.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                }
                else
                {
                    if(user.isInTouchMode())
                        user.setCursorVisible(true);
                    user.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        psswd.addTextChangedListener(new TextWatcher() {
                        @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    psswd.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                } else {
                    psswd.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String name = user.getText().toString().trim();
                String Pass = psswd.getText().toString().trim();
                if(name.isEmpty()) {
                    user.setError("Please enter your username or email !");
                     user.requestFocus();
                }
                else if(Pass.isEmpty()) {
                    psswd.setError("Enter password !");
                    psswd.requestFocus();
                }
                else
                {
                    progressDialog.setMessage("Signing In....");
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(user.getText().toString().trim(),psswd.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent i;
                                i = new Intent(view.getContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivityLogin.this, "Invalid Username Or password !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(view.getContext(),Register.class);
                startActivity(i);
                finish();
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
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit? ")
                .setNegativeButton(android.R.string.no,null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivityLogin.super.onBackPressed();
                        finish();
                    }
                }).create().show();
    }

}
