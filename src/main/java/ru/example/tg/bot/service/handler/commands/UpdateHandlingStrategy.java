package ru.example.tg.bot.service.handler.commands;


import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

public interface UpdateHandlingStrategy {
    List<BotApiMethod<?>> handle(Update update);

    boolean on(Update update);
}
