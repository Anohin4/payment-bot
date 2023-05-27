package ru.example.tg.bot.service.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.tg.bot.service.handler.commands.UpdateHandlingStrategy;
import ru.example.tg.bot.service.handler.other.UserMenuStrategy;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class CommandChainImpl implements CommandChain {
    private final List<UpdateHandlingStrategy> handlerList;
    private final String ownerChatId;

    public CommandChainImpl(List<UpdateHandlingStrategy> handlerList, String ownerChatId) {
        this.handlerList = handlerList;
        this.ownerChatId = ownerChatId;
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        UpdateHandlingStrategy handler = null;
        for (UpdateHandlingStrategy updateHandlingStrategy : handlerList) {
            if (updateHandlingStrategy.on(update)) {
                handler = updateHandlingStrategy;
                break;
            }
        }
        List<BotApiMethod<?>> handleResult = getHanlderResult(handler, update);

        //если нам нужно добавлять меню - добавляем
        var userMenuStrategy = new UserMenuStrategy(ownerChatId, handleResult);
        if(userMenuStrategy.on(update)) {
            handleResult = new ArrayList<>(handleResult);
            handleResult.addAll(userMenuStrategy.handle(update));
        }

        return handleResult;
    }

    private List<BotApiMethod<?>> getHanlderResult(UpdateHandlingStrategy handler, Update update) {
        if (isNull(handler)) {
            return List.of(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Запрос не удалось обработать сообщение")
                    .build());
        }
        return handler.handle(update);
    }

}
