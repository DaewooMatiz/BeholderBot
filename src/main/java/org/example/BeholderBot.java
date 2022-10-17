package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BeholderBot extends TelegramLongPollingBot {

    int Status = 0;
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

    public void SaveData(){

    }

    public int TextToVariants(String text){
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
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            String messageText = update.getMessage().getText();
            switch (Status){

                case 0:
                    switch (TextToVariants(messageText)){
                        case 1:
                            message.setText("Бот помщник Beholder призван помогать скифам выбирать комнаты для посещения, контроллировать нарушителей, следить за графиком дежурств." +
                                    "Возможные команды: Помощь, Нарушители" );
                            break;
                        case 2:
                            Status = 1;
                            message.setText("Введите номер комнаты");

                            break;
                        case 3:
                            if (update.getMessage().getChat().getUserName().equals("RenaultLogan496")
                                    ||update.getMessage().getChat().getUserName().equals("vantouse"))
                            {
                                message.setText("ACCESS GRANTED. HELLO "+update.getMessage().getChat().getFirstName());
                            }
                             else  {
                            message.setText("У вас нет доступа к этой функции");}
                            break;
                        default:
                            message.setText("Привет, "+update.getMessage().getChat().getFirstName()+"!  Я бот-помощник Beholder!");
                            break;

                    }
                    break;
                case 1:

                    message.setText("И чё?");
                    Status = 0;
                    break;
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "beholderskif_bot";
    }

    @Override
    public String getBotToken() {
        return "5688130794:AAGwkOjT0LaXoVmyuhRWL_BIEZXPy4t5IPk";
    }
}