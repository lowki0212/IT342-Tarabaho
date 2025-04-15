"use client"

import { useParams, useNavigate } from "react-router-dom"
import AdminNavbar from "../components/AdminNavbar"
import "../styles/trabahador-details.css"
import Footer from "../components/Footer"
import poldave from "../assets/images/poldave.png"

const TrabahadorDetails = () => {
  const { id } = useParams()
  const navigate = useNavigate()

  // In a real app, you would fetch the trabahador details based on the ID
  // For now, we'll hardcode Paul Dave's details
  const trabahador = {
    id: 1,
    name: "Paul Dave Q. Binoya",
    fullName: "Paul Dave Q. Binoya",
    email: "Pauldeb@gmail.com",
    contactNo: "09120120910",
    birthday: "08/11/2004",
    address: "Lapaloma Cebu City",
    description:
      "dedicated and detail-oriented individual specializing in cleaning and maintaining spaces. Skilled in organizing, dusting, sanitizing and ensuring cleanliness to create a comfortable and hygienic environment.",
    hourlyRate: "₱63.00/hour",
    rating: 5, // Changed to 5 stars to match the screenshot
    services: ["CLEANING", "GARDENING", "TUTORING"],
    documents: [
      { name: "Original NBI Clearance", note: "(Expired ID not allowed)" },
      { name: "Original National ID", note: "" },
      { name: "2x2 picture of you", note: "" },
      { name: "Police Clearance", note: "" },
    ],
  }

  const handleBack = () => {
    navigate(-1)
  }

  return (
    <div className="trabahador-details-page">
      <AdminNavbar />

      <div className="trabahador-details-container">
        <button className="back-button" onClick={handleBack}>
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M15 18L9 12L15 6" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
        </button>

        <div className="profile-section">
          <div className="profile-image-container">
            <img src={poldave || "/placeholder.svg"} alt="Paul Dave Q. Binoya" className="profile-image" />
          </div>

          <div className="profile-info">
            <h2 className="profile-name">{trabahador.name}</h2>
            <p className="profile-description">{trabahador.description}</p>

            <div className="rating">
              {[...Array(5)].map((_, i) => (
                <span
                  key={i}
                  className={`star ${i < Math.floor(trabahador.rating) ? "filled" : i < trabahador.rating ? "half-filled" : ""}`}
                >
                  ★
                </span>
              ))}
            </div>

            <div className="hourly-rate">{trabahador.hourlyRate}</div>

            <div className="services">
              {trabahador.services.map((service, index) => (
                <span key={index} className="service-tag">
                  {service}
                </span>
              ))}
            </div>
          </div>
        </div>

        <div className="details-section">
          <div className="personal-details">
            <div className="detail-item">
              <span className="detail-label">Full name:</span>
              <span className="detail-value">{trabahador.fullName}</span>
            </div>

            <div className="detail-item">
              <span className="detail-label">Email:</span>
              <span className="detail-value">{trabahador.email}</span>
            </div>

            <div className="detail-item">
              <span className="detail-label">Contact no:</span>
              <span className="detail-value">{trabahador.contactNo}</span>
            </div>

            <div className="detail-item">
              <span className="detail-label">Birthday:</span>
              <span className="detail-value">{trabahador.birthday}</span>
            </div>

            <div className="detail-item">
              <span className="detail-label">Address:</span>
              <span className="detail-value">{trabahador.address}</span>
            </div>

            <div className="action-buttons">
              <button className="edit-button">EDIT</button>
              <button className="delete-button">DELETE ACCOUNT</button>
            </div>
          </div>

          <div className="documents-section">
            <h3 className="documents-title">UPLOADED DOCUMENTS:</h3>

            <div className="documents-list">
              {trabahador.documents.map((doc, index) => (
                <div key={index} className="document-item">
                  <div className="document-name">
                    {doc.name} {doc.note && <span className="document-note">{doc.note}</span>}
                  </div>
                  <button className="view-document-button">View</button>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
      <Footer/>
    </div>
  )
}

export default TrabahadorDetails
