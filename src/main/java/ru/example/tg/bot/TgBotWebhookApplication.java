package ru.example.tg.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.tg.bot.database.DatabaseRepository;
import ru.example.tg.bot.database.YdbRepository;
import ru.example.tg.bot.model.Request;
import ru.example.tg.bot.model.Response;
import ru.example.tg.bot.service.UpdateService;
import ru.example.tg.bot.service.handler.CommandChainImpl;
import ru.example.tg.bot.service.handler.callback.CallbackGetPaymentButton;
import ru.example.tg.bot.service.handler.callback.OkPaymentQueryHandler;
import ru.example.tg.bot.service.handler.callback.PreCheckoutQueryHandler;
import ru.example.tg.bot.service.handler.commands.*;

import java.util.List;
import java.util.function.Function;

@Slf4j
public class TgBotWebhookApplication implements Function<Request, Response> {

    @Override
    @SneakyThrows
    public Response apply(Request request) {

        String tgBotToken = System.getenv("tg_bot_token");
        String tgBotPayment = System.getenv("tg_bot_payment");
        String dbConnection = System.getenv("YDB_ENDPOINT");
        String dbPath = System.getenv("YDB_DATABASE");
        String ownerChat = System.getenv("owner_chat");
        ObjectMapper mapper = new ObjectMapper();
        DatabaseRepository ydbRepository = new YdbRepository(dbConnection, dbPath);
        System.out.println(request.getBody());
        Update body = mapper.readValue(request.getBody(), Update.class);


        List<UpdateHandlingStrategy> updateHandlingStrategies = handlingStrategyList(tgBotPayment, ydbRepository, ownerChat);

        CommandChainImpl commandChain = new CommandChainImpl(updateHandlingStrategies, ownerChat);
        UpdateService updateService = new UpdateService(commandChain, tgBotToken, ownerChat);
        updateService.handleUpdate(body);
        return new Response(200, "Ok");
    }

    private List<UpdateHandlingStrategy> handlingStrategyList(String paymentToken, DatabaseRepository repository, String ownerChat) {
        GetInvoiceForUserStrategy getInvoiceStrategy = new GetInvoiceForUserStrategy(repository, paymentToken, ownerChat);
        return List.of(
                new CreateInvoiceStrategy(repository, ownerChat),
                getInvoiceStrategy,
                new PreCheckoutQueryHandler(),
                new StartStrategy(ownerChat),
                new HelpStrategy(ownerChat),
                new GetInvoiceByIdStrategy(repository, ownerChat),
                new OkPaymentQueryHandler(repository, ownerChat),
                new CallbackGetPaymentButton(getInvoiceStrategy),
                new DeleteInvoiceStrategy(repository, ownerChat)

        );
    }
}
