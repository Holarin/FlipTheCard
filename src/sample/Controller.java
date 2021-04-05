package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Card;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    public static List<Card> fullDeck = new ArrayList<>();
    public static List<Card> cardList = new ArrayList<>();
    public static String category = null;
    int currentIndex = 0;

    @FXML
    private Button one;

    @FXML
    private Button two;

    @FXML
    private Button three;

    @FXML
    private Button four;

    @FXML
    private Button five;

    @FXML
    private Button show;

    @FXML
    private Text face;

    @FXML
    private Text back;

    @FXML
    private MenuItem learnWordsMenu;

    @FXML
    private MenuItem repeatWordsMenu;

    @FXML
    private MenuItem editDeckMenu;

    @FXML
    private MenuItem addNewDeckMenu;

    @FXML
    private MenuItem chooseDeckMenu;

    @FXML
    private MenuItem aboutMenu;

    void printCard() {
        face.setText(cardList.get(currentIndex).getFace());
        back.setVisible(false);
        back.setText(cardList.get(currentIndex).getBack());
    }

    void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    void showInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String info = "Result: \n";
        for (int i = 0; i < cardList.size(); i++) {
            info += cardList.get(i).getFace() + ". Rating: " + cardList.get(i).getRating() + "\n";
        }
        alert.setTitle("Result");
        alert.setHeaderText(null);
        alert.setContentText(info);

        alert.showAndWait();
    }

    void updateCard(Card card, int rating) {
        fullDeck.get(fullDeck.indexOf(card)).setRating(rating);
    }

    void setAndShow(int rating) {
        if (cardList.size() == currentIndex) {
            showError("Deck is empty. Choose another deck");
        } else {
            updateCard(cardList.get(currentIndex), rating);
            cardList.get(currentIndex).setRating(rating);
            currentIndex++;
            if (currentIndex == cardList.size()) {
                try {
                    DbWordsHandler handler = DbWordsHandler.getInstance();
                    handler.updateData(fullDeck.get(0).getCategory(), fullDeck);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                showInfo();
                return;
            }
            printCard();
        }
    }

    @FXML
    void initialize() throws SQLException {

        System.out.println(cardList);
        show.setOnAction(event -> {
            back.setVisible(true);
        });

        one.setOnAction(event -> {
            setAndShow(1);
        });

        two.setOnAction(event -> {
            setAndShow(2);
        });

        three.setOnAction(event -> {
            setAndShow(3);
        });

        four.setOnAction(event -> {
            setAndShow(4);
        });

        five.setOnAction(event -> {
            setAndShow(5);
        });

        chooseDeckMenu.setOnAction(event -> {
            openNewScene("ChooseDeck.fxml");
            if (cardList.size() != 0) {
                currentIndex = 0;
                printCard();
            }
        });

        addNewDeckMenu.setOnAction(event -> {
            openNewScene("addDeck.fxml");
        });

        learnWordsMenu.setOnAction(event -> {
            List<Card> oneF = new ArrayList<>();
            for (Card card : fullDeck) {
                if (card.getRating() < 5) {
                    oneF.add(card);
                }
            }
            if (oneF.size() != 0) {
                cardList.clear();
                currentIndex = 0;
                cardList.addAll(oneF);
                printCard();
            } else {
                showError("There are no words in the deck with a rating of less than 5");
            }
        });

        repeatWordsMenu.setOnAction(event -> {
            List<Card> fives = new ArrayList<>();
            for (Card card : fullDeck) {
                if (card.getRating() == 5) {
                    fives.add(card);
                }
            }
            if (fives.size() != 0) {
                cardList.clear();
                currentIndex = 0;
                cardList.addAll(fives);
                printCard();
            } else {
                showError("There are no words in the deck with a rating of 5");
            }
        });

        editDeckMenu.setOnAction(event -> {
            if (fullDeck.size() != 0) {
                category = fullDeck.get(0).getCategory();
                openNewScene("editDeck.fxml");
            } else {
                showError("Nothing to edit");
            }
        });
    }

    public void openNewScene(String window) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        } catch (IOException e){
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
