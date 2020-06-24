package com.fashionapp.fashionapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    public static String value1, value2, pwd;
    private static final String TAG = "Welcome";
    public EditText pass;
    Button next1;
    public TextView t1;
    DatabaseReference Db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: Login");
        //getActionBar().setTitle("Login");
        next1 = findViewById(R.id.next1);

        Bundle bundle = getIntent().getExtras();
        value2 = bundle.getString("email");

        t1 = findViewById(R.id.textView2);
        t1.setText(value2);

        next1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                next1.setClickable(false);
                Login.this.setTitle("Logging in...");
                input();
            }
        });
    }

    private void input()
    {
        Db = FirebaseDatabase.getInstance().getReference();
        pass = findViewById(R.id.pwd);
        pwd = pass.getText().toString().trim();
        Bundle bundle = getIntent().getExtras();
        value1 = bundle.getString("iduser");
        if(TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2))
        {
            Toast.makeText(this, "Something went wrong and its your fault", Toast.LENGTH_SHORT).show();
        }
        else if (!TextUtils.isEmpty(pwd) && isValidPassword(pwd))
        {
            Db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    int flag = 0;

                        for(DataSnapshot em : dataSnapshot.child("credentials").getChildren())
                        {
                            DbCheck db = (em.getValue(DbCheck.class));
                            if(db.password.equals(pwd) && value1.equals(db.id))
                            {
                                flag = 1;
                            }
                            Log.d(TAG, "onDataChange: " + db.password + pwd + "  " + db.id + "  " + value1);
                        }
                        if(flag == 1)
                        {
                            home();
                        }
                        else
                        {
                            Toast.makeText(Login.this, "Password is invalid", Toast.LENGTH_SHORT).show();
                            next1.setClickable(false);
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
            Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
            next1.setClickable(true);
            Login.this.setTitle("Sign Up");
        }
    }

    private void home()
    {
        Toast.makeText(this, "Going Home!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("iduser", value1);
        intent.putExtra("email", value2);
        intent.putExtra("pass", pwd);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Welcome.class);
        Toast.makeText(this, "Aborting!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile("[a-zA-Z0-9!@#$]{4,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }
}


