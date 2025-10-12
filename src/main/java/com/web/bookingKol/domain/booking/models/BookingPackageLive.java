package com.web.bookingKol.domain.booking.models;

import com.web.bookingKol.temp_models.PurchasedServicePackage;
import com.web.bookingKol.domain.user.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "booking_package_lives")
public class BookingPackageLive {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchased_package_id", nullable = false)
    private PurchasedServicePackage purchasedPackage;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "live_id", nullable = false)
    private User live;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}

