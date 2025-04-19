"use client"

import { useState, useEffect, useRef } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import Cookies from "js-cookie"
import AdminNavbar from "../components/AdminNavbar"
import LogoutConfirmation from "../components/Admin-LogoutConfirmation"
import profilePlaceholder from "../assets/images/profile-placeholder.png"
import "../styles/admin-profile.css"

const AdminProfile = () => {
  const navigate = useNavigate()
  const [admin, setAdmin] = useState(null)
  const [isEditing, setIsEditing] = useState(false)
  const [formData, setFormData] = useState({
    email: "",
    address: "",
    password: "",
  })
  const [profileImage, setProfileImage] = useState(profilePlaceholder)
  const [selectedFile, setSelectedFile] = useState(null)
  const [showLogoutModal, setShowLogoutModal] = useState(false)
  const [error, setError] = useState("")
  const fileInputRef = useRef(null)

  const BACKEND_URL = "http://localhost:8080"

  useEffect(() => {
    const fetchAdmin = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/api/admin/me`, {
          withCredentials: true,
        })
        if (response.data) {
          setAdmin(response.data)
          setFormData({
            email: response.data.email || "",
            address: response.data.address || "",
            password: "",
          })
          setProfileImage(
            response.data.profilePicture
              ? `${BACKEND_URL}${response.data.profilePicture}`
              : profilePlaceholder
          )
        }
      } catch (err) {
        console.error("Failed to fetch admin:", err)
        setError("Failed to load profile. Please try again.")
        navigate("/admin-login") // Redirect to login if fetch fails
      }
    }
    fetchAdmin()
  }, [navigate])

  const handleImageClick = () => {
    fileInputRef.current.click()
  }

  const handleFileChange = async (e) => {
    const file = e.target.files[0]
    if (!file) {
      setError("No file selected.")
      return
    }
    setSelectedFile(file)
    console.log("Selected file:", file.name)

    const uploadFormData = new FormData()
    uploadFormData.append("file", file)

    try {
      console.log("Uploading file:", file.name)
      const response = await axios.post(
        `${BACKEND_URL}/api/admin/upload-picture`,
        uploadFormData,
        {
          withCredentials: true,
          headers: { "Content-Type": "multipart/form-data" },
        }
      )
      setAdmin(response.data)
      setProfileImage(
        response.data.profilePicture
          ? `${BACKEND_URL}${response.data.profilePicture}`
          : profilePlaceholder
      )
      setSelectedFile(null)
      setError("")
      console.log("Upload successful:", response.data)
    } catch (err) {
      console.error("Failed to upload picture:", err)
      setError(err.response?.data || "Failed to upload picture. Please try again.")
    }
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
      const updatedAdmin = {
        ...admin,
        email: formData.email,
        address: formData.address,
        password: formData.password || admin.password,
      }
      const response = await axios.put(
        `${BACKEND_URL}/api/admin/edit/${admin.id}`,
        updatedAdmin,
        {
          withCredentials: true,
        }
      )
      setAdmin(response.data)
      setIsEditing(false)
      setError("")
    } catch (err) {
      console.error("Failed to update profile:", err)
      setError(err.response?.data || "Failed to update profile. Please try again.")
    }
  }

  const handleLogout = () => {
    setShowLogoutModal(true)
  }

  const confirmLogout = async () => {
    try {
      await axios.post(
        `${BACKEND_URL}/api/admin/logout`, // Fixed endpoint to admin logout
        {},
        { withCredentials: true }
      )
      Cookies.remove("jwtToken", { path: "/", domain: "localhost" })
      console.log("Admin logged out")
      setShowLogoutModal(false)
      navigate("/admin-login")
    } catch (err) {
      console.error("Logout failed:", err)
      Cookies.remove("jwtToken", { path: "/", domain: "localhost" })
      setShowLogoutModal(false)
      navigate("/admin-login")
    }
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

        {error && <div className="error-message">{error}</div>}

        <div className="profile-container">
          <div className="profile-card">
            <div className="profile-top-section">
              <div className="profile-image-section">
                <img
                  src={profileImage}
                  alt="Profile"
                  className="profile-image"
                  onClick={handleImageClick}
                />
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleFileChange}
                  ref={fileInputRef}
                  style={{ display: "none" }}
                />
              </div>

              <div className="profile-credentials-section">
                <div className="credential-row">
                  <div className="credential-label">Username:</div>
                  <div className="credential-value">
                    {admin?.username || "Loading..."}
                  </div>
                </div>

                <div className="credential-row">
                  <div className="credential-label">Password:</div>
                  <div className="credential-value">
                    {isEditing ? (
                      <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleInputChange}
                        placeholder="Enter new password"
                      />
                    ) : (
                      "********"
                    )}
                  </div>
                  {!isEditing && (
                    <a href="#" className="change-password-link" onClick={handleEditToggle}>
                      Change password
                    </a>
                  )}
                </div>
              </div>
            </div>

            <div className="edit-profile-container">
              {isEditing ? (
                <>
                  <button className="edit-profile-btn" onClick={handleSaveChanges}>
                    SAVE CHANGES
                  </button>
                  <button
                    className="edit-profile-btn cancel-btn"
                    onClick={handleEditToggle}
                    style={{ backgroundColor: "#ff3b30", marginLeft: "1rem" }}
                  >
                    CANCEL
                  </button>
                </>
              ) : (
                <button className="edit-profile-btn" onClick={handleEditToggle}>
                  EDIT PROFILE
                </button>
              )}
            </div>

            <div className="profile-details-section">
              <div className="detail-row">
                <div className="detail-label">Name:</div>
                <div className="detail-value">
                  {admin ? `${admin.firstname} ${admin.lastname}` : "Loading..."}
                </div>
              </div>
              <div className="detail-row">
                <div className="detail-label">Email:</div>
                {isEditing ? (
                  <input
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    className="detail-input"
                  />
                ) : (
                  <div className="detail-value">{admin?.email || "N/A"}</div>
                )}
              </div>
              <div className="detail-row">
                <div className="detail-label">Address:</div>
                {isEditing ? (
                  <input
                    type="text"
                    name="address"
                    value={formData.address}
                    onChange={handleInputChange}
                    className="detail-input"
                  />
                ) : (
                  <div className="detail-value">{admin?.address || "N/A"}</div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>

      {showLogoutModal && (
        <LogoutConfirmation onConfirm={confirmLogout} onCancel={cancelLogout} />
      )}
    </div>
  )
}

export default AdminProfile