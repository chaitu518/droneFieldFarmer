package com.dronefieldfarmer.auth;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Value("${app.otp.expires-in-seconds}")
    private int otpExpiresInSeconds;

    @PostMapping("/request-otp")
    public Map<String, Object> requestOtp(@RequestBody RequestOtpRequest request) {
        return Map.of(
                "requestId", "otp_" + UUID.randomUUID(),
                "expiresInSeconds", otpExpiresInSeconds,
                "mobileNumber", request.mobileNumber()
        );
    }

    @PostMapping("/verify-otp")
    public Map<String, Object> verifyOtp(@RequestBody VerifyOtpRequest request) {
        return Map.of(
                "accessToken", "demo-token-" + UUID.randomUUID(),
                "user", Map.of(
                        "id", request.requestId().replace("otp_", "u_"),
                        "mobileNumber", "+910000000000",
                        "name", "Farmer",
                        "role", "FARMER"
                )
        );
    }

    public record RequestOtpRequest(@NotBlank String mobileNumber) {}
    public record VerifyOtpRequest(@NotBlank String requestId, @NotBlank String otp) {}
}
