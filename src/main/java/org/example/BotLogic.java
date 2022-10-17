package org.example;

import org.telegram.telegrambots.meta.api.objects.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BotLogic {
    private int CurrentDialogueStatus = 0;
    private String UserName = "";
    private Map<String,String[]> LoadData(String filename) throws Exception{
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
    private void SaveData(Map<String,String[]> Data, String filename) throws Exception{
    Set Rooms = Data.keySet();
    Iterator iterator = Rooms.iterator();
    FileWriter file = new FileWriter(filename);
    while(iterator.hasNext()){
        var Current = iterator.next();
            file.write(Data.get(Current)[0]+"/"+
                        Data.get(Current)[1]+"/"+
                        Data.get(Current)[2]+"\n");
        }
    file.close();
    }
    private int TextToVariants(String text){
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

    public void SetDialogueStatus(int status){
        CurrentDialogueStatus = status;
    }
    public int GetDialogueStatusUpdate(){
        return CurrentDialogueStatus;
    }
    public void SetUser(String user) { UserName = user; }
    public String CreateAnswer (String Message){
        String answer = "";
        switch (CurrentDialogueStatus){

            case 0:// главное меню
                switch (TextToVariants(Message)){
                    case 1:
                        answer = "Бот помщник Beholder призван помогать скифам выбирать комнаты для посещения, контроллировать нарушителей, следить за графиком дежурств." +
                                "Возможные команды: Помощь, Нарушители";
                        break;
                    case 2:
                        answer = "Введите номер комнаты";
                        CurrentDialogueStatus = 21;
                        break;
                    case 3:
                        answer = "Введите номер комнаты";
                        CurrentDialogueStatus = 31;
                        break;
                    case 5:
                        if (UserName.equals("RenaultLogan496")
                                ||UserName.equals("vantouse"))
                        {
                            answer = "Введите данные о комнате следующим образом:\n Номер/Фамилии через пробел";
                            CurrentDialogueStatus = 51;
                        }
                        else  {
                            answer = "У вас нет доступа к этой функции";}
                        break;
                    default:
                        answer = "Привет," + "@" + UserName + "!\n Я бот-помощник Бехолдер!\n" +
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
                    Map<String, String[]> Data = LoadData("src/main/java/org/example/data.txt");
                    answer = "Проживают: " + Data.get(Message)[1] + "\nПосещений: " + Data.get(Message)[2];

                } catch (Exception e) {
                    answer = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
                }
                CurrentDialogueStatus = 0;
                break;
            case 31://акт
                try {
                    Map<String, String[]> Data = LoadData("src/main/java/org/example/data.txt");
                    String Fee = Integer.toString(Integer.parseInt(Data.get(Message)[2])+1);
                    String Temp[] = {Data.get(Message)[0],Data.get(Message)[1],Fee};
                    Data.put(Message,Temp);
                    SaveData(Data,"src/main/java/org/example/data.txt");
                    answer = "Успешно";

                } catch (Exception e) {
                    answer = "Хмм, похоже такой комнаты нет в моей базе...\n Напиши Добавить, внеси вклад в общее дело!";
                }
                CurrentDialogueStatus = 0;
                break;
            case 51://добавление новой комнаты
                try {
                    Map<String, String[]> Data = LoadData("src/main/java/org/example/data.txt");
                    Message += "/0";
                    String[] NewRoom = Message.split("/");
                    Data.put(NewRoom[0],NewRoom);
                    SaveData(Data,"src/main/java/org/example/data.txt");
                    answer = "Успешно";

                } catch (Exception e) {
                    answer = "Что-то пошло не так((\nПередай вот это @RenaultLogan496\n" + e.toString();
                }
                CurrentDialogueStatus = 0;
                break;

        }
       return answer;
    }
}
