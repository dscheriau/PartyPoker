package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;

public class Player {
    private String name;
    private boolean isAllIn = false;
    private boolean hasFolded = false;
    private boolean isDealer = false;
    private int chipCount;
    private int currentBid;
    private ArrayList<Card> cards;

    public Player(String name) {
        this.name = name;
        cards = new ArrayList<Card>();
    }

    public String getName() {
        return name;
    }

    public int getChipCount() {
        return chipCount;
    }

    public void setChipCount(int startChips) {
        chipCount = startChips;
    }

    public void takeCard(Card card) {
        System.out.println(name + " got " + card.toString());
    }

    public void removeCards() {
        cards.clear();
    }

    public int getCurrentBid() { return currentBid; }

    public void resetCurrentBid() { currentBid = 0; }

    public int giveBlind(int blind) {
        int returnAmount = blind;

        if (chipCount < blind) {
            returnAmount = chipCount;
            currentBid = chipCount;
            setAllIn();
        }
        else {
            chipCount -= blind;
            currentBid = blind;
        }
        System.out.println(name + " gave blind " + returnAmount);
        return returnAmount; // return amount is either the required blind or the whole chipCount if it's less than the blind
    }

    public int askForAction(int amount) {
        int returnAmount = amount;

        if (Math.random()*3 < Math.random()) {
            if (amount == 0) {
                if (chipCount > 60) {
                    returnAmount = 60;
                    System.out.println(name + " bet: " + returnAmount);
                } else {
                    returnAmount = chipCount + currentBid;
                    setAllIn();
                    System.out.println(name + " bet: " + returnAmount);
                }
            } else {
                if (chipCount > returnAmount * 2) {
                    returnAmount = amount * 2;
                    System.out.println(name + " raised: " + returnAmount);
                } else {
                    if (chipCount > 0) {
                        returnAmount = chipCount + currentBid;
                        setAllIn();
                        System.out.println(name + " raised: " + returnAmount);
                    } else
                        returnAmount = 0;
                }
            }
        }

        else if (Math.random()/2 > Math.random()) {
            returnAmount = currentBid;
            hasFolded = true;
            System.out.println(name + " folded");
        } else {
            if (amount == 0)
                System.out.println(name + " checked");
            else {
                if (chipCount > amount)
                    returnAmount = amount;
                else {
                    returnAmount = chipCount + currentBid;
                    setAllIn();
                }
                System.out.println(name + " called: " + returnAmount);

            }
        }

        if (!isAllIn) {
            chipCount -= returnAmount;
            chipCount += currentBid;
        }

        currentBid = returnAmount;

        return returnAmount; // should be the amount specified by the player
    }

    public boolean hasFolded() {
        return hasFolded;
    }

    public boolean isAllIn() {
        return isAllIn;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void setDealer(boolean isDealer) {
        this.isDealer = isDealer;
    }

    public void payOutPot(int pot) {
        System.out.println(name + " just got " + pot + " chips!");
        chipCount += pot;
    }

    public ArrayList<Card> getPlayerHand() {
        return cards;
    }

    public void activate() {
        hasFolded = false;
        isAllIn = false;
    }

    public void setAllIn() {
        chipCount = 0;
        isAllIn = true;
        System.out.println(name + " is ALL-IN!!!");
    }
}