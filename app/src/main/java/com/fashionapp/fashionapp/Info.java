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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Info extends AppCompatActivity {

    Button butt;
    String id;
    String email, pass;
    EditText name;
    Spinner height, weight, foot, comp;
    RadioGroup gender;
    static int flag = 0;
    public DatabaseReference Db = FirebaseDatabase.getInstance().getReference();;
    private static final String TAG = "Welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Log.d(TAG, "onCreate: Info");
        gender = findViewById(R.id.gender);
        String gender1 = ((RadioButton) findViewById(gender.getCheckedRadioButtonId())).getText().toString();
        Log.d(TAG, "fillup: " + gender1);
        butt = findViewById(R.id.submit);
        name = findViewById(R.id.name);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        foot = findViewById(R.id.foot);
        comp = findViewById(R.id.comp);
        gender = findViewById(R.id.gender);

        Bundle bundle1 = getIntent().getExtras();
        email = bundle1.getString("email");
        pass = bundle1.getString("pass");
        id = bundle1.getString("iduser");

        Db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot em : dataSnapshot.child("credentials").getChildren())
                {
                    DbCheck db = (em.getValue(DbCheck.class));
                    if(id.equals(db.id))
                    {
                        Log.d(TAG, "onDataChange: " + id + email + pass);
                        Log.d(TAG, "onDataChange: " + db.height);
                        if (!TextUtils.isEmpty(db.height) && !TextUtils.isEmpty(db.weight) && !TextUtils.isEmpty(db.foot) && !TextUtils.isEmpty(db.complexion)) {

                            name.setText(db.name);
                            selectSpinnerItemByValue(height, db.height);
                            selectSpinnerItemByValue(weight, db.weight);
                            selectSpinnerItemByValue(foot, db.foot);
                            selectSpinnerItemByValue(comp, db.complexion);
                            selectRadio(gender, db.gender);
                            flag = 1;
                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillup();
            }
        });
    }

    private void selectRadio(RadioGroup gender, String gender1)
    {
        for(int i = 0; i<gender.getChildCount();i++)
        {
            Log.d(TAG, "selectSpinnerItemByValue: " + ((RadioButton) gender.getChildAt(i)).getText() + "   " + gender1);
            if(((RadioButton) gender.getChildAt(i)).getText().equals(gender1))
            {
                ((RadioButton) gender.getChildAt(i)).setChecked(true);
                return;
            }
        }
    }

    private void fillup()
    {
        final String gender1 = ((RadioButton) findViewById(gender.getCheckedRadioButtonId())).getText().toString();
        Bundle bundle1 = getIntent().getExtras();
        final String email = bundle1.getString("email");
        final String pass = bundle1.getString("pass");
        String name1 = name.getText().toString();
        String height1, weight1, foot1, comp1;
        height1 = height.getSelectedItem().toString();
        weight1 = weight.getSelectedItem().toString();
        foot1 = foot.getSelectedItem().toString();
        comp1 = comp.getSelectedItem().toString();
        DbCheck db = new DbCheck(id, email, pass, name1, height1, weight1, foot1, comp1, gender1);
        Db.child("credentials").child(id).setValue(db);
        Toast.makeText(Info.this, "Lets go home!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Info.this, Home.class);
        intent.putExtra("iduser", id);
        intent.putExtra("email", email);
        intent.putExtra("pass", pass);
        startActivity(intent);
        finish();
    }
    public static void selectSpinnerItemByValue(Spinner spinner, String value)
    {
        for(int i = 0; i<spinner.getCount();i++)
        {
            Log.d(TAG, "selectSpinnerItemByValue: " + spinner.getItemAtPosition(i) + "   " + value);
            if(spinner.getItemAtPosition(i).equals(value))
            {
                spinner.setSelection(i);
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(flag == 0)
        {
            Intent intent = new Intent(this, Welcome.class);
            Toast.makeText(this, "Aborting", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }
        else if(flag == 1)
        {
            Intent intent = new Intent(this, Home.class);
            //Toast.makeText(this, "Logging out!", Toast.LENGTH_SHORT).show();
            intent.putExtra("iduser", id);
            intent.putExtra("email", email);
            intent.putExtra("pass", pass);
            startActivity(intent);
            finish();
        }
    }
}


