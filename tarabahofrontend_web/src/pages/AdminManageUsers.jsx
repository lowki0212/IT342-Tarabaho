"use client"

import { useState, useEffect } from "react"
import AdminNavbar from "../components/AdminNavbar"
import Footer from "../components/Footer"
import "../styles/admin-manage-users.css"

const AdminManageUsers = () => {
  // Sample user data - in a real app, this would come from an API or database
  const [users, setUsers] = useState([
    {
      id: 1,
      name: "Angelo C. Quieta",
      username: "angeloquieta123",
      email: "quietaangelo@gmail.com",
      phone: "09266517720",
      birthday: "11/02/2002",
    },
    {
      id: 2,
      name: "Edrey P. Valle",
      username: "ederi23",
      email: "edersi@yahoo.com",
      phone: "9287632180",
      birthday: "05/31/2003",
    },
  ])

  const [searchTerm, setSearchTerm] = useState("")
  const [filteredUsers, setFilteredUsers] = useState(users)
  const [showDeleteModal, setShowDeleteModal] = useState(false)
  const [userToDelete, setUserToDelete] = useState(null)
  const [isLoading, setIsLoading] = useState(true)

  // Mock loading state
  useEffect(() => {
    setTimeout(() => {
      setIsLoading(false)
    }, 1000)
  }, [])

  // Filter users based on search term
  useEffect(() => {
    const filtered = users.filter(
      (user) =>
        user.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(searchTerm.toLowerCase())
    )
    setFilteredUsers(filtered)
  }, [searchTerm, users])

  const handleDeleteClick = (user) => {
    setUserToDelete(user)
    setShowDeleteModal(true)
  }

  const confirmDelete = () => {
    setUsers(users.filter((user) => user.id !== userToDelete.id))
    setShowDeleteModal(false)
    setUserToDelete(null)
  }

  const cancelDelete = () => {
    setShowDeleteModal(false)
    setUserToDelete(null)
  }

  return (
    <div className="admin-manage-users-page">
      {/* Using your existing AdminNavbar component */}
      <AdminNavbar activePage="manage-users" />

      {/* Main Content */}
      <div className="manage-users-container">
        <h1 className="manage-users-title">MANAGE USERS</h1>

        {/* Search Bar */}
        <div className="search-bar-container">
          <div className="search-bar">
            <svg
              className="search-icon"
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <circle cx="11" cy="11" r="7" stroke="#666" strokeWidth="2" />
              <path d="M16 16L20 20" stroke="#666" strokeWidth="2" strokeLinecap="round" />
            </svg>
            <input
              type="text"
              placeholder="Search by name, username, or email..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              aria-label="Search users"
            />
          </div>
        </div>

        {/* Users Table */}
        <div className="users-table-container">
          {isLoading ? (
            <div className="loading-spinner">Loading...</div>
          ) : (
            <div className="users-table">
              <div className="users-table-header">
                <div className="table-cell id-cell">ID</div>
                <div className="table-cell name-cell">NAME</div>
                <div className="table-cell username-cell">USERNAME</div>
                <div className="table-cell email-cell">EMAIL</div>
                <div className="table-cell phone-cell">PHONE</div>
                <div className="table-cell birthday-cell">BIRTHDAY</div>
                <div className="table-cell actions-cell"></div>
              </div>

              {filteredUsers.length === 0 ? (
                <div className="no-users-message">No users found.</div>
              ) : (
                filteredUsers.map((user, index) => (
                  <div
                    key={user.id}
                    className={`users-table-row ${index % 2 === 0 ? "even-row" : "odd-row"}`}
                  >
                    <div className="table-cell id-cell">{user.id}</div>
                    <div className="table-cell name-cell">{user.name}</div>
                    <div className="table-cell username-cell">{user.username}</div>
                    <div className="table-cell email-cell">{user.email}</div>
                    <div className="table-cell phone-cell">{user.phone}</div>
                    <div className="table-cell birthday-cell">{user.birthday}</div>
                    <div className="table-cell actions-cell">
                      <button
                        className="edit-button"
                        aria-label={`Edit user ${user.name}`}
                      >
                        <svg
                          width="16"
                          height="16"
                          viewBox="0 0 24 24"
                          fill="none"
                          xmlns="http://www.w3.org/2000/svg"
                        >
                          <path
                            d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13"
                            stroke="#0078ff"
                            strokeWidth="2"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          />
                          <path
                            d="M18.5 2.5C18.8978 2.10217 19.4374 1.87868 20 1.87868C20.5626 1.87868 21.1022 2.10217 21.5 2.5C21.8978 2.89783 22.1213 3.43739 22.1213 4C22.1213 4.56261 21.8978 5.10217 21.5 5.5L12 15L8 16L9 12L18.5 2.5Z"
                            stroke="#0078ff"
                            strokeWidth="2"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          />
                        </svg>
                        EDIT
                      </button>
                      <button
                        className="delete-button"
                        onClick={() => handleDeleteClick(user)}
                        aria-label={`Delete user ${user.name}`}
                      >
                        <svg
                          width="16"
                          height="16"
                          viewBox="0 0 24 24"
                          fill="none"
                          xmlns="http://www.w3.org/2000/svg"
                        >
                          <path
                            d="M3 6H5H21"
                            stroke="#ff0000"
                            strokeWidth="2"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          />
                          <path
                            d="M8 6V4C8 3.46957 8.21071 2.96086 8.58579 2.58579C8.96086 2.21071 9.46957 2 10 2H14C14.5304 2 15.0391 2.21071 15.4142 2.58579C15.7893 2.96086 16 3.46957 16 4V6M19 6V20C19 20.5304 18.7893 21.0391 18.4142 21.4142C18.0391 21.7893 17.5304 22 17 22H7C6.46957 22 5.96086 21.7893 5.58579 21.4142C5.21071 21.0391 5 20.5304 5 20V6H19Z"
                            stroke="#ff0000"
                            strokeWidth="2"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          />
                        </svg>
                        DELETE
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
          )}

          {/* Tarabaho Watermark */}
          <div className="tarabah-watermark">
            <span className="watermark-text">TARABAHO</span>
            <span className="watermark-tagline">TARA! TRABAHO</span>
          </div>
        </div>
      </div>

      {/* Delete Confirmation Modal */}
      {showDeleteModal && (
        <div className="delete-modal-overlay">
          <div className="delete-modal">
            <h2 className="delete-modal-title">
              Are you sure you want to delete {userToDelete?.name}?
            </h2>
            <div className="delete-modal-actions">
              <button className="delete-confirm-button" onClick={confirmDelete}>
                Yes, Delete
              </button>
              <button className="delete-cancel-button" onClick={cancelDelete}>
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
      <Footer/>
    </div>
  )
}

export default AdminManageUsers