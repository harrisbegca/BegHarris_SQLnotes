package com.example.sqlnotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
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
    TextView name, phone, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        Button submit = findViewById(R.id.submit);
        myDb = new DatabaseHelper(this);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("users");
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("phone", dataSnapshot.child("phone").getValue(String.class)); // Put phone from
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
                addData(v);
                retrieveInfo(name.getText().toString(), phone.getText().toString(), address.getText().toString());
                Toast.makeText(getBaseContext(), "Contact Created", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.viewData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // viewData(view) --> SQLite;
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

    /**
     *
     * @param view The View layout
     */
    public void addData(View view) {
        boolean isInserted = myDb.insertData(name.getText().toString(), address.getText().toString(), phone.getText().toString());
        if (isInserted)
            Toast.makeText(this, "Inserted contact", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Failed inserting contact", Toast.LENGTH_LONG).show();
    }

    public void viewData(View view) {
        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {
            showMessage("Error","No Data Found in DB");
            return;
        }

        StringBuffer stringBuffer = new StringBuffer();
        while (res.moveToNext()) {
            //  append res col 0
            stringBuffer.append("ID: " + res.getString(0) + "\n");
            stringBuffer.append("NAME: " + res.getString(1) + "\n");
            stringBuffer.append("ADDRESS: " + res.getString(2) + "\n");
            stringBuffer.append("PHONE: " + res.getString(3) + "\n");
            showMessage("Data",stringBuffer.toString());
        }
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
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
