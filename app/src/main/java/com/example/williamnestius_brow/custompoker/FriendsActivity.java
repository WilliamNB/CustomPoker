package com.example.williamnestius_brow.custompoker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendsActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRefU;
    private String uid, FriendID, viewID, fUsername;
    private EditText friendID;
    private int amountFriends;
    private boolean userExists;
    private Users f1,f2,f3,f4,f5,f6,f7,f8,f9,f10;
    private Users[] fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        uid = intent.getStringExtra("Uid");
        f1 = new Users(); f2 = new Users(); f3 = new Users(); f4 = new Users(); f5 = new Users();
        f6 = new Users(); f7 = new Users(); f8 = new Users(); f9 = new Users(); f10 = new Users();
        fs = new Users[] {f1, f2, f3, f4, f5, f6, f7, f8, f9, f10};
        database = FirebaseDatabase.getInstance();
        myRefU = database.getReference("Users");

        friendID = (EditText) findViewById(R.id.enteredUserId);

        myRefU.child(uid).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                amountFriends = snapshot.child("amountOfFriends").getValue(int.class);
                int[] deletes = new int[]{R.id.delete1, R.id.delete2, R.id.delete3, R.id.delete4, R.id.delete5, R.id.delete6,
                        R.id.delete7, R.id.delete8, R.id.delete9, R.id.delete10};
                for (int i = 1; i <= amountFriends; i++) {
                    String fname = (String) snapshot.child("f"+i).child("username").getValue();
                    Log.d("get usernames", "onDataChange: " + fname);
                    int[] views = new int[] {R.id.friend1, R.id.friend2, R.id.friend3, R.id.friend4, R.id.friend5, R.id.friend6,
                            R.id.friend7, R.id.friend8, R.id.friend9, R.id.friend10};
                    TextView showFriend = (TextView) findViewById(views[i-1]);
                    showFriend.setText(fname);

                    ImageButton showDelete = (ImageButton) findViewById(deletes[i-1]);
                    showDelete.setVisibility(View.VISIBLE);

                    String j = Integer.toString(i);
                    fs[i-1].setUsername(snapshot.child("f"+j).getValue(Users.class).getUsername());
                    fs[i-1].setUid(snapshot.child("f"+j).getValue(Users.class).getUid());
                }
                for(int x = amountFriends + 1; x <= 10; x++){

                    ImageButton showDelete = (ImageButton) findViewById(deletes[x-1]);
                    showDelete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClickAddFriend(View view) {

        FriendID = friendID.getText().toString();
        myRefU.orderByKey().equalTo(FriendID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("friendqq", "exists: ");
                    //userExists = true;
                    Log.d("friendtest", "in if");
                    myRefU.child(FriendID).child("username").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            fUsername = (String) snapshot.getValue();
                            Log.d("fusername", "" + fUsername);
                            myRefU.child(uid).child("friends").child("f" + (amountFriends + 1)).child("uid").setValue(FriendID);
                            myRefU.child(uid).child("friends").child("f" + (amountFriends + 1)).child("username").setValue(fUsername);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    myRefU.child(uid).child("friends").child("amountOfFriends").setValue(amountFriends+1);
                    Toast toast = Toast.makeText(getApplicationContext(), "Friend added", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Log.d("friendqq", "wasnt found: ");
                    //userExists = false;
                    Log.d("friendqq", "" + userExists);
                    Toast toast = Toast.makeText(getApplicationContext(), "User doesn't exist", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
//        UserExists(FriendID);
//        if(userExists) {
//            Log.d("friendtest", "in if");
//            myRefU.child(FriendID).child("username").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    fUsername = (String) snapshot.getValue();
//                    Log.d("fusername", "" + fUsername);
//                    myRefU.child(uid).child("friends").child("f" + (amountFriends + 1)).child("uid").setValue(FriendID);
//                    myRefU.child(uid).child("friends").child("f" + (amountFriends + 1)).child("username").setValue(fUsername);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                }
//            });
//            myRefU.child(uid).child("friends").child("amountOfFriends").setValue(amountFriends+1);
//            Toast toast = Toast.makeText(getApplicationContext(), "Friend added", Toast.LENGTH_SHORT);
//            toast.show();
//        }else{
//            Log.d("friendqq", "" + userExists);
//            Toast toast = Toast.makeText(getApplicationContext(), "User doesn't exist", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }

//    private void UserExists(final String InputtedUserId) {
//        //Log.d("userinput", "in exists: " + InputtedGameId);
//        myRefU.orderByKey().equalTo(InputtedUserId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    Log.d("friendqq", "exists: " + InputtedUserId);
//                    userExists = true;
//                } else {
//                    Log.d("friendqq", "wasnt found: " + InputtedUserId);
//                    userExists = false;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }

    public void onClickDelete(View view){
        Log.d("friendtest", "" + view.getId());
        switch (view.getId()){
            case R.id.delete1:
                deleteFriend(1);
                break;
            case R.id.delete2:
                deleteFriend(2);
                break;
            case R.id.delete3:
                deleteFriend(3);
                break;
            case R.id.delete4:
                deleteFriend(4);
                break;
            case R.id.delete5:
                deleteFriend(5);
                break;
            case R.id.delete6:
                deleteFriend(6);
                break;
            case R.id.delete7:
                deleteFriend(7);
                break;
            case R.id.delete8:
                deleteFriend(8);
                break;
            case R.id.delete9:
                deleteFriend(9);
                break;
            case R.id.delete10:
                deleteFriend(10);
                break;
        }
    }

    public void deleteFriend(int i){
        if(i == amountFriends){
            myRefU.child(uid).child("friends").child("f"+i).child("uid").setValue("");
            myRefU.child(uid).child("friends").child("f"+i).child("username").setValue("");
        }else {
            while (i < amountFriends) {
                myRefU.child(uid).child("friends").child("f" + i).child("uid").setValue(fs[i].getUid());
                myRefU.child(uid).child("friends").child("f" + i).child("username").setValue(fs[i].getUsername());
                i += 1;
            }
            myRefU.child(uid).child("friends").child("f" + amountFriends).child("uid").setValue("");
            myRefU.child(uid).child("friends").child("f" + amountFriends).child("username").setValue("");
        }
       myRefU.child(uid).child("friends").child("amountOfFriends").setValue(amountFriends - 1);
    }
}
