package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController {

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    @FXML
    private ImageView barajaButton;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;
    @FXML
    private Label machineCounter;

    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.deck, this.machineCounter);
        threadPlayMachine.start();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table, this.threadPlayMachine);
        this.gameUno.startGame();
        this.tableImageView.setImage(this.table.getCardsTable().get(0).getImage());
        Image barajaImage = new Image(String.valueOf(getClass().getResource(EISCUnoEnum.DECK_OF_CARDS.getFilePath())));
        this.barajaButton.setImage(barajaImage);
        printCardsHumanPlayer();
        printCardsMachinePlayer();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer());
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.posInitCardToShow = 0;
        this.machineCounter = new Label();
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();
            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                    if(!this.threadPlayMachine.getHasPlayerPlayed() && this.gameUno.playCard(card)){

                        System.err.println("Click on card, play logic working");

                        this.tableImageView.setImage(card.getImage());
                        this.humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                        this.threadPlayMachine.setHasPlayerPlayed(true);
                        printCardsHumanPlayer();
                        this.gameUno.validateSpecialCard(card, this.machinePlayer);
                    }
            });
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }

    
    /**
     * Prints the human player's cards on the grid pane.
     */
    public void printCardsMachinePlayer() {
        this.gridPaneCardsMachine.getChildren().clear();
        String folderCardPath = EISCUnoEnum.CARD_UNO.getFilePath();

        for (int i = 0; i < 3; i++) { 
            Card foldedCard = new Card(folderCardPath, "1", "red");
            this.gridPaneCardsMachine.add(foldedCard.getCard(), i, 0);
        };

        String machineCardsCount = String.valueOf(this.machinePlayer.getCardsPlayer().size());

        machineCounter = new Label(machineCardsCount);
        System.err.println(machineCardsCount);
        this.gridPaneCardsMachine.add(machineCounter, 3,0);
    }


    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(MouseEvent event) {
        // Implement logic to take a card here
        System.err.println("Taking card");
        this.humanPlayer.addCard(this.deck.takeCard());
        printCardsHumanPlayer();
        onHandleNext(new ActionEvent());
        this.threadPlayMachine.setHasPlayerPlayed(true);
    }


    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        // Implement logic to handle Uno event here
    }
}