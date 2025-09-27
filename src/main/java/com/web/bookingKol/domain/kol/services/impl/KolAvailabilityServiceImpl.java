package com.web.bookingKol.domain.kol.services.impl;


import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.kol.models.KolAvailability;
import com.web.bookingKol.domain.kol.models.KolProfile;
import com.web.bookingKol.domain.kol.repositories.KolAvailabilityRepository;
import com.web.bookingKol.domain.kol.repositories.KolProfileRepository;
import com.web.bookingKol.domain.kol.services.KolAvailabilityService;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KolAvailabilityServiceImpl implements KolAvailabilityService {

    private final KolAvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;
    private final KolProfileRepository kolProfileRepository;

    @Override
    public ApiResponse<List<KolAvailability>> getKolSchedule(UUID userId, OffsetDateTime start, OffsetDateTime end) {
        // Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));


        kolProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Người dùng này chưa có KolProfile"));

        List<KolAvailability> schedules;
        if (start != null && end != null) {
            schedules = availabilityRepository.findByUserAndStartAtBetween(user, start, end);
        } else {
            schedules = availabilityRepository.findByUser(user);
        }

        return ApiResponse.<List<KolAvailability>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Lấy thời khóa biểu thành công"))
                .data(schedules)
                .build();
    }
}
