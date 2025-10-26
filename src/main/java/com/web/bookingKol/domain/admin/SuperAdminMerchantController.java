package com.web.bookingKol.domain.admin;

import com.web.bookingKol.domain.payment.dtos.MerchantRequest;
import com.web.bookingKol.domain.payment.services.MerchantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/superadmin/merchant")
public class SuperAdminMerchantController {
    @Autowired
    private MerchantService merchantService;

    @GetMapping("/all")
    public ResponseEntity<?> GetAllMerchant(Pageable pageable) {
        return ResponseEntity.ok(merchantService.getAllMerchant(pageable));
    }

    @GetMapping("/detail/{merchantId}")
    public ResponseEntity<?> GetDetailMerchant(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(merchantService.getDetailMerchant(merchantId));
    }

    @PutMapping("/active/{merchantId}")
    public ResponseEntity<?> ActivateMerchant(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(merchantService.activateMerchant(merchantId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> CreateMerchant(@RequestBody @Valid MerchantRequest merchantRequest) {
        return ResponseEntity.ok(merchantService.createMerchant(merchantRequest));
    }
}
