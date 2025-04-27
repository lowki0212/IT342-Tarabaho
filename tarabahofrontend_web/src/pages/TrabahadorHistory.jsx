import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import TrabahadorNavbar from "../components/TrabahadorNavbar";
import Footer from "../components/Footer";
import "../styles/BookingSystem.css";

const TrabahadorHistory = () => {
  const [bookings, setBookings] = useState([]);
  const [error, setError] = useState("");
  const [selectedBooking, setSelectedBooking] = useState(null);
  const navigate = useNavigate();
  const BACKEND_URL = "http://localhost:8080";
  const token = localStorage.getItem("jwtToken");

  useEffect(() => {
    const fetchBookings = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/api/booking/worker`, {
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

  const handleAcceptBooking = async (bookingId) => {
    try {
      await axios.post(
        `${BACKEND_URL}/api/booking/${bookingId}/accept`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        }
      );
      setBookings((prevBookings) =>
        prevBookings.map((booking) =>
          booking.id === bookingId ? { ...booking, status: "ACCEPTED" } : booking
        )
      );
      setError("Booking accepted successfully.");
    } catch (err) {
      const errorMessage =
        err.response?.data.replace("⚠️ ", "") ||
        "Failed to accept booking. Please try again.";
      console.error("Failed to accept booking:", err.response?.data, err.message);
      setError(errorMessage);
    }
  };

  const handleRejectBooking = async (bookingId) => {
    try {
      await axios.post(
        `${BACKEND_URL}/api/booking/${bookingId}/reject`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        }
      );
      setBookings((prevBookings) =>
        prevBookings.map((booking) =>
          booking.id === bookingId ? { ...booking, status: "REJECTED" } : booking
        )
      );
      setError("Booking rejected successfully.");
    } catch (err) {
      const errorMessage =
        err.response?.data.replace("⚠️ ", "") ||
        "Failed to reject booking. Please try again.";
      console.error("Failed to reject booking:", err.response?.data, err.message);
      setError(errorMessage);
    }
  };

  const handleCompleteBooking = async (bookingId) => {
    try {
      await axios.post(
        `${BACKEND_URL}/api/booking/${bookingId}/complete`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        }
      );
      setBookings((prevBookings) =>
        prevBookings.map((booking) =>
          booking.id === bookingId ? { ...booking, status: "WORKER_COMPLETED" } : booking
        )
      );
      setError("Booking marked as completed. Waiting for user confirmation.");
    } catch (err) {
      const errorMessage =
        err.response?.data.replace("⚠️ ", "") ||
        "Failed to mark booking as completed. Please try again.";
      console.error("Failed to complete booking:", err.response?.data, err.message);
      setError(errorMessage);
    }
  };

  const handleChatNavigation = (bookingId) => {
    navigate(`/chat/${bookingId}`);
  };

  const handleRowClick = (booking) => {
    setSelectedBooking(booking);
  };

  const activeBookings = bookings.filter((booking) =>
    ["PENDING", "ACCEPTED", "IN_PROGRESS", "WORKER_COMPLETED"].includes(booking.status)
  );
  const pastBookings = bookings.filter((booking) =>
    ["REJECTED", "CANCELLED", "COMPLETED"].includes(booking.status)
  );

  return (
    <div className="page-container">
      <TrabahadorNavbar activePage="history" />
      <div className="content-container">
        <div className="booking-history card">
          <h2>Booking History</h2>
          {error && <div className="error-message">{error}</div>}

          {selectedBooking && (
            <div className="booking-details-modal">
              <h3>Booking Details</h3>
              <p><strong>ID:</strong> {selectedBooking.id}</p>
              <p><strong>Category:</strong> {selectedBooking.category.name}</p>
              <p><strong>Status:</strong> {selectedBooking.status}</p>
              <p><strong>Payment Method:</strong> {selectedBooking.paymentMethod}</p>
              <p><strong>Job Details:</strong> {selectedBooking.jobDetails}</p>
              <p><strong>Created At:</strong> {new Date(selectedBooking.createdAt).toLocaleString()}</p>
              <p><strong>Updated At:</strong> {selectedBooking.updatedAt ? new Date(selectedBooking.updatedAt).toLocaleString() : "N/A"}</p>
              <p><strong>Type:</strong> {selectedBooking.type}</p>
              <p><strong>User:</strong> {selectedBooking.user ? `${selectedBooking.user.firstname} ${selectedBooking.user.lastname}` : "N/A"}</p>
              <p><strong>Location:</strong> {selectedBooking.latitude && selectedBooking.longitude ? `Lat: ${selectedBooking.latitude}, Lon: ${selectedBooking.longitude}` : "N/A"}</p>
              <p><strong>Radius:</strong> {selectedBooking.radius ? `${selectedBooking.radius} km` : "N/A"}</p>
              <button onClick={() => setSelectedBooking(null)}>Close</button>
            </div>
          )}

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
                  <tr
                    key={booking.id}
                    onClick={() => handleRowClick(booking)}
                    style={{ cursor: "pointer" }}
                  >
                    <td>{booking.id}</td>
                    <td>{booking.category.name}</td>
                    <td>{booking.status}</td>
                    <td>{booking.paymentMethod}</td>
                    <td>{booking.jobDetails}</td>
                    <td>{new Date(booking.createdAt).toLocaleString()}</td>
                    <td>
                      {booking.status === "PENDING" && (
                        <>
                          <button
                            className="accept-button"
                            onClick={(e) => {
                              e.stopPropagation();
                              handleAcceptBooking(booking.id);
                            }}
                          >
                            Accept
                          </button>
                          <button
                            className="reject-button"
                            onClick={(e) => {
                              e.stopPropagation();
                              handleRejectBooking(booking.id);
                            }}
                          >
                            Reject
                          </button>
                        </>
                      )}
                      {booking.status === "IN_PROGRESS" && (
                        <button
                          className="complete-button"
                          onClick={(e) => {
                            e.stopPropagation();
                            handleCompleteBooking(booking.id);
                          }}
                        >
                          Mark as Completed
                        </button>
                      )}
                      {(booking.status === "ACCEPTED" || booking.status === "IN_PROGRESS") && (
                        <button
                          className="chat-button"
                          onClick={(e) => {
                            e.stopPropagation();
                            handleChatNavigation(booking.id);
                          }}
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
                  <tr
                    key={booking.id}
                    onClick={() => handleRowClick(booking)}
                    style={{ cursor: "pointer" }}
                  >
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

export default TrabahadorHistory;