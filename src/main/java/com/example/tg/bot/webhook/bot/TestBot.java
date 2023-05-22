package com.example.tg.bot.webhook.bot;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

public class TestBot extends SpringWebhookBot {
    public TestBot(SetWebhook setWebhook, String botToken) {
        super(setWebhook, botToken);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return null;
    }

    @Override
    public String getBotUsername() {
        return null;
    }
}
