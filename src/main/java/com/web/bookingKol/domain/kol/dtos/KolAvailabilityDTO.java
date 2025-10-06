package com.web.bookingKol.domain.kol.dtos;

import com.web.bookingKol.domain.kol.models.KolAvailability;
import com.web.bookingKol.domain.user.models.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public class KolAvailabilityDTO {
    private UUID id;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private String status;
    private String note;

    private UUID userId;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;

    public KolAvailabilityDTO() {}

    public KolAvailabilityDTO(KolAvailability availability) {
        this.id = availability.getId();
        this.startAt = availability.getStartAt();
        this.endAt = availability.getEndAt();
        this.status = availability.getStatus();
        this.note = availability.getNote();

        User user = availability.getUser();
        if (user != null) {
            this.userId = user.getId();
            this.fullName = user.getFullName();
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.avatarUrl = user.getAvatarUrl();
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public OffsetDateTime getStartAt() { return startAt; }
    public void setStartAt(OffsetDateTime startAt) { this.startAt = startAt; }

    public OffsetDateTime getEndAt() { return endAt; }
    public void setEndAt(OffsetDateTime endAt) { this.endAt = endAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}

