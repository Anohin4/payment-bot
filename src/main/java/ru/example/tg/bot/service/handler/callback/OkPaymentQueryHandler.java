package ru.example.tg.bot.service.handler.callback;

import ru.example.tg.bot.database.DatabaseRepository;
import ru.example.tg.bot.model.domain.InvoiceEntity;
import ru.example.tg.bot.service.handler.commands.UpdateHandlingStrategy;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static java.util.Objects.nonNull;


public class OkPaymentQueryHandler implements UpdateHandlingStrategy {
    private final DatabaseRepository databaseRepository;
    private final String ownerChat;

    public OkPaymentQueryHandler(DatabaseRepository databaseRepository, String ownerChat) {
        this.databaseRepository = databaseRepository;
        this.ownerChat = ownerChat;
    }

    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {
        System.out.println("Start of OkPaymentQueryHandler");
        var result = update.getMessage().getSuccessfulPayment();
        int i = result.getTotalAmount() / 100;
        String userName = update.getMessage().getFrom().getUserName();
        String invoicePayload = result.getInvoicePayload();
        databaseRepository.updateEntityPaymentData(InvoiceEntity.builder()
                        .rowId(Long.valueOf(invoicePayload))
                .providerPaymentChargeId(result.getProviderPaymentChargeId())
                .telegramPaymentChargeId(result.getTelegramPaymentChargeId()).build());

        return Optional.of(SendMessage.builder()
                .chatId(ownerChat)
                .text("Был оплачен инвойс "+ invoicePayload + " на сумму " + i + " рублей, пользователем " + userName)
                .build());
    }

    @Override
    public boolean on(Update update) {

        return nonNull(update.getMessage()) && nonNull(update.getMessage().getSuccessfulPayment());

    }
}
