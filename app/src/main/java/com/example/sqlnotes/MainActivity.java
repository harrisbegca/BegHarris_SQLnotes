package com.example.sqlnotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private HashMap<String, HashMap<String, String>> details = new HashMap<>();
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView name = findViewById(R.id.name);
        final TextView phone = findViewById(R.id.phone);
        final TextView address = findViewById(R.id.address);
        Button submit = findViewById(R.id.submit);
        //myDb = new DatabaseHelper(this);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("users");
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("phone", dataSnapshot.child("phone").getValue(String.class));
                    hm.put("address", dataSnapshot.child("address").getValue(String.class));
                    details.put(dataSnapshot.getKey(), hm);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveInfo(name.getText().toString(), phone.getText().toString(), address.getText().toString());
                Toast.makeText(getBaseContext(), "Contact Created", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.viewData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayData();
            }
        });
        findViewById(R.id.searchQuery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Search.class);
                startActivity(i);
            }
        });
    }
    public void retrieveInfo(String name, String phone, String address) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("users").child(name);
        data.child("address").setValue(address);
        data.child("phone").setValue(phone);
        data.child("name").setValue(name.toUpperCase());
    }
    public void displayData() {
        String all = "";
        for (String key : details.keySet()) {
            all+=key+"\n"; // Username
            all+="Address: " + details.get(key).get("address") + "\n"; // Address
            all+="Phone: " + details.get(key).get("phone") + "\n\n"; // Phone
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(all);
        dialog.show();
    }

}
