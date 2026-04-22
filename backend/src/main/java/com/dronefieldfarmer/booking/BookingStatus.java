package com.dronefieldfarmer.booking;

public enum BookingStatus {
    PENDING_ASSIGNMENT,
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public boolean isActive() {
        return this == PENDING_ASSIGNMENT || this == ASSIGNED || this == IN_PROGRESS;
    }
}
