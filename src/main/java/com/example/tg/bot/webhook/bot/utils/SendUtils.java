package com.example.tg.bot.webhook.bot.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class SendUtils {
    public static SendMessage.SendMessageBuilder getMessage(Long who, String what, InlineKeyboardMarkup markup) {
        return SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what)
                .replyMarkup(markup);
    }
    public static SendMessage.SendMessageBuilder getMessage(Long who, String what) {
        return SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what);
    }
}
