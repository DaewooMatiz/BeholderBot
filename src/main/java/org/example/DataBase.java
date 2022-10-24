package org.example;
import java.sql.*;
public class DataBase {
    private final Connection connection;
    private final Statement statement;
    private ResultSet result_set;

    private int boolToInt(Boolean input){
        if (input)
            return 1;
        else
            return 0;
    }

    public DataBase(String path) throws SQLException, ClassNotFoundException {
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
        return new User(result_set.getLong("User_ID"), result_set.getString("User_Name"),
                result_set.getBoolean("Is_SKIF"), result_set.getBoolean("Is_Admin"));
    }

    public Boolean isUserExists(Long User_ID) throws SQLException{
        result_set = statement.executeQuery("SELECT * FROM Users WHERE User_ID IN ('" + User_ID + "')");
        return result_set.getLong("User_ID") > 0;
    }

    public void closeDataBase() throws SQLException{
        connection.close();
        result_set.close();
        statement.close();
    }
}
