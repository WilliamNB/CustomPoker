package com.example.williamnestius_brow.custompoker;

import java.util.List;

public class Player {
    private String uid;
    public boolean active;
    private String username;
    private int currentAmount;
    private Card card1;
    private Card card2;
    private boolean doneAction;
    private Card highCard = null;
    private Ranking ranking = null;
    private List<Card> rankingList = null;

    public Player(){}

    public Player(String Uid, String Username, int CurrentAmount, Card Card1, Card Card2){
        this.uid = Uid;
        this.username = Username;
        this.currentAmount = CurrentAmount;
        this.card1 = Card1;
        this.card2 = Card2;
        this.active = false;
        this.doneAction = false;
    }

    public Player(String Uid, String Username){
        this.uid = Uid;
        this.username = Username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }
    public int getCurrentAmount() {
        return currentAmount;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public boolean isActive() {
        return active; }
    public void setActive(boolean active) {
        this.active = active; }

    public Card getCard1() {
        return card1;
    }
    public void setCard1(Card card1) {
        this.card1 = card1;
    }

    public Card getCard2() {
        return card2;
    }
    public void setCard2(Card card2) {
        this.card2 = card2;
    }

    public boolean isDoneAction() {
        return doneAction;
    }
    public void setDoneAction(boolean doneAction) {
        this.doneAction = doneAction;
    }

    public Ranking getRanking() {
        return ranking;
    }
    public void setRanking(Ranking Ranking) {
        this.ranking = Ranking;
    }

    public List<Card> getRankingList() {
        return rankingList;
    }
    public void setRankingList(List<Card> rankingList) {
        this.rankingList = rankingList;
    }

    public Card getHighCard() {
        return highCard;
    }
    public void setHighCard(Card highCard) {
        this.highCard = highCard;
    }
}


