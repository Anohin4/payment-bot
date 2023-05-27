package ru.example.tg.bot.service;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.example.tg.bot.service.handler.CommandChain;

import static java.util.Objects.nonNull;
import static ru.example.tg.bot.utils.Utils.getInvoiceKeyboard;

@Slf4j
public class UpdateService extends DefaultAbsSender {

    private final CommandChain commandChain;
    private final String ownerChat;

    public UpdateService(CommandChain commandChain, String botToken, String ownerChat) {
        super(new DefaultBotOptions(), botToken);
        this.commandChain = commandChain;
        this.ownerChat = ownerChat;
    }


    @SneakyThrows
    public void handleUpdate(Update update) {
        var resultMessage = commandChain.handle(update);
        for (BotApiMethod<?> botApiMethod : resultMessage) {
            sendText(botApiMethod);
        }
    }

    public void sendText(BotApiMethod<?> method) throws TelegramApiException {
        execute(method);                        //Actually sending the message
    }

}
