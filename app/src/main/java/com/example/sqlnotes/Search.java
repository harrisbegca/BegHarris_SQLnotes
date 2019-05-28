package com.example.sqlnotes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyCharacterMap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Search extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SearchView searchBar = findViewById(R.id.searchView);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                updateTable(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                updateTable(s);
                return false;
            }
        });
    }
    public void updateTable(String query) {
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        Query search = users.orderByChild("name").equalTo(query.toUpperCase());
        final TableLayout table = findViewById(R.id.table);
        final Context c = this;
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                table.removeAllViews();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TableRow tr = new TableRow(getBaseContext());
                    tr.setBackgroundColor(Color.WHITE);
                    //tr.setElevation(20);
                    tr.setPadding(20,20,20,20);
                    tr.setClipToPadding(false);
                    tr.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
                    TextView user = new TextView(getBaseContext());
                    user.setTextColor(Color.BLACK);
                    user.setText(dataSnapshot.getKey());
                    user.setAllCaps(true);
                    TextView phone = new TextView(getBaseContext());
                    TextView address = new TextView(getBaseContext());
                    phone.setText("Phone: " + dataSnapshot.child("phone").getValue(String.class));
                    address.setText("Address: " + dataSnapshot.child("address").getValue(String.class));
                    phone.setTextColor(Color.BLACK);
                    address.setTextColor(Color.BLACK);
                    user.setTypeface(user.getTypeface(), Typeface.BOLD);
                    LinearLayout ll = new LinearLayout(getBaseContext());
                    ll.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
                    tr.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
                    //ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.addView(user);
                    ll.addView(phone);
                    ll.addView(address);
                    tr.addView(ll);
                    table.addView(tr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
