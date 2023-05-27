package ru.example.tg.bot.service.handler.commands;

import ru.example.tg.bot.model.CommandEnum;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Slf4j
public class StartStrategy extends AbstractCommandHandlerStrategy {
    public StartStrategy(String ownerChatId) {
        super(ownerChatId);
    }

    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {
        return Optional.of(SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("""
                        Привет! 
                        С помощью этого бота можно оплатить услуги по локализации видеоигр и переводу текстов, оказанные ИП Анохиной Е.Е..
                        Если на ваше имя пользователя есть инвойс,  бот без проблем его найдет.""")
                .build());

    }


    @Override
    protected Boolean getCommandCondition(String commandText) {
        return commandText.startsWith(CommandEnum.START.getValue());
    }
}
