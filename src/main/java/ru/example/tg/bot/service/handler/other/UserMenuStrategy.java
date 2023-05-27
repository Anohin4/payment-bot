package ru.example.tg.bot.service.handler.other;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.tg.bot.model.CommandEnum;
import ru.example.tg.bot.service.handler.commands.UpdateHandlingStrategy;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.example.tg.bot.utils.Utils.getInvoiceKeyboard;

public class UserMenuStrategy implements UpdateHandlingStrategy {
    private final String ownerChatId;
    private final List<BotApiMethod<?>> botApiMethods;

    public UserMenuStrategy(String ownerChatId, List<BotApiMethod<?>> botApiMethods) {
        this.ownerChatId = ownerChatId;
        this.botApiMethods = botApiMethods;
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        System.out.println("sending menu on update " + update);
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
        if(isNull(update.getMessage()) || isNull(update.getMessage().getChatId())) {
            return false;
        }
        String chatId = String.valueOf(update.getMessage().getChatId());
        return thisIsNotAdminChatAndThisIsRequest(update, chatId) && thisInNotInvoiceRequest();
    }


    private boolean thisIsNotAdminChatAndThisIsRequest(Update update, String chatId) {
        return !chatId.equals(ownerChatId)
                && nonNull(update.getMessage())
                && isNull(update.getMessage().getSuccessfulPayment())
                && isNull(update.getPreCheckoutQuery());
    }

    private boolean thisInNotInvoiceRequest() {
        var first = botApiMethods.stream()
                .map(BotApiMethod::getClass)
                .filter(it -> it.equals(SendInvoice.class))
                .findFirst();
        return first.isEmpty();
    }
}
