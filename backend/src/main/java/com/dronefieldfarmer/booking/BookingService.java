package com.dronefieldfarmer.booking;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BookingService {

    private final Map<String, Booking> activeBookingsByUser = new ConcurrentHashMap<>();

    public Booking createQuickBooking(QuickBookingRequest request) {
        Booking existing = activeBookingsByUser.get(request.userId());
        if (existing != null && existing.status().isActive()) {
            throw new IllegalStateException("You already have an active booking. Please wait until it is completed or cancelled.");
        }

        Booking booking = Booking.create(request.userId(), request);
        activeBookingsByUser.put(request.userId(), booking);
        return booking;
    }

    public Optional<Booking> getActiveBooking(String userId) {
        Booking booking = activeBookingsByUser.get(userId);
        if (booking == null || !booking.status().isActive()) {
            return Optional.empty();
        }
        return Optional.of(booking);
    }
}
