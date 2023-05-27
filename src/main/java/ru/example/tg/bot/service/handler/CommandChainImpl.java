package ru.example.tg.bot.service.handler;

import ru.example.tg.bot.service.handler.commands.UpdateHandlingStrategy;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

public class CommandChainImpl implements CommandChain {
    private final List<UpdateHandlingStrategy> handlerList;

    public CommandChainImpl(List<UpdateHandlingStrategy> handlerList) {
        this.handlerList = handlerList;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Optional<BotApiMethod<?>> methodCall = Optional.empty();
        for (UpdateHandlingStrategy updateHandlingStrategy : handlerList) {
            if (updateHandlingStrategy.on(update)) {
                methodCall = updateHandlingStrategy.handle(update);
                break;
            }
        }
        if (methodCall.isEmpty()) {
            return SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Запрос не удалось обработать сообщение")
                    .build();
        }

        return methodCall.get();
    }

}
