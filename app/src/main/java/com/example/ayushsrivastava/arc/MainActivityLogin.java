package com.example.ayushsrivastava.arc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.ayushsrivastava.arc.NetworkStateChangeReciever.IS_NETWORK_AVAILABLE;

public class MainActivityLogin extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String Email,networkstatus;
    private Snackbar snackbar,snackbar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        final RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.loginpanel);
        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReciever.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE,false);
                networkstatus =isNetworkAvailable?"connected":"disconnected";
                    if (networkstatus.equals("disconnected")) {
                        snackbar =Snackbar.make(relativeLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE);
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
                        snackbar2 = Snackbar.make(relativeLayout,"Conection Etablished !",Snackbar.LENGTH_LONG);
                        ((TextView)snackbar2.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.GREEN);
                        snackbar2.show();
                    }

                }
        },intentFilter);
        TextView frgtpass = (TextView) findViewById(R.id.frgtpass);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            Intent i;
            i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.passwd);
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
                    if (name.isEmpty()) {
                        user.setError("Please enter your E-mail !");
                        user.requestFocus();
                    } else if (Pass.isEmpty()) {
                        psswd.setError("Enter password !");
                        psswd.requestFocus();
                    } else {
                        progressDialog.setMessage("Signing In....");
                        progressDialog.show();
                        firebaseAuth.signInWithEmailAndPassword(user.getText().toString().trim(), psswd.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                                    psswd.setText(null);
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
        frgtpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = user.getText().toString().trim();
                if (Email.isEmpty()) {
                    user.requestFocus();
                    user.setError("Please Enter your E-mail");
                } else {
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    firebaseAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful())
                                Toast.makeText(MainActivityLogin.this, "Please check "+Email+" and follow the link to reset your password ! ", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(MainActivityLogin.this, "Invalid E-mail !", Toast.LENGTH_SHORT).show();
                        }
                    });
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
