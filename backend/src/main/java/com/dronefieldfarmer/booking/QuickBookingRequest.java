package com.dronefieldfarmer.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record QuickBookingRequest(
        @NotBlank String userId,
        @Valid @NotNull Location location,
        @NotBlank String locationSource,
        @Positive double areaEstimate,
        @NotBlank String cropType,
        @NotBlank String paymentMode
) {
    public record Location(
            double lat,
            double lng,
            @NotBlank String addressText
    ) {}
}
