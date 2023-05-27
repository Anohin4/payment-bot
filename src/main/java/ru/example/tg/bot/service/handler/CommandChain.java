package ru.example.tg.bot.service.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandChain {
    BotApiMethod<?> handle(Update update);
}
