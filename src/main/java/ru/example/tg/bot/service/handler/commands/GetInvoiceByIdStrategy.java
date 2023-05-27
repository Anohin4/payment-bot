package ru.example.tg.bot.service.handler.commands;

import ru.example.tg.bot.database.DatabaseRepository;
import ru.example.tg.bot.model.CommandEnum;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Slf4j
public class GetInvoiceByIdStrategy extends AbstractCommandHandlerStrategy implements AdminCommand{

    private final DatabaseRepository repository;


    public GetInvoiceByIdStrategy(DatabaseRepository repository, String ownerChatId) {
        super(ownerChatId);
        this.repository = repository;

    }

    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {
        System.out.println("Start of GetInvoiceStrategyById");
        if (update.getMessage().getText().equals(CommandEnum.GET_INVOICE_BY_ID.getValue())) {
            return Optional.of(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Не указан ИД инвойса")
                    .build());
        }
        Message message = update.getMessage();
        var firstByUserName = repository.findById(message.getDate().longValue());
        if (firstByUserName.isPresent()) {
            return Optional.of(SendMessage.builder()
                    .chatId(message.getChatId())
                    .text(firstByUserName.get().toString())
                    .build());
        }

        return Optional.of(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Такой инвойс не существует")
                .build());


    }

    @Override
    protected Boolean getCommandCondition(String commandText) {
        return commandText.startsWith(CommandEnum.GET_INVOICE_BY_ID.getValue());
    }
}
