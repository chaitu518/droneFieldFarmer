# Product Plan: Drone Field Farmer (MVP)

## Problem
Farmers need quick and reliable drone service booking, but current workflows are fragmented, slow, and often require repeated coordination.

## Solution
A one-tap booking flow where farmers log in with OTP, press **Book Service**, provide or auto-detect location, and submit. Operations handles scheduling manually in the background during MVP.

## User Journey (MVP)
1. Farmer logs in with mobile OTP
2. Farmer taps **Book Service**
3. App captures location:
   - Option A: Detect current location (GPS permission)
   - Option B: Manual location entry
4. System creates booking in `PENDING_ASSIGNMENT`
5. If user already has one active booking, app blocks new booking creation
6. Operations team assigns and executes job manually
7. Final bill generated from pricing rules
8. Farmer pays remaining balance (if any)

## MVP Constraint
- **One user = one active booking at a time.**
- Active statuses: `PENDING_ASSIGNMENT`, `ASSIGNED`, `IN_PROGRESS`.
- Scheduling date/time picker is out of MVP scope and deferred.

## Pricing Logic
`final_amount = base_fee + travel_fee + (verified_area * area_rate) + add_ons`

## MVP Functional Requirements

### Auth
- OTP request/verify
- user creation on first verify
- access token issuance

### Quick Booking
- one-click booking action
- location detection fallback to manual entry
- enforce single active booking rule
- booking status timeline

### Operations
- admin assignment panel (manual scheduling)
- operator job workflow (accepted, in-progress, completed)

### Billing & Payments
- estimate generation at booking
- final invoice generation on completion
- advance payment and balance tracking

### Notifications
- OTP SMS
- booking and status notifications
- payment confirmation

## Key Metrics
- quick booking conversion rate
- active booking conflict rate (blocked second booking attempts)
- booking completion rate
- payment success rate

## Risks & Controls
- **Duplicate demand from same user**: active booking lock
- **Location permission denied**: manual location fallback mandatory
- **OTP abuse**: rate limits and cooldowns
- **Weather delays**: user messaging + reschedule by operations
