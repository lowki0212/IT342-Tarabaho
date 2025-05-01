"use client"

import { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import UserNavbar from "../components/UserNavbar"
import Footer from "../components/Footer"
import "../styles/UserBookmarks.css"
import { FaUser, FaBookmark, FaHistory, FaSignOutAlt } from "react-icons/fa"
import LogoutConfirmation from "../components/User-LogoutConfirmation"

const UserBookmarks = () => {
  const navigate = useNavigate()
  const [showLogoutModal, setShowLogoutModal] = useState(false)
  const [bookmarkedWorkers, setBookmarkedWorkers] = useState([])
  const [error, setError] = useState("")
  const BACKEND_URL = "http://localhost:8080"

  useEffect(() => {
    const fetchBookmarkedWorkers = async () => {
      try {
        console.log("Fetching bookmarked workers")
        const response = await axios.get(`${BACKEND_URL}/api/bookmarks`, {
          withCredentials: true, // Send cookies
        })
        console.log("Bookmarked workers response:", response.data)
        const workersData = response.data.map(worker => ({
          id: worker.id ?? 0,
          name: `${worker.firstName ?? "Unknown"} ${worker.lastName ?? "Worker"}`,
          image: worker.profilePicture ?? "/placeholder.svg",
        }))
        setBookmarkedWorkers(workersData)
      } catch (err) {
        console.error("Failed to fetch bookmarked workers:", err)
        console.error("Error response:", err.response?.data)
        console.error("Error status:", err.response?.status)
        setError(
          err.response?.status === 401
            ? "Your session has expired. Please log in again."
            : err.response?.data?.replace("⚠️ ", "") || "Failed to load bookmarked workers. Please try again."
        )
        if (err.response?.status === 401) {
          navigate("/login")
        }
      }
    }

    fetchBookmarkedWorkers()
  }, [navigate])

  const handleProfileClick = () => {
    navigate("/user-profile")
  }

  const handleHistoryClick = () => {
    navigate("/booking-history")
  }

  const handleLogout = () => {
    setShowLogoutModal(true)
  }

  const confirmLogout = async () => {
    try {
      // Call backend logout endpoint to invalidate the session/cookie
      await axios.post(
        `${BACKEND_URL}/api/user/logout`,
        {},
        {
          withCredentials: true, // Send cookies
        }
      )
      console.log("User logged out")
      setShowLogoutModal(false)
      navigate("/login")
    } catch (err) {
      console.error("Failed to logout:", err)
      // Proceed with logout even if backend call fails
      setShowLogoutModal(false)
      navigate("/login")
    }
  }

  const cancelLogout = () => {
    setShowLogoutModal(false)
  }

  const handleViewProfile = (workerId) => {
    navigate(`/worker-profile-detail/${workerId}`)
  }

  return (
    <div className="bookmarks-page">
      <UserNavbar activePage="user-profile" />

      <div className="bookmarks-content">
        <h1 className="bookmarks-title">BOOKMARKS</h1>

        <div className="bookmarks-container">
          {/* Sidebar */}
          <div className="bookmarks-sidebar">
            <div className="sidebar-item" onClick={handleProfileClick}>
              <FaUser className="sidebar-icon" />
              <span>PROFILE</span>
            </div>
            <div className="sidebar-item active">
              <FaBookmark className="sidebar-icon" />
              <span>BOOKMARKS</span>
            </div>
            <div className="sidebar-item" onClick={handleHistoryClick}>
              <FaHistory className="sidebar-icon" />
              <span>HISTORY</span>
            </div>
            <div className="sidebar-item logout" onClick={handleLogout}>
              <FaSignOutAlt className="sidebar-icon" />
              <span>LOG OUT</span>
            </div>
          </div>

          {/* Main Content - Bookmarked Workers Grid */}
          <div className="bookmarks-main">
            {error && <div className="error-message">{error}</div>}
            {bookmarkedWorkers.length > 0 ? (
              <div className="bookmarked-workers-grid">
                {bookmarkedWorkers.map((worker) => (
                  <div key={worker.id} className="worker-card">
                    <div className="worker-image-container">
                      <img src={worker.image || "/placeholder.svg"} alt={worker.name} className="worker-image" />
                    </div>
                    <div className="worker-info">
                      <div className="worker-name-container">
                        <h3 className="worker-name">{worker.name}</h3>
                        <FaBookmark className="bookmark-icon" />
                      </div>
                      <button className="view-profile-btn" onClick={() => handleViewProfile(worker.id)}>
                        View Profile
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p>No bookmarked workers found.</p>
            )}
          </div>
        </div>
      </div>
      {/* Logout Confirmation Modal */}
      {showLogoutModal && <LogoutConfirmation onConfirm={confirmLogout} onCancel={cancelLogout} />}
      <Footer />
    </div>
  )
}

export default UserBookmarks