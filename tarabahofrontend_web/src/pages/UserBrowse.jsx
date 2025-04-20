"use client"

import { Link } from "react-router-dom"
import UserNavbar from "../components/UserNavbar"
import "../styles/User-browse.css"
import Footer from "../components/Footer"
import cleaningImg from "../assets/images/cleaning.png"
import errandsImg from "../assets/images/errands.png"
import tutoringImg from "../assets/images/tutoring.png"
import babysittingImg from "../assets/images/babysitting.png"
import gardeningImg from "../assets/images/gardening.png"

const Browse = () => {
  return (
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

        <div className="service-categories">
          <Link to="/user-browse-cleaning" className="service-category" aria-label="Browse Cleaning Services">
            <div className="service-icon cleaning-icon">
              <img src={cleaningImg || "/placeholder.svg"} alt="Cleaning" className="service-img" />
            </div>
            <div className="service-name">Cleaning</div>
            <p className="service-tagline">Keep your space spotless</p>
          </Link>

          <Link to="/user-browse-errands" className="service-category" aria-label="Browse Errands Services">
            <div className="service-icon errands-icon">
              <img src={errandsImg || "/placeholder.svg"} alt="Errands" className="service-img" />
            </div>
            <div className="service-name">Errands</div>
            <p className="service-tagline">Get your tasks done</p>
          </Link>

          <Link to="/user-browse-tutoring" className="service-category" aria-label="Browse Tutoring Services">
            <div className="service-icon tutoring-icon">
              <img src={tutoringImg || "/placeholder.svg"} alt="Tutoring" className="service-img" />
            </div>
            <div className="service-name">Tutoring</div>
            <p className="service-tagline">Learn with ease</p>
          </Link>

          <Link to="/user-browse-babysitting" className="service-category" aria-label="Browse Babysitting Services">
            <div className="service-icon babysitting-icon">
              <img src={babysittingImg || "/placeholder.svg"} alt="Babysitting" className="service-img" />
            </div>
            <div className="service-name">Babysitting</div>
            <p className="service-tagline">Care for your kids</p>
          </Link>

          <Link to="/user-browse-gardening" className="service-category" aria-label="Browse Gardening Services">
            <div className="service-icon gardening-icon">
              <img src={gardeningImg || "/placeholder.svg"} alt="Gardening" className="service-img" />
            </div>
            <div className="service-name">Gardening</div>
            <p className="service-tagline">Grow your green space</p>
          </Link>
        </div>
      </div>
      <Footer/>
    </div>
  )
}

export default Browse