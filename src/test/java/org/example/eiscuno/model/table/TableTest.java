package org.example.eiscuno.model.table;

import javafx.stage.Stage;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.player.Player;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class TableTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        // This is required to start the JavaFX application thread.
    }

   @Test
    void addCardOnTheTableTest(){
       var humanPlayer = new Player("HUMAN_PLAYER");
       var machinePlayer = new Player("MACHINE_PLAYER");
       var deck = new Deck();
       var table = new Table();
       //var threadMachine = new ThreadPlayMachine(null, null, null, null, null);
       //var gameUno = new GameUno(humanPlayer, machinePlayer, deck, table, threadMachine);

       boolean isRedCardPut = false;
       while (!isRedCardPut){
           var card = deck.takeCard();
           if(card.getColor().equals("RED")){
               table.setStartCard(card);
               isRedCardPut = true;
           }
       }
       assertEquals("RED", table.getCardsTable().get(0).getColor());
   }
}