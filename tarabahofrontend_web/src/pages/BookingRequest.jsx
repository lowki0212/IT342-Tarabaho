import React, { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import UserNavbar from "../components/UserNavbar";
import Footer from "../components/Footer";
import "../styles/BookingSystem.css";

const BookingRequest = () => {
  const { workerId } = useParams();
  const navigate = useNavigate();
  const [bookingStatus, setBookingStatus] = useState("PENDING");
  const [bookingId, setBookingId] = useState(null);
  const [error, setError] = useState("");
  const BACKEND_URL = "http://localhost:8080";
  const token = localStorage.getItem("jwtToken");
  const hasRun = useRef(false); // Prevent duplicate requests

  useEffect(() => {
    console.log("BookingRequest component mounted");
    return () => console.log("BookingRequest component unmounted");
  }, []);

  useEffect(() => {
    console.log("useEffect triggered for createBooking, workerId:", workerId, "hasRun:", hasRun.current);
    if (hasRun.current) {
      console.log("Skipping duplicate useEffect execution");
      return;
    }
    hasRun.current = true;

    const createBooking = async () => {
      const requestBody = {
        workerId: parseInt(workerId), // Ensure workerId is a number
        categoryName: "Cleaning", // Valid category
        paymentMethod: "CASH", // Valid PaymentMethod enum value
        jobDetails: "AAAA", // Valid job details
      };
      console.log("Sending request to /api/booking/category:", requestBody);

      try {
        // REPLACE WITH DYNAMIC USER ID FROM AUTH (e.g., decoded from JWT)
        const userId = 1; // Placeholder; obtain from authenticated user context
        const response = await axios.post(
          `${BACKEND_URL}/api/booking/category`,
          requestBody,
          {
            headers: { Authorization: `Bearer ${token}` },
            withCredentials: true,
            timeout: 5000, // Prevent retries with a timeout
          }
        );
        if (!hasRun.current) {
          console.log("Component unmounted, ignoring response");
          return;
        }
        setBookingId(response.data.id);
        console.log("Booking created:", response.data);
      } catch (err) {
        if (!hasRun.current) {
          console.log("Component unmounted, ignoring error");
          return;
        }
        const errorMessage =
          err.response?.data.replace("⚠️ ", "") ||
          "Failed to create booking. Please try again.";
        console.error("Failed to create booking:", err.response?.data, err.message);
        setError(
          errorMessage.includes("Category not found")
            ? "The requested category is not available. Please try another service."
            : errorMessage.includes("User already has an active or pending booking")
            ? "You already have a pending or active booking. Please complete or cancel it first."
            : errorMessage
        );
      }
    };

    createBooking();

    return () => {
      console.log("Cleaning up useEffect for createBooking, resetting hasRun");
      hasRun.current = false; // Reset on unmount
    };
  }, [workerId, token]);

  useEffect(() => {
    if (bookingId) {
      const interval = setInterval(async () => {
        try {
          const response = await axios.get(
            `${BACKEND_URL}/api/booking/${bookingId}/status`,
            {
              headers: { Authorization: `Bearer ${token}` },
              withCredentials: true,
            }
          );
          const status = response.data.status;
          setBookingStatus(status);
          if (status === "ACCEPTED") {
            clearInterval(interval);
            navigate(`/chat/${bookingId}`);
          } else if (status === "REJECTED") {
            clearInterval(interval);
            setError("Booking was rejected by the worker.");
          }
        } catch (err) {
          console.error("Failed to check booking status:", err.response?.data, err.message);
          setError("Failed to check booking status.");
        }
      }, 5000);
      return () => clearInterval(interval);
    }
  }, [bookingId, navigate, token]);

  console.log("BookingRequest component rendered");

  return (
    <div className="page-container">
      <UserNavbar activePage="user-browse" />
      <div className="content-container">
        <div className="booking-card card">
          <h2>Booking Request</h2>
          {error && <div className="error-message">{error}</div>}
          {bookingId ? (
            <div>
              <p>Booking request created (ID: {bookingId}).</p>
              <p>Status: {bookingStatus}</p>
              {bookingStatus === "PENDING" && (
                <p>Waiting for worker to accept the booking...</p>
              )}
            </div>
          ) : (
            <p>Creating booking request...</p>
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default React.memo(BookingRequest);