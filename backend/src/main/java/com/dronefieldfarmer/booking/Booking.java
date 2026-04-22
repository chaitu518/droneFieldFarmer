package com.dronefieldfarmer.booking;

import java.time.Instant;
import java.util.UUID;

public record Booking(
        String bookingId,
        String userId,
        double lat,
        double lng,
        String addressText,
        String locationSource,
        double areaEstimate,
        String cropType,
        String paymentMode,
        BookingStatus status,
        Instant createdAt
) {
    public static Booking create(String userId, QuickBookingRequest req) {
        return new Booking(
                "b_" + UUID.randomUUID(),
                userId,
                req.location().lat(),
                req.location().lng(),
                req.location().addressText(),
                req.locationSource(),
                req.areaEstimate(),
                req.cropType(),
                req.paymentMode(),
                BookingStatus.PENDING_ASSIGNMENT,
                Instant.now()
        );
    }
}
