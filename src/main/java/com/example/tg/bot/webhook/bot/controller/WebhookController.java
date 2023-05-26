package com.example.tg.bot.webhook.bot.controller;

import com.example.tg.bot.webhook.bot.service.UpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {
    private final UpdateService service;

    public WebhookController(UpdateService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<?> webhook(@RequestBody Update update) {
        service.handleUpdate(update);
        return ResponseEntity.ok().body("qweqwe");

    }
}
