package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class BotLogic {
    private int CurrentDialogueStatus = 0;
    private String UserName = "";

    public void SetDialogueStatus(int status){
        CurrentDialogueStatus = status;
    }

    public int GetDialogueStatusUpdate(){
        return CurrentDialogueStatus;
    }

    public void SetUser(String user) { UserName = user; }

    public Map<String,String[]> LoadData(String filename) throws Exception{
        String RawData = "";
        FileReader file = new FileReader(filename);
        BufferedReader BufferedFile = new BufferedReader(file);
        Map<String,String[]> Data = new HashMap<String,String[]>();
        int i = 0;
        while((RawData = BufferedFile.readLine()) != null) {
            Data.put(RawData.split(" ")[0],RawData.split(" "));
            i++;
        }
        file.close();
        return Data;
    }

    private int TextToVariants(String text){
        if (text.contains("Выход"))
            return 0;
        if (text.contains("Помощь"))
            return 1;
        if (text.contains("Комнаты"))
            return 2;
        if (text.contains("Настройки"))
            return 3;
        return -1;
    }

    public void SaveData(){

    }

    public String CreateAnswer (String Message){
        String answer = "";
        switch (CurrentDialogueStatus){

            case 0:
                switch (TextToVariants(Message)){
                    case 1:
                        answer = "Бот помщник Beholder призван помогать скифам выбирать комнаты для посещения, контроллировать нарушителей, следить за графиком дежурств." +
                                "Возможные команды: Помощь, Нарушители";
                        break;
                    case 2:
                        answer = "Введите номер комнаты";
                        CurrentDialogueStatus = 1;
                        break;
                    case 3:
                        if (UserName.equals("RenaultLogan496")
                                ||UserName.equals("vantouse"))
                        {
                            answer = "Ладно";
                        }
                        else  {
                            answer = "У вас нет доступа к этой функции";}
                        break;
                    default:
                        answer = "Окей";
                        break;

                }
                break;
            case 1:

                answer = "И чё?";
                CurrentDialogueStatus = 0;
                break;
        }
       return answer;
    }
}
