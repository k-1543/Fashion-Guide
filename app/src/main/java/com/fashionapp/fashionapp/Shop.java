package com.fashionapp.fashionapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Shop extends AppCompatActivity {
    public static final String TAG = "Welcome";

    static String iduser, email, pass;
    Button prev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Bundle bundle = getIntent().getExtras();
        iduser = bundle.getString("iduser");
        email = bundle.getString("email");
        pass = bundle.getString("pass");
        prev = findViewById(R.id.prev);
        prev.setClickable(true);
        DatabaseReference Db = FirebaseDatabase.getInstance().getReference(iduser);
        Db.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot em : dataSnapshot.getChildren())
                {
                    final DbCheck dbCheck = em.getValue(DbCheck.class);
                    if(!TextUtils.isEmpty(dbCheck.visited))
                    {

                        prev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                DatabaseReference dd = FirebaseDatabase.getInstance().getReference(dbCheck.visited);
                                String i = dd.getKey();
                                Log.d(TAG, "onClick: " + i);
                                String uri = "http://maps.google.com/maps?saddr=0.0,0.0";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("iduser", iduser);
        intent.putExtra("email", email);
        intent.putExtra("pass", pass);
        startActivity(intent);
        finish();
    }
}