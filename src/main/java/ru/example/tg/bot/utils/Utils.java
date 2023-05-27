package ru.example.tg.bot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.tg.bot.model.CommandEnum;

import java.util.List;

public class Utils {
    public static InlineKeyboardButton getInvoiceButton() {
        return InlineKeyboardButton.builder()
                .text("Получить инвойс")
                .callbackData(CommandEnum.GET_INVOICE_BY_USER.getValue()).build();
    }

    public static InlineKeyboardMarkup getInvoiceKeyboard() {
        return InlineKeyboardMarkup.builder().keyboardRow(List.of(getInvoiceButton())).build();
    }
}
