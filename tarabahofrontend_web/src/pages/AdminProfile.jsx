"use client"

import { useState } from "react"
import { useNavigate } from "react-router-dom"
import AdminNavbar from "../components/AdminNavbar"
import LogoutConfirmation from "../components/Admin-LogoutConfirmation"
import profilePlaceholder from "../assets/images/profile-placeholder.png"
import Footer from "../components/Footer"
import "../styles/admin-profile.css"

const AdminProfile = () => {
  const navigate = useNavigate()
  const [showLogoutModal, setShowLogoutModal] = useState(false)

  // Mock admin data
  const adminData = {
    username: "Admin1",
    password: "12*****4",
    name: "Admin1",
    email: "quietaangelo@gmail.com",
    address: "Cebu City, Tisa Tabaylawom",
    contactNo: "09266517720",
    birthdate: "11/12/2002",
  }

  const handleLogout = () => {
    setShowLogoutModal(true)
  }

  const confirmLogout = () => {
    console.log("Logging out...")
    setShowLogoutModal(false)
    navigate("/admin-login")
  }

  const cancelLogout = () => {
    setShowLogoutModal(false)
  }

  return (
    <div className="admin-profile-page">
      <AdminNavbar />

      <div className="admin-profile-content">
        <div className="admin-profile-header">
          <h1 className="admin-profile-heading">ADMIN PROFILE</h1>

          {/* Logout button */}
          <button className="logout-button" onClick={handleLogout}>
            <svg
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M9 21H5C4.46957 21 3.96086 20.7893 3.58579 20.4142C3.21071 20.0391 3 19.5304 3 19V5C3 4.46957 3.21071 3.96086 3.58579 3.58579C3.96086 3.21071 4.46957 3 5 3H9"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              />
              <path
                d="M16 17L21 12L16 7"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              />
              <path
                d="M21 12H9"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              />
            </svg>
            LOG OUT
          </button>
        </div>

        <div className="profile-container">
          <div className="profile-card">
            <div className="profile-top-section">
              <div className="profile-image-section">
                <img
                  src={profilePlaceholder || "/placeholder.svg"}
                  alt="Profile"
                  className="profile-image"
                />
              </div>

              <div className="profile-credentials-section">
                <div className="credential-row">
                  <div className="credential-label">Username:</div>
                  <div className="credential-value">{adminData.username}</div>
                </div>

                <div className="credential-row">
                  <div className="credential-label">Password:</div>
                  <div className="credential-value">{adminData.password}</div>
                  <a href="#" className="change-password-link">
                    Change password
                  </a>
                </div>
              </div>
            </div>

            <div className="edit-profile-container">
              <button className="edit-profile-btn">EDIT PROFILE</button>
            </div>

            <div className="profile-details-section">
              <div className="detail-row">
                <div className="detail-label">Name:</div>
                <div className="detail-value">{adminData.name}</div>
              </div>
              <div className="detail-row">
                <div className="detail-label">Email:</div>
                <div className="detail-value">{adminData.email}</div>
              </div>
              <div className="detail-row">
                <div className="detail-label">Address:</div>
                <div className="detail-value">{adminData.address}</div>
              </div>
              <div className="detail-row">
                <div className="detail-label">Contact no.:</div>
                <div className="detail-value">{adminData.contactNo}</div>
              </div>
              <div className="detail-row">
                <div className="detail-label">Birthdate:</div>
                <div className="detail-value">{adminData.birthdate}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {showLogoutModal && (
        <LogoutConfirmation onConfirm={confirmLogout} onCancel={cancelLogout} />
      )}
      <Footer/>
    </div>
  )
}

export default AdminProfile
