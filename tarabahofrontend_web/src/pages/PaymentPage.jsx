import React, { useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import UserNavbar from "../components/UserNavbar"
import Footer from "../components/Footer"
import "../styles/BookingSystem.css"

const PaymentPages = () => {
  const { workerId } = useParams()
  const navigate = useNavigate()
  const [cardDetails, setCardDetails] = useState({
    cardNumber: "",
    expiry: "",
    cvv: "",
  })
  const [error, setError] = useState("")

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setCardDetails((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    if (!cardDetails.cardNumber || !cardDetails.expiry || !cardDetails.cvv) {
      setError("Please fill in all fields.")
      return
    }
    console.log("Payment processed for workerId:", workerId)
    navigate(`/booking/${workerId}/request`)
  }

  return (
    <div className="page-container">
      <UserNavbar activePage="user-browse" />
      <div className="content-container">
        <div className="payment-card card">
          <h2>Payment Details</h2>
          {error && <div className="error-message">{error}</div>}
          <form onSubmit={handleSubmit} className="payment-form">
            <div>
              <label>Card Number</label>
              <input
                type="text"
                name="cardNumber"
                value={cardDetails.cardNumber}
                onChange={handleInputChange}
                placeholder="1234 5678 9012 3456"
              />
            </div>
            <div>
              <label>Expiry Date</label>
              <input
                type="text"
                name="expiry"
                value={cardDetails.expiry}
                onChange={handleInputChange}
                placeholder="MM/YY"
              />
            </div>
            <div>
              <label>CVV</label>
              <input
                type="text"
                name="cvv"
                value={cardDetails.cvv}
                onChange={handleInputChange}
                placeholder="123"
              />
            </div>
            <button type="submit" className="button">
              Pay Now
            </button>
          </form>
        </div>
      </div>
      <Footer />
    </div>
  )
}

export default PaymentPages