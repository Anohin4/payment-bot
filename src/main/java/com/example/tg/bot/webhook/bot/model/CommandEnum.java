package com.example.tg.bot.webhook.bot.model;

public enum CommandEnum {
    NEW_INVOICE("newInvoice"),
    FIND_INVOICE("findInvoice"),
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
