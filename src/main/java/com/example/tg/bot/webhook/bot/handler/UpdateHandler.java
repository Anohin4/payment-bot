package com.example.tg.bot.webhook.bot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public interface UpdateHandler {
    Optional<SendMessage.SendMessageBuilder> handle(Update update);
    boolean on(Update update);
}
