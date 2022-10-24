package org.example;
import java.sql.*;
public class DataBase {
    private static final String path = "jdbc:sqlite:C:/Users/vova2/IdeaProjects/BeholderBot/src/main/DataBase.db";
    private Connection connection;
    private Statement statement;
    private ResultSet result_set;

    private int boolToInt(Boolean input){
        if (input)
            return 1;
        else
            return 0;
    }

    public DataBase() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(path);
        statement = connection.createStatement();
    }

    public void addUser(User hooman) throws SQLException{
        statement.execute("INSERT INTO 'Users' ('User_ID', 'User_Name', 'Is_SKIF', 'Is_Admin') VALUES ('" +
                hooman.User_ID + "', '" + hooman.User_Name + "', '" + boolToInt(hooman.Is_SKIF) + "', '" + boolToInt(hooman.Is_Admin) + "'); ");
    }

    public User getUser(Long User_ID) throws SQLException{
        result_set = statement.executeQuery("SELECT * FROM Users WHERE User_ID IN ('" + User_ID + "')");
        User found_User = new User(result_set.getLong("User_ID"), result_set.getString("User_Name"),
                result_set.getBoolean("Is_SKIF"), result_set.getBoolean("Is_Admin"));
        return found_User;
    }

    public Boolean isUserExists(Long User_ID) throws SQLException{
        result_set = statement.executeQuery("SELECT * FROM Users WHERE User_ID IN ('" + User_ID + "')");
        if (result_set.getLong("User_ID") <= 0) {
            return false;
        }
        return true;
    }

    public void closeDataBase() throws SQLException{
        connection.close();
        result_set.close();
        statement.close();
    }
}
