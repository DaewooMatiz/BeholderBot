package org.example;

import java.sql.SQLException;
import java.util.Date;

public class BotLogic {
    public BotLogic(DataBase db){
       this.db = db;
    }
    private int CurrentDialogueStatus = 0;
    private final DataBase db;


    private int textToVariants(String text) {
        if (text.contains("Выход"))
            return 0;
        if (text.contains("Помощь"))
            return 1;
        if (text.contains("Инфа"))
            return 2;
        if (text.contains("Посетил"))
            return 3;
        if (text.contains("Нарушение"))
            return 4;
        if (text.contains("Добавить"))
            return 5;

        return -1;
    }

    public void setDialogueStatus(int status) {
        CurrentDialogueStatus = status;
    }

    public int getDialogueStatusUpdate() {
        return CurrentDialogueStatus;
    }

    public String createAnswer(String Message, Long user_ID, String user_Name) {
        String answer = "";
        User CurrentUser = new User(user_ID, user_Name, false, false);
        Room CurrentRoom;
        try {
            if (db.isUserExists(user_ID)) {
                CurrentUser = db.getUser(user_ID);
                switch (CurrentDialogueStatus) {

                    case 0:// главное меню
                        switch (textToVariants(Message)) {
                            case 1:
                                answer = "Привет, " + CurrentUser.getUserName() + "!\nЯ бот-помощник Бехолдер!\n" +
                                        "Инфа - информация о комнате\n" +
                                        "Посетил - сообщить о посещении комнаты\n" +
                                        "Нарушение - сообщить о нарушении в комнате\n" +
                                        "Добавить - добавить комнату\n" +
                                        "Вопросы, пожелания, оскорбления в грубой форме пиши ему: @RenaultLogan496";
                                break;
                            case 2:
                                if (CurrentUser.isSKIF()) {
                                    answer = "Введи номер комнаты";
                                    CurrentDialogueStatus = 21;
                                } else {
                                    answer = "У тебя нет доступа к этой функции(";
                                }

                                break;
                            case 3:
                                if (CurrentUser.isSKIF()) {
                                    answer = "Введи номер комнаты";
                                    CurrentDialogueStatus = 31;
                                } else {
                                    answer = "У тебя нет доступа к этой функции(";
                                }
                                break;
                            case 4:
                                if (CurrentUser.isSKIF()) {
                                    answer = "Введи номер комнаты";
                                    CurrentDialogueStatus = 41;
                                } else {
                                    answer = "У тебя нет доступа к этой функции(";
                                }
                                break;
                            case 5:
                                if (CurrentUser.isAdmin()) {
                                    answer = "Введи номер комнаты";
                                    CurrentDialogueStatus = 51;
                                } else {
                                    answer = "У тебя нет доступа к этой функции(";
                                }
                                break;
                            default:
                                answer = "Напиши помощь чтобы получить список команд";
                                break;
                        }
                        break;
                    case 21:// инфа о комнате
                        if (db.isRoomExists(Integer.parseInt(Message))) {
                            CurrentRoom = db.getRoom(Integer.parseInt(Message));
                            answer = "Комната номер " + CurrentRoom.getRoom_Number() + "\nПосещений: " + CurrentRoom.getVisited_Times() +
                                    "\nПоследнее посещение: " + CurrentRoom.getLast_Visit() + "\nНарушений: " + CurrentRoom.getWarnings()
                                    + "\nПоследнее нарушение: " + CurrentRoom.getLast_Warning();
                        }
                        else {
                            answer = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
                        }
                        CurrentDialogueStatus = 0;
                        break;
                    case 31://посещении
                        if (db.isRoomExists(Integer.parseInt(Message))) {
                            CurrentRoom = db.getRoom(Integer.parseInt(Message));
                            CurrentRoom.newVisit(new Date());
                            db.updateRoom(CurrentRoom);
                            answer = "Посещение комнаты " + CurrentRoom.getRoom_Number() + " отмечено.";
                        }
                        else {
                            answer = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
                        }
                        CurrentDialogueStatus = 0;
                        break;
                    case 41://посещении
                        if (db.isRoomExists(Integer.parseInt(Message))) {
                            CurrentRoom = db.getRoom(Integer.parseInt(Message));
                            CurrentRoom.newWarning(new Date());
                            db.updateRoom(CurrentRoom);
                            answer = "Нарушение в комнате " + CurrentRoom.getRoom_Number() + " отмечено.";
                        }
                        else {
                            answer = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
                        }
                        CurrentDialogueStatus = 0;
                        break;
                    case 51://добавление новой комнаты
                        if (!db.isRoomExists(Integer.parseInt(Message))) {
                            CurrentRoom = new Room(Integer.parseInt(Message), 0, "Никогда", 0, "Никогда");
                            db.addRoom(CurrentRoom);
                            answer = "Теперь в базе есть комната " + CurrentRoom.getRoom_Number();
                        }
                        else answer = "Такая комната уже есть";

                        CurrentDialogueStatus = 0;
                        break;

                }
            } else {
                answer = "Похоже тебя нет в базе. Добавляю...";
                db.addUser(CurrentUser);
            }
        } catch (SQLException e) {
            answer = e.toString();
        }

        return answer;
    }
}
