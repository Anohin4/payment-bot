package ru.example.tg.bot.service.handler.commands;

import ru.example.tg.bot.database.DatabaseRepository;
import ru.example.tg.bot.model.CommandEnum;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

public class DeleteInvoiceStrategy extends AbstractCommandHandlerStrategy implements AdminCommand {

    private final DatabaseRepository repository;


    public DeleteInvoiceStrategy(DatabaseRepository repository, String ownerChatId) {
        super(ownerChatId);
        this.repository = repository;
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        System.out.println("Start of DeleteInvoiceStrategy");
        if (update.getMessage().getText().equals(CommandEnum.DELETE_INVOICE.getValue())) {
            return List.of(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Не указан ИД инвойса")
                    .build());
        }
        Message message = update.getMessage();
        repository.deleteInvoiceById(message.getDate().longValue());

        return List.of(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Запрос выполнен")
                .build());


    }

    @Override
    protected Boolean getCommandCondition(String commandText) {
        return commandText.startsWith(CommandEnum.DELETE_INVOICE.getValue());
    }
}
