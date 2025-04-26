import React, { useState, useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import UserNavbar from "../components/UserNavbar";
import TrabahadorNavbar from "../components/TrabahadorNavbar";
import Footer from "../components/Footer";
import "../styles/BookingSystem.css";

const ChatPage = () => {
  const { bookingId } = useParams();
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [error, setError] = useState("");
  const [isUser, setIsUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const chatBoxRef = useRef(null);
  const BACKEND_URL = "http://localhost:8080";
  const token = localStorage.getItem("jwtToken");

  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/api/message/booking/${bookingId}`, {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        });
        console.log("Fetched messages:", response.data);
        setMessages(response.data);
      } catch (err) {
        const errorMessage =
          err.response?.data.replace("⚠️ ", "") ||
          "Failed to fetch messages. Please try again.";
        console.error("Failed to fetch messages:", err.response?.data, err.message);
        setError(errorMessage);
      }
    };

    const determineRole = async () => {
      try {
        await axios.get(`${BACKEND_URL}/api/user/me`, {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        });
        setIsUser(true);
      } catch (err) {
        try {
          await axios.get(`${BACKEND_URL}/api/worker/me`, {
            headers: { Authorization: `Bearer ${token}` },
            withCredentials: true,
          });
          setIsUser(false);
        } catch (err) {
          setError("Unable to determine user role. Please log in.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchMessages();
    determineRole();
  }, [bookingId, token]);

  useEffect(() => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!newMessage.trim()) {
      setError("Message cannot be empty.");
      return;
    }

    try {
      console.log("Sending message:", { bookingId: parseInt(bookingId), content: newMessage });
      const response = await axios.post(
        `${BACKEND_URL}/api/message/send`,
        {
          bookingId: parseInt(bookingId),
          content: newMessage,
        },
        {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        }
      );
      setMessages([...messages, response.data]);
      setNewMessage("");
      setError("");
    } catch (err) {
      const errorMessage =
        err.response?.data.replace("⚠️ ", "") ||
        "Failed to send message. Please try again.";
      console.error("Failed to send message:", err.response?.data, err.message);
      setError(errorMessage);
    }
  };

  if (loading) {
    return (
      <div className="page-container">
        <div className="content-container">
          <div>Loading...</div>
        </div>
        <Footer />
      </div>
    );
  }

  if (error && !messages.length) {
    return (
      <div className="page-container">
        {isUser ? <UserNavbar activePage="booking-history" /> : <TrabahadorNavbar activePage="history" />}
        <div className="content-container">
          <div className="error-message">{error}</div>
        </div>
        <Footer />
      </div>
    );
  }

  return (
    <div className="page-container">
      {isUser ? <UserNavbar activePage="booking-history" /> : <TrabahadorNavbar activePage="history" />}
      <div className="content-container">
        <div className="chat-card card">
          <h2>Chat for Booking #{bookingId}</h2>
          {error && <div className="error-message">{error}</div>}
          <div className="chat-box" ref={chatBoxRef}>
            {messages.length > 0 ? (
              messages.map((message) => (
                <div
                  key={message.id}
                  className={`chat-message ${message.senderUser ? "user" : "worker"}`}
                >
                  <div className="chat-message-content">
                    {message.content}
                    <div className="chat-message-timestamp">
                      {new Date(message.sentAt).toLocaleString()}
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <p>No messages yet.</p>
            )}
          </div>
          <form className="chat-form" onSubmit={handleSendMessage}>
            <input
              type="text"
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="Type a message..."
            />
            <button type="submit">Send</button>
          </form>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default ChatPage;