"use client"

import { useState } from "react"
import { useNavigate } from "react-router-dom"
import UserNavbar from "../components/UserNavbar"
import Footer from "../components/Footer"
import "../styles/UserBookmarks.css"
import { FaUser, FaBookmark, FaHistory, FaSignOutAlt } from "react-icons/fa"
import LogoutConfirmation from "../components/User-LogoutConfirmation"

// Import worker images
import paulBImg from "../assets/images/polbin1.png"
import edreyVImg from "../assets/images/edreyval.png"
import martinJohnTImg from "../assets/images/martin.png"
import dogCImg from "../assets/images/dogcat.png"
import kentMImg from "../assets/images/kent m.png"
import blakeMImg from "../assets/images/blake.png"

const UserBookmarks = () => {
  const navigate = useNavigate()
  const [showLogoutModal, setShowLogoutModal] = useState(false)

  const handleProfileClick = () => {
    navigate("/user-profile")
  }

  const handleHistoryClick = () => {
    navigate("/booking-history")
  }

  const handleLogout = () => {
    setShowLogoutModal(true)
  }

  const confirmLogout = () => {
    console.log("User logged out")
    setShowLogoutModal(false)
    navigate("/signin")
  }

  const cancelLogout = () => {
    setShowLogoutModal(false)
  }

  const handleViewProfile = (workerId) => {
    navigate(`/worker-profile/${workerId}`)
  }

  // Bookmarked workers data
  const bookmarkedWorkers = [
    {
      id: 1,
      name: "Paul B.",
      image: paulBImg,
    },
    {
      id: 2,
      name: "Edrey V.",
      image: edreyVImg,
    },
    {
      id: 3,
      name: "Martin John T.",
      image: martinJohnTImg,
    },
    {
      id: 4,
      name: "Dog C.",
      image: dogCImg,
    },
    {
      id: 5,
      name: "Kent M.",
      image: kentMImg,
    },
    {
      id: 6,
      name: "Blake M.",
      image: blakeMImg,
    },
  ]

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
      <Footer />
    </div>
  )
}

export default UserBookmarks
