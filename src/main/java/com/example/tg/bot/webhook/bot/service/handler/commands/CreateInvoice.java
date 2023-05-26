package com.example.tg.bot.webhook.bot.service.handler.commands;

import com.example.tg.bot.webhook.bot.model.CommandEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class CreateInvoice extends AbstractCommandHandler {

    private final String token;
    int startIndex = CommandEnum.NEW_INVOICE.getValue().length() + 1;

    public CreateInvoice(@Value("${tg.bot.payment}") String token) {
        this.token = token;
    }

    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        if (text.equals(CommandEnum.NEW_INVOICE.getValue())) {
            return getDefaultMessage(message);
        }
        return getInvoice(message);

    }

    private Optional<BotApiMethod<?>> getInvoice(Message message) {
        String text = message.getText();

        String[] split = text.substring(startIndex).split("//");
        if (split.length != 2) return getDefaultMessage(message);

        try {
            int price = getPrice(split[0]);
            return Optional.of(SendInvoice.builder()
                    .chatId(message.getChatId())
                    .title("Оплата работ по локализации")
                    .description(split[1])



                    .payload("testPayload")
                    .providerToken(token)
                    .currency("RUB")
                    .startParameter(UUID.randomUUID().toString())
                    .price(LabeledPrice.builder()
                            .amount(price)
                            .label("Рубли")
                            .build())
                    .build());

        } catch (Exception e) {
            log.info(e.getMessage());
            return getDefaultMessage(message);
        }


    }

    private int getPrice(String s) {
        BigDecimal multiply = new BigDecimal(s).multiply(new BigDecimal(100));
        return multiply.intValue();
    }

    private Optional<BotApiMethod<?>> getDefaultMessage(Message message) {
        return Optional.of(SendMessage.builder()
                .chatId(message.getChatId())
                .text("""
                        Для создания инвойса отправте команду в следующем формате:
                        /newInvoice сумма(копейки через точку)//описание
                        Пример запроса:
                         /newInvoice 1500.28//На оплату перевода за 1500 символов от ООО Ромашка""")
                .build());
    }

    @Override
    protected Boolean getCommandCondition(String commandText) {
        return commandText.startsWith(CommandEnum.NEW_INVOICE.getValue());
    }
}
