package com.example.tg.bot.webhook.bot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandChain {
    SendMessage handle(Update update);
}