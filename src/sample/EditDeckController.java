package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Card;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

public class EditDeckController {

    @FXML
    private TableView<Card> table;

    public TableColumn<Card, String> face;
    public TableColumn<Card, String> back;
    public TableColumn<Card, Integer> rating;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField headField;

    @FXML
    private TextField backField;

    @FXML
    private TextField ratingField;


    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    @FXML
    void initialize() {
        setupTable();
        editButton.setOnAction(event -> {
            if (table.getSelectionModel().getSelectedItem() != null) {
                if (!headField.getText().equals("") &&
                !backField.getText().equals("") &&
                !ratingField.getText().equals("") &&
                headField != null && backField != null && ratingField != null) {
                    try {
                        Card card = new Card();
                        card.setRating(Integer.parseInt(ratingField.getText()) <= 0 ? 1 :
                                Math.min(Integer.parseInt(ratingField.getText()), 5));
                        card.setFace(headField.getText());
                        card.setBack(backField.getText());
                        table.getItems().set(table.getSelectionModel().getSelectedIndex(), card);

                    } catch (NumberFormatException e) {
                        showError("Check rating input");
                    }
                }
            }
        });

        addButton.setOnAction(event -> {
            if (!headField.getText().equals("") &&
                        !backField.getText().equals("") &&
                        !ratingField.getText().equals("") &&
                        headField != null && backField != null && ratingField != null) {
                try {
                    Card card = new Card();
                    card.setRating(Integer.parseInt(ratingField.getText()) <= 0 ? 1 :
                            Math.min(Integer.parseInt(ratingField.getText()), 5));
                    card.setFace(headField.getText());
                    card.setBack(backField.getText());
                    table.getItems().add(card);
                } catch (NumberFormatException e) {
                    showError("Check rating input");
                }
            }
        });

        deleteButton.setOnAction(event -> {
            if (table.getSelectionModel().getSelectedItem() != null) {
                table.getItems().remove(table.getSelectionModel().getSelectedItem());
            }
        });

        saveButton.setOnAction(event -> {
            if (table.getItems().size() != 0) {
                Controller.fullDeck = table.getItems();
                Controller.cardList = table.getItems();
                DbWordsHandler handler;
                try {
                    handler = DbWordsHandler.getInstance();
                    handler.updateData(table.getItems().get(0).getCategory(), table.getItems());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                Controller.fullDeck = table.getItems();
                Controller.cardList = table.getItems();
                DbWordsHandler handler;
                try {
                    handler = DbWordsHandler.getInstance();
                    handler.updateData("", table.getItems());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    public void setupTable() {
        face = new TableColumn<>("Face");
        face.setMinWidth(200.);
        face.setCellValueFactory(new PropertyValueFactory<>("Face"));

        back = new TableColumn<>("Back");
        back.setMinWidth(200.);
        back.setCellValueFactory(new PropertyValueFactory<>("Back"));

        rating = new TableColumn<>("Rating");
        rating.setMinWidth(200.);
        rating.setCellValueFactory(new PropertyValueFactory<>("Rating"));


        table.getColumns().addAll(face, back, rating);
        for (Card card : Controller.fullDeck) {
            table.getItems().add(card);
        }
    }

    void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
}
