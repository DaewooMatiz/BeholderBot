package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BeholderBot extends TelegramLongPollingBot {
    public BeholderBot(BotLogic botLogic){
        this.botLogic = botLogic;
    }
    private final BotLogic botLogic;
    int CurrentDialogueStatus = 0;
    @Override
    public void onUpdateReceived(Update update) {

        String user_name = update.getMessage().getChat().getFirstName();
        Long user_ID = update.getMessage().getChat().getId();
        botLogic.setDialogueStatus(CurrentDialogueStatus);
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            String messageText = update.getMessage().getText();
            message.setText(botLogic.createAnswer(messageText, user_ID, user_name));
            CurrentDialogueStatus = botLogic.getDialogueStatusUpdate();
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