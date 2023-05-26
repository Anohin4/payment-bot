package com.example.tg.bot.webhook.bot.service.handler.commands;

import com.example.tg.bot.webhook.bot.service.handler.UpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;

import java.util.Optional;

import static java.util.Objects.nonNull;

@Component
public class PreCheckoutQueryHandler implements UpdateHandler {
    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {
        PreCheckoutQuery preCheckoutQuery = update.getPreCheckoutQuery();
        AnswerPreCheckoutQuery preCheckoutQueryCommand = AnswerPreCheckoutQuery.builder()
                .ok(true)
                .preCheckoutQueryId(preCheckoutQuery.getId())
                .build();
        return Optional.of(preCheckoutQueryCommand);
    }

    @Override
    public boolean on(Update update) {

       return nonNull(update.getPreCheckoutQuery());

    }
}
