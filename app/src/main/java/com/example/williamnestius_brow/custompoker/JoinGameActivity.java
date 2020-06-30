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
import android.widget.TextView;
import android.widget.Toast;
import static java.lang.Math.toIntExact;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinGameActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRefU;
    private DatabaseReference myRefG;
    private EditText GameRef;
    private String InputtedGameRef, uid, username;
    private DataSnapshot snapshot;
    private boolean gameExists;
    private Long playersInGame;
    private int amountPlayers, playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        database = FirebaseDatabase.getInstance();
        myRefU = database.getReference("Users");
        myRefG = database.getReference("Games");
        GameRef = (EditText)findViewById(R.id.enteredGameId);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();

        myRefU.child(uid).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = (String) snapshot.getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void onJoinGameButtonClick(View view) {
        InputtedGameRef = GameRef.getText().toString() ;
        GameExists(InputtedGameRef);
        Log.d("userinput", "run time " );
        if(gameExists){
            /////upload userdata to gama table
        Log.d("gameExists", "" + gameExists);
        amountPlayers = toIntExact(playersInGame) + 1;
        myRefG.child(InputtedGameRef).child("playersInGame").setValue(amountPlayers);

        switch (amountPlayers){
            case 1:
                Log.d("gameExists", "case 1");
                break;
            case 2:
                Log.d("gameExists", "case 2");
                myRefG.child(InputtedGameRef).child("player2").child("uid").setValue(uid);
                myRefG.child(InputtedGameRef).child("player2").child("username").setValue(username);
                playerId = 2;
                break;
            case 3:
                Log.d("gameExists", "case 3");
                myRefG.child(InputtedGameRef).child("player3").child("uid").setValue(uid);
                myRefG.child(InputtedGameRef).child("player3").child("username").setValue(username);
                playerId = 3;
                break;
            case 4:
                Log.d("gameExists", "case 4");
                myRefG.child(InputtedGameRef).child("player4").child("uid").setValue(uid);
                myRefG.child(InputtedGameRef).child("player4").child("username").setValue(username);
                playerId = 4;
                break;
            case 5:
                Log.d("gameExists", "case 5");
                myRefG.child(InputtedGameRef).child("player5").child("uid").setValue(uid);
                myRefG.child(InputtedGameRef).child("player5").child("username").setValue(username);
                playerId = 5;
                break;
            case 6:
                Log.d("gameExists", "case 6");
                myRefG.child(InputtedGameRef).child("player6").child("uid").setValue(uid);
                myRefG.child(InputtedGameRef).child("player6").child("username").setValue(username);
                playerId = 6;
                break;
            default:
                Log.d("gameExists", "error");
        }
            Intent intent = new Intent(this, LobbyActivity.class);
              intent.putExtra("playerId", playerId);
              intent.putExtra("gameId", InputtedGameRef);
            startActivity(intent);
        }else{
            Log.d("gameExists", "" + gameExists);
            Toast toast = Toast.makeText(getApplicationContext(), "Game doesn't exist", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void onJoinTGameButtonClick(View view) {
        InputtedGameRef = GameRef.getText().toString() ;
        GameExists(InputtedGameRef);
        Log.d("userinput", "run time " );
        if(gameExists){
            /////upload userdata to gama table
            Log.d("gameExists", "" + gameExists);
            amountPlayers = toIntExact(playersInGame) + 1;
            myRefG.child(InputtedGameRef).child("playersInGame").setValue(amountPlayers);

            switch (amountPlayers){
                case 1:
                    Log.d("gameExists", "case 1");
                    break;
                case 2:
                    Log.d("gameExists", "case 2");
                    myRefG.child(InputtedGameRef).child("player2").child("uid").setValue(uid);
                    myRefG.child(InputtedGameRef).child("player2").child("username").setValue(username);
                    playerId = 2;
                    break;
                case 3:
                    Log.d("gameExists", "case 3");
                    myRefG.child(InputtedGameRef).child("player3").child("uid").setValue(uid);
                    myRefG.child(InputtedGameRef).child("player3").child("username").setValue(username);
                    playerId = 3;
                    break;
                case 4:
                    Log.d("gameExists", "case 4");
                    myRefG.child(InputtedGameRef).child("player4").child("uid").setValue(uid);
                    myRefG.child(InputtedGameRef).child("player4").child("username").setValue(username);
                    playerId = 4;
                    break;
                case 5:
                    Log.d("gameExists", "case 5");
                    myRefG.child(InputtedGameRef).child("player5").child("uid").setValue(uid);
                    myRefG.child(InputtedGameRef).child("player5").child("username").setValue(username);
                    playerId = 5;
                    break;
                case 6:
                    Log.d("gameExists", "case 6");
                    myRefG.child(InputtedGameRef).child("player6").child("uid").setValue(uid);
                    myRefG.child(InputtedGameRef).child("player6").child("username").setValue(username);
                    playerId = 6;
                    break;
                default:
                    Log.d("gameExists", "error");
            }
            Intent intent = new Intent(this, LobbyTActivity.class);
            intent.putExtra("playerId", playerId);
            intent.putExtra("gameId", InputtedGameRef);
            startActivity(intent);
        }else{
            Log.d("gameExists", "" + gameExists);
            Toast toast = Toast.makeText(getApplicationContext(), "Game doesn't exist", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void GameExists(final String InputtedGameId){
        //Log.d("userinput", "in exists: " + InputtedGameId);
        myRefG.orderByKey().equalTo(InputtedGameId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("gameExists", "exists: " + InputtedGameId);
                    gameExists = true;
                } else {
                    Log.d("gameExists", "wasnt found: " + InputtedGameId);
                    gameExists = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        myRefG.child(InputtedGameId).child("playersInGame").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playersInGame = snapshot.getValue(Long.class);
                Log.d("gameExists", "Players In Game: " + playersInGame);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
