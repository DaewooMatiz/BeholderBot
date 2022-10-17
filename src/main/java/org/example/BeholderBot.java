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
    int CurrentDialogueStatus = 0;
    @Override
    public void onUpdateReceived(Update update) {

        BotLogic Bot = new BotLogic();
        Bot.SetUser(update.getMessage().getChat().getUserName());
        Bot.SetDialogueStatus(CurrentDialogueStatus);
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            String messageText = update.getMessage().getText();
            message.setText(Bot.CreateAnswer(messageText));
            CurrentDialogueStatus = Bot.GetDialogueStatusUpdate();
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