package ru.example.tg.bot.database;

import ru.example.tg.bot.model.domain.InvoiceEntity;

import java.util.Optional;

public interface DatabaseRepository {

    Optional<InvoiceEntity> findFirstByUserNameAndNotPaid(String username);
    Optional<InvoiceEntity> findById(Long id);
    Long insertInvoice(InvoiceEntity newInvoice);
    boolean updateEntityPaymentData(InvoiceEntity newInvoice);
    boolean deleteInvoiceById(Long id);
}
