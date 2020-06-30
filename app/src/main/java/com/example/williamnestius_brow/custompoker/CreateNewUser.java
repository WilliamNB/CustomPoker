package com.example.williamnestius_brow.custompoker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNewUser extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRefU;
    private FirebaseUser user;
    private EditText username;
    private String email;
    private Users fUser;
    private Friends friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
        database = FirebaseDatabase.getInstance();
        myRefU = database.getReference("Users");
        username = (EditText) findViewById(R.id.InputUsername);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            String uid = user.getUid();
        }
    }

    public void onSubmitClick(View view){

        String Username = username.getText().toString();
        fUser = new Users("","");
        friend = new Friends(fUser);
        Users newUser = new Users(Username, 21, friend);
        myRefU.child(user.getUid()).setValue(newUser);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
