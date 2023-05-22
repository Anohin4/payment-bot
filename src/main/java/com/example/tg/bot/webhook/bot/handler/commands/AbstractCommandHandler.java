package com.example.tg.bot.webhook.bot.handler.commands;

import com.example.tg.bot.webhook.bot.handler.UpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.nonNull;

public abstract class AbstractCommandHandler implements UpdateHandler {
    protected abstract Boolean getCommandCondition(String commandText);

    @Override
    public boolean on(Update update) {
        return nonNull(update.getMessage())
                && nonNull(update.getMessage().getText())
                && getCommandCondition(update.getMessage().getText());
    }
}
