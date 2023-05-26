package com.example.tg.bot.webhook.bot.service;


import com.example.tg.bot.webhook.bot.service.handler.CommandChain;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class UpdateService extends DefaultAbsSender {


    @Value("${tg.bot.name}")
    private String botName;
    private final CommandChain commandChain;
    private final ObjectMapper objectMapper;

    public UpdateService(CommandChain commandChain, ObjectMapper objectMapper, @Value("${tg.bot.token}") String token) {
        super(new DefaultBotOptions(), token);
        this.commandChain = commandChain;
        this.objectMapper = objectMapper;
    }


    @SneakyThrows
    public void handleUpdate(Update update) {
        log.info(objectMapper.writeValueAsString(update));
        var handle = commandChain.handle(update);
        sendText(handle);
    }

    public void sendText(BotApiMethod<?> method) {
        try {
            execute(method);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

}
