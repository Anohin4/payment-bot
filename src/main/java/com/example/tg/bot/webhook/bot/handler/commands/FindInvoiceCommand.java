package com.example.tg.bot.webhook.bot.handler.commands;

import com.example.tg.bot.webhook.bot.model.CallbackCommandEnum;
import com.example.tg.bot.webhook.bot.model.CommandEnum;
import com.example.tg.bot.webhook.bot.utils.SendUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Optional;

@Component
public class FindInvoiceCommand extends AbstractCommandHandler {
    @Override
    public Optional<SendMessage.SendMessageBuilder> handle(Update update) {
        String[] s = update.getMessage().getText().split(" ");
        String invoiceValue = s[1];
        InlineKeyboardButton paymentButton = InlineKeyboardButton.builder()
                .text("Оплатить")
                .callbackData(CallbackCommandEnum.PAY_TO_INVOICE.getValue() + "_" + invoiceValue)
                .build();
        var keyboardM2 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(paymentButton))
                .build();

        Long id = update.getMessage().getFrom().getId();
        return Optional.of(SendUtils.getMessage(id, invoiceValue + " найден", keyboardM2));
    }

    @Override
    protected Boolean getCommandCondition(String commandText) {
        return commandText.startsWith(CommandEnum.FIND_INVOICE.getValue());
    }
}
