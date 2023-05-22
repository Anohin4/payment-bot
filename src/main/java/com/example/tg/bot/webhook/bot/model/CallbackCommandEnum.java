package com.example.tg.bot.webhook.bot.model;

public enum CallbackCommandEnum {
    PAY_TO_INVOICE("payToInvoice_");
    private final String value;

    CallbackCommandEnum(String value) {
        this.value = "$" + value;
    }

    public String getValue() {
        return value;
    }
}
