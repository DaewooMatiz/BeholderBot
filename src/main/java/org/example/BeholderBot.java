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
       if (text.contains("Помощь"))
           return 1;
       return 0;
    };
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            String messageText = update.getMessage().getText();
            switch (TextToVariants(messageText)){
                case '1':
                {
                    message.setText("Привет, "+update.getMessage().getChat().getFirstName()+"!  Я бот-помощник Beholder!" );
                    break;
                }
                default:
                {
                    message.setText("Ничего не понимаю. Отправь помощь, чтобы получить список возможных команд "+ TextToVariants(messageText));
                    break;
                }
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