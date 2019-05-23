package com.example.sqlnotes;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private Map<String, Object> details;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Hi", "Hello World");
        final TextView name = (TextView) findViewById(R.id.name);
        final TextView phone = (TextView) findViewById(R.id.phone);
        final TextView address = (TextView) findViewById(R.id.address);
        Button submit = (Button) findViewById(R.id.submit);
        //myDb = new DatabaseHelper(this);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("users");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            Map<String, Map<String, String>> td = (HashMap<String,Object>) dataSnapshot.getValue();
                            details = td;
                            Log.d(TAG, (Map<String, Object>)(details.
                                    get("hello"))toString());
                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveInfo(name.getText().toString(), phone.getText().toString(), address.getText().toString());
            }
        });
    }
    public void retrieveInfo(String name, String phone, String address) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("users").child(name);
        data.child("address").setValue(address);
        data.child("phone").setValue(phone);
        Log.d(TAG, data.child("phone").getKey());

    }
}
