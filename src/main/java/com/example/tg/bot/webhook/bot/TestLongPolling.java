package com.example.tg.bot.webhook.bot;

import com.example.tg.bot.webhook.bot.handler.CommandChain;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@Slf4j
public class TestLongPolling extends TelegramLongPollingBot {
    private final ObjectMapper objectMapper;

    @Value("${tg.bot.name}")
    private String botName;
    private final CommandChain commandChain;


    public TestLongPolling(ObjectMapper objectMapper, @Value("${tg.bot.token}") String myToken, CommandChain commandChain) {
        super(myToken);
        this.objectMapper = objectMapper;
        this.commandChain = commandChain;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        log.info(objectMapper.writeValueAsString(update));

        SendMessage handle = commandChain.handle(update);

        sendText(handle);


    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void sendText(SendMessage sm) {

        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
}
