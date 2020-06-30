package com.example.williamnestius_brow.custompoker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LobbyTActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRefG, myRefR, myRefU;
    private String gameID, ruleID;
    private boolean isHost;
    private int playerId, playersInLobby;
    private String sPlayerId, uid;
    private String hostname, player2name, player3name, player4name, player5name, player6name;
    private String hostId, player2Id, player3Id, player4Id, player5Id, player6Id;
    private EditText startAmount;
    private Long minAmount;
    private boolean[] playersIn;
    private int[] adds;
    private String[] friends;
    private int buyIn, bigBlindValue, smallBlindValue, minBet, handsPerBlindLevel, maxBlindLevel, blindIncrease;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_t);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        myRefG = database.getReference("Games");
        myRefR = database.getReference("Rules");
        myRefU = database.getReference("Users");

        Intent intent = getIntent();
        gameID = intent.getStringExtra("gameId");
        isHost = intent.getBooleanExtra("host", false);
        playerId = intent.getIntExtra("playerId", 1);
        Log.d("isHost", "" + isHost);

        sPlayerId = Integer.toString(playerId);
        startAmount = (EditText) findViewById(R.id.EtStartAmount);

        playersIn = new boolean[]{true, false, false, false, false, false};
        adds = new int[] {R.id.addPlayer1, R.id.addPlayer2, R.id.addPlayer3, R.id.addPlayer4, R.id.addPlayer5, R.id.addPlayer6};
        friends = new String[10];

        myRefG.child(gameID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playersInLobby = snapshot.child("playersInGame").getValue(int.class);
                getFriends();
                hostname = (String) snapshot.child("host").child("username").getValue();
                hostId = (String) snapshot.child("host").child("uid").getValue();
                TextView hostName = (TextView) findViewById(R.id.TvPlayer1);
                hostName.setText("host:  " + hostname);
                Log.d("alreadyf", "length: " + friends.length);
                if(!hostname.isEmpty()){
                    playersIn[0] = true;
                }
                showAdd(0);
                for(int i = 0; i < friends.length; i++){
                    Log.d("alreadyf", "1tt1: " + friends[i]);
                    if(hostname.equals(friends[i])){
                        Log.d("alreadyf", "if1: true ");
                        ImageButton showAdd = (ImageButton) findViewById(R.id.addPlayer1);
                        showAdd.setVisibility(View.INVISIBLE);
                    }
                }
                player2name = (String) snapshot.child("player2").child("username").getValue();
                player2Id = (String) snapshot.child("player2").child("uid").getValue();
                TextView p2Name = (TextView) findViewById(R.id.TvPlayer2);
                p2Name.setText("player2:  " + player2name);
                if(!player2name.isEmpty()){
                    playersIn[1] = true;
                   // myRefG.child(gameID).child("table").child("player2In").setValue(true);
                }
                showAdd(1);
                for(int i = 0; i < friends.length; i++){
                    Log.d("alreadyf", "1tt2: " + friends[i]);
                    if(player2name.equals(friends[i])){
                        Log.d("alreadyf", "if2: true ");
                        ImageButton showAdd = (ImageButton) findViewById(R.id.addPlayer2);
                        showAdd.setVisibility(View.INVISIBLE);
                    }
                }
                player3name = (String) snapshot.child("player3").child("username").getValue();
                player3Id = (String) snapshot.child("player3").child("uid").getValue();
                TextView p3Name = (TextView) findViewById(R.id.TvPlayer3);
                p3Name.setText("player3:  " + player3name);
                if(!player3name.isEmpty()){
                    playersIn[2] = true;
                  //  myRefG.child(gameID).child("table").child("player3In").setValue(true);
                }
                showAdd(2);
                for(int i = 0; i < friends.length; i++){
                    Log.d("alreadyf", "1tt3: " + friends[i]);
                    if(player3name.equals(friends[i])){
                        Log.d("alreadyf", "if3: true ");
                        ImageButton showAdd = (ImageButton) findViewById(R.id.addPlayer3);
                        showAdd.setVisibility(View.INVISIBLE);
                    }
                }
                player4name = (String) snapshot.child("player4").child("username").getValue();
                player4Id = (String) snapshot.child("player4").child("uid").getValue();
                TextView p4Name = (TextView) findViewById(R.id.TvPlayer4);
                p4Name.setText("player4:  " + player4name);
                if(!player4name.isEmpty()){
                    playersIn[3] = true;
                  //  myRefG.child(gameID).child("table").child("player4In").setValue(true);
                }
                showAdd(3);
                for(int i = 0; i < friends.length; i++){
                    Log.d("alreadyf", "1tt4: " + friends[i]);
                    if(player4name.equals(friends[i])){
                        Log.d("alreadyf", "if4: true ");
                        ImageButton showAdd = (ImageButton) findViewById(R.id.addPlayer4);
                        showAdd.setVisibility(View.INVISIBLE);
                    }
                }
                player5name = (String) snapshot.child("player5").child("username").getValue();
                player5Id = (String) snapshot.child("player5").child("uid").getValue();
                TextView p5Name = (TextView) findViewById(R.id.TvPlayer5);
                p5Name.setText("player5:  " + player5name);
                if(!player5name.isEmpty()){
                    playersIn[4] = true;
                   // myRefG.child(gameID).child("table").child("player5In").setValue(true);
                }
                showAdd(4);
                for(int i = 0; i < friends.length; i++){
                    Log.d("alreadyf", "1tt6: " + friends[i]);
                    if(player5name.equals(friends[i])){
                        Log.d("alreadyf", "if5: true ");
                        ImageButton showAdd = (ImageButton) findViewById(R.id.addPlayer5);
                        showAdd.setVisibility(View.INVISIBLE);
                    }
                }
                player6name = (String) snapshot.child("player6").child("username").getValue();
                player6Id = (String) snapshot.child("player6").child("uid").getValue();
                TextView p6Name = (TextView) findViewById(R.id.TvPlayer6);
                p6Name.setText("player6:  " + player6name);
                if(!player6name.isEmpty()){
                    playersIn[5] = true;
                  //  myRefG.child(gameID).child("table").child("player6In").setValue(true);
                }
                showAdd(5);
                for(int i = 0; i < friends.length; i++){
                    Log.d("alreadyf", "1tt6: " + friends[i]);
                    if(player6name.equals(friends[i])){
                        Log.d("alreadyf", "if6: true ");
                        ImageButton showAdd = (ImageButton) findViewById(R.id.addPlayer6);
                        showAdd.setVisibility(View.INVISIBLE);
                    }
                }

                if(!isHost){
                    Button nextRound = (Button) findViewById(R.id.startGame);
                    nextRound.setVisibility(View.INVISIBLE);
                }
                ruleID = (String) snapshot.child("gameRulesId").getValue();
                Log.d("minBuyIn", ": " + ruleID);
                getRules();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        myRefG.child(gameID).child("isActive").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean active = (boolean) dataSnapshot.getValue();
                Log.d("autoload", "" + active);
                if (active == true) {
                    if(isHost){
                        myRefG.child(gameID).child("host").child("currentAmount").setValue(buyIn);
                        for (int i = 2; i <= playersInLobby; i++) {
                            myRefG.child(gameID).child("table").child("player" + i + "In").setValue(true);
                        }
                    }else {
                        myRefG.child(gameID).child("player" + sPlayerId).child("currentAmount").setValue(buyIn);
                    }
                    enterGame();
                } else {
                    Log.d("autoload", "false");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("listener", "loadPost:onCancelled", databaseError.toException());
            }
        });

        TextView gameName = (TextView) findViewById(R.id.TvGameId);
        gameName.setText("GameId:  " + gameID);

    }

    public void getRules(){
        myRefR.child("Tournament").child(ruleID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                buyIn =  snapshot.child("buyIn").getValue(int.class);
                bigBlindValue =  snapshot.child("bigBlind").getValue(int.class);
                smallBlindValue =  snapshot.child("smallBlind").getValue(int.class);
                minBet = snapshot.child("minBet").getValue(int.class);
                handsPerBlindLevel = snapshot.child("handsPerBlindLevel").getValue(int.class);
                maxBlindLevel = snapshot.child("maxBlindLevel").getValue(int.class);
                blindIncrease = snapshot.child("blindIncrease").getValue(int.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public  void getFriends(){
        myRefU.child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int amountFriends = snapshot.child("amountOfFriends").getValue(int.class);
                for(int i = 1; i <= amountFriends; i++){
                    friends[i-1] = snapshot.child("f"+i).child("username").getValue(String.class);
                }
                //show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void onClickStartGame(View view){
        if(isHost) {
            if(player2name.isEmpty()){
                Toast toast = Toast.makeText(getApplicationContext(), "Need more than one player", Toast.LENGTH_SHORT);
                toast.show();
            }else {
                myRefG.child(gameID).child("isActive").setValue(true);
//                myRefG.child(gameID).child("host").child("currentAmount").setValue(buyIn);
//                for(int i = 2; i <= playersIn)
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Only host can start", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void enterGame(){
        myRefG.child(gameID).child("currentPlayAmount").setValue(bigBlindValue);
        Intent startIntent = new Intent(this, TournamentGameActivity.class);
        startIntent.putExtra("GameID", gameID);
        startIntent.putExtra("RuleID", ruleID);
        startIntent.putExtra("IsHost", isHost);
        startIntent.putExtra("BuyIn", buyIn);
        startIntent.putExtra("BigBlindValue", bigBlindValue);
        startIntent.putExtra("SmallBlindValue", smallBlindValue);
        startIntent.putExtra("MinBet", minBet);
        startIntent.putExtra("MaxBlindLevel", maxBlindLevel);
        startIntent.putExtra("HandsPerBlindLevel", handsPerBlindLevel);
        startIntent.putExtra("BlindIncrease", blindIncrease);
        startActivity(startIntent);
    }

    public void showAdd(int i){
        if(playersIn[i] == true){
            Log.d("showAdd", "if  1");
            ImageButton showAdd = (ImageButton) findViewById(adds[i]);
            if(isHost && i==0){
                Log.d("showAdd", "if  2");
                showAdd.setVisibility(View.INVISIBLE);
            }else if((playerId - 1) == i ) {
                Log.d("showAdd", "else if  1");
                showAdd.setVisibility(View.INVISIBLE);
            }else{
                Log.d("showAdd", "else 1");
                showAdd.setVisibility(View.VISIBLE);
            }
        }else{
            Log.d("showAdd", "else 2");
            ImageButton showAdd = (ImageButton) findViewById(adds[i]);
            showAdd.setVisibility(View.INVISIBLE);
        }
    }
    public void onClickAdd(View view) {
        switch (view.getId()) {
            case R.id.addPlayer1:
                addPlayer(hostId, hostname);
                ImageButton showAdd = (ImageButton) findViewById(R.id.addPlayer1);
                showAdd.setVisibility(View.INVISIBLE);
                break;
            case R.id.addPlayer2:
                addPlayer(player2Id, player2name);
                ImageButton showAdd2 = (ImageButton) findViewById(R.id.addPlayer2);
                showAdd2.setVisibility(View.INVISIBLE);
                break;
            case R.id.addPlayer3:
                addPlayer(player3Id, player3name);
                ImageButton showAdd3 = (ImageButton) findViewById(R.id.addPlayer3);
                showAdd3.setVisibility(View.INVISIBLE);
                break;
            case R.id.addPlayer4:
                addPlayer(player4Id, player4name);
                ImageButton showAdd4 = (ImageButton) findViewById(R.id.addPlayer4);
                showAdd4.setVisibility(View.INVISIBLE);
                break;
            case R.id.addPlayer5:
                addPlayer(player5Id, player5name);
                ImageButton showAdd5 = (ImageButton) findViewById(R.id.addPlayer5);
                showAdd5.setVisibility(View.INVISIBLE);
                break;
            case R.id.addPlayer6:
                addPlayer(player6Id, player6name);
                ImageButton showAdd6 = (ImageButton) findViewById(R.id.addPlayer6);
                showAdd6.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void addPlayer(final String FriendID, final String fUsername){
        myRefU.child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int amountFriends = snapshot.child("amountOfFriends").getValue(int.class);
                //  fUsername.setUsername(snapshot.getValue(Users.class).getUsername());
                //   Log.d("friendtest", "usrname" + fUsername.getUsername());
                myRefU.child(uid).child("friends").child("f" + (amountFriends + 1)).child("uid").setValue(FriendID);
                myRefU.child(uid).child("friends").child("f" + (amountFriends + 1)).child("username").setValue(fUsername);
                myRefU.child(uid).child("friends").child("amountOfFriends").setValue(amountFriends+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Toast toast = Toast.makeText(getApplicationContext(), "Friend added", Toast.LENGTH_SHORT);
        toast.show();
    }
}

