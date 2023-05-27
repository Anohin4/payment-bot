package ru.example.tg.bot.service.handler.callback;

import ru.example.tg.bot.model.CommandEnum;
import ru.example.tg.bot.service.handler.commands.UpdateHandlingStrategy;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.List;

import static java.util.Objects.nonNull;

public class CallbackGetPaymentButton implements UpdateHandlingStrategy {
    private final UpdateHandlingStrategy chainHandler;

    public CallbackGetPaymentButton(UpdateHandlingStrategy chainHandler) {
        this.chainHandler = chainHandler;
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String userName = callbackQuery.getFrom().getUserName();
        Chat chat = callbackQuery.getMessage().getChat();
        //System.out.println("callback query " + callbackQuery.toString());

        Message message = new Message();

        message.setChat(chat);
        User user = new User();
        user.setUserName(userName);
        message.setFrom(user);
        update.setMessage(message);

        return chainHandler.handle(update);
    }

    @Override
    public boolean on(Update update) {
        return nonNull(update.getCallbackQuery())
                && nonNull(update.getCallbackQuery().getData())
                && update.getCallbackQuery().getData().equals(CommandEnum.GET_INVOICE_BY_USER.getValue());
    }
}
