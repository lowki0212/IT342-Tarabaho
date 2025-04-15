"use client"

import { useState } from "react"
import { useNavigate } from "react-router-dom"
import logo from "../assets/images/logowhite.png"
import Footer from "../components/Footer"
import "../styles/register-admin.css"

const RegisterAdmin = () => {
  const navigate = useNavigate()
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    address: "",
    contactNo: "",
    birthday: "",
    username: "",
    password: "",
    confirmPassword: "",
  })

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    // Add registration logic here
    console.log("Admin Registration data:", formData)
    // Navigate to success page or login page after successful registration
  }

  const handleBack = () => {
    navigate("/register")
  }

  return (
    <div className="register-admin-container">
      <button className="back-button" onClick={handleBack}>
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M15 19L8 12L15 5" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
      </button>

      <form onSubmit={handleSubmit} className="register-admin-form">
        <div className="form-left-section">
          <div className="logo-section">
            <div className="logo-container">
              <div style={{ display: "flex", alignItems: "center" }}>
                 <img src={logo || "/placeholder.svg"} alt="Tarabaho Logo" className="logo" />
              </div>

            </div>
          </div>

          <div className="left-content">
            <h2 className="form-title">Administrator Sign-Up Form</h2>
            <p className="form-description">
              Sign up now and get started quickly. Create your admin account with a few clicks.
            </p>

            <div className="form-group">
              <label htmlFor="username">
                Username <span className="required">*</span>
              </label>
              <input
                type="text"
                id="username"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="confirmPassword">
                Confirm Password <span className="required">*</span>
              </label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                required
              />
            </div>
          </div>
        </div>

        <div className="form-right-section">
          <div className="right-content">
            <div className="name-group">
              <label>Your name</label>
              <div className="name-inputs">
                <input
                  type="text"
                  name="firstName"
                  placeholder="First"
                  value={formData.firstName}
                  onChange={handleChange}
                  required
                />
                <input
                  type="text"
                  name="lastName"
                  placeholder="Last"
                  value={formData.lastName}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="email">Email address</label>
              <input type="email" id="email" name="email" value={formData.email} onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label htmlFor="address">Address</label>
              <input
                type="text"
                id="address"
                name="address"
                value={formData.address}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="contactNo">Contact no.</label>
              <input
                type="tel"
                id="contactNo"
                name="contactNo"
                value={formData.contactNo}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="birthday">Birthday</label>
              <input
                type="date"
                id="birthday"
                name="birthday"
                value={formData.birthday}
                onChange={handleChange}
                required
              />
            </div>

            <button type="submit" className="signup-button">
              Sign Up
            </button>
          </div>
        </div>
      </form>
      <Footer/>
    </div>
  )
}

export default RegisterAdmin