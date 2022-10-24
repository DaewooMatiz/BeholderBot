package org.example;
import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.*;

public class BotLogic {
    private int CurrentDialogueStatus = 0;
    private String CurrentUserName = "";
    private Map<String,String[]> loadData(String filename) throws Exception{
        String RawData;
        FileReader file = new FileReader(filename);
        BufferedReader BufferedFile = new BufferedReader(file);
        Map<String,String[]> Data = new HashMap<>();
        int i = 0;
        while((RawData = BufferedFile.readLine()) != null) {
            Data.put(RawData.split("/")[0],RawData.split("/"));
            i++;
        }
        file.close();
        return Data;
    }
    private void saveData(Map<String,String[]> Data, String filename) throws Exception{
    Set Rooms = Data.keySet();
    Iterator iterator = Rooms.iterator();
    FileWriter file = new FileWriter(filename);
    while(iterator.hasNext()){
        var Current = iterator.next();
            file.write(Data.get(Current)[0]+"/"+
                        Data.get(Current)[1]+"/"+
                        Data.get(Current)[2]+"/"+
                        Data.get(Current)[3]+"\n");
        }
    file.close();
    }
    private int textToVariants(String text){
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
    public void setDialogueStatus(int status){
        CurrentDialogueStatus = status;
    }
    public int getDialogueStatusUpdate(){
        return CurrentDialogueStatus;
    }
    public void setUser(String user) { CurrentUserName = user; }
    public String createAnswer(String Message, Long user_ID, String user_Name){
        String answer = "";
        User CurrentUser = new User(user_ID, user_Name, false, false);
        try {
            try {
                DataBase db = new DataBase();
                    if(db.isUserExists(user_ID)){
                    CurrentUser = db.getUser(user_ID);
                    switch (CurrentDialogueStatus){

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
                                Message = Message;
                                Map<String, String[]> Data = loadData("src/main/java/org/example/data.txt");
                                answer = "Проживают: " + Data.get(Message)[1] + "\nПосещений: " + Data.get(Message)[2] +
                                        "\nПоследнее посещение: " + Data.get(Message)[3];

                            } catch (Exception e) {
                                answer = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
                            }
                            CurrentDialogueStatus = 0;
                            break;
                        case 31://посещении
                            try {
                                Map<String, String[]> Data = loadData("src/main/java/org/example/data.txt");
                                String Visit = Integer.toString(Integer.parseInt(Data.get(Message)[2])+1);
                                Date Date = new Date();
                                String DateStr= Date.toString().split(" ")[2]+ " " +
                                        Date.toString().split(" ")[1] + " " +
                                        Date.toString().split(" ")[3];
                                String Temp[] = {Data.get(Message)[0],Data.get(Message)[1],Visit,DateStr};
                                Data.put(Message,Temp);
                                saveData(Data,"src/main/java/org/example/data.txt");
                                answer = "Успешно";

                            } catch (Exception e) {
                                answer = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
                            }
                            CurrentDialogueStatus = 0;
                            break;
                        case 51://добавление новой комнаты
                            try {
                                Map<String, String[]> Data = loadData("src/main/java/org/example/data.txt");
                                Message += "/0/Никогда";
                                String[] NewRoom = Message.split("/");
                                Data.put(NewRoom[0],NewRoom);
                                saveData(Data,"src/main/java/org/example/data.txt");
                                answer = "Успешно";

                            } catch (Exception e) {
                                answer = "Что-то пошло не так((\nПередай вот это @RenaultLogan496\n" + e.toString() + "\nА ещё ты базу данных сломал.\nТоже ему передай, он обрадуется";
                            }
                            CurrentDialogueStatus = 0;
                            break;

                    }
                    }
                else {
                    answer = "Похоже тебя нет в базе. Добавляю...";
                    db.addUser(CurrentUser);
                }
                db.closeDataBase();


        }
            catch (SQLException e) {
                answer = e.toString();
        }
        } catch (ClassNotFoundException a){
            answer = a.toString();
        }





       return answer;
    }
}
