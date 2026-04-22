# API Contract (MVP Draft - Quick Booking)

## Auth

### POST `/auth/request-otp`
Request OTP for mobile login.

**Body**
```json
{
  "mobileNumber": "+919999999999"
}
```

### POST `/auth/verify-otp`
Verify OTP and return access token.

## Booking

### POST `/bookings/quick`
Create booking via one-tap flow with detected or manually provided location.

**Body**
```json
{
  "location": {
    "lat": 12.9716,
    "lng": 77.5946,
    "addressText": "Village A, Taluk B"
  },
  "locationSource": "GPS",
  "areaEstimate": 3.5,
  "cropType": "PADDY",
  "paymentMode": "ADVANCE"
}
```

**Rules**
- If user already has an active booking, return conflict (`409`).
- Active statuses: `PENDING_ASSIGNMENT`, `ASSIGNED`, `IN_PROGRESS`.

**Success Response**
```json
{
  "bookingId": "b_101",
  "status": "PENDING_ASSIGNMENT",
  "estimatedPrice": {
    "baseFee": 300,
    "travelFee": 100,
    "areaRate": 250,
    "estimatedTotal": 1275
  }
}
```

**Conflict Response (`409`)**
```json
{
  "code": "ACTIVE_BOOKING_EXISTS",
  "message": "You already have an active booking. Please wait until it is completed or cancelled."
}
```

### GET `/bookings/me/active`
Return active booking for logged-in user, if present.

## Operations

### POST `/admin/bookings/{id}/assign`
Assign operator. (Scheduling logic/manual process handled in ops for MVP)

### POST `/jobs/{id}/complete`
Complete job and provide verified area.

## Payments

### POST `/payments/{bookingId}/initiate`
Initiate payment (advance or balance).

### POST `/payments/webhook`
Payment provider callback endpoint.

## Status Enums (Suggested)
- `PENDING_ASSIGNMENT`
- `ASSIGNED`
- `IN_PROGRESS`
- `COMPLETED`
- `CANCELLED`
