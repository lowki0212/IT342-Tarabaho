import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // Add useNavigate
import axios from "axios";
import UserNavbar from "../components/UserNavbar";
import Footer from "../components/Footer";
import "../styles/BookingSystem.css";

const BookingHistory = () => {
  const [bookings, setBookings] = useState([]);
  const [error, setError] = useState("");
  const navigate = useNavigate(); // Add navigate
  const BACKEND_URL = "http://localhost:8080";
  const token = localStorage.getItem("jwtToken");

  useEffect(() => {
    const fetchBookings = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/api/booking/user`, {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        });
        setBookings(response.data);
      } catch (err) {
        const errorMessage =
          err.response?.data.replace("⚠️ ", "") ||
          "Failed to fetch booking history. Please try again.";
        console.error("Failed to fetch bookings:", err.response?.data, err.message);
        setError(errorMessage);
      }
    };

    fetchBookings();
  }, [token]);

  const handleCancelBooking = async (bookingId) => {
    try {
      await axios.post(
        `${BACKEND_URL}/api/booking/${bookingId}/cancel`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        }
      );
      setBookings((prevBookings) =>
        prevBookings.map((booking) =>
          booking.id === bookingId ? { ...booking, status: "CANCELLED" } : booking
        )
      );
      setError("Booking cancelled successfully.");
    } catch (err) {
      const errorMessage =
        err.response?.data.replace("⚠️ ", "") ||
        "Failed to cancel booking. Please try again.";
      console.error("Failed to cancel booking:", err.response?.data, err.message);
      setError(errorMessage);
    }
  };

  const handleChatNavigation = (bookingId) => {
    navigate(`/chat/${bookingId}`);
  };

  const activeBookings = bookings.filter((booking) =>
    ["PENDING", "ACCEPTED", "IN_PROGRESS"].includes(booking.status)
  );
  const pastBookings = bookings.filter((booking) =>
    ["REJECTED", "CANCELLED", "COMPLETED"].includes(booking.status)
  );

  return (
    <div className="page-container">
      <UserNavbar activePage="booking-history" />
      <div className="content-container">
        <div className="booking-history card">
          <h2>Booking History</h2>
          {error && <div className="error-message">{error}</div>}

          <h3>Active Bookings</h3>
          {activeBookings.length > 0 ? (
            <table className="booking-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Category</th>
                  <th>Status</th>
                  <th>Payment Method</th>
                  <th>Job Details</th>
                  <th>Created At</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {activeBookings.map((booking) => (
                  <tr key={booking.id}>
                    <td>{booking.id}</td>
                    <td>{booking.category.name}</td>
                    <td>{booking.status}</td>
                    <td>{booking.paymentMethod}</td>
                    <td>{booking.jobDetails}</td>
                    <td>{new Date(booking.createdAt).toLocaleString()}</td>
                    <td>
                      {booking.status === "PENDING" && (
                        <button
                          className="cancel-button"
                          onClick={() => handleCancelBooking(booking.id)}
                        >
                          Cancel
                        </button>
                      )}
                      {(booking.status === "ACCEPTED" || booking.status === "IN_PROGRESS") && (
                        <button
                          className="chat-button"
                          onClick={() => handleChatNavigation(booking.id)}
                        >
                          Chat
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>No active bookings.</p>
          )}

          <h3>Past Bookings</h3>
          {pastBookings.length > 0 ? (
            <table className="booking-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Category</th>
                  <th>Status</th>
                  <th>Payment Method</th>
                  <th>Job Details</th>
                  <th>Created At</th>
                </tr>
              </thead>
              <tbody>
                {pastBookings.map((booking) => (
                  <tr key={booking.id}>
                    <td>{booking.id}</td>
                    <td>{booking.category.name}</td>
                    <td>{booking.status}</td>
                    <td>{booking.paymentMethod}</td>
                    <td>{booking.jobDetails}</td>
                    <td>{new Date(booking.createdAt).toLocaleString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>No past bookings.</p>
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default BookingHistory;