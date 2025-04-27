import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import UserNavbar from "../components/UserNavbar";
import Footer from "../components/Footer";
import "../styles/BookingSystem.css";

const BookingHistory = () => {
  const [bookings, setBookings] = useState([]);
  const [error, setError] = useState("");
  const [selectedBooking, setSelectedBooking] = useState(null);
  const [showRatingModal, setShowRatingModal] = useState(false);
  const [ratingBookingId, setRatingBookingId] = useState(null); // New state to store booking ID for rating
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");
  const navigate = useNavigate();
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
      setError(""); // Clear any previous errors
      alert("Booking cancelled successfully.");
    } catch (err) {
      const errorMessage =
        err.response?.data.replace("⚠️ ", "") ||
        "Failed to cancel booking. Please try again.";
      console.error("Failed to cancel booking:", err.response?.data, err.message);
      setError(errorMessage);
    }
  };

  const handleStartJob = async (bookingId) => {
    try {
      await axios.post(
        `${BACKEND_URL}/api/booking/${bookingId}/start`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        }
      );
      setBookings((prevBookings) =>
        prevBookings.map((booking) =>
          booking.id === bookingId ? { ...booking, status: "IN_PROGRESS" } : booking
        )
      );
      setError(""); // Clear any previous errors
      alert("Job started successfully.");
    } catch (err) {
      const errorMessage =
        err.response?.data.replace("⚠️ ", "") ||
        "Failed to start job. Please try again.";
      console.error("Failed to start job:", err.response?.data, err.message);
      setError(errorMessage);
    }
  };

  const handleAcceptCompletion = async (bookingId) => {
    try {
      await axios.post(
        `${BACKEND_URL}/api/booking/${bookingId}/complete/accept`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        }
      );
      setBookings((prevBookings) =>
        prevBookings.map((booking) =>
          booking.id === bookingId ? { ...booking, status: "COMPLETED" } : booking
        )
      );
      setRatingBookingId(bookingId); // Store the booking ID for rating
      setShowRatingModal(true);
      setSelectedBooking(null); // Close the booking details modal
    } catch (err) {
      const errorMessage =
        err.response?.data.replace("⚠️ ", "") ||
        "Failed to accept completion. Please try again.";
      console.error("Failed to accept completion:", err.response?.data, err.message);
      setError(errorMessage);
    }
  };

  const handleSubmitRating = async () => {
    if (rating < 1 || rating > 5) {
      setError("Please select a rating between 1 and 5.");
      return;
    }
    try {
      await axios.post(
        `${BACKEND_URL}/api/rating`,
        { bookingId: ratingBookingId, rating, comment }, // Use ratingBookingId
        {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        }
      );
      setShowRatingModal(false);
      setRatingBookingId(null); // Clear the booking ID
      setRating(0);
      setComment("");
      alert("Thank you for your rating!");
    } catch (err) {
      const errorMessage =
        err.response?.data.replace("⚠️ ", "") ||
        "Failed to submit rating. Please try again.";
      console.error("Failed to submit rating:", err.response?.data, err.message);
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
      <UserNavbar activePage="booking-history" />
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
              <p><strong>Worker:</strong> {selectedBooking.worker ? `${selectedBooking.worker.firstName} ${selectedBooking.worker.lastName}` : "Not assigned"}</p>
              <p><strong>Location:</strong> {selectedBooking.latitude && selectedBooking.longitude ? `Lat: ${selectedBooking.latitude}, Lon: ${selectedBooking.longitude}` : "N/A"}</p>
              <p><strong>Radius:</strong> {selectedBooking.radius ? `${selectedBooking.radius} km` : "N/A"}</p>
              <button onClick={() => setSelectedBooking(null)}>Close</button>
            </div>
          )}

          {showRatingModal && (
            <div className="rating-modal">
              <h3>Rate Your Experience</h3>
              <p>Thank you for using our app! Please provide a rating for the worker (optional but appreciated).</p>
              <div>
                <label>Rating (1-5):</label>
                <input
                  type="number"
                  min="1"
                  max="5"
                  value={rating}
                  onChange={(e) => setRating(parseInt(e.target.value))}
                />
              </div>
              <div>
                <label>Comment (optional):</label>
                <textarea
                  value={comment}
                  onChange={(e) => setComment(e.target.value)}
                  maxLength="500"
                />
              </div>
              <button onClick={handleSubmitRating}>
                Submit Rating
              </button>
              <button onClick={() => {
                setShowRatingModal(false);
                setRatingBookingId(null);
                setRating(0);
                setComment("");
              }}>
                Skip
              </button>
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
                        <button
                          className="cancel-button"
                          onClick={(e) => {
                            e.stopPropagation();
                            handleCancelBooking(booking.id);
                          }}
                        >
                          Cancel
                        </button>
                      )}
                      {booking.status === "ACCEPTED" && (
                        <button
                          className="start-button"
                          onClick={(e) => {
                            e.stopPropagation();
                            handleStartJob(booking.id);
                          }}
                        >
                          Start Job
                        </button>
                      )}
                      {booking.status === "WORKER_COMPLETED" && (
                        <button
                          className="complete-button"
                          onClick={(e) => {
                            e.stopPropagation();
                            handleAcceptCompletion(booking.id);
                          }}
                        >
                          Accept Completion
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

export default BookingHistory;