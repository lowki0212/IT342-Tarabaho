"use client"

import React, { useState, useEffect } from "react"
import { useParams, useNavigate } from "react-router-dom"
import axios from "axios"
import UserNavbar from "../components/UserNavbar"
import "../styles/User-browse-category.css"
import { FaStar, FaRegStar } from "react-icons/fa"
import Footer from "../components/Footer"

class ErrorBoundary extends React.Component {
  state = { hasError: false, error: null }

  static getDerivedStateFromError(error) {
    return { hasError: true, error }
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="error-message">
          <h2>Something went wrong.</h2>
          <p>{this.state.error?.message || "An unexpected error occurred."}</p>
        </div>
      )
    }
    return this.props.children
  }
}

const UserBrowseCategory = () => {
  const { categoryName } = useParams()
  const navigate = useNavigate()
  const [workers, setWorkers] = useState([])
  const [filteredWorkers, setFilteredWorkers] = useState([])
  const [category, setCategory] = useState(null)
  const [error, setError] = useState("")
  const [searchQuery, setSearchQuery] = useState("")
  const [sortBy, setSortBy] = useState("rating")
  const BACKEND_URL = "http://localhost:8080"
  const token = localStorage.getItem("jwtToken")

  useEffect(() => {
    const fetchCategoryAndWorkers = async () => {
      try {
        const categoryResponse = await axios.get(`${BACKEND_URL}/api/categories`, {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        })
        const data = Array.isArray(categoryResponse.data) ? categoryResponse.data : []
        const foundCategory = data.find(
          (cat) => cat && cat.name && cat.name.toLowerCase() === categoryName.toLowerCase()
        )
        if (!foundCategory) {
          setError("Category not found.")
          return
        }
        setCategory(foundCategory)

        const formattedCategoryName =
          categoryName.charAt(0).toUpperCase() + categoryName.slice(1)
        const workersResponse = await axios.get(
          `${BACKEND_URL}/api/worker/category/${formattedCategoryName}/available`,
          {
            headers: { Authorization: `Bearer ${token}` },
            withCredentials: true,
          }
        )
        setWorkers(workersResponse.data)
        setFilteredWorkers(workersResponse.data) // Initialize filtered workers
      } catch (err) {
        console.error(`Failed to fetch data for ${categoryName}:`, err)
        console.error("Error response:", err.response?.data)
        console.error("Error status:", err.response?.status)
        setError(
          err.response?.status === 401
            ? "Please log in to view workers."
            : err.response?.data?.replace("⚠️ ", "") || "Failed to load workers. Please try again."
        )
        if (err.response?.status === 401) {
          navigate("/login")
        }
      }
    }
    fetchCategoryAndWorkers()
  }, [categoryName, navigate, token])

  // Handle search and sort
  useEffect(() => {
    let updatedWorkers = [...workers]

    // Filter by search query
    if (searchQuery) {
      updatedWorkers = updatedWorkers.filter(worker => {
        const fullName = `${worker.firstName || ""} ${worker.lastName || ""}`.toLowerCase()
        return fullName.startsWith(searchQuery.toLowerCase())
      })
    }

    // Sort by selected criterion
    updatedWorkers.sort((a, b) => {
      if (sortBy === "rating") {
        return (b.stars || 0) - (a.stars || 0) // Highest rating first
      } else if (sortBy === "price") {
        return (a.hourly || 0) - (b.hourly || 0) // Lowest price first
      }
      return 0
    })

    setFilteredWorkers(updatedWorkers)
  }, [searchQuery, sortBy, workers])

  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value)
  }

  const handleSortChange = (e) => {
    setSortBy(e.target.value)
  }

  const renderStars = (rating = 0) => {
    const stars = []
    for (let i = 1; i <= 5; i++) {
      stars.push(
        i <= rating ? (
          <FaStar key={i} className="star-filled" />
        ) : (
          <FaRegStar key={i} className="star-empty" />
        )
      )
    }
    return stars
  }

  const handleViewWorker = (workerId) => {
    navigate(`/worker-profile/${workerId}`)
  }

  const displayCategoryName = categoryName
    ? categoryName.charAt(0).toUpperCase() + categoryName.slice(1)
    : "Category"

  return (
    <ErrorBoundary>
      <div className={`browse-category-page ${categoryName?.toLowerCase() || ''} page-container`}>
        <UserNavbar activePage="user-browse" />
        <div className="filter-section">
          <div className="sort-container">
            <span className="sort-label">Sort by</span>
            <select
              className="sort-dropdown"
              value={sortBy}
              onChange={handleSortChange}
            >
              <option value="rating">Rating</option>
              <option value="price">Price</option>
            </select>
          </div>
          <div className="search-container">
            <input
              type="text"
              className="search-input"
              placeholder="Search by name"
              value={searchQuery}
              onChange={handleSearchChange}
            />
          </div>
        </div>
        <div className="service-providers-grid">
          <h2>{displayCategoryName} Workers</h2>
          {error && <div className="error-message">{error}</div>}
          {filteredWorkers.length > 0 ? (
            filteredWorkers.map((worker) => {
              const displayName = worker.firstName && worker.lastName
                ? `${worker.firstName} ${worker.lastName}`
                : worker.username || "Unknown Worker"
              return (
                <div key={worker.id} className="provider-card">
                  <div className="provider-image">
                    <img
                      src={
                        worker.profilePicture
                          ? `${BACKEND_URL}${worker.profilePicture}`
                          : "/placeholder.svg"
                      }
                      alt={displayName}
                    />
                  </div>
                  <div className="provider-details">
                    <h3
                      className="provider-name"
                      style={{ color: "black", fontSize: "1.2rem", margin: "0.5rem 0" }}
                    >
                      {displayName}
                    </h3>
                    <div className="provider-rating">{renderStars(worker.stars)}</div>
                    <p className="provider-description">
                      {worker.biography || "No biography available"}
                    </p>
                    <p className="provider-rate">₱{worker.hourly.toFixed(2)}/hour</p>
                    <button
                      className="view-button"
                      onClick={() => handleViewWorker(worker.id)}
                    >
                      View
                    </button>
                  </div>
                </div>
              )
            })
          ) : (
            <p>No workers found for {displayCategoryName}.</p>
          )}
        </div>
      </div>
      <Footer />
    </ErrorBoundary>
  )
}

export default UserBrowseCategory