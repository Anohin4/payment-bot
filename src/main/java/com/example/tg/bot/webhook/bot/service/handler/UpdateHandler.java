package com.example.tg.bot.webhook.bot.service.handler;


import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public interface UpdateHandler {
    Optional<BotApiMethod<?>> handle(Update update);

    boolean on(Update update);
}
