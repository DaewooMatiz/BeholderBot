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
        if (text.contains("Информация"))
            return 2;
        if (text.contains("Посетил"))
            return 3;
        if (text.contains("Нарушения"))
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
                                answer = "Бот помщник Beholder призван помогать скифам выбирать комнаты для посещения, контроллировать нарушителей, следить за графиком дежурств." +
                                        "Возможные команды: Помощь, Нарушители";
                                break;
                            case 2:
                                if (CurrentUser.Is_SKIF) {
                                    answer = "Введите номер комнаты";
                                    CurrentDialogueStatus = 21;
                                } else {
                                    answer = "У вас нет доступа к этой функции";
                                }

                                break;
                            case 3:
                                if (CurrentUser.Is_SKIF) {
                                    answer = "Введите номер комнаты";
                                    CurrentDialogueStatus = 31;
                                } else {
                                    answer = "У вас нет доступа к этой функции";
                                }
                                break;
                            case 5:
                                if (CurrentUser.Is_Admin) {
                                    answer = "Введите данные о комнате следующим образом:\n Номер/Фамилии через пробел";
                                    CurrentDialogueStatus = 51;
                                } else {
                                    answer = "У вас нет доступа к этой функции";
                                }
                                break;
                            default:
                                answer = "Привет," + CurrentUser.User_Name + "!\n Я бот-помощник Бехолдер!\n" +
                                        "Информация - информация о комнате\n" +
                                        "Посетил - сообщить о посещении комнаты\n" +
                                        "Добавить - добавить комнату\n" +
                                        "Вопросы, пожелания, оскорбления в грубой форме пиши ему: @RenaultLogan496";
                                break;
                        }
                        break;
                    case 21:// инфа о комнате
                        if (db.isRoomExists(Integer.parseInt(Message))) {
                            CurrentRoom = db.getRoom(Integer.parseInt(Message));
                            answer = "Комната номер " + CurrentRoom.getRoom_Number() + "\nПосещений: " + CurrentRoom.getVisited_Times() +
                                    "\nПоследнее посещение: " + CurrentRoom.getLast_Visit() + "\nНарушений: " + CurrentRoom.getWarnings();
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
                            answer = "Успешно обновлена информация о комнате " + CurrentRoom.getRoom_Number();
                        }
                        else {
                            answer = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
                        }
                        CurrentDialogueStatus = 0;
                        break;
                    case 51://добавление новой комнаты

                            CurrentRoom = new Room(Integer.parseInt(Message),0,"Never", 0);
                            db.addRoom(CurrentRoom);
                            answer = "Успешно добавлена комната " + CurrentRoom.getRoom_Number();

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
