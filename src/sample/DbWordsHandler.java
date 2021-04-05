package sample;

import java.sql.*;
import java.util.*;
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

    public boolean isExist(String category) {
        try (Statement statement = this.conn.createStatement()) {
            ResultSet rs =  statement.executeQuery("SELECT EXISTS(SELECT * FROM words WHERE category = '" + category + "')");
            if (rs.getInt(1) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return false;
    }

    public List<Deck> getDecks() {
        try (Statement statement = this.conn.createStatement()) {
            //Сюда загружаем полученные из БД юзеров
            List<Deck> decks = new ArrayList<>();
            //выполняeм SQL запрос для получения доступа к данным
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT category FROM words");
            //Пока записи есть - добавляем их в контейнер студентов
            while (resultSet.next()) {
               decks.add(new Deck(resultSet.getString("category")));
            }
            return decks;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void updateData(String category, List<Card> cards) {
        try (Statement statement = this.conn.createStatement()) {
            statement.execute("DELETE FROM words WHERE category = '" + category + "'");
            for (Card card : cards) {
                addCard(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public List<Card> getCards(String category) {
        //Statement используется для выполнения SQL Запроса
        try (Statement statement = this.conn.createStatement()) {
            //Сюда загружаем полученные из БД юзеров
            List<Card> cards = new ArrayList<>();
            //выполняeм SQL запрос для получения доступа к данным
            ResultSet resultSet = statement.executeQuery("SELECT * FROM words WHERE category = '" + category + "'");
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

    public void addCard(Card card) {
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
}
