"use client"

import { useState } from "react"
import { useNavigate } from "react-router-dom"
import logo from "../assets/images/logowhite.png"
import "../styles/RegisterTrabahador.css"

const RegisterTrabahador = () => {
  const navigate = useNavigate()
  const [currentStep, setCurrentStep] = useState(1)
  const [isLoading, setIsLoading] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)

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
    picture: null, // 2x2 picture, will be used as profile picture
    certificates: [], // Array to store multiple certificates
    hourly: "", // Added hourly field
  })

  // Certificate state for dynamic form
  const [certificateForm, setCertificateForm] = useState({
    courseName: "",
    certificateNumber: "",
    issueDate: "",
    certificateFile: null,
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

  const handleCertificateInputChange = (e) => {
    const { name, value } = e.target
    setCertificateForm({
      ...certificateForm,
      [name]: value,
    })
  }

  const handleCertificateFileChange = (e) => {
    const { files } = e.target
    if (files && files[0]) {
      setCertificateForm({
        ...certificateForm,
        certificateFile: files[0],
      })
    }
  }

  const addCertificate = () => {
    const newErrors = {}
    if (!certificateForm.courseName.trim()) newErrors.courseName = "Course name is required"
    if (!certificateForm.certificateNumber.trim()) newErrors.certificateNumber = "Certificate number is required"
    if (!certificateForm.issueDate) newErrors.issueDate = "Issue date is required"
    if (!certificateForm.certificateFile) newErrors.certificateFile = "Certificate file is required"

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }

    setFormData({
      ...formData,
      certificates: [...formData.certificates, { ...certificateForm }],
    })

    // Reset certificate form
    setCertificateForm({
      courseName: "",
      certificateNumber: "",
      issueDate: "",
      certificateFile: null,
    })

    // Reset file input
    document.getElementById("certificateFile").value = null
    setErrors({})
  }

  const removeCertificate = (index) => {
    const updatedCertificates = formData.certificates.filter((_, i) => i !== index)
    setFormData({
      ...formData,
      certificates: updatedCertificates,
    })
  }

  const checkDuplicates = async () => {
    try {
      const workerData = {
        username: formData.username,
        email: formData.email,
        phoneNumber: formData.contactNo,
      }

      const response = await fetch("http://localhost:8080/api/worker/check-duplicates", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(workerData),
        credentials: "include",
      })

      if (!response.ok) {
        const errorText = await response.text()
        if (errorText.includes("Username already exists")) {
          return { isValid: false, field: "username", message: "Username already exists" }
        } else if (errorText.includes("Email already exists")) {
          return { isValid: false, field: "email", message: "Email already exists" }
        } else if (errorText.includes("Phone number already exists")) {
          return { isValid: false, field: "contactNo", message: "Phone number already exists" }
        }
        return { isValid: false, field: "general", message: errorText || "Failed to validate details" }
      }

      return { isValid: true, field: null, message: null }
    } catch (error) {
      return { isValid: false, field: "general", message: "Failed to connect to server: " + error.message }
    }
  }

  const validateStep1 = async () => {
    const newErrors = {}

    // Client-side validation
    if (!formData.username.trim()) newErrors.username = "Username is required"
    if (!formData.password) newErrors.password = "Password is required"
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match"
    }
    if (!formData.firstName.trim() || !formData.lastName.trim()) {
      newErrors.name = "Full name is required"
    }
    if (!formData.email.trim()) newErrors.email = "Email is required"
    else if (!formData.email.match(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/)) {
      newErrors.email = "Invalid email format"
    }
    if (!formData.contactNo.trim()) newErrors.contactNo = "Contact number is required"
    if (!formData.address.trim()) newErrors.address = "Address is required"
    if (!formData.hourly || formData.hourly <= 0) newErrors.hourly = "Hourly rate must be greater than 0"

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return false
    }

    // Server-side duplicate check
    setIsLoading(true)
    const duplicateCheck = await checkDuplicates()
    setIsLoading(false)

    if (!duplicateCheck.isValid) {
      setErrors({ [duplicateCheck.field]: duplicateCheck.message })
      return false
    }

    return true
  }

  const validateStep2 = () => {
    const newErrors = {}

    if (!formData.picture) newErrors.picture = "2x2 picture is required"
    if (formData.certificates.length === 0) newErrors.certificates = "At least one TESDA certificate is required"

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleNext = async () => {
    if (await validateStep1()) {
      setCurrentStep(2)
      window.scrollTo(0, 0)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (!validateStep2()) {
      return
    }

    setIsSubmitting(true)
    setErrors({})

    try {
      // Step 1: Register the worker
      const workerData = {
        username: formData.username,
        password: formData.password,
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        phoneNumber: formData.contactNo,
        birthday: formData.birthday,
        address: formData.address,
        hourly: parseFloat(formData.hourly), // Added hourly rate
      }

      console.log("Sending registration request with data:", workerData)

      const workerResponse = await fetch("http://localhost:8080/api/worker/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(workerData),
        credentials: "include",
      })

      if (!workerResponse.ok) {
        let errorText = await workerResponse.text().catch(() => "No error message available")
        try {
          const jsonError = JSON.parse(errorText)
          errorText = jsonError.message || jsonError || errorText
        } catch (e) {
          // Not JSON, use raw text
        }
        console.error(`Registration failed with status: ${workerResponse.status}, response: ${errorText}`)
        throw new Error(`Failed to register worker: ${errorText}`)
      }

      const worker = await workerResponse.json()
      const workerId = worker.id

      console.log("Worker registered successfully, ID:", workerId)

      // Step 2: Upload profile picture (2x2 picture)
      if (formData.picture) {
        const pictureFormData = new FormData()
        pictureFormData.append("file", formData.picture)

        const pictureResponse = await fetch(`http://localhost:8080/api/worker/${workerId}/upload-initial-picture`, {
          method: "POST",
          body: pictureFormData,
          credentials: "include",
        })

        if (!pictureResponse.ok) {
          let errorText = await pictureResponse.text().catch(() => "No error message available")
          try {
            const jsonError = JSON.parse(errorText)
            errorText = jsonError.message || jsonError || errorText
          } catch (e) {
            // Not JSON, use raw text
          }
          console.error(`Profile picture upload failed with status: ${pictureResponse.status}, response: ${errorText}`)
          throw new Error(`Failed to upload profile picture: ${errorText}`)
        }

        console.log("Profile picture uploaded successfully")
      }

      // Step 3: Add certificates
      for (const cert of formData.certificates) {
        const certFormData = new FormData()
        certFormData.append("courseName", cert.courseName)
        certFormData.append("certificateNumber", cert.certificateNumber)
        certFormData.append("issueDate", cert.issueDate)
        if (cert.certificateFile) {
          certFormData.append("certificateFile", cert.certificateFile)
        }

        const certResponse = await fetch(`http://localhost:8080/api/certificate/worker/${workerId}`, {
          method: "POST",
          body: certFormData,
          credentials: "include",
        })

        if (!certResponse.ok) {
          let errorText = await certResponse.text().catch(() => "No error message available")
          try {
            const jsonError = JSON.parse(errorText)
            errorText = jsonError.message || jsonError || errorText
          } catch (e) {
            // Not JSON, use raw text
          }
          console.error(`Certificate upload failed with status: ${certResponse.status}, response: ${errorText}`)
          throw new Error(`Failed to upload certificate: ${errorText}`)
        }

        console.log("Certificate uploaded successfully")
      }

      // Step 4: Redirect to login page
      console.log("Registration successful, redirecting to login page")
      setIsSubmitting(false)
      navigate("/signin")
    } catch (error) {
      console.error("Registration failed:", error)
      setErrors({ general: error.message || "Registration failed. Please try again." })
      setIsSubmitting(false)
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
              {errors.general && <div className="error-message">{errors.general}</div>}
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
                  <label htmlFor="email">
                    Email address <span className="required">*</span>
                  </label>
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
                  <label htmlFor="contactNo">
                    Contact no. <span className="required">*</span>
                  </label>
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

                <div className="form-group">
                  <label htmlFor="hourly">
                    Hourly Rate (in PHP) <span className="required">*</span>
                  </label>
                  <input
                    type="number"
                    id="hourly"
                    name="hourly"
                    value={formData.hourly}
                    onChange={handleInputChange}
                    placeholder="Enter hourly rate"
                    min="1"
                  />
                  {errors.hourly && <div className="error-message">{errors.hourly}</div>}
                </div>

                <button className="next-button" onClick={handleNext} disabled={isLoading}>
                  {isLoading ? "Checking..." : "Next"}
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
              {errors.general && <div className="error-message">{errors.general}</div>}
            </div>

            <div className="form-right-section">
              <div className="right-content">
                <div className="form-group">
                  <label htmlFor="picture">
                    2x2 picture of you (Profile Picture) <span className="required">*</span>
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

                {/* TESDA Certificates Section */}
                <div className="form-group">
                  <label>TESDA Certificates <span className="required">*</span></label>
                  <div className="certificate-inputs">
                    <div className="form-group">
                      <label htmlFor="courseName">Course Name</label>
                      <input
                        type="text"
                        id="courseName"
                        name="courseName"
                        value={certificateForm.courseName}
                        onChange={handleCertificateInputChange}
                        placeholder="Enter course name"
                      />
                      {errors.courseName && <div className="error-message">{errors.courseName}</div>}
                    </div>

                    <div className="form-group">
                      <label htmlFor="certificateNumber">Certificate Number</label>
                      <input
                        type="text"
                        id="certificateNumber"
                        name="certificateNumber"
                        value={certificateForm.certificateNumber}
                        onChange={handleCertificateInputChange}
                        placeholder="Enter certificate number"
                      />
                      {errors.certificateNumber && <div className="error-message">{errors.certificateNumber}</div>}
                    </div>

                    <div className="form-group">
                      <label htmlFor="issueDate">Issue Date</label>
                      <input
                        type="date"
                        id="issueDate"
                        name="issueDate"
                        value={certificateForm.issueDate}
                        onChange={handleCertificateInputChange}
                      />
                      {errors.issueDate && <div className="error-message">{errors.issueDate}</div>}
                    </div>

                    <div className="form-group">
                      <label htmlFor="certificateFile">Certificate File</label>
                      <div className="file-input-container">
                        <input
                          type="file"
                          id="certificateFile"
                          name="certificateFile"
                          onChange={handleCertificateFileChange}
                          accept="image/*,application/pdf"
                          className="file-input"
                        />
                        <button
                          type="button"
                          className="choose-file-btn"
                          onClick={() => document.getElementById("certificateFile").click()}
                        >
                          Choose File
                        </button>
                        <span className="file-name">
                          {certificateForm.certificateFile ? certificateForm.certificateFile.name : "No file chosen"}
                        </span>
                      </div>
                      {errors.certificateFile && <div className="error-message">{errors.certificateFile}</div>}
                    </div>

                    <button type="button" className="add-certificate-btn" onClick={addCertificate}>
                      Add Certificate
                    </button>
                  </div>

                  {/* Display added certificates */}
                  {formData.certificates.length > 0 && (
                    <div className="certificate-list">
                      <h4>Added Certificates:</h4>
                      {formData.certificates.map((cert, index) => (
                        <div key={index} className="certificate-item">
                          <p>
                            {cert.courseName} - {cert.certificateNumber} (Issued: {cert.issueDate})
                          </p>
                          <button
                            type="button"
                            className="remove-certificate-btn"
                            onClick={() => removeCertificate(index)}
                          >
                            Remove
                          </button>
                        </div>
                      ))}
                    </div>
                  )}
                  {errors.certificates && <div className="error-message">{errors.certificates}</div>}
                </div>

                <button className="signup-button" onClick={handleSubmit} disabled={isSubmitting}>
                  {isSubmitting ? "Submitting..." : "Sign Up"}
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