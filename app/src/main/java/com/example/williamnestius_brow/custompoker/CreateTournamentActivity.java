package com.example.williamnestius_brow.custompoker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateTournamentActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRefR, myRefG, myRefU;
    private String ruleID, gameID, uid;
    private EditText BuyIn, BigBlind, SmallBlind, MinBet, HandsPerBlindLevel, MaxBlindLevel, BlindIncrease;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        database = FirebaseDatabase.getInstance();
        myRefR = database.getReference("Rules");
        myRefG = database.getReference("Games");
        myRefU = database.getReference("Users");

        BuyIn = (EditText) findViewById(R.id.enterBuyIn);
        BigBlind = (EditText) findViewById(R.id.enterBigBlind);
        SmallBlind = (EditText) findViewById(R.id.enterSmallBlind);
        MinBet = (EditText) findViewById(R.id.enterMinBet);
        HandsPerBlindLevel = (EditText) findViewById(R.id.handsPerBlindLevel);
        MaxBlindLevel = (EditText) findViewById(R.id.maxBlindLevel);
        BlindIncrease = (EditText) findViewById(R.id.blindIncrease);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        myRefU.child(uid).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = (String) snapshot.getValue();
                Log.d("getUsername", "username " + username);
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

    public void onClickSubmit(View view) {
        if (BuyIn.getText().toString().isEmpty() || BigBlind.getText().toString().isEmpty() || SmallBlind.getText().toString().isEmpty()
                || MinBet.getText().toString().isEmpty() || HandsPerBlindLevel.getText().toString().isEmpty()
                || MaxBlindLevel.getText().toString().isEmpty() || BlindIncrease.getText().toString().isEmpty()) {

            Toast toast = Toast.makeText(getApplicationContext(), "Enter each variable", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            int buyIn = Integer.parseInt(BuyIn.getText().toString());
            int bigBlind = Integer.parseInt(BigBlind.getText().toString());
            int smallBlind = Integer.parseInt(SmallBlind.getText().toString());
            int minBet = Integer.parseInt(MinBet.getText().toString());
            int handsPerBlindLevel = Integer.parseInt(HandsPerBlindLevel.getText().toString());
            int maxBlindLevel = Integer.parseInt(MaxBlindLevel.getText().toString());
            int blindIncrease = Integer.parseInt(BlindIncrease.getText().toString());

            if (bigBlind < smallBlind) {
                Toast toast = Toast.makeText(getApplicationContext(), "Big blind is < small", Toast.LENGTH_SHORT);
                toast.show();
            } else if (bigBlind > buyIn - minBet) {
                Toast toast = Toast.makeText(getApplicationContext(), "Big blind is to large", Toast.LENGTH_SHORT);
                toast.show();
            } else if (handsPerBlindLevel == 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "0 cant not be selected for on the 4th setting", Toast.LENGTH_LONG);
                toast.show();
            } else if (minBet == 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "min bet cannot = 0", Toast.LENGTH_LONG);
                toast.show();
            } else {
                ////////////////////////create rules
                TournamentRules rule = new TournamentRules(buyIn, bigBlind, smallBlind, minBet, handsPerBlindLevel, maxBlindLevel, blindIncrease);
                ruleID = myRefR.child("Tournament").push().getKey();
                Log.d("valuesFromSub", "Rule id: " + ruleID);
                myRefR.child("Tournament").child(ruleID).setValue(rule);


                /////////////////////////////create game
                Card card = new Card(Suit.SPADES, Rank.ACE);
                Player player = new Player("", "", 0, card, card);
                TableHand tableHand = new TableHand(true, false, false, false, false, false, card, card, card, card, card);
                Player host = new Player(uid, username, 0, card, card);
                Games game = new Games(host, player, player, player, player, player, false, 1, tableHand, ruleID, 0, 0);
                gameID = myRefG.push().getKey();
                myRefG.child(gameID).setValue(game);

                Intent intent = new Intent(this, LobbyTActivity.class);
                intent.putExtra("host", true);
                intent.putExtra("gameId", gameID);
                startActivity(intent);
            }

        }

    }}
