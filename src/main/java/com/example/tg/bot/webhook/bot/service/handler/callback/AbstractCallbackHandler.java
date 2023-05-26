package com.example.tg.bot.webhook.bot.service.handler.callback;

import com.example.tg.bot.webhook.bot.service.handler.UpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.nonNull;

public abstract class AbstractCallbackHandler implements UpdateHandler {
    protected abstract Boolean getCallbackCondition(String commandText);

    @Override
    public boolean on(Update update) {
        return nonNull(update.getCallbackQuery())
                && nonNull(update.getCallbackQuery().getData())
                && getCallbackCondition(update.getCallbackQuery().getData());
    }
}
