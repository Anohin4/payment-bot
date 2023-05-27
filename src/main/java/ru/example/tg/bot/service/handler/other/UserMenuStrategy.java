package ru.example.tg.bot.service.handler.other;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.tg.bot.model.CommandEnum;
import ru.example.tg.bot.service.handler.commands.UpdateHandlingStrategy;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.example.tg.bot.utils.Utils.getInvoiceKeyboard;

public class UserMenuStrategy implements UpdateHandlingStrategy {
    private final String ownerChatId;

    public UserMenuStrategy(String ownerChatId) {
        this.ownerChatId = ownerChatId;
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        return List.of(SendMessage
                .builder()
                .chatId(update.getMessage().getChatId())
                .text("""
                        Обязательно проверьте данные инвойса перед оплатой!
                                                
                        Чтобы получить инвойс, просто нажмите ⬇️""")
                .replyMarkup(getInvoiceKeyboard()).build());
    }

    @Override
    public boolean on(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        return thisIsNotAdminChatAndThisIsRequest(update, chatId) && thisInNotInvoiceRequest(update);
    }


    private boolean thisIsNotAdminChatAndThisIsRequest(Update update, String chatId) {
        return !chatId.equals(ownerChatId)
                && nonNull(update.getMessage())
                && isNull(update.getMessage().getSuccessfulPayment());
    }

    private boolean thisInNotInvoiceRequest(Update update) {
        String commandText = update.getMessage().getText();
        return isNull(commandText) || !commandText.startsWith(CommandEnum.GET_INVOICE_BY_USER.getValue());
    }
}
