"use client"

import { useState, useEffect } from "react"
import { useParams, useNavigate } from "react-router-dom"
import UserNavbar from "../components/UserNavbar"
import Footer from "../components/Footer"
import "../styles/WorkerProfileDetail.css"
import { FaStar, FaRegStar, FaBookmark } from "react-icons/fa"

// Import worker images for reviews
import martinJohnTImg from "../assets/images/martin.png"
import edreyVImg from "../assets/images/edreyval.png"
import jieRImg from "../assets/images/polbin1.png" // Using polbin1 for Jie R.
import kentMImg from "../assets/images/kent m.png"

const WorkerProfileDetail = () => {
  const { workerId } = useParams()
  const navigate = useNavigate()
  const [worker, setWorker] = useState(null)
  const [error, setError] = useState("")
  const [isBookmarked, setIsBookmarked] = useState(false)
  const BACKEND_URL = "http://localhost:8080"

  useEffect(() => {
    const fetchWorker = async () => {
      try {
        // In a real app, this would fetch from your API
        // For now, we'll use mock data that matches the screenshot
        const mockWorker = {
          id: workerId,
          firstName: "Paul Dave",
          lastName: "Q. Binoya",
          profilePicture: "/assets/images/polbin1.png",
          stars: 4,
          hourly: 63.0,
          birthday: "08/11/2004",
          email: "Pauldeb@gmail.com",
          address: "Lapaloma Cebu City",
          contactNo: "091201209102",
          description:
            "Dedicated and detail-oriented individual specializing in cleaning and maintaining spaces. Skilled in organizing, dusting, sanitizing, and ensuring cleanliness to create a comfortable and hygienic environment.",
          services: ["CLEANING", "GARDENING", "TUTORING"],
          reviews: [
            {
              id: 1,
              user: "Martin John T.",
              userImage: martinJohnTImg,
              rating: 4,
              comment: "Scammer!!! gi kuha among balde :(",
            },
            {
              id: 2,
              user: "Jie R.",
              userImage: jieRImg,
              rating: 5,
              comment: "5 star would hire again",
            },
            {
              id: 3,
              user: "Martin John T.",
              userImage: martinJohnTImg,
              rating: 4,
              comment: "Scammer!!! gi kuha among balde :(",
            },
            {
              id: 4,
              user: "Jie R.",
              userImage: jieRImg,
              rating: 5,
              comment: "5 star would hire again",
            },
            {
              id: 5,
              user: "Edrey V.",
              userImage: edreyVImg,
              rating: 4,
              comment: "Ampogi ni kuya! pero bati mo impyo",
            },
            {
              id: 6,
              user: "Edrey V.",
              userImage: edreyVImg,
              rating: 4,
              comment: "Ampogi ni kuya! pero bati mo impyo",
            },
          ],
          similarWorkers: [
            {
              id: 101,
              name: "Martin John T.",
              image: martinJohnTImg,
              stars: 5,
              hourly: 80.0,
              description:
                "An experienced cleaner with a keen eye for detail, ensuring every corner shines. Utilizes effective cleaning techniques to maintain a tidy, organized, and welcoming space.",
            },
            {
              id: 102,
              name: "Kent M.",
              image: kentMImg,
              stars: 5,
              hourly: 93.0,
              description:
                "A reliable and efficient cleaning specialist focused on keeping homes and workplaces immaculate. Passionate about delivering clean with a commitment to quality and care.",
            },
          ],
        }

        setWorker(mockWorker)
      } catch (err) {
        console.error("Failed to fetch worker:", err)
        setError(
          err.response?.status === 401
            ? "Please log in to view worker details."
            : "Failed to load worker details. Please try again.",
        )
      }
    }
    fetchWorker()
  }, [workerId])

  const renderStars = (rating = 0) => {
    const stars = []
    for (let i = 1; i <= 5; i++) {
      stars.push(
        i <= rating ? <FaStar key={i} className="star-filled" /> : <FaRegStar key={i} className="star-empty" />,
      )
    }
    return stars
  }

  const handleBookNow = () => {
    navigate(`/booking/${workerId}/payment`)
  }

  const toggleBookmark = () => {
    setIsBookmarked(!isBookmarked)
    // In a real app, you would make an API call to save the bookmark status
  }

  const handleSelectWorker = (workerId) => {
    navigate(`/worker-profile/${workerId}`)
  }

  if (error) {
    return (
      <div className="page-container">
        <UserNavbar activePage="user-browse" />
        <div className="content-container">
          <div className="error-message">{error}</div>
        </div>
        <Footer />
      </div>
    )
  }

  if (!worker) {
    return (
      <div className="page-container">
        <UserNavbar activePage="user-browse" />
        <div className="content-container">
          <div className="loading">Loading...</div>
        </div>
        <Footer />
      </div>
    )
  }

  return (
    <div className="worker-profile-detail-page">
      <UserNavbar activePage="user-browse" />

      <div className="worker-profile-hero">
        <div className="worker-profile-container">
          <div className="worker-profile-header">
            <div className="worker-image-container">
              <img
                src={worker.profilePicture || "/placeholder.svg?height=300&width=300"}
                alt={`${worker.firstName} ${worker.lastName}`}
                className="worker-image"
              />
            </div>

            <div className="worker-info">
              <div className="worker-name-bookmark">
                <h1>
                  {worker.firstName} {worker.lastName}
                </h1>
                <button
                  className={`bookmark-button ${isBookmarked ? "bookmarked" : ""}`}
                  onClick={toggleBookmark}
                  aria-label={isBookmarked ? "Remove from bookmarks" : "Add to bookmarks"}
                >
                  <FaBookmark />
                </button>
              </div>

              <p className="worker-description">{worker.description}</p>

              <div className="worker-rating">{renderStars(worker.stars)}</div>

              <div className="worker-rate">₱{worker.hourly.toFixed(2)}/hour</div>

              <div className="worker-services">
                {worker.services.map((service, index) => (
                  <span key={index} className="service-tag">
                    {service}
                  </span>
                ))}
              </div>
            </div>

            <div className="personal-info-box">
              <h3>Personal Information</h3>
              <div className="info-item">
                <span className="info-label">Birthday:</span>
                <span className="info-value">08/11/2004</span>
              </div>
              <div className="info-item">
                <span className="info-label">Email:</span>
                <span className="info-value">Pauldeb@gmail.com</span>
              </div>
              <div className="info-item">
                <span className="info-label">Address:</span>
                <span className="info-value">Lapaloma Cebu City</span>
              </div>
              <div className="info-item">
                <span className="info-label">Contact no.:</span>
                <span className="info-value">09120120102</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="worker-profile-content">
        <div className="worker-profile-container">
          <div className="reviews-section">
            <h2 className="section-title">
              <span className="icon">≡</span> Reviews
            </h2>

            <div className="reviews-grid">
              {worker.reviews.map((review) => (
                <div key={review.id} className="review-card">
                  <div className="reviewer-info">
                    <img
                      src={review.userImage || "/placeholder.svg?height=50&width=50"}
                      alt={review.user}
                      className="reviewer-image"
                    />
                    <div className="reviewer-details">
                      <div className="reviewer-name">{review.user}</div>
                      <div className="reviewer-rating">{renderStars(review.rating)}</div>
                    </div>
                  </div>
                  <p className="review-comment">{review.comment}</p>
                </div>
              ))}
            </div>
          </div>

          <div className="similar-workers-section">
            <h2 className="section-title">Similar people</h2>

            <div className="similar-workers-grid">
              {worker.similarWorkers.map((similarWorker) => (
                <div key={similarWorker.id} className="similar-worker-card">
                  <div className="similar-worker-image-container">
                    <img
                      src={similarWorker.image || "/placeholder.svg?height=150&width=150"}
                      alt={similarWorker.name}
                      className="similar-worker-image"
                    />
                  </div>
                  <div className="similar-worker-rating">{renderStars(similarWorker.stars)}</div>
                  <p className="similar-worker-description">{similarWorker.description}</p>
                  <div className="similar-worker-rate">₱{similarWorker.hourly.toFixed(2)}/hour</div>
                  <button className="select-worker-btn" onClick={() => handleSelectWorker(similarWorker.id)}>
                    Select
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  )
}

export default WorkerProfileDetail
