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
                hooman.getUserID() + "', '" + hooman.getUserName() + "', '" + boolToInt(hooman.isSKIF()) + "', '" + boolToInt(hooman.isAdmin()) + "'); ");
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

    public void addRoom(Room room) throws SQLException{
        statement.execute("INSERT INTO 'Rooms' ('Room_Number', 'Visited_Times', 'Last_Visit', 'Warnings', 'Last_Warning') VALUES ('" +
                room.getRoom_Number() + "', '" + room.getVisited_Times() + "', '" + room.getLast_Visit() + "', '" + room.getWarnings() + "', '" + room.getLast_Warning() + "'); ");
    }

    public Room getRoom(int Room_Number) throws SQLException{
        result_set = statement.executeQuery("SELECT * FROM Rooms WHERE Room_Number IN ('" + Room_Number + "')");
        return new Room(result_set.getInt("Room_Number"), result_set.getInt("Visited_Times"),
                result_set.getString("Last_Visit"), result_set.getInt("Warnings"), result_set.getString("Last_Warning"));
    }

    public Boolean isRoomExists(int Room_Number) throws SQLException{
        result_set = statement.executeQuery("SELECT * FROM Rooms WHERE Room_Number IN ('" + Room_Number + "')");
        return result_set.getLong("Room_Number") > 0;
    }

    public void updateRoom(Room room) throws SQLException{
        statement.execute("UPDATE Rooms " +
                "SET Visited_Times = '" + room.getVisited_Times() +"', Last_Visit = '" + room.getLast_Visit() + "', Warnings = '" +
                room.getWarnings() + "', Last_Warning = '" + room.getLast_Warning() + "' " +
                "WHERE Room_Number = " + room.getRoom_Number() + ";");
    }

}
