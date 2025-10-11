package com.web.bookingKol.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.ZoneOffset;

@Configuration
public class TimezoneConfig {
    @Value("${app.timezone:UTC}")
    private String timezone;

    public ZoneId getZoneId() {
        return ZoneId.of(timezone);
    }

    public ZoneOffset getZoneOffset() {
        return getZoneId().getRules().getOffset(java.time.Instant.now());
    }
}
