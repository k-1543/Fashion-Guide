package com.fashionapp.fashionapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    public static String value2;
    public EditText pass;
    Button next1;
    public TextView t1;

    public DatabaseReference Db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        next1 = findViewById(R.id.next1);
        //getActionBar().setTitle("Sign Up");

        Bundle bundle = getIntent().getExtras();

        value2 = bundle.getString("email");
        if(!TextUtils.isEmpty(value2))
        {
            t1 = findViewById(R.id.textView2);
            t1.setText(value2);
        }
        else
        {
            Toast.makeText(this, "Problem encountered", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Welcome.class);
            startActivity(intent);
            finish();
        }

        next1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                next1.setClickable(false);
                SignUp.this.setTitle("Signing in...");
                input();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Welcome.class);
        Toast.makeText(this, "Aborting!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    private void input()
    {
        String pwd;
        pass = (EditText) findViewById(R.id.pwd);
        pwd = pass.getText().toString().trim();
        if (!TextUtils.isEmpty(pwd) && isValidPassword(pwd))
        {

            Db = FirebaseDatabase.getInstance().getReference("credentials");
            String id = Db.push().getKey();
            if(!TextUtils.isEmpty(id))
            {
                DbCheck db = new DbCheck(id, value2, pwd);
                Db.child(id).setValue(db);
                Toast.makeText(this, "Lets know you a bit more", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, Info.class);
                intent1.putExtra("iduser", id);
                intent1.putExtra("email", value2);
                intent1.putExtra("pass", pwd);
                startActivity(intent1);
                finish();
            }
            else
            {
                Toast.makeText(this, "Unable to contact database", Toast.LENGTH_SHORT).show();
                next1.setClickable(true);
                Intent intent = new Intent(this, Welcome.class);
                startActivity(intent);
                finish();
            }
        }
        else
        {
            Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
            next1.setClickable(true);
            SignUp.this.setTitle("Sign Up");
        }
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile("[a-zA-Z0-9!@#$]{4,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }
}
