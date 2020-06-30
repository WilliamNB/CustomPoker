package com.example.williamnestius_brow.custompoker;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    private Suit suit;
    private Rank rank;
    private ArrayList<Card> deck;
    private Random randomGenerator = new Random();
    private Random random = new Random();

    public Deck() {
        this.deck = new ArrayList<Card>();
        createDeck();
    }

    public int deckSize(){
        return deck.size();
    }

    public void createDeck(){
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add( new Card(suit, rank));
            }
        }
    }

    public Card dealRandomCard() {
        int index = randomGenerator.nextInt(deck.size());
        Card card = deck.get(index);
        return card;
    }

    public Card nextCard() {
        int index = randomGenerator.nextInt(deck.size());
        Card card = deck.get(index);
        return card;
    }
}