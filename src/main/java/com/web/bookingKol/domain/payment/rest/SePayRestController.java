package com.web.bookingKol.domain.payment.rest;

import com.web.bookingKol.domain.payment.dtos.SePayWebhookRequest;
import com.web.bookingKol.domain.payment.services.SePayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class SePayRestController {
    @Autowired
    private SePayService sePayService;


    @PostMapping("/sepay/callback")
    public ResponseEntity<?> handleWebhook(@RequestBody SePayWebhookRequest request) {
        return ResponseEntity.ok().body(sePayService.handleWebhook(request));
    }
}
