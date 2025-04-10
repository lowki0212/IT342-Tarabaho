"use client"

import { useState } from "react"
import { useNavigate } from "react-router-dom"
import logo from "../assets/images/logowhite.png"
import "../styles/RegisterTrabahador.css"

const RegisterTrabahador = () => {
  const navigate = useNavigate()
  const [currentStep, setCurrentStep] = useState(1)

  // Form state
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    firstName: "",
    lastName: "",
    email: "",
    contactNo: "",
    birthday: "",
    address: "",
    zipCode: "",
    nbiClearance: null,
    nationalId: null,
    picture: null,
    policeClearance: null,
  })

  // Error state
  const [errors, setErrors] = useState({})

  const handleBack = () => {
    if (currentStep === 1) {
      navigate("/register")
    } else {
      setCurrentStep(1)
    }
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData({
      ...formData,
      [name]: value,
    })

    // Clear error when user types
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: "",
      })
    }
  }

  const handleFileChange = (e) => {
    const { name, files } = e.target
    if (files && files[0]) {
      setFormData({
        ...formData,
        [name]: files[0],
      })

      // Clear error when user selects a file
      if (errors[name]) {
        setErrors({
          ...errors,
          [name]: "",
        })
      }
    }
  }

  const validateStep1 = () => {
    const newErrors = {}

    if (!formData.username.trim()) newErrors.username = "Username is required"
    if (!formData.password) newErrors.password = "Password is required"
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match"
    }
    if (!formData.firstName.trim() || !formData.lastName.trim()) {
      newErrors.name = "Full name is required"
    }
    if (!formData.email.trim()) newErrors.email = "Email is required"
    if (!formData.contactNo.trim()) newErrors.contactNo = "Contact number is required"
    if (!formData.address.trim()) newErrors.address = "Address is required"

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const validateStep2 = () => {
    const newErrors = {}

    if (!formData.zipCode.trim()) newErrors.zipCode = "Zip code is required"
    if (!formData.nbiClearance) newErrors.nbiClearance = "NBI Clearance is required"
    if (!formData.nationalId) newErrors.nationalId = "National ID is required"
    if (!formData.picture) newErrors.picture = "2x2 picture is required"
    if (!formData.policeClearance) newErrors.policeClearance = "Police Clearance is required"

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleNext = () => {
    if (validateStep1()) {
      setCurrentStep(2)
      window.scrollTo(0, 0)
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    if (validateStep2()) {
      // Here you would typically send the data to your backend
      console.log("Form submitted:", formData)

      // Navigate to success page or login
      alert("Registration successful! You can now log in.")
      navigate("/signin")
    }
  }

  return (
    <div className="register-trabahador-container">
      <button className="back-button" onClick={handleBack}>
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M15 19L8 12L15 5" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
      </button>

      <div className="register-trabahador-form">
        {currentStep === 1 ? (
          <>
            {/* Step 1: Personal Information */}
            <div className="form-left-section">
              <div className="logo-container">
                <img src={logo || "/placeholder.svg"} alt="Tarabaho Logo" className="logo" />
              </div>
              <h2 className="form-title">Worker Sign-Up Form</h2>
              <p className="form-description">
                Use this form and get started quickly. Create an online account with a few clicks.
              </p>
            </div>

            <div className="form-right-section">
              <div className="right-content">
                <div className="form-group">
                  <label htmlFor="username">
                    Username <span className="required">*</span>
                  </label>
                  <input
                    type="text"
                    id="username"
                    name="username"
                    value={formData.username}
                    onChange={handleInputChange}
                    placeholder="Enter username"
                  />
                  {errors.username && <div className="error-message">{errors.username}</div>}
                </div>

                <div className="form-group">
                  <label htmlFor="password">
                    Password <span className="required">*</span>
                  </label>
                  <input
                    type="password"
                    id="password"
                    name="password"
                    value={formData.password}
                    onChange={handleInputChange}
                    placeholder="Enter password"
                  />
                  {errors.password && <div className="error-message">{errors.password}</div>}
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
                    onChange={handleInputChange}
                    placeholder="Confirm password"
                  />
                  {errors.confirmPassword && <div className="error-message">{errors.confirmPassword}</div>}
                </div>

                <div className="name-group">
                  <label>Your name</label>
                  <div className="name-inputs">
                    <input
                      type="text"
                      name="firstName"
                      value={formData.firstName}
                      onChange={handleInputChange}
                      placeholder="First"
                    />
                    <input
                      type="text"
                      name="lastName"
                      value={formData.lastName}
                      onChange={handleInputChange}
                      placeholder="Last"
                    />
                  </div>
                  {errors.name && <div className="error-message">{errors.name}</div>}
                </div>

                <div className="form-group">
                  <label htmlFor="email">Email address</label>
                  <input
                    type="email"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    placeholder="Enter email address"
                  />
                  {errors.email && <div className="error-message">{errors.email}</div>}
                </div>

                <div className="form-group">
                  <label htmlFor="contactNo">Contact no.</label>
                  <input
                    type="text"
                    id="contactNo"
                    name="contactNo"
                    value={formData.contactNo}
                    onChange={handleInputChange}
                    placeholder="Enter contact number"
                  />
                  {errors.contactNo && <div className="error-message">{errors.contactNo}</div>}
                </div>

                <div className="form-group">
                  <label htmlFor="birthday">Birthday</label>
                  <input
                    type="date"
                    id="birthday"
                    name="birthday"
                    value={formData.birthday}
                    onChange={handleInputChange}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="address">
                    Address <span className="required">*</span>
                  </label>
                  <input
                    type="text"
                    id="address"
                    name="address"
                    value={formData.address}
                    onChange={handleInputChange}
                    placeholder="Enter your address"
                  />
                  {errors.address && <div className="error-message">{errors.address}</div>}
                </div>

                <button className="next-button" onClick={handleNext}>
                  Next
                </button>
              </div>
            </div>
          </>
        ) : (
          <>
            {/* Step 2: Documents */}
            <div className="form-left-section">
              <div className="logo-container">
                <img src={logo || "/placeholder.svg"} alt="Tarabaho Logo" className="logo" />
              </div>
              <h2 className="form-title">Worker Sign-Up Form</h2>
              <p className="form-description">
                Use this form and get started quickly. Create an online account with a few clicks.
              </p>
            </div>

            <div className="form-right-section">
              <div className="right-content">
                <div className="form-group">
                  <label htmlFor="zipCode">
                    Zip Code <span className="required">*</span>
                  </label>
                  <input
                    type="text"
                    id="zipCode"
                    name="zipCode"
                    value={formData.zipCode}
                    onChange={handleInputChange}
                    placeholder="Enter zip code"
                  />
                  {errors.zipCode && <div className="error-message">{errors.zipCode}</div>}
                </div>

                <div className="form-group">
                  <label htmlFor="nbiClearance">
                    Original NBI Clearance (Expired ID not allowed) <span className="required">*</span>
                  </label>
                  <div className="file-input-container">
                    <input
                      type="file"
                      id="nbiClearance"
                      name="nbiClearance"
                      onChange={handleFileChange}
                      className="file-input"
                    />
                    <button
                      type="button"
                      className="choose-file-btn"
                      onClick={() => document.getElementById("nbiClearance").click()}
                    >
                      Choose File
                    </button>
                    <span className="file-name">
                      {formData.nbiClearance ? formData.nbiClearance.name : "No file chosen"}
                    </span>
                  </div>
                  {errors.nbiClearance && <div className="error-message">{errors.nbiClearance}</div>}
                </div>

                <div className="form-group">
                  <label htmlFor="nationalId">
                    Original National ID <span className="required">*</span>
                  </label>
                  <div className="file-input-container">
                    <input
                      type="file"
                      id="nationalId"
                      name="nationalId"
                      onChange={handleFileChange}
                      className="file-input"
                    />
                    <button
                      type="button"
                      className="choose-file-btn"
                      onClick={() => document.getElementById("nationalId").click()}
                    >
                      Choose File
                    </button>
                    <span className="file-name">
                      {formData.nationalId ? formData.nationalId.name : "No file chosen"}
                    </span>
                  </div>
                  {errors.nationalId && <div className="error-message">{errors.nationalId}</div>}
                </div>

                <div className="form-group">
                  <label htmlFor="picture">
                    2x2 picture of you <span className="required">*</span>
                  </label>
                  <div className="file-input-container">
                    <input
                      type="file"
                      id="picture"
                      name="picture"
                      onChange={handleFileChange}
                      accept="image/*"
                      className="file-input"
                    />
                    <button
                      type="button"
                      className="choose-file-btn"
                      onClick={() => document.getElementById("picture").click()}
                    >
                      Choose File
                    </button>
                    <span className="file-name">{formData.picture ? formData.picture.name : "No file chosen"}</span>
                  </div>
                  {errors.picture && <div className="error-message">{errors.picture}</div>}
                </div>

                <div className="form-group">
                  <label htmlFor="policeClearance">
                    Police Clearance <span className="required">*</span>
                  </label>
                  <div className="file-input-container">
                    <input
                      type="file"
                      id="policeClearance"
                      name="policeClearance"
                      onChange={handleFileChange}
                      className="file-input"
                    />
                    <button
                      type="button"
                      className="choose-file-btn"
                      onClick={() => document.getElementById("policeClearance").click()}
                    >
                      Choose File
                    </button>
                    <span className="file-name">
                      {formData.policeClearance ? formData.policeClearance.name : "No file chosen"}
                    </span>
                  </div>
                  {errors.policeClearance && <div className="error-message">{errors.policeClearance}</div>}
                </div>

                <button className="signup-button" onClick={handleSubmit}>
                  Sign Up
                </button>
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  )
}

export default RegisterTrabahador
