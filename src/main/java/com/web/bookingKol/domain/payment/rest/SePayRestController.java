package com.web.bookingKol.domain.payment.rest;

import com.web.bookingKol.domain.payment.dtos.SePayWebhookRequest;
import com.web.bookingKol.domain.payment.services.SePayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class SePayRestController {
    @Autowired
    private SePayService sePayService;


    @PostMapping("/sepay/callback")
    public ResponseEntity<?> handleWebhook(@RequestBody SePayWebhookRequest request,
                                           @RequestHeader(name = "Authorization") String authorizationHeader) {
        return ResponseEntity.ok().body(sePayService.handleWebhook(authorizationHeader, request));
    }
}
