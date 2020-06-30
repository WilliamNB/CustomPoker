package com.example.williamnestius_brow.custompoker;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import static com.example.williamnestius_brow.custompoker.Rank.*;
import static com.example.williamnestius_brow.custompoker.Ranking.*;

/*
 * 	01) ROYAL_FLUSH,
 *	02) STRAIGHT_FLUSH,
 *	03) FOUR_OF_A_KIND,
 *	04) FULL_HOUSE,
 *	05) FLUSH,
 *	06) STRAIGHT,
 *	07) THREE_OF_A_KIND,
 *	08) TWO_PAIR,
 *	09) ONE_PAIR,
 *	10) HIGH_CARD
 */
public class HandEval {

    private HandEval() {
    }

    public static Integer getRankingPos(Player player) {
        return player.getRanking().ordinal();
    }

    public static void rankHand(Player player, List<Card> tableCards) {
        Log.d("userHand", "in check ranking ");
        //HIGH_CARD
        Card highCard = getHighCard(player, tableCards);
        player.setHighCard(highCard);

        //ROYAL_FLUSH
        List<Card> rankingList = getRoyalFlush(player, tableCards);
        if (rankingList != null) {
            Log.d("rankingerr", "has royal");
            setRankingAndList(player, ROYAL_FLUSH, rankingList);
            return;
        }
        //STRAIGHT_FLUSH
        rankingList = getStraightFlush(player, tableCards);
        if (rankingList != null) {
            Log.d("rankingerr", "has straightflush");
            setRankingAndList(player, STRAIGHT_FLUSH, rankingList);
            return;
        }
        //FOUR_OF_A_KIND
        rankingList = getFourOfAKind(player, tableCards);
        if (rankingList != null) {
            Log.d("rankingerr", "4 of");
            setRankingAndList(player, FOUR_OF_A_KIND, rankingList);
            return;
        }
        //FULL_HOUSE
        rankingList = getFullHouse(player, tableCards);
        if (rankingList != null) {
            Log.d("rankingerr", "has full");
            setRankingAndList(player, FULL_HOUSE, rankingList);
            return;
        }
        //FLUSH
        rankingList = getFlush(player, tableCards);
        if (rankingList != null) {
            Log.d("rankingerr", "has flush");
            setRankingAndList(player, FLUSH, rankingList);
            return;
        }
        //STRAIGHT
        rankingList = getStraight(player, tableCards);
        if (rankingList != null) {
            Log.d("rankingerr", "has straigt");
            setRankingAndList(player, STRAIGHT, rankingList);
            return;
        }
        //THREE_OF_A_KIND
        rankingList = getThreeOfAKind(player, tableCards);
        if (rankingList != null) {
            Log.d("rankingerr", "has three of kin");
            setRankingAndList(player, THREE_OF_A_KIND,
                    rankingList);
            return;
        }
        //TWO_PAIR
        rankingList = getTwoPair(player, tableCards);
        if (rankingList != null) {
            Log.d("rankingerr", "has 2 pair ");
            setRankingAndList(player, TWO_PAIR, rankingList);
            return;
        }
        //ONE_PAIR
        rankingList = getOnePair(player, tableCards);
        if (rankingList != null) {
            Log.d("rankingerr", "has 1 pair  ");
            setRankingAndList(player, ONE_PAIR, rankingList);
            return;
        }
        //HIGH_CARD
        player.setRanking(HIGH_CARD);
        List<Card> highCardRankingList = new ArrayList<Card>();
        highCardRankingList.add(highCard);
        player.setRankingList(highCardRankingList);
        return;
    }

    public static List<Card> getRoyalFlush(Player player, List<Card> tableCards) {
        if (!isSameSuit(player, tableCards)) {
            return null;
        }

        List<Rank> rankEnumList = toRankEnumList(player, tableCards);

        if (rankEnumList.contains(TEN)
                && rankEnumList.contains(JACK)
                && rankEnumList.contains(QUEEN)
                && rankEnumList.contains(KING)
                && rankEnumList.contains(ACE)) {

            return getMergedCardList(player, tableCards);
        }

        return null;
    }

    public static List<Card> getStraightFlush(Player player, List<Card> tableCards) {
        return getSequence(player, tableCards, 5, true);
    }

    public static List<Card> getFourOfAKind(Player player, List<Card> tableCards) {
        List<Card> mergedList = getMergedCardList(player, tableCards);
        return checkPair(mergedList, 4);
    }

    public static List<Card> getFullHouse(Player player, List<Card> tableCards) {
        List<Card> mergedList = getMergedCardList(player, tableCards);
        List<Card> threeList = checkPair(mergedList, 3);
        if (threeList != null) {
            mergedList.removeAll(threeList);
            List<Card> twoList = checkPair(mergedList, 2);
            if (twoList != null) {
                threeList.addAll(twoList);
                return threeList;
            }
        }
        return null;
    }

    public static List<Card> getFlush(Player player, List<Card> tableCards) {
        List<Card> mergedList = getMergedCardList(player, tableCards);
        List<Card> flushList = new ArrayList<Card>();

        for (Card card1 : mergedList) {
            for (Card card2 : mergedList) {
                if (card1.getSuit().equals(card2.getSuit())) {
                    if (!flushList.contains(card1)) {
                        flushList.add(card1);
                    }
                    if (!flushList.contains(card2)) {
                        flushList.add(card2);
                    }
                }
            }
            if (flushList.size() == 5) {
                return flushList;
            }
            flushList.clear();
        }
        return null;
    }

    public static List<Card> getStraight(Player player, List<Card> tableCards) {
        return getSequence(player, tableCards, 5, false);
    }

    public static List<Card> getThreeOfAKind(Player player, List<Card> tableCards) {
        List<Card> mergedList = getMergedCardList(player, tableCards);
        return checkPair(mergedList, 3);
    }

    public static List<Card> getTwoPair(Player player, List<Card> tableCards) {
        Log.d("userHand", "in get2pair ");
        List<Card> mergedList = getMergedCardList(player, tableCards);
        List<Card> twoPair1 = checkPair(mergedList, 2);
        if (twoPair1 != null) {
            mergedList.removeAll(twoPair1);
            List<Card> twoPair2 = checkPair(mergedList, 2);
            if (twoPair2 != null) {
                Log.d("userHand", "return twoPair ");
                twoPair1.addAll(twoPair2);
                return twoPair1;
            }
        }
        return null;
    }

    public static List<Card> getOnePair(Player player, List<Card> tableCards) {
        Log.d("userHand", "in get1pair ");
        List<Card> mergedList = getMergedCardList(player, tableCards);
        return checkPair(mergedList, 2);
    }

    public static Card getHighCard(Player player, List<Card> tableCards) {
        Log.d("userHand", "in get high ");
        List<Card> allCards = new ArrayList<Card>();
        allCards.addAll(tableCards);
        allCards.add(player.getCard1());
        allCards.add(player.getCard2());
        Log.d("userHand", "in check length of all cards " + allCards.size());

        Card highCard = allCards.get(0);
        for (Card card : allCards) {
            if (card.getRankToInt() > highCard.getRankToInt()) {
                highCard = card;
            }
        }
        return highCard;
    }

    private static List<Card> getSequence(Player player, List<Card> tableCards, Integer sequenceSize, Boolean compareSuit) {
        List<Card> orderedList = getOrderedCardList(player, tableCards);
        List<Card> sequenceList = new ArrayList<Card>();

        Card cardPrevious = null;
        for (Card card : orderedList) {
            if (cardPrevious != null) {
                if ((card.getRankToInt() - cardPrevious.getRankToInt()) == 1) {
                    if (!compareSuit || cardPrevious.getSuit().equals(card.getSuit())) {
                        if (sequenceList.size() == 0) {
                            sequenceList.add(cardPrevious);
                        }
                        sequenceList.add(card);
                    }
                } else {
                    if (sequenceList.size() == sequenceSize) {
                        return sequenceList;
                    }
                    sequenceList.clear();
                }
            }
            cardPrevious = card;
        }

        return (sequenceList.size() == sequenceSize) ? sequenceList : null;
    }

    private static List<Card> getMergedCardList(Player player, List<Card> tableCards) {
        Log.d("userHand", "merge list ");
        List<Card> merged = new ArrayList<Card>();
        merged.addAll(tableCards);
        merged.add(player.getCard1());
        merged.add(player.getCard2());
        return merged;
    }

    private static List<Card> getOrderedCardList(Player player, List<Card> tableCards) {
        List<Card> ordered = getMergedCardList(player, tableCards);
        Collections.sort(ordered, new Comparator<Card>() {

            public int compare(Card c1, Card c2) {
               // return c1.getRankToInt() < c2.getRankToInt() ? -1 : 1;
                return c1.getRankToInt().compareTo(c2.getRankToInt());
            }

        });
        return ordered;
    }

    private static List<Card> checkPair(List<Card> mergedList, Integer pairSize) {
        Log.d("userHand", "checkPair ");
        List<Card> checkedPair = new ArrayList<Card>();
        for (Card card1 : mergedList) {
            Log.d("userHand", "check pair merge length ");
            checkedPair.add(card1);
            for (Card card2 : mergedList) {
                if (!card1.equals(card2) && card1.getRank().equals(card2.getRank())) {
                    checkedPair.add(card2);
                }
            }
            if (checkedPair.size() == pairSize) {
                Log.d("userHand", "return checkPair ");
                return checkedPair;
            }
            checkedPair.clear();
        }
        return null;
    }

    private static Boolean isSameSuit(Player player, List<Card> tableCards) {
        Suit suit = player.getCard1().getSuit();

        if (!suit.equals(player.getCard2().getSuit())) {
            return false;
        }

        for (Card card : tableCards) {
            if (!card.getSuit().equals(suit)) {
                return false;
            }
        }

        return true;
    }

    private static List<Rank> toRankEnumList(Player player, List<Card> tableCards) {
        List<Rank> rankEnumList = new ArrayList<Rank>();

        for (Card card : tableCards) {
            rankEnumList.add(card.getRank());
        }

        rankEnumList.add(player.getCard1().getRank());
        rankEnumList.add(player.getCard2().getRank());

        return rankEnumList;
    }

    private static void setRankingAndList(Player player, Ranking ranking, List<Card> rankingList) {
        player.setRanking(ranking);
        player.setRankingList(rankingList);
    }
}

