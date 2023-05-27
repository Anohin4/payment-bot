package ru.example.tg.bot.model;

public enum CommandEnum {
    NEW_INVOICE("newInvoice"),
    GET_INVOICE_BY_USER("getInvoice"),
    GET_INVOICE_BY_ID("getInvoiceById"),
    DELETE_INVOICE("deleteInvoice"),
    HELP("help"),
    START("start");

    public String getValue() {
        return value;
    }

    private final String value;

    CommandEnum(String value) {
        this.value = "/" + value;
    }
}
