package com.dronefieldfarmer.booking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    private final BookingService bookingService = new BookingService();

    @Test
    void createsQuickBookingWhenNoActiveBookingExists() {
        QuickBookingRequest request = new QuickBookingRequest(
                "u_1",
                new QuickBookingRequest.Location(12.97, 77.59, "Village A"),
                "GPS",
                2.5,
                "PADDY",
                "ADVANCE"
        );

        Booking booking = bookingService.createQuickBooking(request);

        assertNotNull(booking.bookingId());
        assertEquals("u_1", booking.userId());
        assertEquals(BookingStatus.PENDING_ASSIGNMENT, booking.status());
        assertTrue(bookingService.getActiveBooking("u_1").isPresent());
    }

    @Test
    void throwsConflictWhenActiveBookingAlreadyExists() {
        QuickBookingRequest request = new QuickBookingRequest(
                "u_1",
                new QuickBookingRequest.Location(12.97, 77.59, "Village A"),
                "GPS",
                2.5,
                "PADDY",
                "ADVANCE"
        );

        bookingService.createQuickBooking(request);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> bookingService.createQuickBooking(request));

        assertTrue(ex.getMessage().contains("active booking"));
    }
}
