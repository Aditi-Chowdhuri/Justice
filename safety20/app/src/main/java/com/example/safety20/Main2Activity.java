package com.example.safety20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {
Button c;
EditText a,b;
String ph1,ph2;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        c=(Button)findViewById(R.id.button3);
        a=(EditText)findViewById(R.id.editText);
        b=(EditText)findViewById(R.id.editText2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("ph1").setValue("");
        mDatabase.child("ph1").setValue("");


        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ph1=a.getText().toString().trim();
                ph2=b.getText().toString().trim();
                mDatabase.child("ph1").setValue(ph1);
                mDatabase.child("ph2").setValue(ph2);

                if ((ph1.length() == 10) && (ph2.length() == 10)) {

                    // Create the Intent object of this class Context() to Second_activity class
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "error:enter 10 digit no.", Toast.LENGTH_SHORT).show();
                }
            }
        });

}}
