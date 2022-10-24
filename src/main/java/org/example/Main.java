package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try {
            BotLogic botLogic = new BotLogic(new DataBase(System.getenv("URL_DB")));
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new BeholderBot(System.getenv("BOT_TOKEN"), System.getenv("BOT_NAME"), botLogic));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}