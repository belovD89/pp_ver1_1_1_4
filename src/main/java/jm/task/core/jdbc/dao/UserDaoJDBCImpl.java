package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

public class UserDaoJDBCImpl implements UserDao {
    //вызов статического метода getConnection() у класса Util(соединение с БД)
    private final Connection connection = Util.getConnection();  // статик финал поле для подключения к БД

    public UserDaoJDBCImpl() {

    }

    // создание таблицы, если не создана
    public void createUsersTable() {
        String createTable = "CREATE TABLE usersTable (" +
                "  id INTEGER NOT NULL AUTO_INCREMENT ," +
                "  name VARCHAR(45) NOT NULL," +
                "  last_name VARCHAR(45) NOT NULL," +
                "  age INT NOT NULL," +
                "  PRIMARY KEY  (id)," +
                "  UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE);";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTable);
            System.out.println("Таблица создана");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("При создании таблицы произошла ошибка");
        }
    }

    // удаление таблицы, если она уже существует
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS usersTable");
            System.out.println("Удалена старая таблица");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка при удалении таблицы");
        }

    }

    // добавление в таблицу
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement pstm = connection.prepareStatement("INSERT INTO usersTable (name, last_name, age) VALUES (?, ?, ?)")) {
            pstm.setString(1, name);
            pstm.setString(2, lastName);
            pstm.setByte(3, age);
            pstm.executeUpdate();
            System.out.println("Добавлен пользователь:" + name);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Не добавлен пользователь:" + name);
        }
    }

    // удаление user(s) из таблицы
    public void removeUserById(long id) {
        try (PreparedStatement pstm = connection.prepareStatement("DELETE FROM usersTable WHERE id = ?")) {
            pstm.setLong(1, id);
            pstm.executeUpdate();
            System.out.println("Добавлен пользователь:");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка при удалении пользоватля");
        }

    }

    // получить всех user(s) из таблицы
    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM usersTable";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));


                userList.add(user);
                System.out.println("Получены пользователи");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка при получении пользоватлей");
        }
        return userList;
    }

    // удаление значений в таблице
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE usersTable");
            System.out.println("Теперь таблица пустая");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка при удалении значений из таблицы");
        }

    }
}
