package com.example.ayushsrivastava.arc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.isSrgb;
import static android.graphics.Color.red;
import static android.graphics.Color.rgb;
import static android.graphics.Color.valueOf;

public class MainActivityLogin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
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
            public void onClick(View view) {
                /*String name = user.getText().toString().trim();
                String Pass = psswd.getText().toString().trim();
                if(name.isEmpty()) {
                    user.setError("Please enter your username or email !");
                     user.requestFocus();
                }
                else if(Pass.isEmpty()) {
                    psswd.setError("Enter password !");
                    psswd.requestFocus();
                }*/
                //else
                //{
                    Intent i;
                    i = new Intent(view.getContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                //}
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
