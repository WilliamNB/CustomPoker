package com.example.williamnestius_brow.custompoker;

public class CashRules {

    private int maxBuyIn;
    private int minBuyIn;
    private int bigBlind;
    int smallBlind;
    int minBet;

    public CashRules(){}

    public CashRules(int MaxBuyIn, int MinBuyIn, int BigBlind, int SmallBlind, int MinBet){
        this.maxBuyIn = MaxBuyIn;
        this.minBuyIn = MinBuyIn;
        this.bigBlind = BigBlind;
        this.smallBlind = SmallBlind;
        this.minBet = MinBet;
    }

    public int getMaxBuyIn() {
        return maxBuyIn;
    }

    public int getMinBuyIn() {
        return minBuyIn;
    }

    public int getBigBlind() {
        return bigBlind;
    }


}
