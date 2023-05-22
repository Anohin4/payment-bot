package com.example.tg.bot.webhook.bot.handler.callback;

import com.example.tg.bot.webhook.bot.model.CallbackCommandEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static com.example.tg.bot.webhook.bot.utils.SendUtils.getMessage;

@Component
public class PayInvoiceCallbackAction extends AbstractCallbackHandler {
    @Override
    public Optional<SendMessage.SendMessageBuilder> handle(Update update) {
        String[] data = update.getCallbackQuery().getData().split("_");
        String invoiceNumber = data[1];
        return Optional.of(getMessage(update.getCallbackQuery().getFrom().getId(), "Инвойс " + invoiceNumber + " оплачен"));
    }

    @Override
    protected Boolean getCallbackCondition(String commandText) {
        return commandText.startsWith(CallbackCommandEnum.PAY_TO_INVOICE.getValue());
    }
}
