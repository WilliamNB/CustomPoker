package com.example.williamnestius_brow.custompoker;

public class TableHand {
    public boolean player1In;
    public boolean player2In;
    public boolean player3In;
    public boolean player4In;
    public boolean player5In;
    public boolean player6In;
    public Card tableCard1;
    public Card tableCard2;
    public Card tableCard3;
    public Card tableCard4;
    public Card tableCard5;
    public int smallBlindP, bigBlindP, actionCounter;

    public TableHand(){}

    public TableHand(boolean Player1In, boolean Player2In, boolean Player3In, boolean Player4In, boolean Player5In, boolean Player6In,
                     Card TableCard1, Card TableCard2, Card TableCard3, Card TableCard4, Card TableCard5){
        this.player1In = Player1In;
        this.player2In = Player2In;
        this.player3In = Player3In;
        this.player4In = Player4In;
        this.player5In = Player5In;
        this.player6In = Player6In;
        this.tableCard1 = TableCard1;
        this.tableCard2 = TableCard2;
        this.tableCard3 = TableCard3;
        this.tableCard4 = TableCard4;
        this.tableCard5 = TableCard5;
        this.smallBlindP = 0;
        this.bigBlindP = 0;
        this.actionCounter = 0;
    }


    public Card getTableCard1() {
        return tableCard1;
    }
    public void setTableCard1(Card tableCard1) {
        this.tableCard1 = tableCard1;
    }

    public Card getTableCard2() {
        return tableCard2;
    }
    public void setTableCard2(Card tableCard2) {
        this.tableCard2 = tableCard2;
    }

    public Card getTableCard3() {
        return tableCard3;
    }
    public void setTableCard3(Card tableCard3) {
        this.tableCard3 = tableCard3;
    }

    public Card getTableCard4() {
        return tableCard4;
    }
    public void setTableCard4(Card tableCard4) {
        this.tableCard4 = tableCard4;
    }

    public Card getTableCard5() {
        return tableCard5;
    }
    public void setTableCard5(Card tableCard5) {
        this.tableCard5 = tableCard5;
    }

    public int getSmallBlindP() {
        return smallBlindP;
    }
    public void setSmallBlindP(int smallBlindP) {
        this.smallBlindP = smallBlindP;
    }

    public int getBigBlindP() {
        return bigBlindP;
    }
    public void setBigBlindP(int bigBlindP) {
        this.bigBlindP = bigBlindP;
    }

    public boolean isPlayer1In() {
        return player1In;
    }
    public void setPlayer1In(boolean player1In) {
        this.player1In = player1In;
    }

    public boolean isPlayer2In() {
        return player2In;
    }
    public void setPlayer2In(boolean player2In) {
        this.player2In = player2In;
    }

    public boolean isPlayer3In() {
        return player3In;
    }
    public void setPlayer3In(boolean player3In) {
        this.player3In = player3In;
    }

    public boolean isPlayer4In() {
        return player4In;
    }
    public void setPlayer4In(boolean player4In) {
        this.player4In = player4In;
    }

    public boolean isPlayer5In() {
        return player5In;
    }
    public void setPlayer5In(boolean player5In) {
        this.player5In = player5In;
    }

    public boolean isPlayer6In() {
        return player6In;
    }
    public void setPlayer6In(boolean player6In) {
        this.player6In = player6In;
    }

    public int getActionCounter() {
        return actionCounter;
    }
    public void setActionCounter(int actionCounter) {
        this.actionCounter = actionCounter;
    }

    public int nextIn(int start){
        boolean[] players = new boolean[6];
        players[0] = player1In;
        players[1] = player2In;
        players[2] = player3In;
        players[3] = player4In;
        players[4] = player5In;
        players[5] = player6In;
        int counter = start;
        while(players[start] == false){
            if (start == 5){
                start = 0;
            }else{
                start += 1;
            }
            if(counter == start){
                return 10;
            }
        }
        start += 1;
        return  start;
    }

    public int lastPlayer(){
        boolean[] players = new boolean[6];
        players[0] = player1In;
        players[1] = player2In;
        players[2] = player3In;
        players[3] = player4In;
        players[4] = player5In;
        players[5] = player6In;
        int counter = 0;
        for(int i = 0; i <= 5; i++){
            if(players[i] == true){
                counter += 1;
            }

        }
        return counter;
    }

    public int getLast(){
        boolean[] players = new boolean[6];
        players[0] = player1In;
        players[1] = player2In;
        players[2] = player3In;
        players[3] = player4In;
        players[4] = player5In;
        players[5] = player6In;
        int j = 0;
        for(int i = 0; i <= 5; i++){
            if(players[i] == true){
                j = i;
            }
        }
        return j+1;
    }

    public int[] remainingPlayers(){
        boolean[] players = new boolean[6];
        players[0] = player1In;
        players[1] = player2In;
        players[2] = player3In;
        players[3] = player4In;
        players[4] = player5In;
        players[5] = player6In;
        int[] remaining = new int[6];
        int counter = 0;
        for(int i = 0; i <= 5; i++ ){
            if(players[i] == true){
                remaining[counter] = i+1;
                counter += 1;
            }
        }
        return remaining;
    }

    public boolean isIn(int player){
        boolean[] players = new boolean[6];
        players[0] = player1In;
        players[1] = player2In;
        players[2] = player3In;
        players[3] = player4In;
        players[4] = player5In;
        players[5] = player6In;

        if(players[player - 1] == true){
            return true;
        }else{
            return false;
        }
    }

}

