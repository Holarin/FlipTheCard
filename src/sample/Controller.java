package sample;

import javafx.fxml.FXML;
import javafx.scene.AccessibleAction;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import model.Card;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    List<Card> cardList = new ArrayList<>();
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

    void printCard() {
        face.setText(cardList.get(currentIndex).getFace());
        back.setVisible(false);
        back.setText(cardList.get(currentIndex).getBack());
    }

    void setAndShow(int rating) {
        cardList.get(currentIndex).setRating(rating);
        currentIndex++;
        if (cardList.size() == currentIndex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            String info = "Result: \n";
            for (int i = 0; i < cardList.size(); i++) {
                info += cardList.get(i).getFace() + ". Rating: " + cardList.get(i).getRating() + "\n";
            }
            alert.setTitle("Result");
            alert.setHeaderText(null);
            alert.setContentText(info);

            alert.showAndWait();
        } else {
            printCard();
        }
    }

    @FXML
    void initialize() {
        cardList.add(new Card("Face", "Лицо", "Английский", 5));
        cardList.add(new Card("Back", "Назад", "Английский", 5));
        cardList.add(new Card("Rock", "Камень", "Английский", 5));
        cardList.add(new Card("Picture", "Картинка", "Английский", 5));
        printCard();
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
    }
}
