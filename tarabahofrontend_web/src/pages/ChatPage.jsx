import React, { useState } from "react"
import { useParams } from "react-router-dom"
import UserNavbar from "../components/UserNavbar"
import Footer from "../components/Footer"
import "../styles/BookingSystem.css"

const ChatPage = () => {
  const { bookingId } = useParams()
  const [messages, setMessages] = useState([])
  const [newMessage, setNewMessage] = useState("")

  const handleSendMessage = (e) => {
    e.preventDefault()
    if (newMessage.trim()) {
      const message = {
        bookingId,
        content: newMessage,
        sender: "user",
        timestamp: new Date(),
      }
      setMessages((prev) => [...prev, message])
      setNewMessage("")
    }
  }

  return (
    <div className="page-container">
      <UserNavbar activePage="user-browse" />
      <div className="content-container">
        <div className="chat-card card">
          <h2>Chat for Booking #{bookingId}</h2>
          <div className="chat-box">
            {messages.map((msg, index) => (
              <div
                key={index}
                className={`chat-message ${msg.sender === "user" ? "user" : "worker"}`}
              >
                <p className="chat-message-content">{msg.content}</p>
                <p className="chat-message-timestamp">
                  {new Date(msg.timestamp).toLocaleTimeString()}
                </p>
              </div>
            ))}
          </div>
          <form onSubmit={handleSendMessage} className="chat-form">
            <input
              type="text"
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="Type your message..."
            />
            <button type="submit">Send</button>
          </form>
        </div>
      </div>
      <Footer />
    </div>
  )
}

export default ChatPage