package ru.example.tg.bot.model.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvoiceEntity {

    Long rowId;
    String amount;
    String description;
    String userName;
    String telegramPaymentChargeId;
    String providerPaymentChargeId;

}
