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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TournamentGameActivity extends AppCompatActivity {

    int buyIn, bigBlindValue, smallBlindValue, minBet, handsPerBlindLevel, maxBlindLevel, blindIncrease, blindLevel,smallBlindP, bigBlindP, activeP;
    int playersInGame;
    private FirebaseDatabase database;
    private DatabaseReference myRefG, myRefR, myRefU;
    private String gameID, ruleID, playerID, uid;
    private boolean isHost, isActive, sDone, bDone, runOnce, foldOnce, start;
    private Player player1, player2, player3, player4, player5, player6, userPlayer;
    private TableHand table;
    private int[] playersInHand;
    private ArrayList<Card> playerHand;
    // private ArrayList<Player> playersInHand;
    private List<Card> tableCards;
    private List<Player> endPlayers, allPlayers;
    private int playerNumber;
    private int amountPlayed, currentPot, amountToPlay, currentStage, actionCounter, winner, round, checkRound, Rc;
    private ImageView playerCard1, playerCard2, tableCard1, tableCard2, tableCard3, tableCard4, tableCard5;
    private String PlayerCard1, PlayerCard2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        myRefG = database.getReference("Games");
        myRefR = database.getReference("Rules");
        myRefU = database.getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        Intent startIntent = getIntent();
        gameID = startIntent.getStringExtra("GameID");
        ruleID = startIntent.getStringExtra("RuleID");
        isHost = startIntent.getBooleanExtra("IsHost", false);
        buyIn = startIntent.getIntExtra("BuyIn", buyIn);
        bigBlindValue = startIntent.getIntExtra("BigBlindValue", bigBlindValue);
        smallBlindValue = startIntent.getIntExtra("SmallBlindValue", smallBlindValue);
        minBet = startIntent.getIntExtra("MinBet", minBet);
        handsPerBlindLevel = startIntent.getIntExtra("HandsPerBlindLevel", handsPerBlindLevel);
        maxBlindLevel = startIntent.getIntExtra("MaxBlindLevel", maxBlindLevel);
        blindIncrease = startIntent.getIntExtra("BlindIncrease", blindIncrease);
        Log.d("rules", "hands" + handsPerBlindLevel);
        Log.d("rules", "max blind" + maxBlindLevel);
        Log.d("rules", "increase" + blindIncrease);

        playerHand = new ArrayList<Card>();
        playerID = Integer.toString(playerNumber);
        amountPlayed = 0;
        checkRound = 0;
        Rc = 0;
        runOnce = true; foldOnce = true;
        start = true;
        blindLevel = 1;


        playerCard1 = (ImageView) findViewById(R.id.playerCard1);
        playerCard2 = (ImageView) findViewById(R.id.playerCard2);

        tableCards = new ArrayList<Card>();
        tableCard1 = (ImageView) findViewById(R.id.tableCard1);
        tableCard2 = (ImageView) findViewById(R.id.tableCard2);
        tableCard3 = (ImageView) findViewById(R.id.tableCard3);
        tableCard4 = (ImageView) findViewById(R.id.tableCard4);
        tableCard5 = (ImageView) findViewById(R.id.tableCard5);

        player1 = new Player();
        player2 = new Player();
        player3 = new Player();
        player4 = new Player();
        player5 = new Player();
        player6 = new Player();
        userPlayer = new Player();
        endPlayers = new ArrayList<Player>();
        allPlayers = new ArrayList<Player>();

        table = new TableHand();

        //gets each players info, names, amounts, cards
        myRefG.child(gameID).child("round").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               Log.d("firstToShow", "round");
                round = snapshot.getValue(int.class);
                if(blindLevel <= maxBlindLevel){
                    Log.d("rules", "blind level " + blindLevel);
                    if((handsPerBlindLevel+1) % (round + 2) == 0){
                        Log.d("rules", "blind increase");
                        bigBlindValue += blindIncrease;
                        smallBlindValue += blindIncrease;
                        blindLevel += 1;
                    }
                }
           }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        myRefG.child(gameID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playersInGame = snapshot.child("playersInGame").getValue(int.class);
                round = snapshot.child("round").getValue(int.class);
                if (playersInGame == 1){
                    Toast toast = Toast.makeText(getApplicationContext(), "Last Player", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if(checkRound != round){
                    int imageId = getResources().getIdentifier("back_of_card", "drawable", getPackageName());
                    tableCard1.setImageResource(imageId);
                    tableCard2.setImageResource(imageId);
                    tableCard3.setImageResource(imageId);
                    tableCard4.setImageResource(imageId);
                    tableCard5.setImageResource(imageId);
                    if(!isHost && userPlayer.getCurrentAmount() <= 0){
                        Log.d("leaving", "no money not host ");
                        leave(playerNumber);
                    }
                    checkRound += 1;
                }

                player1.setUsername(snapshot.child("host").getValue(Player.class).getUsername());
                player1.setUid(snapshot.child("host").getValue(Player.class).getUid());
                TextView p1Name = (TextView) findViewById(R.id.UserName1);
                p1Name.setText(player1.getUsername());
                player1.setCurrentAmount(snapshot.child("host").getValue(Player.class).getCurrentAmount());
                player1.setCard1(snapshot.child("host").getValue(Player.class).getCard1());
                player1.setCard2(snapshot.child("host").getValue(Player.class).getCard2());
                player1.setDoneAction(snapshot.child("host").getValue(Player.class).isDoneAction());
                player1.setActive(snapshot.child("host").getValue(Player.class).isActive());
                TextView p1Amount = (TextView) findViewById(R.id.UserAmount1);
                String amount1 = Integer.toString(player1.getCurrentAmount());
                p1Amount.setText(amount1);
                allPlayers.add(0,player1);

                player2.setUsername(snapshot.child("player2").getValue(Player.class).getUsername());
                player2.setUid(snapshot.child("player2").getValue(Player.class).getUid());
                TextView p2Name = (TextView) findViewById(R.id.UserName2);
                p2Name.setText(player2.getUsername());
                player2.setCurrentAmount(snapshot.child("player2").getValue(Player.class).getCurrentAmount());
                player2.setCard1(snapshot.child("player2").getValue(Player.class).getCard1());
                player2.setCard2(snapshot.child("player2").getValue(Player.class).getCard2());
                player2.setDoneAction(snapshot.child("player2").getValue(Player.class).isDoneAction());
                player2.setActive(snapshot.child("player2").getValue(Player.class).isActive());
                TextView p2Amount = (TextView) findViewById(R.id.UserAmount2);
                String amount2 = Integer.toString(player2.getCurrentAmount());
                p2Amount.setText(amount2);
                allPlayers.add(1,player2);

                player3.setUsername(snapshot.child("player3").getValue(Player.class).getUsername());
                player3.setUid(snapshot.child("player3").getValue(Player.class).getUid());
                TextView p3Name = (TextView) findViewById(R.id.UserName3);
                p3Name.setText(player3.getUsername());
                player3.setCurrentAmount(snapshot.child("player3").getValue(Player.class).getCurrentAmount());
                player3.setCard1(snapshot.child("player3").getValue(Player.class).getCard1());
                player3.setCard2(snapshot.child("player3").getValue(Player.class).getCard2());
                player3.setDoneAction(snapshot.child("player3").getValue(Player.class).isDoneAction());
                player3.setActive(snapshot.child("player3").getValue(Player.class).isActive());
                TextView p3Amount = (TextView) findViewById(R.id.UserAmount3);
                String amount3 = Integer.toString(player3.getCurrentAmount());
                p3Amount.setText(amount3);
                allPlayers.add(2,player3);

                player4.setUsername(snapshot.child("player4").getValue(Player.class).getUsername());
                player4.setUid(snapshot.child("player4").getValue(Player.class).getUid());
                TextView p4Name = (TextView) findViewById(R.id.UserName4);
                p4Name.setText(player4.getUsername());
                player4.setCurrentAmount(snapshot.child("player4").getValue(Player.class).getCurrentAmount());
                player4.setCard1(snapshot.child("player4").getValue(Player.class).getCard1());
                player4.setCard2(snapshot.child("player4").getValue(Player.class).getCard2());
                player4.setDoneAction(snapshot.child("player4").getValue(Player.class).isDoneAction());
                player4.setActive(snapshot.child("player4").getValue(Player.class).isActive());
                TextView p4Amount = (TextView) findViewById(R.id.UserAmount4);
                String amount4 = Integer.toString(player4.getCurrentAmount());
                p4Amount.setText(amount4);
                allPlayers.add(3,player4);

                player5.setUsername(snapshot.child("player5").getValue(Player.class).getUsername());
                player5.setUsername(snapshot.child("player5").getValue(Player.class).getUid());
                TextView p5Name = (TextView) findViewById(R.id.UserName5);
                p5Name.setText(player5.getUsername());
                player5.setCurrentAmount(snapshot.child("player5").getValue(Player.class).getCurrentAmount());
                player5.setCard1(snapshot.child("player5").getValue(Player.class).getCard1());
                player5.setCard2(snapshot.child("player5").getValue(Player.class).getCard2());
                player5.setDoneAction(snapshot.child("player5").getValue(Player.class).isDoneAction());
                player5.setActive(snapshot.child("player5").getValue(Player.class).isActive());
                TextView p5Amount = (TextView) findViewById(R.id.UserAmount5);
                String amount5 = Integer.toString(player5.getCurrentAmount());
                p5Amount.setText(amount5);
                allPlayers.add(4,player5);

                player6.setUsername(snapshot.child("player6").getValue(Player.class).getUsername());
                player6.setUid(snapshot.child("player6").getValue(Player.class).getUid());
                TextView p6Name = (TextView) findViewById(R.id.UserName6);
                p6Name.setText(player6.getUsername());
                player6.setCurrentAmount(snapshot.child("player6").getValue(Player.class).getCurrentAmount());
                player6.setCard1(snapshot.child("player6").getValue(Player.class).getCard1());
                player6.setCard2(snapshot.child("player6").getValue(Player.class).getCard2());
                player6.setDoneAction(snapshot.child("player6").getValue(Player.class).isDoneAction());
                player6.setActive(snapshot.child("player6").getValue(Player.class).isActive());
                TextView p6Amount = (TextView) findViewById(R.id.UserAmount6);
                String amount6 = Integer.toString(player6.getCurrentAmount());
                p6Amount.setText(amount6);
                allPlayers.add(5,player6);

                for(int i = 2; i <= playersInGame; i++){
                    String si = Integer.toString(i);
                    String gotUid = (String) snapshot.child("player" + si).child("uid").getValue();
                    if(uid.equals(gotUid)){
                        playerNumber = i;
                        break;
                    }
                }

                if (isHost && start) {
                    Log.d("maxBuyIn", "players before deal " + playersInGame);
                    playerNumber = 1;
                    start = false;
                    newRound();
                }

                for(int i = 0; i < playersInGame; i++){
                    int[] imageViewIDs = new int[] {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6};
                    if(allPlayers.get(i).isActive()){
                        ImageView imageView = (ImageView) findViewById(imageViewIDs[i]);
                        imageView.setBackgroundColor(getResources().getColor(R.color.active));
                    }else {
                        ImageView imageView = (ImageView) findViewById(imageViewIDs[i]);
                        imageView.setBackgroundColor(getResources().getColor(R.color.notActive));
                    }
                }

                Log.d("firstToShow", "player variables");


                Log.d("doneAction", "p1" + player1.isDoneAction());
                Log.d("doneAction", "p2" + player2.isDoneAction());
                Log.d("doneAction", "AC" + actionCounter);

                currentStage = snapshot.child("currentStage").getValue(int.class);
                amountToPlay = snapshot.child("currentPlayAmount").getValue(int.class);
                currentPot = snapshot.child("currentPot").getValue(int.class);
                TextView PotAmount = (TextView) findViewById(R.id.potAmount);
                PotAmount.setText("Current Pot:  " + currentPot);
                showPlayerCards();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        myRefG.child(gameID).child("table").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot blinds){
                Log.d("firstToShow", "table variables");
                table.setSmallBlindP(blinds.getValue(TableHand.class).getSmallBlindP());
                table.setBigBlindP(blinds.getValue(TableHand.class).getBigBlindP());
                table.setPlayer1In(blinds.getValue(TableHand.class).isPlayer1In());
                table.setPlayer2In(blinds.getValue(TableHand.class).isPlayer2In());
                table.setPlayer3In(blinds.getValue(TableHand.class).isPlayer3In());
                table.setPlayer4In(blinds.getValue(TableHand.class).isPlayer4In());
                table.setPlayer5In(blinds.getValue(TableHand.class).isPlayer5In());
                table.setPlayer6In(blinds.getValue(TableHand.class).isPlayer6In());
                table.setTableCard1(blinds.getValue(TableHand.class).getTableCard1());
                table.setTableCard2(blinds.getValue(TableHand.class).getTableCard2());
                table.setTableCard3(blinds.getValue(TableHand.class).getTableCard3());
                table.setTableCard4(blinds.getValue(TableHand.class).getTableCard4());
                table.setTableCard5(blinds.getValue(TableHand.class).getTableCard5());
                actionCounter = blinds.child("actionCounter").getValue(int.class);


                if(actionCounter == playersInGame){
                    showTableCards();
                }

                asignCurrentUser();

                int[] imageViewBlind = new int[] {R.id.blind1, R.id.blind1, R.id.blind2, R.id.blind3, R.id.blind4, R.id.blind5, R.id.blind6};
                for(int i = 1; i < imageViewBlind.length; i++) {
                    if (i == table.getSmallBlindP()) {
                        ImageView imageViewSb = (ImageView) findViewById(imageViewBlind[table.getSmallBlindP()]);
                        imageViewSb.setBackgroundColor(getResources().getColor(R.color.smallBlind));
                    } else if (i == table.getBigBlindP()) {
                        ImageView imageViewBb = (ImageView) findViewById(imageViewBlind[table.getBigBlindP()]);
                        imageViewBb.setBackgroundColor(getResources().getColor(R.color.bigBlind));
                    } else if (i == activeP && i != table.getSmallBlindP() && i != table.getBigBlindP()) {
                        ImageView imageViewA = (ImageView) findViewById(imageViewBlind[i]);
                        imageViewA.setBackgroundColor(getResources().getColor(R.color.active));
                    } else {
                        ImageView imageViewB = (ImageView) findViewById(imageViewBlind[i]);
                        imageViewB.setBackgroundColor(getResources().getColor(R.color.notActive));
                    }
                }


                if(currentStage == 0 && !sDone && !bDone ) {
                    if (table.getSmallBlindP() == playerNumber) {
                        int newCurrent = (userPlayer.getCurrentAmount() - smallBlindValue);
                        if (isHost) {
                            myRefG.child(gameID).child("host").child("currentAmount").setValue(newCurrent);
                            sDone = true;
                            amountPlayed += smallBlindValue;
                        } else {
                            myRefG.child(gameID).child("player" + playerNumber).child("currentAmount").setValue(newCurrent);
                            sDone = true;
                            amountPlayed += smallBlindValue;
                        }
                        Rc += 1;
                    } else if (table.getBigBlindP() == playerNumber) {
                        int newCurrent = (userPlayer.getCurrentAmount() - bigBlindValue);
                        if (isHost) {
                            myRefG.child(gameID).child("host").child("currentAmount").setValue(newCurrent);
                            bDone = true;
                            amountPlayed += bigBlindValue;
                        } else {
                            myRefG.child(gameID).child("player" + playerNumber).child("currentAmount").setValue(newCurrent);
                            bDone = true;
                            amountPlayed += bigBlindValue;
                        }
                        myRefG.child(gameID).child("currentPot").setValue(smallBlindValue + bigBlindValue);
                        Rc += 1;
                    }
                    myRefG.child(gameID).child("currentPlayAmount").setValue(bigBlindValue);
                }


                int last = table.lastPlayer();
                if (last == 1 && foldOnce) {
                    winner = table.getLast();
                    if(winner == playerNumber) {
                        if(isHost){
                            Toast toast = Toast.makeText(getApplicationContext(), "Winner" + winner, Toast.LENGTH_SHORT);
                            toast.show();
                            runOnce = false;
                            winOnFold(winner);
                            foldOnce = false;
                        }
                        Toast toast = Toast.makeText(getApplicationContext(), "Winner" + winner, Toast.LENGTH_SHORT);
                        toast.show();
                        int imageId = getResources().getIdentifier("back_of_card", "drawable", getPackageName());
                        tableCard1.setImageResource(imageId);
                        tableCard2.setImageResource(imageId);
                        tableCard3.setImageResource(imageId);
                        tableCard4.setImageResource(imageId);
                        tableCard5.setImageResource(imageId);
                        bDone = false;
                        sDone = false;
                        amountPlayed = 0;
                    }else if(isHost) {
                        Log.d("matchblind", "winner != host ");
                        runOnce = false;
                        winOnFold(winner);
                        foldOnce = false;
                    }else{
                        Log.d("matchblind", "winner != player || h ");
                        bDone = false;
                        sDone = false;
                        amountPlayed = 0;
                        int imageId = getResources().getIdentifier("back_of_card", "drawable", getPackageName());
                        tableCard1.setImageResource(imageId);
                        tableCard2.setImageResource(imageId);
                        tableCard3.setImageResource(imageId);
                        tableCard4.setImageResource(imageId);
                        tableCard5.setImageResource(imageId);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        Button btnC= (Button) findViewById(R.id.checkButton);
        btnC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (userPlayer.isActive()) {
                    if(amountPlayed == amountToPlay){
                        changeAction();
                        changeActive();
                    }else if((userPlayer.getCurrentAmount() == 0) && table.isIn(playerNumber)) {
                        changeAction();
                        changeActive();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bet has been made, can't check", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        Button btnM= (Button) findViewById(R.id.matchButton);
        btnM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userPlayer.isActive()) {
                    int amountToMatch = (amountToPlay - amountPlayed);
                    if(isHost){
                        myRefG.child(gameID).child("host").child("currentAmount").setValue(userPlayer.getCurrentAmount() - amountToMatch);
                    }else {
                        myRefG.child(gameID).child("player" + playerNumber).child("currentAmount").setValue(userPlayer.getCurrentAmount() - amountToMatch);
                    }
                    amountPlayed += amountToMatch;
                    myRefG.child(gameID).child("currentPot").setValue(currentPot + amountToMatch);
                    changeAction();
                    changeActive();
                }else{
                    Log.d("setuptabel", "clicked match but not active");
                }
            }
        });

        Button btnR= (Button) findViewById(R.id.raiseButton);
        btnR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userPlayer.isActive()) {
                    EditText raiseAmount = (EditText) findViewById(R.id.raiseInput);
                    if(raiseAmount.getText().toString().isEmpty()){ //&& (amountPlayed == amountToPlay)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Enter Raise Value", Toast.LENGTH_SHORT);
                        toast.show();
                    }else {
                        int RaiseAmount = Integer.parseInt(raiseAmount.getText().toString());
                        int amountToMatch = (amountToPlay - amountPlayed);
                        if (RaiseAmount < minBet) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Raise above the min bet", Toast.LENGTH_SHORT);
                            toast.show();
                        }else if(RaiseAmount > userPlayer.getCurrentAmount()){
                            Toast toast = Toast.makeText(getApplicationContext(), "Cannot raise more than you have", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (RaiseAmount > amountToMatch) {
                            int amountIncrease = RaiseAmount - amountToMatch;
                            amountToPlay += amountIncrease;
                            amountPlayed += RaiseAmount;
                            if (isHost) {
                                myRefG.child(gameID).child("host").child("currentAmount").setValue(userPlayer.getCurrentAmount() - RaiseAmount);
                            } else {
                                myRefG.child(gameID).child("player" + playerNumber).child("currentAmount").setValue(userPlayer.getCurrentAmount() - RaiseAmount);
                            }
                            myRefG.child(gameID).child("currentPot").setValue(currentPot + RaiseAmount);
                            myRefG.child(gameID).child("currentPlayAmount").setValue(amountToPlay);
                            actionCounter = 0;
                            changeAction();
                            changeActive();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Raise amount is < last bet", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    int[] array = table.remainingPlayers();
                    for(int i: array){
                        int player = array[i];
                        if(player == 1 && player != playerNumber){
                            myRefG.child(gameID).child("host").child("doneAction").setValue(false);
                        }else if(player != playerNumber) {
                            myRefG.child(gameID).child("player" + player).child("doneAction").setValue(false);
                        }
                    }
                }else{
                    Log.d("setuptabel", "clicked bet but not active");
                }
            }
        });

        Button btnF= (Button) findViewById(R.id.foldButton);
        btnF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userPlayer.isActive()) {
                    Log.d("setuptabel", "clicked fold");
                    myRefG.child(gameID).child("table").child("player"+playerNumber+"In").setValue(false);
                    bDone = true;
                    sDone = true;
                    changeAction();
                    changeActive();
                }else{
                    Log.d("setuptabel", "clicked fold but not active");
                }
            }
        });
//
    }

    public void showPlayerCards(){
        switch (playerNumber){
            case 2:
                PlayerCard1 = getCardImage(player2.getCard1());
                setCardImage(PlayerCard1, playerCard1);
                PlayerCard2 = getCardImage(player2.getCard2());
                setCardImage(PlayerCard2, playerCard2);
                break;
            case 3:
                PlayerCard1 = getCardImage(player3.getCard1());
                setCardImage(PlayerCard1, playerCard1);
                PlayerCard2 = getCardImage(player3.getCard2());
                setCardImage(PlayerCard2, playerCard2);
                break;
            case 4:
                PlayerCard1 = getCardImage(player4.getCard1());
                setCardImage(PlayerCard1, playerCard1);
                PlayerCard2 = getCardImage(player4.getCard2());
                setCardImage(PlayerCard2, playerCard2);
                break;
            case 5:
                PlayerCard2 = getCardImage(player5.getCard1());
                setCardImage(PlayerCard1, playerCard1);
                PlayerCard2 = getCardImage(player5.getCard2());
                setCardImage(PlayerCard2, playerCard2);
                break;
            case 6:
                PlayerCard1 = getCardImage(player6.getCard1());
                setCardImage(PlayerCard1, playerCard1);
                PlayerCard2 = getCardImage(player6.getCard2());
                setCardImage(PlayerCard2, playerCard2);
                break;
        }
    }
    public void showTableCards(){
        Log.d("doneAction", "in show table cards");
        if(currentStage == 0) {
            setCardImage(getCardImage(table.getTableCard1()), tableCard1);
            setCardImage(getCardImage(table.getTableCard2()), tableCard2);
            setCardImage(getCardImage(table.getTableCard3()), tableCard3);
            myRefG.child(gameID).child("table").child("actionCounter").setValue(0);
            myRefG.child(gameID).child("currentStage").setValue(1);
        }else if(currentStage == 1){
            setCardImage(getCardImage(table.getTableCard4()), tableCard4);
            myRefG.child(gameID).child("table").child("actionCounter").setValue(0);
            myRefG.child(gameID).child("currentStage").setValue(2);
        }else if(currentStage == 2){
            setCardImage(getCardImage(table.getTableCard5()), tableCard5);
            myRefG.child(gameID).child("table").child("actionCounter").setValue(0);
            myRefG.child(gameID).child("currentStage").setValue(3);
        }else if(currentStage == 3){
            if(isHost && runOnce) {
                Log.d("userHand", "in host stage 3");
                runOnce = false;
                myRefG.child(gameID).child("table").child("actionCounter").setValue(0);
                amountPlayed = 0;
                getHand();
                Button nextRound = (Button) findViewById(R.id.nextRounBtn);
                nextRound.setVisibility(View.VISIBLE);
            }
            else{
                amountPlayed = 0;
                bDone = false;
                sDone = false;
            }
        }

    }

    public void dealNewTable(){
        Deck newDeck = new Deck();
        Card newCard;
        for(int i = 1; i <= playersInGame; i++){
            Log.d("maxBuyIn", "i " + i);
            Log.d("maxBuyIn", "players " + playersInGame);
            if(i == 1){
                newCard = newDeck.nextCard();
                PlayerCard1 = getCardImage(newCard);
                setCardImage(PlayerCard1, playerCard1);
                myRefG.child(gameID).child("host").child("card1").setValue(newCard);

                newCard = newDeck.nextCard();
                PlayerCard2 = getCardImage(newCard);
                setCardImage(PlayerCard2, playerCard2);
                playerHand.add(newCard);
                myRefG.child(gameID).child("host").child("card2").setValue(newCard);
            }else{
                String si = Integer.toString(i);
                newCard = newDeck.nextCard();
                myRefG.child(gameID).child("player" + si).child("card1").setValue(newCard);
                newCard = newDeck.nextCard();
                myRefG.child(gameID).child("player" + si).child("card2").setValue(newCard);
            }
        }

        newCard = newDeck.nextCard();
        myRefG.child(gameID).child("table").child("tableCard1").setValue(newCard);
        newCard = newDeck.nextCard();
        myRefG.child(gameID).child("table").child("tableCard2").setValue(newCard);
        newCard = newDeck.nextCard();
        myRefG.child(gameID).child("table").child("tableCard3").setValue(newCard);
        newCard = newDeck.nextCard();
        myRefG.child(gameID).child("table").child("tableCard4").setValue(newCard);
        newCard = newDeck.nextCard();
        myRefG.child(gameID).child("table").child("tableCard5").setValue(newCard);
    }

    public String getCardImage(Card card){
        Rank cardRank1 = card.getRank();
        Suit cardSuit1 = card.getSuit();
        String cardDets1 = cardRank1 + " of " + cardSuit1;
        String PlayerCard = card.getCardIcon(cardDets1);
        return PlayerCard;
    }


    public void setCardImage(String card, ImageView imageView) {
        //card param example= "ace_of_spades"
        int imageId = getResources().getIdentifier(card, "drawable", getPackageName());
        imageView.setImageResource(imageId);
    }

    public void changeAction(){
        if(isHost){
            myRefG.child(gameID).child("host").child("doneAction").setValue(true);
        }else {
            myRefG.child(gameID).child("player" + playerNumber).child("doneAction").setValue(true);
        }
        actionCounter += 1;
        myRefG.child(gameID).child("table").child("actionCounter").setValue(actionCounter);
    }
    public void changeActive(){
        if(isHost){
            myRefG.child(gameID).child("host").child("active").setValue(false);
            int nextPlayer = table.nextIn(1);
            myRefG.child(gameID).child("player" + nextPlayer).child("active").setValue(true);
        }else {
            if (playerNumber == playersInGame) {
                myRefG.child(gameID).child("player" + playerNumber).child("active").setValue(false);
                int nextPlayer = table.nextIn(0);
                if(nextPlayer == 1){
                    myRefG.child(gameID).child("host").child("active").setValue(true);
                }else {
                    myRefG.child(gameID).child("player" + nextPlayer).child("active").setValue(true);
                }
            }else {
                myRefG.child(gameID).child("player" + playerNumber).child("active").setValue(false);
                int nextPlayer = table.nextIn(playerNumber );
                myRefG.child(gameID).child("player" + nextPlayer).child("active").setValue(true);
            }
        }
    }

    public void addPlayersToHand(){
        Log.d("setuptabel", "in addPlayersToHand");
        playersInHand = new int[playersInGame];
        for(int i = 0; i < playersInGame; i++){
            int uNum = i +1;
            playersInHand[i] = uNum;
            String Si =Integer.toString(uNum);
            String userIn = "player" + Si + "In";
            myRefG.child(gameID).child("table").child(userIn).setValue(true);
        }
        ////// array index starts at 0
        if(playersInHand.length == 2){
            Log.d("setuptabel", "only 2 players");
            smallBlindP = playersInHand[0];
            bigBlindP = playersInHand[1];
            activeP = playersInHand[0];
            String si = Integer.toString(2);
            myRefG.child(gameID).child("player" + si).child("active").setValue(true);
        }else{
            Log.d("setuptabel", "normal");
            smallBlindP = playersInHand[0];
            bigBlindP = playersInHand[1];
            activeP = playersInHand[2];
            String si = Integer.toString(activeP);
            myRefG.child(gameID).child("player" + si).child("active").setValue(true);
        }
        userPlayer = player1;
        Log.d("setuptabe", "sb = "+smallBlindP);
        Log.d("setuptabe", "bb = "+bigBlindP);
        myRefG.child(gameID).child("table").child("smallBlindP").setValue(smallBlindP);
        myRefG.child(gameID).child("table").child("bigBlindP").setValue(bigBlindP);

        myRefG.child(gameID).child("currentPot").setValue(table.getSmallBlindP() + table.getBigBlindP());
    }

    public void newRound(){
        addPlayersToHand();
        dealNewTable();
    }

    public void asignCurrentUser(){
        switch (playerNumber){
            case 2:
                userPlayer = player2;
                Log.d("setuptab", "userplayer asigned player2");
                break;
            case 3:
                userPlayer = player3;
                break;
            case 4:
                userPlayer = player4;
                break;
            case 5:
                userPlayer = player5;
                break;
            case 6:
                userPlayer = player6;
                break;
        }
    }

    public void moveBlinds(){
        bDone = false;
        sDone = false;
        int sb = smallBlindP;
        int bb = bigBlindP;
        if(playersInGame == 2){
            Log.d("blindsChange", "1");
            smallBlindP = bb;
            bigBlindP = sb;
        }else if(bigBlindP == playersInGame){
            Log.d("blindsChange", "2");
            bigBlindP = 1;
            smallBlindP += 1;
        }else if(smallBlindP == playersInGame){
            Log.d("blindsChange", "3");
            smallBlindP = 1;
            bigBlindP += 1;
        }else{
            Log.d("blindsChange", "4");
            smallBlindP += 1;
            bigBlindP += 1;
        }
        if(bigBlindP == playersInGame){
            myRefG.child(gameID).child("host").child("active").setValue(true);
        }else {
            int nextActive = bigBlindP + 1;
            myRefG.child(gameID).child("player"+nextActive).child("active").setValue(true);
        }
        myRefG.child(gameID).child("currentStage").setValue(0);
        myRefG.child(gameID).child("table").child("smallBlindP").setValue(smallBlindP);
        myRefG.child(gameID).child("table").child("bigBlindP").setValue(bigBlindP);
    }

    public void nextRound(){
        //dealNewTable();
        myRefG.child(gameID).child("currentPlayAmount").setValue(0);
        myRefG.child(gameID).child("currentPot").setValue(0);
        myRefG.child(gameID).child("host").child("doneAction").setValue(false);
        myRefG.child(gameID).child("host").child("active").setValue(false);
        myRefG.child(gameID).child("table").child("player1In").setValue(true);
        for(int i = 2; i <= playersInGame; i++){
            myRefG.child(gameID).child("player" + i).child("doneAction").setValue(false);
            myRefG.child(gameID).child("player" + i).child("active").setValue(false);
            myRefG.child(gameID).child("table").child("player"+i+"In").setValue(true);
        }
        moveBlinds();
        dealNewTable();
        int imageId = getResources().getIdentifier("back_of_card", "drawable", getPackageName());
        tableCard1.setImageResource(imageId);
        tableCard2.setImageResource(imageId);
        tableCard3.setImageResource(imageId);
        tableCard4.setImageResource(imageId);
        tableCard5.setImageResource(imageId);
        runOnce = true;
        foldOnce = true;
    }

    public void winOnFold(int winner) {
        if (winner == 1) {
            myRefG.child(gameID).child("host").child("currentAmount").setValue(userPlayer.getCurrentAmount() + currentPot);
        } else {
            myRefG.child(gameID).child("player" + winner).child("currentAmount").setValue(allPlayers.get(winner - 1).getCurrentAmount() + currentPot);
        }
        if (isHost) {
            myRefG.child(gameID).child("table").child("actionCounter").setValue(0);
            amountPlayed = 0;
            Button nextRound = (Button) findViewById(R.id.nextRounBtn);
            nextRound.setVisibility(View.VISIBLE);
        }
    }


    private void checkPlayersRanking() {
        for (Player player : endPlayers) {
            HandEval.rankHand(player, tableCards);
        }
    }

    public void getHand() {
        Log.d("userHand", "in get hand");
        int[] test = table.remainingPlayers();
        for(int i = 0; i < test.length; i++){
            switch (test[i]){
                case 1:
                    endPlayers.add(player1);
                    break;
                case 2:
                    endPlayers.add(player2);
                    break;
                case 3:
                    endPlayers.add(player3);
                    break;
                case 4:
                    endPlayers.add(player4);
                    break;
                case 5:
                    endPlayers.add(player5);
                    break;
                case 6:
                    endPlayers.add(player6);
                    break;
            }
        }
        tableCards.add(table.getTableCard1());
        tableCards.add(table.getTableCard2());
        tableCards.add(table.getTableCard3());
        tableCards.add(table.getTableCard4());
        tableCards.add(table.getTableCard5());
        checkPlayersRanking();
        List<Player> Champion = getWinner();
        if(Champion.get(0).equals(player1)){
            winner = 1;
        }else if(Champion.get(0).equals(player2)){
            winner = 2;
        }else if(Champion.get(0).equals(player3)){
            winner = 3;
        }else if(Champion.get(0).equals(player4)){
            winner = 4;
        }else if(Champion.get(0).equals(player5)){
            winner = 5;
        }else if(Champion.get(0).equals(player6)){
            winner = 6;
        }else{
            Log.d("error", "no winner");
        }
        Toast toast = Toast.makeText(getApplicationContext(), "Winner " + Champion.get(0).getUsername() + ", with" + Champion.get(0).getRanking(), Toast.LENGTH_SHORT);
        toast.show();
        if(winner == 1){
            myRefG.child(gameID).child("host").child("currentAmount").setValue(userPlayer.getCurrentAmount() + currentPot);

        }else {
            myRefG.child(gameID).child("player" + winner).child("currentAmount").setValue(userPlayer.getCurrentAmount() + currentPot);
        }

    }

    public List<Player> getWinner() {
        List<Player> winnerList = new ArrayList<Player>();
        Player winner = endPlayers.get(0);
        Integer winnerRank = HandEval.getRankingPos(winner);
        winnerList.add(winner);
        for (int i = 1; i < endPlayers.size(); i++) {
            Player player = endPlayers.get(i);
            Integer playerRank = HandEval.getRankingPos(player);
            if (winnerRank == playerRank) {
                Player highHandPlayer = checkSequence(winner, player);
                if (highHandPlayer == null) {
                    highHandPlayer = checkHighCard(winner, player);
                }
                if (highHandPlayer != null && !winner.equals(highHandPlayer)) {
                    winner = highHandPlayer;
                    winnerList.clear();
                    winnerList.add(winner);
                } else if (highHandPlayer == null) {
                    winnerList.add(winner);
                }
            } else if (winnerRank < playerRank) {
                winner = player;
                winnerList.clear();
                winnerList.add(winner);
            }
            winnerRank = HandEval.getRankingPos(winner);
        }
        return winnerList;
    }

    private Player checkSequence(Player player1, Player player2) {
        Integer player1Rank = sumRankingList(player1);
        Integer player2Rank = sumRankingList(player2);
        if (player1Rank > player2Rank) {
            return player1;
        } else if (player1Rank < player2Rank) {
            return player2;
        }
        return null;
    }
    private Player checkHighCard(Player player1, Player player2) {
        Player winner = compareHighCard(player1, player1.getHighCard(),
                player2, player2.getHighCard());
        if (winner == null) {
            Card player1Card = HandEval.getHighCard(player1, Collections.EMPTY_LIST);
            Card player2Card = HandEval.getHighCard(player2, Collections.EMPTY_LIST);
            winner = compareHighCard(player1, player1Card, player2, player2Card);
            if (winner != null) {
                player1.setHighCard(player1Card);
                player2.setHighCard(player2Card);
            } else if (winner == null) {
                winner = player1;
            }
        }
        return winner;
    }

    private Player compareHighCard(Player player1, Card player1HighCard,
                                   Player player2, Card player2HighCard) {
        if (player1HighCard.getRankToInt() > player2HighCard.getRankToInt()) {
            return player1;
        } else if (player1HighCard.getRankToInt() < player2HighCard
                .getRankToInt()) {
            return player2;
        }
        return null;
    }
    private Integer sumRankingList(Player player) {
        Integer sum = 0;
        for (Card card : player.getRankingList()) {
            sum += card.getRankToInt();
        }
        return sum;
    }

    public void onClickLeave(View view){
        Log.d("will brexit ever happen", "?");
        if(isHost){
            if (playersInGame == 1){
                leave(1);
            }else {
                Toast toast = Toast.makeText(getApplicationContext(), "Host Cant Leave when other players are in", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else if(userPlayer.isActive()){
            changeActive();
            leave(playerNumber);
            myRefG.child(gameID).child("playersInGame").setValue(playersInGame - 1);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Can only leave on your turn", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void leave(int playerNum){
        Log.d("Leaving", "leave: ");
        for(int i = playerNum; i <= playersInGame; i++) {
            myRefG.child(gameID).child("player" + i).setValue(allPlayers.get(i));
        }
        myRefG.child(gameID).child("playersInGame").setValue(playersInGame - 1);
        changeActive();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    public void onClickNextRound(View view){
        nextRound();
        Button nextRound = (Button) findViewById(R.id.nextRounBtn);
        nextRound.setVisibility(View.INVISIBLE);
        myRefG.child(gameID).child("round").setValue(round + 1);
    }


}



