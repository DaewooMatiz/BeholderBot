package org.example;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class BotLogic {
    public BotLogic(DataBase db){
       this.db = db;
    }
    private int CurrentDialogueStatus = 0;
    private final DataBase db;
    private String[] buttons;


    private int textToVariants(String text) {
        if (text.contains("Выход"))
            return 0;
        if (text.contains("Помощь"))
            return 1;
        if (text.contains("Информация"))
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
        for (Object o : array) {
            Violator violator = db.getViolator((int) o);
            result += violator.getViolatorFullName() + "(" + violator.getViolatorID() + ")\n";
        }
        return result;
    }
    private String findViolators(int room) throws SQLException {
        ArrayList array = db.findViolators(room);
        String result = "";
        for (Object o : array) {
            Violator violator = db.getViolator((int) o);
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
            result = "Айди циферками пишется.";
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
                    "\nКомната: " + violator.getViolatorRoom() +
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
    private String roomsToVisit() throws SQLException{
        ArrayList array = db.getUnvisitedRooms();
        String result = "";
        for (Object o : array) {
            result += o +"\n";
        }
        return result;
    }
    private void buttonsMainMenu(){
        buttons = new String[5];
        buttons[0] = "Информация";
        buttons[1] = "Посетил";
        buttons[2] = "Нарушение";
        buttons[3] = "Добавить";
        buttons[4] = "Дежурство";
    }
    private void buttonsRoomViolator(){
        buttons = new String[3];
        buttons[0] = "Комната";
        buttons[1] = "Нарушитель";
        buttons[2] = "Выход";
    }
    private void buttonsViolatorChoose(String key) throws SQLException{
        try {
            if (db.isViolatorsExists(Integer.parseInt(key))) {
                String input = findViolators(Integer.parseInt(key));
                input = input.replaceAll("[^0-9\n]", "");
                buttons = input.split("\n");
            } else {
                buttons = new String[2];
                buttons[0] = "Добавить";
                buttons[1] = "Выход";
            }
        } catch(NumberFormatException e) {
            if (db.isViolatorsExists(key)) {
                String input = findViolators(key);
                input = input.replaceAll("[^0-9\n]", "");
                buttons = input.split("\n");
            } else {
                buttons = new String[2];
                buttons[0] = "Добавить";
                buttons[1] = "Выход";
            }

        }
    }
    private void buttonsCommon(){
        buttons = new String[1];
        buttons[0] = "Выход";
    }

    public void setDialogueStatus(int status) {
        CurrentDialogueStatus = status;
    }
    public int getDialogueStatusUpdate() {
        return CurrentDialogueStatus;
    }
    public String[] getButtons(){
        return buttons;
    }


    public String createAnswer(String Message, Long user_ID, String user_Name) {
        String answer = "";
        User CurrentUser = new User(user_ID, user_Name, false, false);
        Room CurrentRoom;
        try {
            if (db.isUserExists(user_ID)) {
                CurrentUser = db.getUser(user_ID);
                if (textToVariants(Message) == 0) {
                    answer = "Главное меню";
                    CurrentDialogueStatus = 0;
                    buttonsMainMenu();
                } else {
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
                                    buttonsMainMenu();
                                    break;
                                case 2:
                                    if (CurrentUser.isSKIF()) {
                                        answer = "Комната или Нарушитель?";
                                        CurrentDialogueStatus = 21;
                                        buttonsRoomViolator();
                                    } else {
                                        answer = "У тебя нет доступа к этой функции(";
                                        buttonsMainMenu();
                                    }

                                    break;
                                case 3:
                                    if (CurrentUser.isSKIF()) {
                                        answer = "Введи номер комнаты";
                                        CurrentDialogueStatus = 31;
                                        buttonsCommon();
                                    } else {
                                        answer = "У тебя нет доступа к этой функции(";
                                        buttonsMainMenu();
                                    }
                                    break;
                                case 4:
                                    if (CurrentUser.isSKIF()) {
                                        answer = "Введи имя или номер комнаты нарушителя";
                                        CurrentDialogueStatus = 41;
                                        buttonsCommon();
                                    } else {
                                        answer = "У тебя нет доступа к этой функции(";
                                        buttonsMainMenu();
                                    }
                                    break;
                                case 5:
                                    if (CurrentUser.isAdmin()) {
                                        answer = "Комната или Нарушитель?";
                                        CurrentDialogueStatus = 51;
                                        buttonsRoomViolator();
                                    } else {
                                        answer = "У тебя нет доступа к этой функции(";
                                    }
                                    break;
                                case 6:
                                    if (CurrentUser.isSKIF()) {
                                        answer = "Выходишь на дежурство, " + CurrentUser.getUserName() + "? Так держать!\n" +
                                                "Можешь посетить эти комнаты: \n" + roomsToVisit();
                                        buttonsMainMenu();
                                    } else {
                                        answer = "У тебя нет доступа к этой функции(";
                                        buttonsMainMenu();
                                    }
                                    break;
                                case 7:
                                    if (CurrentUser.isAdmin()) {
                                        answer = "Уверен? Да/Нет";
                                        CurrentDialogueStatus = 71;
                                        buttonsCommon();
                                    } else {
                                        answer = "У тебя нет доступа к этой функции(";
                                        buttonsMainMenu();
                                    }
                                    break;
                                default:
                                    answer = "Напиши помощь чтобы получить список команд";
                                    buttonsMainMenu();
                                    break;

                            }
                            break;
                        case 21:// инфа развилка
                            if (Message.contains("Комната")) {
                                CurrentDialogueStatus = 211;
                                answer = "Введи номер комнаты";
                                buttonsCommon();
                            } else if (Message.contains("Нарушитель")) {
                                CurrentDialogueStatus = 212;
                                answer = "Введи имя или номер комнаты нарушителя";
                                buttonsCommon();
                            } else {
                                answer = "Комната или Нарушитель?";
                                buttonsRoomViolator();
                            }
                            break;
                        case 31:// посещение
                            answer = newVisit(Message);
                            CurrentDialogueStatus = 0;
                            buttonsMainMenu();
                            break;
                        case 41: // результат поиска
                            answer = findViolatorsResult(Message);
                            CurrentDialogueStatus = 411;
                            buttonsViolatorChoose(Message);
                            break;
                        case 51:// добавить развилка
                            if (Message.contains("Комната")) {
                                CurrentDialogueStatus = 511;
                                answer = "Введи номер комнаты";
                                buttonsCommon();
                            } else if (Message.contains("Нарушитель")) {
                                CurrentDialogueStatus = 512;
                                answer = "Введи информацию о нарушителе следующим образом:\n" +
                                        "Полное имя, комната в которой проживает";
                                buttonsCommon();
                            } else {
                                answer = "Комната или Нарушитель?";
                                buttonsRoomViolator();
                            }
                            break;
                        case 71:// сброс развилка
                            if (Message.contains("Да")) {
                                db.resetRooms();
                                answer = "Информация о посещении комнат сброшена";
                                CurrentDialogueStatus = 0;
                                buttonsMainMenu();
                            } else if (Message.contains("Нет")) {
                                CurrentDialogueStatus = 0;
                                answer = "Ошибся? Бывает.";
                                buttonsMainMenu();
                            } else {
                                answer = "Уверен? Да/Нет";
                                buttonsCommon();
                            }
                            break;

                        case 211:// инфа о комнате
                            answer = getRoomInfo(Message);
                            CurrentDialogueStatus = 0;
                            buttonsMainMenu();
                            break;
                        case 212:// результат поиска
                            answer = findViolatorsResult(Message);
                            CurrentDialogueStatus = 2121;
                            buttonsViolatorChoose(Message);
                            break;
                        case 411:// нарушение
                            if (Message.contains("Добавить")) {
                                answer = "Введи информацию о нарушителе следующим образом:\n" +
                                        "Полное имя, комната в которой проживает";
                                CurrentDialogueStatus = 512;
                                buttonsCommon();
                            } else {
                                answer = newViolation(Message);
                                CurrentDialogueStatus = 0;
                                buttonsMainMenu();
                            }
                            break;
                        case 511:// добавление новой комнаты
                            answer = newRoom(Message);
                            CurrentDialogueStatus = 0;
                            buttonsMainMenu();
                            break;
                        case 512:// добавление нового нарушителя
                            answer = newViolator(Message);
                            CurrentDialogueStatus = 0;
                            buttonsMainMenu();
                            break;
                        case 2121:// инфа о нарушителе
                            if (Message.contains("Добавить")) {
                                answer = "Введи информацию о нарушителе следующим образом:\n" +
                                        "Полное имя, комната в которой проживает";
                                CurrentDialogueStatus = 512;
                                buttonsCommon();
                            } else {
                                answer = getViolatorInfo(Message);
                                CurrentDialogueStatus = 0;
                                buttonsMainMenu();
                            }
                    }
                }
            } else {
                answer = "Похоже тебя нет в базе. Добавляю...";
                buttonsMainMenu();
                db.addUser(CurrentUser);
            }
        } catch (SQLException e) {
            answer = "Произошла ошибка! Информация для разработчика: " + e.toString();
            CurrentDialogueStatus = 0;
            buttonsMainMenu();
        }

        return answer;
    }
}
