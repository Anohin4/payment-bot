package ru.example.tg.bot.service;


import ru.example.tg.bot.model.CommandEnum;
import ru.example.tg.bot.service.handler.CommandChain;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static java.util.Objects.nonNull;

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
        sendText(resultMessage);
        var chatId = String.valueOf(update.getMessage().getChatId());
        if (thisIsNotAdminChatAndThisIsRequest(update, chatId) && weMustSendInvoiceButton(resultMessage)) {
            sendMenuOfActions(update);
        }
    }


    private void sendMenuOfActions(Update update) throws TelegramApiException {
        InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder()
                .text("Получить инвойс")
                .callbackData(CommandEnum.GET_INVOICE_BY_USER.getValue()).build();
        InlineKeyboardMarkup build = InlineKeyboardMarkup.builder().keyboardRow(List.of(keyboardButton)).build();
        sendText(SendMessage
                .builder()
                .chatId(update.getMessage().getChatId())
                .text("""
                        Обязательно проверьте данные инвойса перед оплатой!
                                                
                        Чтобы получить инвойс, просто нажмите ⬇️""")
                .replyMarkup(build).build());

    }

    private boolean weMustSendInvoiceButton(BotApiMethod<?> resultMessage) {
        return !resultMessage.getClass().equals(SendInvoice.class);
    }


    private boolean thisIsNotAdminChatAndThisIsRequest(Update update, String chatId) {
        return !chatId.equals(ownerChat) && nonNull(update.getMessage());
    }

    public void sendText(BotApiMethod<?> method) throws TelegramApiException {
        execute(method);                        //Actually sending the message
    }

}
