package com.example.williamnestius_brow.custompoker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Profile extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRefU;
    private String uid;
    private String username;
    public Users currentUser;
    private EditText NewUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NewUserName = (EditText) findViewById(R.id.newUsernameET);
        currentUser = new Users();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            currentUser.setUid(uid);
        }

        database = FirebaseDatabase.getInstance();
        myRefU = database.getReference("Users");

        myRefU.child(uid).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = (String) snapshot.getValue();
                currentUser.setUsername(username);
                Log.d("userprofile", "username1 " + username);

                TextView textView1 = (TextView)findViewById(R.id.TvUsername);
                textView1.setText("User name: " + currentUser.getUsername());

                TextView textView = (TextView)findViewById(R.id.TvUserId);
                textView.setText("User Id: " + currentUser.getUid());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void onClickChangeUsername(View view){
        String newUsername= NewUserName.getText().toString();
        if(newUsername.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Enter Value to submit", Toast.LENGTH_SHORT);
            toast.show();
        }else if(newUsername.length() > 10 ){
            Toast toast = Toast.makeText(getApplicationContext(), "Max length is 10 characters", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            myRefU.child(uid).child("username").setValue(newUsername);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
