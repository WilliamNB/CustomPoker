package com.example.williamnestius_brow.custompoker;

public class TournamentRules {
    int buyIn;
    int bigBlind;
    int smallBlind;
    int minBet;
    int handsPerBlindLevel;
    int maxBlindLevel;
    int blindIncrease;

    public TournamentRules(){}

    public TournamentRules(int BuyIn, int BigBlind, int SmallBlind, int MinBet, int HandsPerBlindLevel, int MaxBlindLevel, int BlindIncrease){
        this.buyIn = BuyIn;
        this.bigBlind = BigBlind;
        this.smallBlind = SmallBlind;
        this.minBet = MinBet;
        this.handsPerBlindLevel = HandsPerBlindLevel;
        this.maxBlindLevel = MaxBlindLevel;
        this.blindIncrease = BlindIncrease;
    }
}
