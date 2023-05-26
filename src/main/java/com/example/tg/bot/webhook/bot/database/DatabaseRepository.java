package com.example.tg.bot.webhook.bot.database;

import com.example.tg.bot.webhook.bot.model.InvoiceEntity;

public interface DatabaseRepository {

    InvoiceEntity findFirstByUserName(String username);
    InvoiceEntity deleteById(Long id);
    boolean insertInvoice(InvoiceEntity newInvoice);

}
