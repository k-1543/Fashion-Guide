package com.fashionapp.fashionapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    static String iduser, email, pass;
    Button boutique, saloon, foot, tailor, rate, extra, information;
    private static final String TAG = "Welcome";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bundle = getIntent().getExtras();
        iduser =  bundle.getString("iduser");
        email =  bundle.getString("email");
        pass =  bundle.getString("pass");
        Log.d(TAG, "onClick1: " + email + pass + iduser);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Home.this.setTitle("Choose a Makeover");
            }
        }, 500);

        boutique = findViewById(R.id.boutique);
        saloon = findViewById(R.id.saloon);
        foot = findViewById(R.id.foot);
        tailor = findViewById(R.id.tailor);
        rate = findViewById(R.id.rate);
        extra = findViewById(R.id.extra);
        information = findViewById(R.id.information);

        boutique.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                mapintent("boutique");
            }
        });
        saloon.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                mapintent("saloon");
            }
        });
        foot.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                mapintent("foot");
            }
        });
        tailor.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                mapintent("tailor");
            }
        });
        rate.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(Home.this, Shop.class);
                intent.putExtra("iduser", iduser);
                intent.putExtra("email", email);
                intent.putExtra("pass", pass);
                startActivity(intent);
                finish();
            }
        });
        extra.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                /*Intent intent = new Intent(Home.this, Map.class);
                startActivity(intent);
                finish();*/
                Toast.makeText(Home.this, "Extra", Toast.LENGTH_SHORT).show();
            }
        });
        information.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(Home.this, Info.class);
                Log.d(TAG, "onClick: " + email + pass + iduser);
                intent.putExtra("iduser", iduser);
                intent.putExtra("email", email);
                intent.putExtra("pass", pass);
                startActivity(intent);
                finish();
            }
        });
    }

    private void mapintent(String button)
    {
        Intent intent = new Intent(Home.this, MapsActivity.class);
        intent.putExtra("button", button);
        intent.putExtra("iduser", iduser);
        intent.putExtra("email", email);
        intent.putExtra("pass", pass);
        boutique.setClickable(false);
        tailor.setClickable(false);
        saloon.setClickable(false);
        foot.setClickable(false);
        Toast.makeText(this, "Wait for 3 seconds", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Welcome.class);
        Toast.makeText(this, "Logging out!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }
}
