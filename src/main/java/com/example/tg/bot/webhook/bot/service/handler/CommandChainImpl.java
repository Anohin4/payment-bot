package com.example.tg.bot.webhook.bot.service.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Component
public class CommandChainImpl implements CommandChain {
    private final List<UpdateHandler> handlerList;

    public CommandChainImpl(List<UpdateHandler> handlerList) {
        this.handlerList = handlerList;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        Optional<BotApiMethod<?>> builder = Optional.empty();
        for (UpdateHandler updateHandler : handlerList) {
            if (updateHandler.on(update)) {
                builder = updateHandler.handle(update);
                break;
            }
        }
        if (builder.isEmpty()) {
            throw new RuntimeException("No handler");
        }

        return builder.get();
    }
}
