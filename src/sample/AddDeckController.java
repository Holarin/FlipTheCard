package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Card;

import java.sql.SQLException;

public class AddDeckController {

    private String category = "";

    @FXML
    private TableView<Card> table;

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
    private Button submitButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField categoryField;

    public TableColumn<Card, String> face;
    public TableColumn<Card, String> back;
    public TableColumn<Card, Integer> rating;

    @FXML
    void initialize() {
        setupTable();

        submitButton.setOnAction(event -> {
            category = categoryField.getText();

            if (isCategoryEmpty()) {
                showError("Please, input category");
                return;
            }

            for (Card card : table.getItems()) {
                card.setCategory(category);
            }
        });

        deleteButton.setOnAction(event -> {
            if (table.getSelectionModel().getSelectedItem() != null) {
                table.getItems().remove(table.getSelectionModel().getSelectedItem());
            }
        });

        editButton.setOnAction(event -> {
            if (isCategoryEmpty()) {
                showError("Please, input category");
                return;
            }
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
            if (isCategoryEmpty()) {
                showError("Please, input category");
                return;
            }
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
                    card.setCategory(category);
                    table.getItems().add(card);
                } catch (NumberFormatException e) {
                    showError("Check rating input");
                }
            }
        });

        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        saveButton.setOnAction(event -> {
            DbWordsHandler handler;
            try {
                handler = DbWordsHandler.getInstance();
                if (!handler.isExist(category)) {
                    for (Card card : table.getItems()) {
                        handler.addCard(card);
                    }
                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.close();
                } else {
                    showError("Category already used, please, change category");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });
    }

    boolean isCategoryEmpty() {
        if (category == null) {
            return false;
        }
        return category.equals("");
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
    }

    void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
}
