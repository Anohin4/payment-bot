package com.example.tg.bot.webhook.bot.service.handler.callback;

import com.example.tg.bot.webhook.bot.model.CallbackCommandEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static com.example.tg.bot.webhook.bot.utils.SendUtils.getMessage;

@Component
public class PayInvoiceCallbackAction extends AbstractCallbackHandler {
    private final RestTemplate restTemplate;

    public PayInvoiceCallbackAction(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {
        String[] data = update.getCallbackQuery().getData().split("_");
        String invoiceNumber = data[1];
        return Optional.of(getMessage(update.getCallbackQuery().getFrom().getId(), "Инвойс " + invoiceNumber + " оплачен"));
    }

    @Override
    protected Boolean getCallbackCondition(String commandText) {
        return commandText.startsWith(CallbackCommandEnum.PAY_TO_INVOICE.getValue());
    }
}
