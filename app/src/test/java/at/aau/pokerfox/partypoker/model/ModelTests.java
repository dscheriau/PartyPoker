package at.aau.pokerfox.partypoker.model;

import android.drm.DrmStore;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;

import java.util.ArrayList;

import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.activities.GameActivity;
import at.aau.pokerfox.partypoker.model.network.messages.client.ActionMessage;
import at.aau.pokerfox.partypoker.model.network.messages.client.CheatPenaltyMessage;
import at.aau.pokerfox.partypoker.model.network.messages.client.ReplaceCardMessage;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by TimoS on 22.06.2018.
 */

public class ModelTests {

    Player testPlayer = null;
    Player testPlayer2 = null;

    ArrayList<Player> players;
    ArrayList<Card> cards;


    @Before
    public void onStartup() {
        testPlayer = new Player();
        testPlayer.setName("harald");
        testPlayer2 = new Player("heinz");

        players= new ArrayList<>();
        cards = new ArrayList<>();

        PartyPokerApplication.setIsHost(true);
        boolean isCheatingAllowed = false;
        Game.init(50,100,1000,2, isCheatingAllowed, new ModActSimulator());
    }


    @Test
    public void testPlayerNames() {
        assertTrue(testPlayer.getName() == "harald");
        assertTrue(testPlayer2.getName() == "heinz");

    }

    @Test
    public void testActivate() {
        Card card2 = new Card(0,2);
        Card card3 = new Card(1,3);
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(card2);
        cards.add(card3);

        testPlayer.activate();
        testPlayer.setCheatStatus(true);
        testPlayer.setChipCount(50);
        testPlayer.setCards(cards);
        testPlayer.setCurrentBid(50);
        testPlayer.setCheckStatus(true);
        testPlayer.setDeviceId("blubb");
        testPlayer.setIsDealer(false);
        testPlayer.setFolded();

        assertEquals(cards, testPlayer.getCards());
        assertTrue(testPlayer.getCheatStatus());
        assertTrue(testPlayer.getChipCount() == 50);
        assertTrue(testPlayer.getCurrentBid() == 50);
        assertTrue(testPlayer.getCheckStatus());
        assertTrue(testPlayer.getDeviceId() == "blubb");
        assertTrue(!testPlayer.isDealer());
        assertTrue(testPlayer.hasFolded());

    }

    @Test
    public void winnerTaskTest() {
        players.add(testPlayer);
        players.add(testPlayer2);

        Game.addPlayer(testPlayer);
        Game.addPlayer((testPlayer2));

        PartyPokerApplication.setIsHost(true);
        ShowWinnerTask showWinnerTask = new ShowWinnerTask();
        showWinnerTask.doInBackground(20);
        showWinnerTask.onPostExecute(20);
        GameActivity activity = new GameActivity();
        assertTrue(true);

        ActionMessage msg = new ActionMessage();
        msg.Amount = 1;
        msg.HasFolded = true;

        CheatPenaltyMessage msg1 = new CheatPenaltyMessage();
        String c = msg1.cheater;
        String d = msg1.complainer;
        msg1.penalizeCheater = true;


        ReplaceCardMessage msg2 = new ReplaceCardMessage();
        msg2.replaceCard1 = true;
        Card g = msg2.replacementCard;

    }
}
