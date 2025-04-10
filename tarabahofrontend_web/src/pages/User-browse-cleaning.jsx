"use client"

import UserNavbar from "../components/UserNavbar"
import "../styles/User-browse-cleaning.css"
import cleaningBanner from "../assets/images/cleaningbanner.png"
import { FaStar, FaRegStar } from "react-icons/fa"
// Import all provider images
import paulBImg from "../assets/images/paul b.png"
import martinJohnTImg from "../assets/images/martin john t.png"
import kentMImg from "../assets/images/kent m.png"
import edreyVImg from "../assets/images/edrey v.png"
import dogCImg from "../assets/images/dog c.png"
import blakeMImg from "../assets/images/blake m.png"

const UserBrowseCleaning = () => {
  return (
    <div className="browse-cleaning-page">
      <UserNavbar activePage="user-browse" />

      {/* Banner Section */}
      <div className="service-banner">
        <img src={cleaningBanner || "/placeholder.svg"} alt="Cleaning Service" className="banner-img" />
        <div className="banner-overlay">
          <h1 className="banner-title">CLEANING</h1>
        </div>
      </div>

      {/* Filter Section */}
      <div className="filter-section">
        <div className="sort-container">
          <span className="sort-label">Sort by</span>
          <select className="sort-dropdown">
            <option value="rating">Rating</option>
            <option value="price">Price</option>
            <option value="experience">Experience</option>
          </select>
        </div>
        <div className="search-container">
          <input type="text" className="search-input" placeholder="Search" />
        </div>
      </div>

      {/* Service Providers Grid */}
      <div className="service-providers-grid">
        {/* Provider 1 - Paul B. */}
        <div className="provider-card">
          <div className="provider-image">
            <img src={paulBImg || "/placeholder.svg"} alt="Paul B." />
          </div>
          <div className="provider-details">
            <div className="provider-rating">
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
            </div>
            <p className="provider-description">
              Dedicated and detail-oriented individual specializing in cleaning and maintaining homes, offices, and
              organizations. Committed to creating a comfortable and hygienic environment.
            </p>
            <p className="provider-rate">₱63.00/hour</p>
            <button className="view-button">View</button>
          </div>
        </div>

        {/* Provider 2 - Martin John T. */}
        <div className="provider-card">
          <div className="provider-image">
            <img src={martinJohnTImg || "/placeholder.svg"} alt="Martin John T." />
          </div>
          <div className="provider-details">
            <div className="provider-rating">
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
            </div>
            <p className="provider-description">
              An experienced cleaner with a keen eye for detail, ensuring every corner shines. Utilizes effective
              cleaning techniques to maintain a well-organized and welcoming space.
            </p>
            <p className="provider-rate">₱80.00/hour</p>
            <button className="view-button">View</button>
          </div>
        </div>

        {/* Provider 3 - Kent M. */}
        <div className="provider-card">
          <div className="provider-image">
            <img src={kentMImg || "/placeholder.svg"} alt="Kent M." />
          </div>
          <div className="provider-details">
            <div className="provider-rating">
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
            </div>
            <p className="provider-description">
              A reliable and efficient cleaning specialist focused on keeping homes and workplaces spotless and
              sanitized, creating clean with a commitment to quality and care.
            </p>
            <p className="provider-rate">₱93.00/hour</p>
            <button className="view-button">View</button>
          </div>
        </div>

        {/* Provider 4 - Edrey V. */}
        <div className="provider-card">
          <div className="provider-image">
            <img src={edreyVImg || "/placeholder.svg"} alt="Edrey V." />
          </div>
          <div className="provider-details">
            <div className="provider-rating">
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaRegStar className="star-empty" />
            </div>
            <p className="provider-description">
              A hardworking professional with expertise in cleaning and organizing. Known for delivering thorough
              cleaning services, ensuring spaces are fresh, sanitized, and well-maintained.
            </p>
            <p className="provider-rate">₱100.00/hour</p>
            <button className="view-button">View</button>
          </div>
        </div>

        {/* Provider 5 - Dog C. */}
        <div className="provider-card">
          <div className="provider-image">
            <img src={dogCImg || "/placeholder.svg"} alt="Dog C." />
          </div>
          <div className="provider-details">
            <div className="provider-rating">
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
            </div>
            <p className="provider-description">
              A meticulous cleaner dedicated to maintaining immaculate spaces and excelling in a variety of cleaning
              tasks with precision and ensuring a wholesome and hygienic environment.
            </p>
            <p className="provider-rate">₱93.00/hour</p>
            <button className="view-button">View</button>
          </div>
        </div>

        {/* Provider 6 - Blake M. */}
        <div className="provider-card">
          <div className="provider-image">
            <img src={blakeMImg || "/placeholder.svg"} alt="Blake M." />
          </div>
          <div className="provider-details">
            <div className="provider-rating">
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaStar className="star-filled" />
              <FaRegStar className="star-empty" />
            </div>
            <p className="provider-description">
              A reliable and efficient cleaning specialist focused on keeping homes and workplaces immaculate.
              Passionate about delivering a clean environment to quality and care.
            </p>
            <p className="provider-rate">₱176.00/hour</p>
            <button className="view-button">View</button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default UserBrowseCleaning
