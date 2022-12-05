package org.example;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private final Connection connection;
    private final Statement statement;
    private ResultSet result_set;

    private int boolToInt(Boolean input){
        return input ? 1 : 0;
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
        statement.execute("INSERT INTO 'Rooms' ('Room_Number', 'Visited_Times', 'Last_Visit') VALUES ('" +
                room.getRoom_Number() + "', '" + room.getVisited_Times() + "', '" + room.getLast_Visit() + "'); ");
    }

    public Room getRoom(int Room_Number) throws SQLException{
        result_set = statement.executeQuery("SELECT * FROM Rooms WHERE Room_Number IN ('" + Room_Number + "')");
        return new Room(result_set.getInt("Room_Number"), result_set.getInt("Visited_Times"),
                result_set.getString("Last_Visit"));
    }

    public Boolean isRoomExists(int Room_Number) throws SQLException{
        result_set = statement.executeQuery("SELECT * FROM Rooms WHERE Room_Number IN ('" + Room_Number + "')");
        return result_set.getLong("Room_Number") > 0;
    }

    public void updateRoom(Room room) throws SQLException {
        statement.execute("UPDATE Rooms " +
                "SET Visited_Times = '" + room.getVisited_Times() + "', Last_Visit = '" + room.getLast_Visit() + "' " +
                "WHERE Room_Number = " + room.getRoom_Number() + ";");
    }
    public void resetRooms() throws SQLException {
        statement.execute("UPDATE Rooms " +
                "SET Visited_Times = '0', Last_Visit = 'Никогда';");
    }

    public ArrayList getUnvisitedRooms() throws SQLException{
        ArrayList array = new ArrayList<>();
        result_set = statement.executeQuery("SELECT * FROM Rooms WHERE Visited_Times IN ('0')");
        while(result_set.next()){
            array.add(result_set.getInt("Room_Number"));
        }
        return array;
    }

    public void addViolator(Violator violator) throws SQLException{
        statement.execute("INSERT INTO Violators ('Violator_FullName', 'Room', 'Violations', 'Last_Violation') VALUES ('" +
                violator.getViolatorFullName() + "', '" + violator.getViolatorRoom() + "', '" + violator.getViolations() + "', '" + violator.getLast_Violation() + "'); ");

    }
    public Violator getViolator(int Violator_ID) throws SQLException {
        result_set = statement.executeQuery("SELECT * FROM Violators WHERE Violator_ID IN ('" + Violator_ID + "')");
        return new Violator(result_set.getInt("Violator_ID"), result_set.getString("Violator_FullName"),
                result_set.getInt("Room"), result_set.getInt("Violations"), result_set.getString("Last_Violation"));
    }

    public ArrayList findViolators(int Room) throws SQLException{
        ArrayList array = new ArrayList<>();
        result_set = statement.executeQuery("SELECT * FROM Violators WHERE Room IN ('" + Room + "')");
        while(result_set.next()){
            array.add(result_set.getInt("Violator_ID"));
        }
        return array;
    }

    public ArrayList findViolators(String Violator_FullName) throws SQLException{
        ArrayList array = new ArrayList<>();
        result_set = statement.executeQuery("SELECT * FROM Violators WHERE Violator_FullName LIKE ('%" + Violator_FullName + "%')");
        while(result_set.next()){
            array.add(result_set.getInt("Violator_ID"));
        }
        return array;
    }

    public boolean isViolatorsExists(String Violator_FullName) throws SQLException{
        result_set = statement.executeQuery("SELECT * FROM Violators WHERE Violator_FullName LIKE ('%" + Violator_FullName + "%')");
        return result_set.getInt("Violator_ID") != 0;
    }

    public boolean isViolatorsExists(int Room) throws SQLException{
        result_set = statement.executeQuery("SELECT * FROM Violators WHERE Room IN ('" + Room + "')");
        return result_set.getInt("Violator_ID") != 0;
    }

    public void updateViolator(Violator violator) throws SQLException{
        statement.execute("UPDATE Violators " +
                "SET Violations = '" + violator.getViolations() +"', Last_Violation = '" + violator.getLast_Violation() + "'"+
                "WHERE Violator_ID = " + violator.getViolatorID() + ";");
    }
}
