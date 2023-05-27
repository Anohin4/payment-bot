package ru.example.tg.bot.database;

import ru.example.tg.bot.model.domain.InvoiceEntity;
import tech.ydb.auth.iam.CloudAuthHelper;
import tech.ydb.core.grpc.GrpcTransport;
import tech.ydb.table.SessionRetryContext;
import tech.ydb.table.TableClient;
import tech.ydb.table.query.DataQueryResult;
import tech.ydb.table.query.Params;
import tech.ydb.table.result.ResultSetReader;
import tech.ydb.table.transaction.TxControl;
import tech.ydb.table.values.PrimitiveValue;

import java.util.Optional;


public class YdbRepository implements DatabaseRepository {
    private GrpcTransport transport;
    private TableClient tableClient;
    private final String database;
    private final String connectionString;
    private SessionRetryContext retryCtx;

    public YdbRepository(String connectionString, String databasePath) {
        this.database = databasePath;
        this.connectionString = connectionString;
    }

    @Override
    public Optional<InvoiceEntity> findFirstByUserNameAndNotPaid(String username) {
        System.out.println("YdbRepository.findByUsername " + username);
        connectToDb();
        String query = """
                DECLARE $username AS Utf8;
                select * from INVOICE
                where USERNAME = $username and TELEGRAM_PAYMENT_CHARGE_ID is null;
                """;
        Params params = Params.of(
                "$username", PrimitiveValue.newText(username));
        DataQueryResult dataQueryResult = callDb(query, params);
        ResultSetReader rs = dataQueryResult.getResultSet(0);
        if (rs.next()) {
            return Optional.of(mapEntity(rs));
        }
        return Optional.empty();
    }

    @Override
    public Optional<InvoiceEntity> findById(Long id) {
        System.out.println("YdbRepository.findById " + id);
        connectToDb();
        String query
                = """
                 DECLARE $rowId AS Uint64;
                SELECT *
                FROM INVOICE WHERE ROW_ID = $rowId;""";

        Params params = Params.of(
                "$rowId", PrimitiveValue.newUint64(id));
        DataQueryResult dataQueryResult = callDb(query, params);
        ResultSetReader rs = dataQueryResult.getResultSet(0);
        if (rs.next()) {
            return Optional.of(mapEntity(rs));
        }
        return Optional.empty();
    }

    @Override
    public Long insertInvoice(InvoiceEntity newInvoice) {
        System.out.println("YdbRepository.insertInvoice " + newInvoice.toString());
        connectToDb();

        String query = """
                     DECLARE $rowId AS Uint64;
                     DECLARE $amount AS Int32;
                     DECLARE $description AS Utf8;
                     DECLARE $username AS Utf8;
                     INSERT INTO INVOICE  ( ROW_ID, AMOUNT, DESCRIPTION, USERNAME )
                VALUES ($rowId, $amount, $description, $username);""";
        Params params = Params.of(
                "$rowId", PrimitiveValue.newUint64(newInvoice.getRowId()),
                "$amount", PrimitiveValue.newInt32(Integer.parseInt(newInvoice.getAmount())),
                "$description", PrimitiveValue.newText(newInvoice.getDescription()),
                "$username", PrimitiveValue.newText(newInvoice.getUserName())
        );

        callDb(query, params);

        return newInvoice.getRowId();
    }


    @Override
    public boolean updateEntityPaymentData(InvoiceEntity newInvoice) {
        System.out.println("YdbRepository.updateEntityPaymentData " + newInvoice.toString());
        connectToDb();

        String query = """
                     DECLARE $rowId AS Uint64;
                     DECLARE $tgPayment AS Utf8;
                     DECLARE $provaiderPayment AS Utf8;
                     UPSERT INTO INVOICE  ( ROW_ID, TELEGRAM_PAYMENT_CHARGE_ID, PROVIDER_PAYMENT_CHARGE_ID)
                VALUES ($rowId, $tgPayment, $provaiderPayment);""";
        Params params = Params.of(
                "$rowId", PrimitiveValue.newUint64(newInvoice.getRowId()),
                "$tgPayment", PrimitiveValue.newText(newInvoice.getTelegramPaymentChargeId()),
                "$provaiderPayment", PrimitiveValue.newText(newInvoice.getProviderPaymentChargeId())
        );

        callDb(query, params);

        return true;
    }

    @Override
    public boolean deleteInvoiceById(Long id) {
        System.out.println("YdbRepository.deleteInvoiceById " + id);
        connectToDb();
        String query
                = """
                 DECLARE $rowId AS Uint64;
                   DELETE
                   FROM INVOICE
                   WHERE
                     ROW_ID =$rowId;""";

        Params params = Params.of(
                "$rowId", PrimitiveValue.newUint64(id));
        callDb(query, params);

        return true;
    }

    private void connectToDb() {
        this.transport = GrpcTransport.forConnectionString(connectionString)
                .withAuthProvider(CloudAuthHelper.getAuthProviderFromEnviron())
                .build();
        this.tableClient = TableClient.newClient(transport).build();
        this.retryCtx = SessionRetryContext.create(tableClient).build();
    }
    private DataQueryResult callDb(String query, Params params) {
        TxControl txControl = TxControl.serializableRw().setCommitTx(true);

        // Executes data query with specified transaction control settings.
        return retryCtx.supplyResult(session -> session.executeDataQuery(query, txControl, params))
                .join().getValue();
    }

    private InvoiceEntity mapEntity(ResultSetReader rs) {
        System.out.println("Start map of entity");
        InvoiceEntity build = InvoiceEntity.builder()
                .rowId(rs.getColumn("ROW_ID").getUint64())
                .amount(String.valueOf(rs.getColumn("AMOUNT").getInt32()))
                .description(rs.getColumn("DESCRIPTION").getText())
                .userName(rs.getColumn("USERNAME").getText())
                .telegramPaymentChargeId(rs.getColumn("TELEGRAM_PAYMENT_CHARGE_ID").getText())
                .providerPaymentChargeId(rs.getColumn("PROVIDER_PAYMENT_CHARGE_ID").getText())
                .build();
        System.out.println("End map of entity");
        return build;
    }
}
