package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Card;

import java.sql.SQLException;

public class ChooseDeckController {

    @FXML
    private TableView<Deck> table;
    public TableColumn<Deck, String> deck;

    @FXML
    private Button loadButton;

    @FXML
    private Button cancel;

    @FXML
    void initialize() {
        deck = new TableColumn<>("Deck");
        deck.setMinWidth(200.);
        deck.setMaxWidth(1000.);
        deck.setCellValueFactory(new PropertyValueFactory<>("DeckName"));
        table.getColumns().addAll(deck);

        DbWordsHandler handler = null;
        try {
            handler = DbWordsHandler.getInstance();
            table.getItems().setAll(handler.getDecks());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        DbWordsHandler finalHandler = handler;

        loadButton.setOnAction(event -> {
            if (table.getSelectionModel().getSelectedItem() != null) {
                Controller.fullDeck = finalHandler.getCards(table.getSelectionModel().getSelectedItem().getDeckName());
                Controller.cardList = finalHandler.getCards(table.getSelectionModel().getSelectedItem().getDeckName());
            } else {
                showError("Please, choose deck");
                return;
            }
            Stage stage = (Stage) loadButton.getScene().getWindow();
            stage.close();
        });

        cancel.setOnAction(event -> {
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        });
    }

    void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
}
