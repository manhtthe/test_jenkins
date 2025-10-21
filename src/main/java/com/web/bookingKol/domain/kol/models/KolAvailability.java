package com.web.bookingKol.domain.kol.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "kol_availabilities")
public class KolAvailability {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kol_id", nullable = false)
    private KolProfile kol;

    @NotNull
    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @NotNull
    @Column(name = "end_at", nullable = false)
    private OffsetDateTime endAt;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @OneToMany(mappedBy = "availability", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KolWorkTime> workTimes = new ArrayList<>();


    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

}