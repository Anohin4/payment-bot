package ru.example.tg.bot.service.handler.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.nonNull;

public abstract class AbstractCommandHandlerStrategy implements UpdateHandlingStrategy {
    protected AbstractCommandHandlerStrategy(String ownerChatId) {
        this.ownerId = ownerChatId;
    }

    protected abstract Boolean getCommandCondition(String commandText);
    protected final String ownerId;
    @Override
    public boolean on(Update update) {
        return nonNull(update.getMessage())
                && nonNull(update.getMessage().getText())
                && getCommandCondition(update.getMessage().getText()) && isAllowed(update);
    }

    /**
     * Проверяем, что админскую команду вызывает админ
     */
    protected boolean isAllowed(Update update) {
        String currentChatId = String.valueOf(update.getMessage().getChatId());

        if(AdminCommand.class.isAssignableFrom(this.getClass())) {
            return currentChatId.equals(ownerId);
        }
        return true;
    }
}
