package com.example.sqlnotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Hi", "Hello World");
        final TextView name = (TextView) findViewById(R.id.name);
        final TextView phone = (TextView) findViewById(R.id.phone);
        final TextView address = (TextView) findViewById(R.id.address);
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveInfo(name.getText().toString(), phone.getText().toString(), address.getText().toString());
            }
        });
    }
    public void retrieveInfo(String name, String phone, String address) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "BTN ");
        data.child("users").child(name).child("address").setValue(address);
        data.child("users").child(name).child("phone").setValue(phone);
    }
}
