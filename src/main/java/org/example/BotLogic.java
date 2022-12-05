package org.example;

import java.sql.SQLException;
import java.util.ArrayList;

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
        if (text.contains("Дежурство"))
            return 6;
        if (text.contains("Сброс"))
            return 7;

        return -1;
    }
    private String findViolators(String name) throws SQLException {
        ArrayList array = db.findViolators(name);
        String result = "";
        for (int i = 0; i < array.size(); i++) {
            Violator violator = db.getViolator((int) array.get(i));
            result += violator.getViolatorFullName() + "(" + violator.getViolatorID() + ")\n";
        }
        return result;
    }
    private String findViolators(int room) throws SQLException {
        ArrayList array = db.findViolators(room);
        String result = "";
        for (int i = 0; i < array.size(); i++) {
            Violator violator = db.getViolator((int) array.get(i));
            result += violator.getViolatorFullName() + "(" + violator.getViolatorID() + ")\n";
        }
        return result;
    }
    private String findViolatorsResult(String key) throws SQLException {
        try {
            if (db.isViolatorsExists(Integer.parseInt(key))) {
                return "По запросу найдены: \n" + findViolators(Integer.parseInt(key)) + "Напиши (айди) нужного.\nЕсли среди них нет нужного напиши Добавить, чтобы добавить нового.";
            } else {
                return "Таких нарушителей не найдено, напиши Добавить чтобы добавить нового.";
            }
        } catch(NumberFormatException e) {
            if (db.isViolatorsExists(key)) {
                return "По запросу найдены: \n" + findViolators(key) + "Напиши (айди) нужного.\nЕсли среди них нет нужного напиши Добавить, чтобы добавить нового.";
            } else {
                return "Таких нарушителей не найдено, напиши Добавить чтобы добавить нового.";
            }

        }
    }
    private String newVisit(String key) throws SQLException {
        String result;
        try{
            if (db.isRoomExists(Integer.parseInt(key))) {
                Room CurrentRoom = db.getRoom(Integer.parseInt(key));
                CurrentRoom.newVisit();
                db.updateRoom(CurrentRoom);
                result = "Посещение комнаты " + CurrentRoom.getRoom_Number() + " отмечено.";
            }
            else {
                result = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
            }
        } catch(NumberFormatException e) {
            result = "НОМЕР! Я просил Н-О-М-Е-Р! ЦИФРАМИ!";
        }
        return result;
    }
    private String newViolation(String key) throws SQLException {
        String result;
        try{
            Violator violator = db.getViolator(Integer.parseInt(key));
            violator.newViolation();
            db.updateViolator(violator);
            result = "Нарушение успешно отмечено.";
        } catch(NumberFormatException e) {
            result = "Да никак вы блять не научитесь!";
        }
        return result;
    }
    private String getRoomInfo(String key) throws SQLException {
        String result;
        try{
            if (db.isRoomExists(Integer.parseInt(key))) {
                Room CurrentRoom = db.getRoom(Integer.parseInt(key));
                result = "Комната номер " + CurrentRoom.getRoom_Number() +
                        "\nПосещений: " + CurrentRoom.getVisited_Times() +
                        "\nПоследнее посещение: " + CurrentRoom.getLast_Visit() +
                        "\nПроживают:\n" + findViolators(Integer.parseInt(key));
            }
            else {
                result = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
            }
        } catch(NumberFormatException e) {
            result = "НОМЕР! Я просил Н-О-М-Е-Р! ЦИФРАМИ!";
        }
        return result;
    }
    private String getViolatorInfo(String key) throws SQLException {
        String result;
        try{
            Violator violator = db.getViolator(Integer.parseInt(key));
            result = "Полное имя: " + violator.getViolatorFullName() +
                    "\nАйди: " + violator.getViolatorID() +
                    "\nНарушений: " + violator.getViolations() +
                    "\nПоследнее нарушение: " + violator.getLast_Violation();
        } catch(NumberFormatException e) {
            result = "Айди циферками пишется.";
        }
        return result;
    }
    private String newRoom(String key) throws SQLException {
        String result;
        try{
            if (!db.isRoomExists(Integer.parseInt(key))) {
                Room CurrentRoom = new Room(Integer.parseInt(key), 0, "Никогда");
                db.addRoom(CurrentRoom);
                result = "Теперь в базе есть комната " + CurrentRoom.getRoom_Number();
            }
            else result = "Такая комната уже есть";

        } catch(NumberFormatException e) {
            result = "НОМЕР! Я просил Н-О-М-Е-Р! ЦИФРАМИ!";
        }
        return result;
    }
    private String newViolator(String key) throws SQLException {
        String result;
        try{
            if (!db.isViolatorsExists(key.split(",")[0])) {
                Violator violator = new Violator(0,
                        key.split(",")[0],
                        Integer.parseInt(key.replace(" ", "").split(",")[1]),
                        0, "Никогда");
                violator.newViolation();
                db.addViolator(violator);
                result = violator.getViolatorFullName() + " успешно добавлен в базу нарушителей";
            } else {
                result = "Такой нарушитель уже существует!";
            }
        } catch(NumberFormatException e) {
            result = "НОМЕР просил ЦИФРАМИ!";
        }
        return result;
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
                                        "Информация - информация о комнатах и нарушителях\n" +
                                        "Посетил - сообщить о посещении комнаты\n" +
                                        "Нарушение - сообщить о нарушителе\n" +
                                        "Добавить - добавить комнату\n" +
                                        "Дежурство - сообщить о выходе на дежурство и получить полезную информацию\n" +
                                        "Вопросы, пожелания, оскорбления в грубой форме пиши ему: @RenaultLogan496";
                                break;
                            case 2:
                                if (CurrentUser.isSKIF()) {
                                    answer = "Комната или Нарушитель?";
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
                                    answer = "Введи имя или номер комнаты нарушителя";
                                    CurrentDialogueStatus = 41;
                                } else {
                                    answer = "У тебя нет доступа к этой функции(";
                                }
                                break;
                            case 5:
                                if (CurrentUser.isAdmin()) {
                                    answer = "Комната или Нарушитель?";
                                    CurrentDialogueStatus = 51;
                                } else {
                                    answer = "У тебя нет доступа к этой функции(";
                                }
                                break;
                            case 6:
                                if (CurrentUser.isSKIF()) {
                                    answer = "Выходишь на дежурство, " + CurrentUser.getUserName() + "? Так держать!";
                                } else {
                                    answer = "У тебя нет доступа к этой функции(";
                                }
                                break;
                            case 7:
                                if (CurrentUser.isAdmin()) {
                                    answer = "Уверен? Да/Нет";
                                    CurrentDialogueStatus = 71;
                                } else {
                                    answer = "У тебя нет доступа к этой функции(";
                                }
                                break;
                            default:
                                answer = "Напиши помощь чтобы получить список команд";
                                break;

                        }
                        break;
                    case 21:// инфа развилка
                        if (Message.contains("Комната")) {
                            CurrentDialogueStatus = 211;
                            answer = "Введи номер комнаты";
                        } else if (Message.contains("Нарушитель")) {
                            CurrentDialogueStatus = 212;
                            answer = "Введи имя или номер комнаты нарушителя";
                        } else {
                            answer = "Комната или Нарушитель?";
                        }
                        break;
                    case 31:// посещение
                        answer = newVisit(Message);
                        CurrentDialogueStatus = 0;
                        break;
                    case 41: // результат поиска
                        answer = findViolatorsResult(Message);
                        CurrentDialogueStatus = 411;
                        break;
                    case 51:// добавить развилка
                        if (Message.contains("Комната")) {
                            CurrentDialogueStatus = 511;
                            answer = "Введи номер комнаты";
                        } else if (Message.contains("Нарушитель")) {
                            CurrentDialogueStatus = 512;
                            answer = "Введи информацию о нарушителе следующим образом:\n" +
                                    "Полное имя, комната в которой проживает";
                        } else {
                            answer = "Комната или Нарушитель?";
                        }
                        break;
                    case 71:// добавить развилка
                        if (Message.contains("Да")) {
                            db.resetRooms();
                            answer = "Информация о посещении комнат сброшена";
                            CurrentDialogueStatus = 0;
                        } else if (Message.contains("Нет")) {
                            CurrentDialogueStatus = 0;
                            answer = "Ошибся? Бывает.";
                        } else {
                            answer = "Уверен? Да/Нет";
                        }
                        break;

                    case 211:// инфа о комнате
                        answer = getRoomInfo(Message);
                        CurrentDialogueStatus = 0;
                        break;
                    case 212:// результат поиска
                        answer = findViolatorsResult(Message);
                        CurrentDialogueStatus = 2121;
                        break;
                    case 411:// нарушение
                        if (Message.contains("Добавить")){
                            answer = "Введи информацию о нарушителе следующим образом:\n" +
                                    "Полное имя, комната в которой проживает";
                            CurrentDialogueStatus = 512;
                        } else {
                            answer = newViolation(Message);
                            CurrentDialogueStatus = 0;
                        }
                        break;
                    case 511:// добавление новой комнаты
                        answer = newRoom(Message);
                        CurrentDialogueStatus = 0;
                        break;
                    case 512:// добавление новой комнаты
                        answer = newViolator(Message);
                        CurrentDialogueStatus = 0;
                        break;
                    case 2121:// инфа о нарушителе
                        if (Message.contains("Добавить")){
                            answer = "Введи информацию о нарушителе следующим образом:\n" +
                                    "Полное имя, комната в которой проживает";
                            CurrentDialogueStatus = 512;
                        } else {
                            answer = getViolatorInfo(Message);
                            CurrentDialogueStatus = 0;
                        }
                }
            } else {
                answer = "Похоже тебя нет в базе. Добавляю...";
                db.addUser(CurrentUser);
            }
        } catch (SQLException e) {
            answer = "Произошла ошибка! Информация для разработчика: " + e.toString();
        }

        return answer;
    }
}
