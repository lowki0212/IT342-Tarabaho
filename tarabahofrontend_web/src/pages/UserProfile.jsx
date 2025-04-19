"use client"

import { useState, useEffect, useRef } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import UserNavbar from "../components/UserNavbar"
import Footer from "../components/Footer"
import LogoutConfirmation from "../components/User-LogoutConfirmation"
import "../styles/User-Profile.css"
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
  FaEdit,
} from "react-icons/fa"

const UserProfile = () => {
  const navigate = useNavigate()
  const [user, setUser] = useState(null)
  const [isEditing, setIsEditing] = useState(false)
  const [formData, setFormData] = useState({
    email: "",
    location: "",
    birthday: "",
    password: "",
  })
  const [selectedFile, setSelectedFile] = useState(null)
  const [profileImage, setProfileImage] = useState("/placeholder.svg")
  const [isUploading, setIsUploading] = useState(false) // Added loading state
  const [showLogoutModal, setShowLogoutModal] = useState(false)
  const [connectedAccounts, setConnectedAccounts] = useState({
    facebook: false,
    instagram: false,
    tiktok: false,
  })
  const [error, setError] = useState("")
  const fileInputRef = useRef(null)

  const BACKEND_URL = "http://localhost:8080"

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/api/user/me`, {
          withCredentials: true,
        })
        if (response.data) {
          setUser(response.data)
          setFormData({
            email: response.data.email || "",
            location: response.data.location || "",
            birthday: response.data.birthday || "",
            password: "",
          })
          setProfileImage(
            response.data.profilePicture
              ? `${BACKEND_URL}${response.data.profilePicture}`
              : "/placeholder.svg"
          )
        }
      } catch (err) {
        console.error("Failed to fetch user:", err)
        if (err.response?.status === 401) {
          navigate("/signin")
        } else {
          setError("Failed to load profile. Please try again.")
        }
      }
    }
    fetchUser()
  }, [navigate]) // Added navigate as a dependency

  const handleConnectToggle = (platform) => {
    setConnectedAccounts((prev) => ({
      ...prev,
      [platform]: !prev[platform],
    }))
  }

  const handleFileChange = async (e) => {
    const file = e.target.files[0]
    if (!file) {
      setError("No file selected.")
      return
    }
    setSelectedFile(file)
    setIsUploading(true) // Show loading state
    console.log("Selected file:", file.name)

    const formData = new FormData()
    formData.append("file", file)

    try {
      console.log("Uploading file:", file.name)
      const response = await axios.post(
        `${BACKEND_URL}/api/user/upload-picture`,
        formData,
        {
          withCredentials: true,
          headers: { "Content-Type": "multipart/form-data" },
        }
      )
      setUser(response.data)
      setProfileImage(
        response.data.profilePicture
          ? `${BACKEND_URL}${response.data.profilePicture}`
          : "/placeholder.svg"
      )
      setSelectedFile(null)
      setError("")
      console.log("Upload successful:", response.data)
    } catch (err) {
      console.error("Failed to upload picture:", err)
      if (err.response?.status === 401) {
        navigate("/signin")
      } else {
        setError(err.response?.data || "Failed to upload picture. Please try again.")
      }
    } finally {
      setIsUploading(false) // Hide loading state
    }
  }

  const handleImageClick = () => {
    fileInputRef.current.click()
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleEditToggle = () => {
    setIsEditing(!isEditing)
    setError("")
  }

  const handleSaveChanges = async () => {
    try {
      const response = await axios.put(
        `${BACKEND_URL}/api/user/update-profile`,
        formData,
        {
          withCredentials: true,
        }
      )
      setUser(response.data)
      setIsEditing(false)
      setError("")
    } catch (err) {
      console.error("Failed to update profile:", err)
      if (err.response?.status === 401) {
        navigate("/signin")
      } else {
        setError(err.response?.data || "Failed to update profile. Please try again.")
      }
    }
  }

  const handleLogout = () => {
    setShowLogoutModal(true)
  }

  const confirmLogout = async () => {
    try {
      await axios.post(`${BACKEND_URL}/api/user/logout`, {}, { withCredentials: true })
      setShowLogoutModal(false)
      navigate("/signin")
    } catch (err) {
      console.error("Logout failed:", err)
      setError("Logout failed. Please try again.")
    }
  }

  const cancelLogout = () => {
    setShowLogoutModal(false)
  }

  return (
    <div className="profile-page">
      <UserNavbar activePage="user-profile" />

      <div className="profile-content">
        <h1 className="profile-title">MY PROFILE</h1>

        {error && <div className="error-message">{error}</div>}

        <div className="profile-container">
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

          <div className="profile-main">
            <div className="profile-info-section">
              <div className="profile-image-container">
                <img
                  src={profileImage}
                  alt="User Profile"
                  className="profile-image"
                  onClick={handleImageClick}
                />
                {isUploading && <div className="uploading-spinner">Uploading...</div>}
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleFileChange}
                  className="file-input"
                  ref={fileInputRef}
                  style={{ display: "none" }}
                />
              </div>

              <div className="profile-details">
                {isEditing ? (
                  <div className="edit-form">
                    <div className="form-group">
                      <label>
                        <FaEnvelope className="detail-icon" /> Email:
                      </label>
                      <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleInputChange}
                        className="form-input"
                      />
                    </div>
                    <div className="form-group">
                      <label>
                        <FaMapMarkerAlt className="detail-icon" /> Address:
                      </label>
                      <input
                        type="text"
                        name="location"
                        value={formData.location}
                        onChange={handleInputChange}
                        className="form-input"
                      />
                    </div>
                    <div className="form-group">
                      <label>
                        <FaBirthdayCake className="detail-icon" /> Birthdate:
                      </label>
                      <input
                        type="date"
                        name="birthday"
                        value={formData.birthday}
                        onChange={handleInputChange}
                        className="form-input"
                      />
                    </div>
                    <div className="form-group">
                      <label>
                        <FaUser className="detail-icon" /> Password:
                      </label>
                      <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleInputChange}
                        className="form-input"
                        placeholder="Enter new password"
                      />
                    </div>
                    <div className="form-actions">
                      <button className="save-btn" onClick={handleSaveChanges}>
                        Save Changes
                      </button>
                      <button className="cancel-btn" onClick={handleEditToggle}>
                        Cancel
                      </button>
                    </div>
                  </div>
                ) : (
                  <>
                    <div className="profile-detail-item">
                      <span className="detail-label">
                        <FaUser className="detail-icon" /> Name:
                      </span>
                      <span className="detail-value">
                        {user ? `${user.firstname} ${user.lastname}` : "Loading..."}
                      </span>
                    </div>
                    <div className="profile-detail-item">
                      <span className="detail-label">
                        <FaEnvelope className="detail-icon" /> Email:
                      </span>
                      <span className="detail-value">{user?.email || "N/A"}</span>
                    </div>
                    <div className="profile-detail-item">
                      <span className="detail-label">
                        <FaMapMarkerAlt className="detail-icon" /> Address:
                      </span>
                      <span className="detail-value">{user?.location || "N/A"}</span>
                    </div>
                    <div className="profile-detail-item">
                      <span className="detail-label">
                        <FaPhone className="detail-icon" /> Contact no.:
                      </span>
                      <span className="detail-value">{user?.phoneNumber || "N/A"}</span>
                    </div>
                    <div className="profile-detail-item">
                      <span className="detail-label">
                        <FaBirthdayCake className="detail-icon" /> Birthdate:
                      </span>
                      <span className="detail-value">{user?.birthday || "N/A"}</span>
                    </div>
                    <button className="edit-profile-btn" onClick={handleEditToggle}>
                      <FaEdit /> Edit Profile
                    </button>
                  </>
                )}
              </div>
            </div>

            <div className="account-info-section">
              <div className="account-credentials">
                <div className="credential-item">
                  <span className="credential-label">Username:</span>
                  <span className="credential-value">{user?.username || "N/A"}</span>
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

        <div className="tarabaho-watermark">
          <span className="watermark-text">TARABAHO</span>
          <span className="watermark-subtext">TARA! TRABAHO</span>
        </div>
      </div>

      {showLogoutModal && <LogoutConfirmation onConfirm={confirmLogout} onCancel={cancelLogout} />}
      <Footer />
    </div>
  )
}

export default UserProfile