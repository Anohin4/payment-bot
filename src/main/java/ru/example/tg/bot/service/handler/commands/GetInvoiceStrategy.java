package ru.example.tg.bot.service.handler.commands;

import ru.example.tg.bot.database.DatabaseRepository;
import ru.example.tg.bot.model.CommandEnum;
import ru.example.tg.bot.model.domain.InvoiceEntity;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class GetInvoiceStrategy extends AbstractCommandHandlerStrategy {

    private final DatabaseRepository repository;
    private final String token;

    public GetInvoiceStrategy(DatabaseRepository repository, String paymentToken, String ownerChatId) {
        super(ownerChatId);
        this.repository = repository;
        this.token = paymentToken;
    }

    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {

        Message message = update.getMessage();
        String userName = message.getFrom().getUserName();

        var firstByUserName = repository.findFirstByUserNameAndNotPaid(userName);

        if (firstByUserName.isPresent()) {
            InvoiceEntity invoiceEntity = firstByUserName.get();
            return Optional.of(SendInvoice.builder()
                    .chatId(message.getChatId())
                    .title("Оплата работ по локализации")
                    .description(invoiceEntity.getDescription())
                    .payload(String.valueOf(invoiceEntity.getRowId()))
                    .providerToken(token)
                    .currency("RUB")
                    .startParameter(UUID.randomUUID().toString())
                    .price(LabeledPrice.builder()
                            .amount(Integer.valueOf(invoiceEntity.getAmount()))
                            .label("Рубли")
                            .build())
                    .build());
        }

        return Optional.of(SendMessage.builder()
                .chatId(message.getChatId())
                .text("У вас нет не оплаченных инвойсов")
                .build());


    }

    @Override
    protected Boolean getCommandCondition(String commandText) {
        return commandText.startsWith(CommandEnum.GET_INVOICE_BY_USER.getValue());
    }
}
