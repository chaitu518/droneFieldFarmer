package com.dronefieldfarmer.booking;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Value("${app.pricing.base-fee}")
    private int baseFee;

    @Value("${app.pricing.travel-fee}")
    private int travelFee;

    @Value("${app.pricing.area-rate}")
    private int areaRate;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/quick")
    public Map<String, Object> quickBook(@Valid @RequestBody QuickBookingRequest request) {
        Booking booking = bookingService.createQuickBooking(request);
        int estimatedTotal = (int) (baseFee + travelFee + (request.areaEstimate() * areaRate));

        return Map.of(
                "bookingId", booking.bookingId(),
                "status", booking.status(),
                "estimatedPrice", Map.of(
                        "baseFee", baseFee,
                        "travelFee", travelFee,
                        "areaRate", areaRate,
                        "estimatedTotal", estimatedTotal
                )
        );
    }

    @GetMapping("/me/active")
    public Map<String, Object> getActive(@RequestParam String userId) {
        return bookingService.getActiveBooking(userId)
                .<Map<String, Object>>map(booking -> Map.of("hasActiveBooking", true, "booking", booking))
                .orElseGet(() -> Map.of("hasActiveBooking", false));
    }
}
