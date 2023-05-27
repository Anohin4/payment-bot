package ru.example.tg.bot.service.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface CommandChain {
    List<BotApiMethod<?>> handle(Update update);
}
