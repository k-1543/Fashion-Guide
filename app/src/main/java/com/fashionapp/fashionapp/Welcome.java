package com.fashionapp.fashionapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Welcome extends AppCompatActivity {

    private Button next1;
    private EditText email;
    public String iduser;
    //ImageView img;

    DatabaseReference Db = FirebaseDatabase.getInstance().getReference();

    private static final String TAG = "Welcome";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        isServices();
        this.setTitle("Welcome");
        next1 = findViewById(R.id.next1);
        email = findViewById(R.id.email);
        //img = findViewById(R.id.imageView);
        next1.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                Welcome.this.setTitle("Checking...");
                next1.setClickable(false);
                check();
            }
        });
    }
    public void isServices()
    {
        Log.d(TAG, "Checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Welcome.this);
        if(available == ConnectionResult.SUCCESS)
        {
            Log.d(TAG, "Google services is OK");

        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG, "Google services is fixable");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Welcome.this, available, ERROR_DIALOG_REQUEST);
        }
        else
        {
            Log.d(TAG, "Map requests unavailable");
        }
    }

    private void check()
    {
        final String email1 = email.getText().toString();
        if (!TextUtils.isEmpty(email1) && Patterns.EMAIL_ADDRESS.matcher(email1).matches())
        {
            Db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    int flag = 0;
                    if (dataSnapshot.child("credentials").exists())
                    {
                        for (DataSnapshot em : dataSnapshot.child("credentials").getChildren())
                        {
                            DbCheck db = (em.getValue(DbCheck.class));
                            if (email1.equals(db.emailid))
                            {
                                flag = 1;
                                iduser = db.id;
                                break;
                            }
                        }
                        if (flag == 1)
                        {
                            login();
                        }
                        else
                        {
                            signup();
                        }
                    }
                    else
                    {
                        signup();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }
        else
        {
            Toast.makeText(this, "Please enter a valid Email ID", Toast.LENGTH_LONG).show();
            Welcome.this.setTitle("Welcome");
            //img.setVisibility(View.INVISIBLE);
            next1.setClickable(true);
        }

    }

    private void signup()
    {
        Toast.makeText(this, "User does not exist",Toast.LENGTH_LONG).show();
        email = (EditText) findViewById(R.id.email);
        final String em = email.getText().toString();
        Intent intent = new Intent(this, SignUp.class);
        if(!TextUtils.isEmpty(em))
        {
            intent.putExtra("email", em);
            intent.putExtra("flag", "1");
        }
        else
        {
            intent.putExtra("flag", "0");
        }
        startActivity(intent);
        finish();
    }

    private void login()
    {
        Toast.makeText(this, "User exists",Toast.LENGTH_LONG).show();
        email = (EditText) findViewById(R.id.email);
        final String em = email.getText().toString();
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("email", em);
        intent.putExtra("iduser", iduser);
        //if(mainflag==0)
        //{
        //    mainflag = 1;
            startActivity(intent);
        //}
        finish();
    }
}
