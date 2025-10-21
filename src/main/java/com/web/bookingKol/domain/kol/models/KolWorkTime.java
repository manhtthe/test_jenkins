package com.web.bookingKol.domain.kol.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "kol_work_times")
public class KolWorkTime {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availability_id")
    private KolAvailability availability;

    @NotNull
    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @NotNull
    @Column(name = "end_at", nullable = false)
    private OffsetDateTime endAt;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "status", length = 50)
    private String status;
}

