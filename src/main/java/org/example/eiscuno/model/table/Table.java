package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table  {
    private ArrayList<Card> cardsTable;

    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table() {
        this.cardsTable = new ArrayList<Card>();
    }

    /**
     * Adds a card to the table.
     * @param card The card to be added to the table.
     */
    public Boolean addCardOnTheTable(Card card) {
        Card currentCardOnTheTable = this.cardsTable.get(this.cardsTable.size() - 1);

        System.out.println("Current card on the table: Color = " + currentCardOnTheTable.getColor() + ", Value = " + currentCardOnTheTable.getValue());
        System.out.println("Card to be added: Color = " + card.getColor() + ", Value = " + card.getValue());

        if (currentCardOnTheTable.getColor().equals(card.getColor()) ||
                currentCardOnTheTable.getValue().equals(card.getValue()) ||
                currentCardOnTheTable.getColor().equals("NON_COLOR") ||
                card.getColor().equals("NON_COLOR")
        ) {
            this.cardsTable.add(card);
            System.out.println("Card added to the table.");
            return true;
        }

        System.out.println("Card not added to the table.");
        return false;
    }


    public ArrayList<Card> getCardsTable() {
        return cardsTable;
    }

    public void setStartCard(Card card) {
        this.cardsTable.add(card);
    }
}
