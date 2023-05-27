package ru.example.tg.bot.service.handler.callback;

import ru.example.tg.bot.service.handler.commands.UpdateHandlingStrategy;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;


public class PreCheckoutQueryHandler implements UpdateHandlingStrategy {
    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        System.out.println("Start of PreCheckoutQueryHandler");
        PreCheckoutQuery preCheckoutQuery = update.getPreCheckoutQuery();
        AnswerPreCheckoutQuery preCheckoutQueryCommand = AnswerPreCheckoutQuery.builder()
                .ok(true)
                .preCheckoutQueryId(preCheckoutQuery.getId())
                .build();
        return List.of(preCheckoutQueryCommand);
    }

    @Override
    public boolean on(Update update) {

        return nonNull(update.getPreCheckoutQuery());

    }
}
