package com.example.williamnestius_brow.custompoker;

public class Games {
    public Player host;
    public Player player2;
    public Player player3;
    public Player player4;
    public Player player5;
    public Player player6;
    public boolean isActive;
    public int playersInGame, currentPot,currentPlayAmount,currentStage, round;
    public TableHand table;
    public String gameRulesId;


    public Games(){}

    public Games(Player Host, Player Player2, Player Player3, Player Player4, Player Player5, Player Player6, boolean IsActive,
                 int PlayersInGame,TableHand tableHand, String GameRulesId, int CurrentPot, int CurrentPlayAmount ){
        this.host = Host;
        this.player2 = Player2;
        this.player3 = Player3;
        this.player4 = Player4;
        this.player5 = Player5;
        this.player6 = Player6;
        this.isActive = IsActive;
        this.playersInGame = PlayersInGame;
        this.table = tableHand;
        this.gameRulesId = GameRulesId;
        this.currentPot = CurrentPot;
        this.currentPlayAmount = CurrentPlayAmount;
        this.currentStage = 0;
        this.round = 0;
    }

    public Games(Player Host, int PlayersInGame, boolean IsActive){
        this.host = Host;
        this.playersInGame = PlayersInGame;
        this.isActive = IsActive;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }


    public int getPlayersInGame() {
        return playersInGame;
    }
    public void setPlayersInGame(int playersInGame) {
        this.playersInGame = playersInGame;
    }

    public int getCurrentStage() {
        return currentStage;
    }
    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }
}
