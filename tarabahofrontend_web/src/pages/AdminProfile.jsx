"use client"

import { useState } from "react"
import { useNavigate } from "react-router-dom"
import AdminNavbar from "../components/AdminNavbar"
import LogoutConfirmation from "../components/Admin-LogoutConfirmation"
import profilePlaceholder from "../assets/images/profile-placeholder.png"
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
    // Perform logout actions
    console.log("Logging out...")
    setShowLogoutModal(false)
    navigate("/admin-login") // Redirect to login page
  }

  const cancelLogout = () => {
    setShowLogoutModal(false)
  }

  return (
    <div className="admin-profile-page">
      <AdminNavbar />

      <div className="admin-profile-content">
        <h1 className="admin-profile-heading">ADMIN PROFILE</h1>

        <button className="logout-button" onClick={handleLogout}>
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
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
            <path d="M21 12H9" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
          LOG OUT
        </button>

        <div className="profile-container">
          <div className="profile-card">
            <div className="profile-header">
              <div className="profile-image-container">
                <img src={profilePlaceholder || "/placeholder.svg"} alt="Profile" className="profile-image" />
              </div>

              <div className="profile-credentials">
                <div className="profile-field">
                  <span className="field-label">Username:</span>
                  <span className="field-value">{adminData.username}</span>
                </div>

                <div className="profile-field">
                  <span className="field-label">Password:</span>
                  <span className="field-value">{adminData.password}</span>
                  <a href="#" className="change-password">
                    Change password
                  </a>
                </div>
              </div>
            </div>

            <button className="edit-profile-button">EDIT PROFILE</button>

            <div className="profile-details">
              <div className="profile-field">
                <span className="field-label">Name:</span>
                <span className="field-value">{adminData.name}</span>
              </div>

              <div className="profile-field">
                <span className="field-label">Email:</span>
                <span className="field-value">{adminData.email}</span>
              </div>

              <div className="profile-field">
                <span className="field-label">Address:</span>
                <span className="field-value">{adminData.address}</span>
              </div>

              <div className="profile-field">
                <span className="field-label">Contact no.:</span>
                <span className="field-value">{adminData.contactNo}</span>
              </div>

              <div className="profile-field">
                <span className="field-label">Birthdate:</span>
                <span className="field-value">{adminData.birthdate}</span>
              </div>
            </div>
          </div>

          <div className="profile-watermark">
            <div className="watermark-text">T A R A B A H</div>
            <div className="watermark-tagline">T A R A ! T R A B A H O</div>
          </div>
        </div>
      </div>

      {showLogoutModal && <LogoutConfirmation onConfirm={confirmLogout} onCancel={cancelLogout} />}
    </div>
  )
}

export default AdminProfile
