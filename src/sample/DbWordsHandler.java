package sample;

import java.io.IOException;

import java.sql.*;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Card;
import org.sqlite.JDBC;


public class DbWordsHandler {
    //Указыаем адрес базы данных пользователей для авторизации
    private static final String USERDATA = "jdbc:sqlite:words.db";
     //Будем использовать один экземпляр класса
    private static DbWordsHandler instance = null;
    
    public static synchronized DbWordsHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DbWordsHandler();
        return instance;
    }
    //здесь будем хранить соединение с БД
    private Connection conn;
    
    private DbWordsHandler() throws SQLException {
        //Регистрация драйвера, который обеспечит взаимодействие с БД
        DriverManager.registerDriver(new JDBC());
        //Подключаемся к БД
        this.conn = DriverManager.getConnection(USERDATA);
    }
    
    public List<Card> getCards(String category) {
        //Statement используется для выполнения SQL Запроса
        try (Statement statement = this.conn.createStatement()) {
            //Сюда загружаем полученные из БД юзеров
            List<Card> cards = new ArrayList<>();
            //выполняeм SQL запрос для получения доступа к данным
            ResultSet resultSet = statement.executeQuery("SELECT * FROM words WHERE category = " + category);
            //Пока записи есть - добавляем их в контейнер студентов
            while (resultSet.next()) {
                cards.add(new Card(resultSet.getString("face"),
                        resultSet.getString("back"),
                        resultSet.getString("category"),
                        resultSet.getInt("rating")));
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    //Добавить юзера
    public void addCard(Card card) {
        //Создаём заранее приготовленный запрос
        try (PreparedStatement statement = this.conn.prepareStatement(
        "INSERT INTO words('face', 'back', 'category', 'rating') VALUES(?, ?, ?, ?)")) {
            //Вставляем данные
            statement.setObject(1, card.getFace());
            statement.setObject(2, card.getBack());
            statement.setObject(3, card.getCategory());
            statement.setObject(4, card.getRating());
            //Делаем Card
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //открытие новой сцены
        public void openNewScene(String window, Node node) {
        Stage oldStage = (Stage)node.getScene().getWindow();
        oldStage.close();
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
        stage.setResizable(false);
        stage.showAndWait();
    }
}
