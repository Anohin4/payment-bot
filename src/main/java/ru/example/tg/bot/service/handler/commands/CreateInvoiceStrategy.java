package ru.example.tg.bot.service.handler.commands;

import ru.example.tg.bot.database.DatabaseRepository;
import ru.example.tg.bot.model.CommandEnum;
import ru.example.tg.bot.model.domain.InvoiceEntity;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class CreateInvoiceStrategy extends AbstractCommandHandlerStrategy implements AdminCommand{

    private final DatabaseRepository repository;
    int startIndex = CommandEnum.NEW_INVOICE.getValue().length() + 1;

    public CreateInvoiceStrategy(DatabaseRepository repository, String ownerChatId) {
        super(ownerChatId);
        this.repository = repository;
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        System.out.println("Start of CreateInvoiceStrategy");
        Message message = update.getMessage();
        String text = message.getText();
        if (text.equals(CommandEnum.NEW_INVOICE.getValue())) {
            return getDefaultMessage(message);
        }
        return getInvoice(message);

    }

    private List<BotApiMethod<?>> getInvoice(Message message) {
        String text = message.getText();
        long rowId = message.getDate().longValue();

        Optional<InvoiceEntity> byId = repository.findById(rowId);
        if (byId.isPresent()) return Collections.emptyList();

        String[] split = text.substring(startIndex).split("//");
        if (split.length != 3) return getDefaultMessage(message);

        try {
            String userNme = split[0];
            int price = getPrice(split[1]);
            String description = split[2];
            InvoiceEntity entity = InvoiceEntity.builder()
                    .rowId(message.getMessageId().longValue())
                    .userName(userNme)
                    .amount(String.valueOf(price))
                    .description(description)
                    .build();
            Long id = repository.insertInvoice(entity);
            return getSuccessMessage(message, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return getErrorMessage(message, e.getMessage());
        }

    }

    private int getPrice(String s) {
        BigDecimal multiply = new BigDecimal(s).multiply(new BigDecimal(100));
        return multiply.intValue();
    }

    private List<BotApiMethod<?>> getDefaultMessage(Message message) {
        return List.of(SendMessage.builder()
                .chatId(message.getChatId())
                .text("""
                        Для создания инвойса отправте команду в следующем формате:
                        /newInvoice юзернейм клиента в ТГ//сумма(копейки через точку)//описание
                        Пример запроса:
                         /newInvoice valeiy91//1500.28//На оплату перевода за 1500 символов от ООО Ромашка""")
                .build());
    }

    private List<BotApiMethod<?>> getErrorMessage(Message message, String eMessage) {
        return List.of(SendMessage.builder()
                .chatId(message.getChatId())
                .text("При создании инвойса произошла ошибка. Убедитесь, что корретно выполнен ввод данных " + eMessage)
                .build());
    }

    private List<BotApiMethod<?>> getSuccessMessage(Message message, Long id) {
        return List.of(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Инвойс успешно создан под номером " + id)
                .build());
    }

    @Override
    protected Boolean getCommandCondition(String commandText) {
        return commandText.startsWith(CommandEnum.NEW_INVOICE.getValue());
    }
}
