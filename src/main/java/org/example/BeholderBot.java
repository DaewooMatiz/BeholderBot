package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.w3c.dom.Text;

import java.io.File;

public class BeholderBot extends TelegramLongPollingBot {
    public int TextToVariants(String text){
       if (text.contains("Выход"))
           return 0;
       if (text.contains("Помощь"))
           return 1;
       if (text.contains("Нарушители"))
           return 2;
       if (text.contains("Настройки"))
           return 3;
       return -1;
    };
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            String messageText = update.getMessage().getText();
            switch (TextToVariants(messageText)){
                case 1:
                    message.setText("Бот помщник Beholder призван помогать скифам выбирать комнаты для посещения, контроллировать нарушителей, следить за графиком дежурств." +
                            "Возможные команды: Помощь, Нарушители" );
                    break;
                case 2:
                    message.setText("Я ещё в разработке и не могу вывести список нарушителей.");
                    break;
                case 3:
                    if (update.getMessage().getChat().getUserName().equals("RenaultLogan496")||update.getMessage().getChat().getUserName().equals("vantouse"))
                    {
                        message.setText("ACCESS GRANTED. HELLO "+update.getMessage().getChat().getFirstName());
                    }
                     else  {
                    message.setText("Ай-яй-яй! Дитям тута не место!");}
                    break;
                default:
                    message.setText("Привет, "+update.getMessage().getChat().getFirstName()+"!  Я бот-помощник Beholder!");
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