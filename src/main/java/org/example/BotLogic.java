package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.*;

public class BotLogic {
    public BotLogic(DataBase db){
       this.db = db;
    }
    private int CurrentDialogueStatus = 0;
    private final DataBase db;
    private Map<String, String[]> loadData() throws Exception {
        String RawData;
        FileReader file = new FileReader("src/main/java/org/example/data.txt");
        BufferedReader BufferedFile = new BufferedReader(file);
        Map<String, String[]> Data = new HashMap<>();
        while ((RawData = BufferedFile.readLine()) != null) {
            Data.put(RawData.split("/")[0], RawData.split("/"));
        }
        file.close();
        return Data;
    }

    private void saveData(Map<String, String[]> Data) throws Exception {
        Set<String> Rooms = Data.keySet();
        Iterator<String> iterator = Rooms.iterator();
        FileWriter file = new FileWriter("src/main/java/org/example/data.txt");
        while (iterator.hasNext()) {
            var Current = iterator.next();
            file.write(Data.get(Current)[0] + "/" +
                    Data.get(Current)[1] + "/" +
                    Data.get(Current)[2] + "/" +
                    Data.get(Current)[3] + "\n");
        }
        file.close();
    }

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
                        try {
                            Map<String, String[]> Data = loadData();
                            answer = "Проживают: " + Data.get(Message)[1] + "\nПосещений: " + Data.get(Message)[2] +
                                    "\nПоследнее посещение: " + Data.get(Message)[3];

                        } catch (Exception e) {
                            answer = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
                        }
                        CurrentDialogueStatus = 0;
                        break;
                    case 31://посещении
                        try {
                            Map<String, String[]> Data = loadData();
                            String Visit = Integer.toString(Integer.parseInt(Data.get(Message)[2]) + 1);
                            Date Date = new Date();
                            String DateStr = Date.toString().split(" ")[2] + " " +
                                    Date.toString().split(" ")[1] + " " +
                                    Date.toString().split(" ")[3];
                            String[] Temp = {Data.get(Message)[0], Data.get(Message)[1], Visit, DateStr};
                            Data.put(Message, Temp);
                            saveData(Data);
                            answer = "Успешно";

                        } catch (Exception e) {
                            answer = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
                        }
                        CurrentDialogueStatus = 0;
                        break;
                    case 51://добавление новой комнаты
                        try {
                            Map<String, String[]> Data = loadData();
                            Message += "/0/Никогда";
                            String[] NewRoom = Message.split("/");
                            Data.put(NewRoom[0], NewRoom);
                            saveData(Data);
                            answer = "Успешно";

                        } catch (Exception e) {
                            answer = "Что-то пошло не так((\nПередай вот это @RenaultLogan496\n" + e + "\nА ещё ты базу данных сломал.\nТоже ему передай, он обрадуется";
                        }
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
