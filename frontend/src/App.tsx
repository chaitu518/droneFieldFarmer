import { useState } from 'react'
import './App.css'

type ActiveBookingResponse = {
  hasActiveBooking: boolean
  booking?: {
    bookingId: string
    status: string
    addressText: string
  }
}

const API_BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api'

export default function App() {
  const [userId] = useState('u_demo_1')
  const [message, setMessage] = useState('Ready to book your drone service.')
  const [loading, setLoading] = useState(false)

  const bookService = () => {
    setLoading(true)
    navigator.geolocation.getCurrentPosition(
      async (pos) => {
        await submitBooking(pos.coords.latitude, pos.coords.longitude, 'GPS detected location')
      },
      async () => {
        await submitBooking(12.9716, 77.5946, 'Manual fallback location')
      },
    )
  }

  const submitBooking = async (lat: number, lng: number, addressText: string) => {
    try {
      const payload = {
        userId,
        location: { lat, lng, addressText },
        locationSource: 'GPS',
        areaEstimate: 3.5,
        cropType: 'PADDY',
        paymentMode: 'ADVANCE',
      }

      const response = await fetch(`${API_BASE}/bookings/quick`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })

      if (response.status === 409) {
        const conflict = await response.json()
        setMessage(conflict.message)
        return
      }

      const data = await response.json()
      setMessage(`Booking created: ${data.bookingId} (${data.status})`)
    } catch {
      setMessage('Unable to create booking. Check VITE_API_BASE_URL and backend status.')
    } finally {
      setLoading(false)
    }
  }

  const loadActiveBooking = async () => {
    const response = await fetch(`${API_BASE}/bookings/me/active?userId=${userId}`)
    const data: ActiveBookingResponse = await response.json()
    if (data.hasActiveBooking && data.booking) {
      setMessage(`Active booking: ${data.booking.bookingId} (${data.booking.status})`)
    } else {
      setMessage('No active booking found for this user.')
    }
  }

  return (
    <div className="page">
      <main className="card">
        <section className="hero">
          <div className="field" aria-hidden="true">
            <div className="shadow" />
            <div className="drone">
              <div className="body">
                <div className="tank" />
              </div>
              <div className="arm" />
              <div className="arm vertical" />
              <div className="rotor r1" />
              <div className="rotor r2" />
              <div className="rotor r3" />
              <div className="rotor r4" />
              <div className="spray" />
            </div>
          </div>

          <div className="content">
            <h1>Drone Field Farmer MVP</h1>
            <p>3D-style agriculture drone preview with one-tap booking workflow.</p>
            <p>
              API Base: <code>{API_BASE}</code>
            </p>

            <div className="actions">
              <button onClick={bookService} disabled={loading}>
                {loading ? 'Booking...' : 'Book Service'}
              </button>
              <button onClick={loadActiveBooking}>Check Active Booking</button>
            </div>

            <div className="status">{message}</div>
          </div>
        </section>
      </main>
    </div>
  )
}
