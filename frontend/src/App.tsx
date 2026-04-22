import { useState } from 'react'

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
    <main style={{ maxWidth: 640, margin: '3rem auto', fontFamily: 'sans-serif' }}>
      <h1>Drone Field Farmer MVP</h1>
      <p>One-tap booking prototype with location detection and one active booking rule.</p>
      <p>
        API Base: <code>{API_BASE}</code>
      </p>
      <div style={{ display: 'flex', gap: 12 }}>
        <button onClick={bookService} disabled={loading}>
          {loading ? 'Booking...' : 'Book Service'}
        </button>
        <button onClick={loadActiveBooking}>Check Active Booking</button>
      </div>
      <p style={{ marginTop: 16 }}>{message}</p>
    </main>
  )
}
