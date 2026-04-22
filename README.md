# Drone Field Farmer

A service-booking platform for paddy-field drone operations.

This project is built as a **modular monolith**:
- **Backend:** Java Spring Boot
- **Frontend:** Vite + React + TypeScript
- **Database:** PostgreSQL
- **Cache/OTP store:** Redis

## Product Vision
Enable farmers to book drone services quickly using mobile OTP login, tap one booking button, share/detect location, and get reliable drone service fulfillment.

## MVP Scope (Updated)

### Core user roles
- **Farmer**: creates one active booking and pays (advance or after completion)
- **Admin/Operations**: handles assignment and operations manually for now
- **Drone Operator**: executes assigned jobs and records completion data

### MVP booking model
1. Farmer logs in with OTP
2. Farmer taps **Book Service**
3. App asks for location (manual pin/dropdown) or uses device location if permitted
4. Booking is created immediately in `PENDING_ASSIGNMENT`
5. User cannot create another booking while one active booking exists

> Scheduling/date selection is intentionally postponed to a later phase.

### MVP features
1. Mobile OTP authentication
2. One-tap booking with location input/detection
3. Single active booking per user rule
4. Basic pricing estimate (base + travel + area)
5. Admin assignment flow (manual ops)
6. Job completion with verified area
7. Payment flow (advance / post-completion)
8. Notifications via SMS/WhatsApp (status updates)

## Recommended Tech Stack

### Backend (Spring Boot)
- Java 21
- Spring Boot (Web, Security, Validation, Data JPA)
- PostgreSQL
- Redis
- JWT for session tokens after OTP verification

### Frontend (Vite + React + TypeScript)
- React + TypeScript
- React Router
- TanStack Query
- Form validation (Zod + React Hook Form)
- Browser Geolocation API + manual fallback input

## Repository Plan

```text
.
├─ backend/                  # Spring Boot app
│  ├─ src/main/java/...      # modules: auth, booking, pricing, payment, admin
│  ├─ src/main/resources/
│  └─ src/test/
├─ frontend/                 # Vite + React + TS app
│  ├─ src/
│  └─ public/
├─ docs/
│  ├─ PRODUCT_PLAN.md
│  ├─ API_CONTRACT.md
│  └─ ROADMAP.md
└─ README.md
```

## API Surface (MVP Draft)
- `POST /auth/request-otp`
- `POST /auth/verify-otp`
- `POST /bookings/quick`
- `GET /bookings/me/active`
- `POST /admin/bookings/{id}/assign`
- `POST /jobs/{id}/complete`
- `POST /payments/{bookingId}/initiate`
- `POST /payments/webhook`

See `docs/API_CONTRACT.md` for endpoint details.

## Non-Functional Priorities
- Very low-friction booking (single tap + location)
- Transparent pricing and billing audit trail
- Single active booking guardrail to simplify ops in MVP
- Strong OTP abuse protection (rate limits, cooldowns)

## Build Order (Suggested)
1. OTP auth module
2. Quick booking + geolocation + single active booking rule
3. Pricing computation
4. Assignment + completion
5. Payments + reconciliation
6. Notifications + operational reporting

## Development Standards
- Use clear module boundaries in backend (`auth`, `booking`, `pricing`, `payment`, `admin`)
- Keep DTOs/versioned API contracts explicit
- Write migrations for schema changes
- Keep README + docs updated as scope evolves

## Next Step
Start implementation by scaffolding:
1. `backend/` Spring Boot project
2. `frontend/` Vite + React + TypeScript app
3. shared environment and local run scripts

## Environment Configuration

### Backend (`backend/.env.example`)
- `APP_PORT` - backend server port
- `CORS_ALLOWED_ORIGIN` - allowed frontend origin for API access
- `OTP_EXPIRES_IN_SECONDS` - OTP expiry duration
- `PRICING_BASE_FEE`, `PRICING_TRAVEL_FEE`, `PRICING_AREA_RATE` - pricing defaults

### Frontend (`frontend/.env.example`)
- `VITE_API_BASE_URL` - backend API base URL (e.g., `http://localhost:8080/api`)

## Local Run (Current Prototype)

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

Frontend expects backend at `http://localhost:8080`.

## Testing

> Note: Using public Maven repository setup for now. If your network blocks it later, we can re-add mirror/proxy config quickly.

### Backend
```bash
cd backend
mvn test
```

Current test coverage includes `BookingServiceTest` for:
- successful quick booking creation
- single active booking conflict behavior

### Frontend npm install/build troubleshooting
If `cd frontend && npm install` or `npm run build` fails with `E403` from npm registry:
1. Your environment likely blocks direct access to `registry.npmjs.org`.
2. Configure npm to use your org package registry/proxy (Artifactory/Nexus/Verdaccio):
```bash
cd frontend
npm config set registry https://<your-registry>/
npm install
npm run build
```
3. If your registry needs auth, run `npm login --registry https://<your-registry>/` or add an auth token in `.npmrc`.
4. We added `frontend/.npmrc` defaults, but corporate policy can still override with environment-level npm configs.
