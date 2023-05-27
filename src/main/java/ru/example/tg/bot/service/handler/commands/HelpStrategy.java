package ru.example.tg.bot.service.handler.commands;

import ru.example.tg.bot.model.CommandEnum;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class HelpStrategy extends AbstractCommandHandlerStrategy {


    public HelpStrategy(String ownerChatId) {
        super(ownerChatId);
    }

    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {
        System.out.println("Start of HelpStrategy");
        return Optional.of(SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("""
                        Доступные команды (но не на все могут быть права):
                        /newInvoice - создание инвойса
                        /getInvoice - получение инвойса для вас(параметр не требуется)
                        /getInvoiceById _номер инвойса_   - получение инвойса по его номеру
                        /deleteInvoiceById _номер инвойса_   - удаление инвойса по его номеру""")
                .build());

    }


    @Override
    protected Boolean getCommandCondition(String commandText) {
        return commandText.startsWith(CommandEnum.HELP.getValue());
    }
}
