package com.web.bookingKol.domain.admin;

import com.web.bookingKol.domain.booking.services.BookingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/admin/booking/requests")
public class AdminBookingRestController {
    @Autowired
    private BookingRequestService bookingRequestService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllBookingRequestAdmin(@RequestParam(required = false) UUID kolId,
                                                       @RequestParam(required = false) String status,
                                                       @RequestParam(required = false) LocalDate startAt,
                                                       @RequestParam(required = false) LocalDate endAt,
                                                       @RequestParam(required = false) LocalDate createdAtFrom,
                                                       @RequestParam(required = false) LocalDate createdAtTo,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(bookingRequestService.getAllRequestAdmin(
                kolId, status, startAt, endAt, createdAtFrom, createdAtTo, page, size));
    }

    @GetMapping("/detail/{requestId}")
    public ResponseEntity<?> getDetailBookingRequestAdmin(@PathVariable UUID requestId) {
        return ResponseEntity.ok(bookingRequestService.getDetailBooking(requestId));
    }
}
