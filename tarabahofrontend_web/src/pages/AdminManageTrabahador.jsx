"use client"

import { useState, useEffect } from "react"
import { Link } from "react-router-dom"
import AdminNavbar from "../components/AdminNavbar"
import "../styles/admin-manage-trabahador.css"

const AdminManageTrabahador = () => {
  // Sample Trabahador data - in a real app, this would come from an API or database
  const [trabahadors, setTrabahadors] = useState([
    {
      id: 1,
      name: "Paul Dave Q. Binoya",
      username: "polbin",
      email: "pauldeb@gmail.com",
      phone: "09266517720",
      birthday: "11/02/2002",
    },
    {
      id: 2,
      name: "Blake Mongoni",
      username: "blakesoy",
      email: "blakemongoni@yahoo.com",
      phone: "9287632180",
      birthday: "05/31/2003",
    },
  ])

  const [searchTerm, setSearchTerm] = useState("")
  const [filteredTrabahadors, setFilteredTrabahadors] = useState(trabahadors)
  const [isLoading, setIsLoading] = useState(true)

  // Mock loading state
  useEffect(() => {
    setTimeout(() => {
      setIsLoading(false)
    }, 1000)
  }, [])

  // Filter Trabahadors based on search term
  useEffect(() => {
    const filtered = trabahadors.filter(
      (trabahador) =>
        trabahador.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        trabahador.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
        trabahador.email.toLowerCase().includes(searchTerm.toLowerCase())
    )
    setFilteredTrabahadors(filtered)
  }, [searchTerm, trabahadors])

  return (
    <div className="admin-manage-trabahador-page">
      {/* Using your existing AdminNavbar component */}
      <AdminNavbar activePage="manage-trabahador" />

      {/* Main Content */}
      <div className="manage-trabahador-container">
        <h1 className="manage-trabahador-title">MANAGE TRABAHADOR</h1>

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
              aria-label="Search Trabahadors"
            />
          </div>
        </div>

        {/* Trabahador Table */}
        <div className="trabahador-table-container">
          {isLoading ? (
            <div className="loading-spinner">Loading...</div>
          ) : (
            <div className="trabahador-table">
              <div className="trabahador-table-header">
                <div className="table-cell id-cell">ID</div>
                <div className="table-cell name-cell">NAME</div>
                <div className="table-cell username-cell">USERNAME</div>
                <div className="table-cell email-cell">EMAIL</div>
                <div className="table-cell phone-cell">PHONE</div>
                <div className="table-cell birthday-cell">BIRTHDAY</div>
                <div className="table-cell actions-cell"></div>
              </div>

              {filteredTrabahadors.length === 0 ? (
                <div className="no-trabahadors-message">No Trabahadors found.</div>
              ) : (
                filteredTrabahadors.map((trabahador, index) => (
                  <div
                    key={trabahador.id}
                    className={`trabahador-table-row ${index % 2 === 0 ? "even-row" : "odd-row"}`}
                  >
                    <div className="table-cell id-cell">{trabahador.id}</div>
                    <div className="table-cell name-cell">{trabahador.name}</div>
                    <div className="table-cell username-cell">{trabahador.username}</div>
                    <div className="table-cell email-cell">{trabahador.email}</div>
                    <div className="table-cell phone-cell">{trabahador.phone}</div>
                    <div className="table-cell birthday-cell">{trabahador.birthday}</div>
                    <div className="table-cell actions-cell">
                      <Link
                        to={`/admin/trabahador/${trabahador.id}`}
                        className="view-details-button"
                        aria-label={`View details of ${trabahador.name}`}
                      >
                        <svg
                          width="16"
                          height="16"
                          viewBox="0 0 24 24"
                          fill="none"
                          xmlns="http://www.w3.org/2000/svg"
                        >
                          <path
                            d="M1 12C1 12 5 4 12 4C19 4 23 12 23 12C23 12 19 20 12 20C5 20 1 12 1 12Z"
                            stroke="#0078ff"
                            strokeWidth="2"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          />
                          <path
                            d="M12 15C13.6569 15 15 13.6569 15 12C15 10.3431 13.6569 9 12 9C10.3431 9 9 10.3431 9 12C9 13.6569 10.3431 15 12 15Z"
                            stroke="#0078ff"
                            strokeWidth="2"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          />
                        </svg>
                        VIEW DETAILS
                      </Link>
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
    </div>
  )
}

export default AdminManageTrabahador