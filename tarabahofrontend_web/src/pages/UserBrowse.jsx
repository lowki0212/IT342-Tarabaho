"use client"

import React, { useState, useEffect } from "react"
import { Link } from "react-router-dom"
import axios from "axios"
import UserNavbar from "../components/UserNavbar"
import "../styles/User-browse.css"
import Footer from "../components/Footer"
import cleaningImg from "../assets/images/cleaning.png"
import errandsImg from "../assets/images/errands.png"
import tutoringImg from "../assets/images/tutoring.png"
import babysittingImg from "../assets/images/babysitting.png"
import gardeningImg from "../assets/images/gardening.png"

// Error Boundary Component
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

const Browse = () => {
  const [categories, setCategories] = useState([])
  const [error, setError] = useState("")
  const BACKEND_URL = "http://localhost:8080"

  // Fallback icons and taglines
  const categoryIcons = {
    Cleaning: cleaningImg,
    Errands: errandsImg,
    Tutoring: tutoringImg,
    Babysitting: babysittingImg,
    Gardening: gardeningImg,
  }

  const categoryTaglines = {
    Cleaning: "Keep your space spotless",
    Errands: "Get your tasks done",
    Tutoring: "Learn with ease",
    Babysitting: "Care for your kids",
    Gardening: "Grow your green space",
  }

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/api/categories`, {
          withCredentials: true,
        })
        // Normalize response data
        const data = Array.isArray(response.data) ? response.data : [];
        setCategories(data)
        if (data.length === 0) {
          setError("No categories available.")
        }
      } catch (err) {
        console.error("Failed to fetch categories:", err)
        setError(
          err.response?.status === 401
            ? "Please log in to view categories."
            : `Failed to load categories: ${err.message}`
        )
      }
    }
    fetchCategories()
  }, [])

  return (
    <ErrorBoundary>
      <div className="browse-page">
        <UserNavbar activePage="user-browse" />

        <div className="hero-section">
          <div className="hero-overlay"></div>
          <div className="hero-content">
            <h1 className="hero-title">
              Find on-demand help for your daily tasks anytime, anywhere with Tarabaho!
            </h1>
          </div>
        </div>

        <div className="browse-content">
          <button className="look-for-trabahador-button" aria-label="Look for Trabahador">
            Look for Trabahador
          </button>

          <p className="browse-description">
            Browse Trabahador profiles and find the perfect match for your task.
          </p>

          {error && <div className="error-message">{error}</div>}

          <div className="service-categories">
            {categories.length > 0 ? (
              categories.map((category) => {
                // Defensive checks
                if (!category || !category.name) {
                  console.warn("Invalid category:", category)
                  return null
                }
                return (
                  <Link
                    key={category.id || category.name}
                    to={`/user-browse/${category.name.toLowerCase()}`}
                    className="service-category"
                    aria-label={`Browse ${category.name} Services`}
                  >
                    <div className={`service-icon ${category.name.toLowerCase()}-icon`}>
                      <img
                        src={
                          category.iconUrl
                            ? `${BACKEND_URL}${category.iconUrl}`
                            : categoryIcons[category.name] || "/placeholder.svg"
                        }
                        alt={category.name}
                        className="service-img"
                      />
                    </div>
                    <div className="service-name">{category.name}</div>
                    <p className="service-tagline">
                      {category.tagline || categoryTaglines[category.name] || "Explore this service"}
                    </p>
                  </Link>
                )
              }).filter(Boolean)
            ) : (
              <p>No categories available.</p>
            )}
          </div>
        </div>
        <Footer />
      </div>
    </ErrorBoundary>
  )
}

export default Browse