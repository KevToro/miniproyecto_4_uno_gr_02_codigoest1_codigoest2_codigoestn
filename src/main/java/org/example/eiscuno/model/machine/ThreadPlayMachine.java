package org.example.eiscuno.model.machine;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private Deck deck;
    private Label machineCounter;
    private volatile boolean hasPlayerPlayed;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, Deck deck, Label machineCounter) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.deck = deck;
        this.machineCounter = machineCounter;
    }

    public void run() {
        while (true){
            if(hasPlayerPlayed){
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta
                putCardOnTheTable();
                hasPlayerPlayed = false;
            }
        }
    }

    public void updateMachineCounter(){
        
    }

    private void putCardOnTheTable(){
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        Card card = machinePlayer.getCard(index);
        if(table.addCardOnTheTable(card)) {
            this.tableImageView.setImage(card.getImage());
            machinePlayer.removeCard(index);
            String machineCardsCount = String.valueOf(this.machinePlayer.getCardsPlayer().size());
            this.machineCounter.setText(machineCardsCount);

            System.err.println(machineCardsCount);

            validateSpecialCard(card, this.machinePlayer);
        } else {
            machinePlayer.addCard(deck.takeCard());
        }
    }

    /**
     * Validate if is a special card.
     *
     */
    public void validateSpecialCard(Card card, Player player) {
        int numberOfCards = 0;

        if(card.getValue().contains("+2")) {
            numberOfCards = 2;
        } else if (card.getValue().contains("+4")) {
            numberOfCards = 4;
        }else if (card.getValue().contains("SKIP")){
            setHasPlayerPlayed(false);
        }else if(card.getValue().contains("REVERSE")){
            setHasPlayerPlayed(false);
        }


        if(numberOfCards > 0){
            System.out.println(player.getTypePlayer() + " have: " + player.getCardsPlayer().size() + " cards");
        }

        for (int i = 0; i < numberOfCards; i++) {
            player.addCard(this.deck.takeCard());
        }

        if(numberOfCards > 0){
            System.out.println(player.getTypePlayer() + " eat now: " + numberOfCards + " cards");
            System.out.println(player.getTypePlayer() + " have now: " + player.getCardsPlayer().size() + " cards");
        }
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    public boolean getHasPlayerPlayed() {
        return hasPlayerPlayed;
    }
}