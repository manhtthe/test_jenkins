package com.web.bookingKol.domain.kol.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.common.services.EmailService;
import com.web.bookingKol.domain.kol.dtos.KolAvailabilityDTO;
import com.web.bookingKol.domain.kol.dtos.WorkTimeDTO;
import com.web.bookingKol.domain.kol.models.KolAvailability;
import com.web.bookingKol.domain.kol.models.KolProfile;
import com.web.bookingKol.domain.kol.repositories.KolAvailabilityRepository;
import com.web.bookingKol.domain.kol.services.KolAvailabilityService;
import com.web.bookingKol.domain.user.models.User;
import com.web.bookingKol.domain.user.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KolAvailabilityServiceImpl implements KolAvailabilityService {

    private final KolAvailabilityRepository kolAvailabilityRepository;
    private final UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(KolAvailabilityServiceImpl.class);
    @Autowired
    private EmailService emailService;

    @Override
    public ApiResponse<List<KolAvailabilityDTO>> getKolSchedule(UUID kolId, OffsetDateTime start, OffsetDateTime end) {
        var list = kolAvailabilityRepository.findByKolIdAndDateRange(kolId, start, end)
                .stream()
                .map(KolAvailabilityDTO::new)
                .toList();

        return ApiResponse.<List<KolAvailabilityDTO>>builder()
                .status(200)
                .message(List.of("Lấy thời khóa biểu thành công"))
                .data(list)
                .build();
    }

    @Override
    public ApiResponse<KolAvailabilityDTO> createKolSchedule(UUID userId, KolAvailabilityDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy KOL"));
        KolProfile kol = user.getKolProfile();
        if (kol == null) {
            return ApiResponse.<KolAvailabilityDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(List.of("Người dùng không phải KOL"))
                    .build();
        }

        KolAvailability existing = kolAvailabilityRepository.findByKolIdAndDateRange(
                user.getKolProfile().getId(), dto.getStartAt(), dto.getEndAt()
        ).stream().findFirst().orElse(null);

        if (existing != null && (existing.getStartAt().equals(dto.getStartAt()) && existing.getEndAt().equals(dto.getEndAt()))) {
            return ApiResponse.<KolAvailabilityDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(List.of("Khoảng thời gian này đã bị trùng với lịch khác"))
                    .build();
        }
        try {
            KolAvailability entity = new KolAvailability();
            entity.setId(UUID.randomUUID());
            entity.setKol(user.getKolProfile());
            entity.setStartAt(dto.getStartAt());
            entity.setEndAt(dto.getEndAt());
            entity.setCreatedAt(Instant.now());
            entity.setStatus("SUCCESS");
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json = objectMapper.writeValueAsString(dto.getTimeLine());
//            entity.setNote(json);
            String kolEmail = user.getEmail();
            if (kolEmail != null && !kolEmail.isEmpty()) {
                String subject = "Lịch làm việc mới đã được tạo";
                String content = """
                        <html>
                        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                            <h2 style="color:#2E86C1;">Xin chào %s 👋</h2>
                            <p>Lịch làm việc của bạn đã được <strong style="color:green;">tạo thành công</strong> 🎉</p>
                        
                            <div style="border:1px solid #ccc; padding:15px; border-radius:8px; background-color:#f9f9f9; margin:10px 0;">
                                <p><strong>🗓️ Thời gian bắt đầu:</strong> %s</p>
                                <p><strong>⏰ Thời gian kết thúc:</strong> %s</p>
                                <p><strong>📝 Ghi chú:</strong> %s</p>
                            </div>
                        
                            <p>💡 Bạn có thể đăng nhập lại hệ thống <a href="#####" style="color:#2E86C1; text-decoration:none;">BookingKOL</a> để xem hoặc cập nhật lịch làm việc của mình.</p>
                        
                            <p style="margin-top:20px;">Trân trọng,<br><strong>Đội ngũ BookingKOL</strong></p>
                        </body>
                        </html>
                        """.formatted(
                        user.getFullName() != null ? user.getFullName() : "KOL",
                        dto.getStartAt(),
                        dto.getEndAt(),
                        dto.getNote() != null ? dto.getNote() : "(Không có ghi chú)"
                );
                emailService.sendHtmlEmail(kolEmail, subject, content);
                kolAvailabilityRepository.save(entity);
            } else {
                logger.warn("KOL với ID {} không có email, không thể gửi thông báo.", userId);
            }
            return ApiResponse.<KolAvailabilityDTO>builder()
                    .status(HttpStatus.CREATED.value())
                    .message(List.of("Tạo lịch thành công"))
                    .data(new KolAvailabilityDTO(entity)).build();
        } catch (Exception e) {
            logger.error("Lỗi khi tạo lịch hoặc gửi mail: {}", e.getMessage(), e);
            return ApiResponse.<KolAvailabilityDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(List.of("Tạo lịch thất bại: " + e.getMessage()))
                    .build();
        }
    }

    @Override
    public ApiResponse<KolAvailabilityDTO> getKolAvailabilityById(UUID availabilityId) {

        KolAvailability availability = kolAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lịch làm việc"));

        ObjectMapper mapper = new ObjectMapper();
        List<WorkTimeDTO> workTimes = new ArrayList<>();
        if (availability.getNote() != null && !availability.getNote().isEmpty()) {
            try {
                workTimes = mapper.readValue(availability.getNote(), new TypeReference<List<WorkTimeDTO>>() {
                });
            } catch (Exception e) {
                System.out.println("Lỗi khi parse note JSON: " + e.getMessage());
            }
        }
        KolAvailabilityDTO dto = new KolAvailabilityDTO();
        dto.setId(availability.getId());
        dto.setStartAt(availability.getStartAt());
        dto.setEndAt(availability.getEndAt());
        dto.setStatus(availability.getStatus());
        dto.setTimeLine(workTimes);
        dto.setKolId(availability.getKol().getId());
        dto.setEmail(availability.getKol().getUser().getEmail());
        dto.setFullName(availability.getKol().getUser().getFullName());
        dto.setPhone(availability.getKol().getUser().getPhone());
        dto.setAvatarUrl(availability.getKol().getUser().getAvatarUrl());

        return ApiResponse.<KolAvailabilityDTO>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Lấy thông tin lịch làm việc thành công"))
                .data(dto)
                .build();
    }

    @Override
    public ApiResponse<List<KolAvailabilityDTO>> getKolAvailabilitiesByKol(
            UUID kolId,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startAt"));

        Page<KolAvailability> availabilities =
                kolAvailabilityRepository.findByKolIdAndDateRangePaged(kolId, startDate, endDate, pageable);

        List<KolAvailabilityDTO> dtoList = availabilities.getContent()
                .stream()
                .map(KolAvailabilityDTO::new)
                .toList();

        return ApiResponse.<List<KolAvailabilityDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(List.of("Lấy danh sách lịch làm việc thành công"))
                .data(dtoList)
                .build();
    }

}