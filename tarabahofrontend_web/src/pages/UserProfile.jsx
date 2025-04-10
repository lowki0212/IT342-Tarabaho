"use client"

import { useState } from "react"
import { useNavigate } from "react-router-dom"
import UserNavbar from "../components/UserNavbar"
import LogoutConfirmation from "../components/User-LogoutConfirmation"
import "../styles/User-Profile.css"
import angeloImg from "../assets/images/angelo.png"
import {
  FaFacebook,
  FaInstagram,
  FaTiktok,
  FaUser,
  FaBookmark,
  FaHistory,
  FaSignOutAlt,
  FaEnvelope,
  FaMapMarkerAlt,
  FaPhone,
  FaBirthdayCake,
} from "react-icons/fa"

const UserProfile = () => {
  const navigate = useNavigate()
  const [showLogoutModal, setShowLogoutModal] = useState(false)
  const [connectedAccounts, setConnectedAccounts] = useState({
    facebook: false,
    instagram: false,
    tiktok: false,
  })

  const handleConnectToggle = (platform) => {
    setConnectedAccounts((prev) => ({
      ...prev,
      [platform]: !prev[platform],
    }))
  }

  const handleLogout = () => {
    setShowLogoutModal(true)
  }

  const confirmLogout = () => {
    console.log("User logged out")
    setShowLogoutModal(false)
    // Redirect to login page or homepage
    navigate("/signin")
  }

  const cancelLogout = () => {
    setShowLogoutModal(false)
  }

  return (
    <div className="profile-page">
      <UserNavbar activePage="user-profile" />

      <div className="profile-content">
        <h1 className="profile-title">MY PROFILE</h1>

        <div className="profile-container">
          {/* Sidebar */}
          <div className="profile-sidebar">
            <div className="sidebar-item active">
              <FaUser className="sidebar-icon" />
              <span>PROFILE</span>
            </div>
            <div className="sidebar-item">
              <FaBookmark className="sidebar-icon" />
              <span>BOOKMARKS</span>
            </div>
            <div className="sidebar-item">
              <FaHistory className="sidebar-icon" />
              <span>HISTORY</span>
            </div>
            <div className="sidebar-item logout" onClick={handleLogout}>
              <FaSignOutAlt className="sidebar-icon" />
              <span>LOG OUT</span>
            </div>
          </div>

          {/* Main Profile Content */}
          <div className="profile-main">
            {/* Profile Info Section */}
            <div className="profile-info-section">
              <div className="profile-image-container">
                <img src={angeloImg || "/placeholder.svg"} alt="User Profile" className="profile-image" />
                <button className="edit-profile-btn" aria-label="Edit Profile">
                  EDIT PROFILE
                </button>
              </div>

              <div className="profile-details">
                <div className="profile-detail-item">
                  <span className="detail-label">
                    <FaUser className="detail-icon" /> Name:
                  </span>
                  <span className="detail-value">Angelo C. Quieta</span>
                </div>
                <div className="profile-detail-item">
                  <span className="detail-label">
                    <FaEnvelope className="detail-icon" /> Email:
                  </span>
                  <span className="detail-value">quietaangelo@gmail.com</span>
                </div>
                <div className="profile-detail-item">
                  <span className="detail-label">
                    <FaMapMarkerAlt className="detail-icon" /> Address:
                  </span>
                  <span className="detail-value">Cebu City, Tisa Tabaylawom</span>
                </div>
                <div className="profile-detail-item">
                  <span className="detail-label">
                    <FaPhone className="detail-icon" /> Contact no.:
                  </span>
                  <span className="detail-value">09266517720</span>
                </div>
                <div className="profile-detail-item">
                  <span className="detail-label">
                    <FaBirthdayCake className="detail-icon" /> Birthdate:
                  </span>
                  <span className="detail-value">17/12/2002</span>
                </div>
              </div>
            </div>

            {/* Account Info Section */}
            <div className="account-info-section">
              <div className="account-credentials">
                <div className="credential-item">
                  <span className="credential-label">Username:</span>
                  <span className="credential-value">angeloquieta</span>
                </div>
                <div className="credential-item">
                  <span className="credential-label">Password:</span>
                  <span className="credential-value">12****4</span>
                  <a href="#" className="change-password">
                    Change password
                  </a>
                </div>
              </div>

              <div className="connected-accounts">
                <h3 className="connected-accounts-title">CONNECTED ACCOUNTS</h3>
                <div className="account-divider"></div>

                <div className="social-account-item">
                  <FaFacebook className="social-icon facebook" />
                  <button
                    className={`connect-btn ${connectedAccounts.facebook ? "connected" : ""}`}
                    onClick={() => handleConnectToggle("facebook")}
                    aria-label={connectedAccounts.facebook ? "Disconnect Facebook" : "Connect Facebook"}
                  >
                    {connectedAccounts.facebook ? "Disconnect" : "Connect"}
                  </button>
                </div>
                <div className="social-account-item">
                  <FaInstagram className="social-icon instagram" />
                  <button
                    className={`connect-btn ${connectedAccounts.instagram ? "connected" : ""}`}
                    onClick={() => handleConnectToggle("instagram")}
                    aria-label={connectedAccounts.instagram ? "Disconnect Instagram" : "Connect Instagram"}
                  >
                    {connectedAccounts.instagram ? "Disconnect" : "Connect"}
                  </button>
                </div>
                <div className="social-account-item">
                  <FaTiktok className="social-icon tiktok" />
                  <button
                    className={`connect-btn ${connectedAccounts.tiktok ? "connected" : ""}`}
                    onClick={() => handleConnectToggle("tiktok")}
                    aria-label={connectedAccounts.tiktok ? "Disconnect TikTok" : "Connect TikTok"}
                  >
                    {connectedAccounts.tiktok ? "Disconnect" : "Connect"}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Tarabaho Watermark */}
        <div className="tarabaho-watermark">
          <span className="watermark-text">TARABAHO</span>
          <span className="watermark-subtext">TARA! TRABAHO</span>
        </div>
      </div>

      {/* Logout Confirmation Modal */}
      {showLogoutModal && <LogoutConfirmation onConfirm={confirmLogout} onCancel={cancelLogout} />}
    </div>
  )
}

export default UserProfile
