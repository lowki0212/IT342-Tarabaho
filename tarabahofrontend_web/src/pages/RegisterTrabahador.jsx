"use client"

import { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import logo from "../assets/images/logowhite.png"
import styles from "../styles/register-trabahador.module.css"

const RegisterTrabahador = () => {
  const navigate = useNavigate()
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

  const [errors, setErrors] = useState({})
  const [step, setStep] = useState(1)
  const [isLoading, setIsLoading] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [passwordStrength, setPasswordStrength] = useState(0)
  const [showPassword, setShowPassword] = useState(false)

  // Initialize progress bar on component mount
  useEffect(() => {
    const progressBar = document.querySelector(`.${styles.formProgressBar}`)
    if (progressBar) {
      progressBar.style.width = step === 1 ? "50%" : "100%"
    }
  }, [step])

  const validateEmail = (email) => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return re.test(email)
  }

  const validatePassword = (password) => {
    let strength = 0
    if (password.length >= 8) strength += 1
    if (/[A-Z]/.test(password)) strength += 1
    if (/[0-9]/.test(password)) strength += 1
    if (/[^A-Za-z0-9]/.test(password)) strength += 1

    setPasswordStrength(strength)
    return strength >= 3
  }

  const handleBack = () => {
    if (step === 1) {
      navigate("/register")
    } else {
      setStep(1)
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

    // Update password strength when password changes
    if (name === "password") {
      validatePassword(value)
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
    else if (formData.password.length < 6) newErrors.password = "Password must be at least 6 characters"
    
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match"
    }
    if (!formData.firstName.trim() || !formData.lastName.trim()) {
      newErrors.name = "Full name is required"
    }
    if (!formData.email.trim()) newErrors.email = "Email is required"
    else if (!validateEmail(formData.email)) newErrors.email = "Invalid email format"
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

  const handleNextStep = async (e) => {
    e.preventDefault()
    console.log("Next button clicked")

    if (await validateStep1()) {
      setStep(2)
      // Update progress bar
      try {
        const progressBar = document.querySelector(`.${styles.formProgressBar}`)
        if (progressBar) {
          progressBar.style.width = "100%"
        }
      } catch (error) {
        console.error("Error updating progress bar:", error)
      }
      window.scrollTo(0, 0)
    }
  }

  const handlePrevStep = () => {
    console.log("Back button clicked")
    setStep(1)
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
        hourly: Number.parseFloat(formData.hourly),
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

      // Show success message
      const successMessage = document.querySelector(`.${styles.successMessage}`)
      if (successMessage) {
        successMessage.classList.add(styles.show)
      }

      // Redirect after showing success message
      setTimeout(() => {
        navigate("/signin")
      }, 2000)
    } catch (error) {
      console.error("Registration failed:", error)
      setErrors({ general: error.message || "Registration failed. Please try again." })
      setIsSubmitting(false)
    }
  }

  const getPasswordStrengthText = () => {
    switch (passwordStrength) {
      case 0:
        return "Very weak"
      case 1:
        return "Weak"
      case 2:
        return "Medium"
      case 3:
        return "Strong"
      case 4:
        return "Very strong"
      default:
        return ""
    }
  }

  const getPasswordStrengthColor = () => {
    switch (passwordStrength) {
      case 0:
        return "#ff4d4d"
      case 1:
        return "#ff9933"
      case 2:
        return "#ffcc00"
      case 3:
        return "#99cc33"
      case 4:
        return "#00cc66"
      default:
        return "#ccc"
    }
  }

  return (
    <div className={styles.registerTrabahadorContainer}>
      <button className={styles.backButton} onClick={handleBack} aria-label="Go back">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M15 19L8 12L15 5" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
      </button>

      <div className={styles.successMessage}>
        <div className={styles.successIcon}>✓</div>
        <h3>Registration Successful!</h3>
        <p>Redirecting to login page...</p>
      </div>

      <form onSubmit={handleSubmit} className={styles.registerTrabahadorForm}>
        <div className={styles.formProgress}>
          <div className={styles.formProgressBar}></div>
        </div>
        <div className={styles.formSteps}>
          <div className={`${styles.formStep} ${step >= 1 ? styles.active : ""} ${step > 1 ? styles.completed : ""}`}>
            <div className={styles.stepNumber}>1</div>
            <span className={styles.stepLabel}>Account</span>
          </div>
          <div className={`${styles.formStep} ${step >= 2 ? styles.active : ""}`}>
            <div className={styles.stepNumber}>2</div>
            <span className={styles.stepLabel}>Documents</span>
          </div>
        </div>

        <div className={styles.formLeftSection}>
          <div className={styles.logoContainer}>
            <img src={logo || "/placeholder.svg"} alt="Tarabaho Logo" className={styles.logo} />
          </div>

          <div className={styles.leftContent}>
            <h2 className={styles.formTitle}>Create Worker Account</h2>
            <p className={styles.formDescription}>
              Join Tarabaho today and connect with opportunities that match your skills and schedule.
            </p>

            {errors.general && <div className={`${styles.errorMessage} ${styles.general}`}>{errors.general}</div>}

            {step === 1 && (
              <>
                <div className={styles.formGroup}>
                  <label htmlFor="username">
                    Username <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.inputWithIcon}>
                    <input
                      type="text"
                      id="username"
                      name="username"
                      value={formData.username}
                      onChange={handleInputChange}
                      className={errors.username ? styles.error : ""}
                      placeholder="Choose a username"
                    />
                  </div>
                  {errors.username && <div className={styles.errorText}>{errors.username}</div>}
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="password">
                    Password <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.inputWithIcon}>
                    <input
                      type={showPassword ? "text" : "password"}
                      id="password"
                      name="password"
                      value={formData.password}
                      onChange={handleInputChange}
                      className={errors.password ? styles.error : ""}
                      placeholder="Create a strong password"
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className={styles.togglePassword}
                      tabIndex="0"
                    >
                      {showPassword ? "Hide" : "Show"}
                    </button>
                  </div>
                  {errors.password && <div className={styles.errorText}>{errors.password}</div>}

                  {formData.password && (
                    <div className={styles.passwordStrength}>
                      <div className={styles.strengthBars}>
                        <div
                          className={styles.strengthBar}
                          style={{ backgroundColor: passwordStrength >= 1 ? getPasswordStrengthColor() : "" }}
                        ></div>
                        <div
                          className={styles.strengthBar}
                          style={{ backgroundColor: passwordStrength >= 2 ? getPasswordStrengthColor() : "" }}
                        ></div>
                        <div
                          className={styles.strengthBar}
                          style={{ backgroundColor: passwordStrength >= 3 ? getPasswordStrengthColor() : "" }}
                        ></div>
                        <div
                          className={styles.strengthBar}
                          style={{ backgroundColor: passwordStrength >= 4 ? getPasswordStrengthColor() : "" }}
                        ></div>
                      </div>
                      <span className={styles.strengthText} style={{ color: getPasswordStrengthColor() }}>
                        {getPasswordStrengthText()}
                      </span>
                    </div>
                  )}
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="confirmPassword">
                    Confirm Password <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.inputWithIcon}>
                    <input
                      type="password"
                      id="confirmPassword"
                      name="confirmPassword"
                      value={formData.confirmPassword}
                      onChange={handleInputChange}
                      className={errors.confirmPassword ? styles.error : ""}
                      placeholder="Confirm your password"
                    />
                  </div>
                  {errors.confirmPassword && <div className={styles.errorText}>{errors.confirmPassword}</div>}
                </div>
              </>
            )}

            {step === 2 && (
              <div className={styles.formNavigation}>
                <button type="button" className={styles.prevButton} onClick={handlePrevStep}>
                  Back
                </button>
              </div>
            )}
          </div>
        </div>

        <div className={styles.formRightSection}>
          <div className={styles.rightContent}>
            {step === 1 && (
              <>
                <h3 className={styles.sectionTitle}>Personal Information</h3>

                <div className={styles.nameGroup}>
                  <label>
                    Your name <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.nameInputs}>
                    <div className={styles.inputWrapper}>
                      <input
                        type="text"
                        name="firstName"
                        placeholder="First"
                        value={formData.firstName}
                        onChange={handleInputChange}
                        className={errors.firstName ? styles.error : ""}
                      />
                      {errors.firstName && <div className={styles.errorText}>{errors.firstName}</div>}
                    </div>
                    <div className={styles.inputWrapper}>
                      <input
                        type="text"
                        name="lastName"
                        placeholder="Last"
                        value={formData.lastName}
                        onChange={handleInputChange}
                        className={errors.lastName ? styles.error : ""}
                      />
                      {errors.lastName && <div className={styles.errorText}>{errors.lastName}</div>}
                    </div>
                  </div>
                  {errors.name && <div className={styles.errorText}>{errors.name}</div>}
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="email">
                    Email address <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.inputWithIcon}>
                    <input
                      type="email"
                      id="email"
                      name="email"
                      value={formData.email}
                      onChange={handleInputChange}
                      className={errors.email ? styles.error : ""}
                      placeholder="your.email@example.com"
                    />
                  </div>
                  {errors.email && <div className={styles.errorText}>{errors.email}</div>}
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="contactNo">
                    Contact no. <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.inputWithIcon}>
                    <input
                      type="tel"
                      id="contactNo"
                      name="contactNo"
                      value={formData.contactNo}
                      onChange={handleInputChange}
                      className={errors.contactNo ? styles.error : ""}
                      placeholder="e.g., +63 912 345 6789"
                    />
                  </div>
                  {errors.contactNo && <div className={styles.errorText}>{errors.contactNo}</div>}
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="birthday">
                    Birthday <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.inputWithIcon}>
                    <input
                      type="date"
                      id="birthday"
                      name="birthday"
                      value={formData.birthday}
                      onChange={handleInputChange}
                      className={errors.birthday ? styles.error : ""}
                    />
                  </div>
                  {errors.birthday && <div className={styles.errorText}>{errors.birthday}</div>}
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="address">
                    Address <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.inputWithIcon}>
                    <input
                      type="text"
                      id="address"
                      name="address"
                      value={formData.address}
                      onChange={handleInputChange}
                      className={errors.address ? styles.error : ""}
                      placeholder="Your full address"
                    />
                  </div>
                  {errors.address && <div className={styles.errorText}>{errors.address}</div>}
                </div>

                <div className={styles.formGroup}>
                  <label htmlFor="hourly">
                    Hourly Rate (in PHP) <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.inputWithIcon}>
                    <input
                      type="number"
                      id="hourly"
                      name="hourly"
                      value={formData.hourly}
                      onChange={handleInputChange}
                      className={errors.hourly ? styles.error : ""}
                      placeholder="Enter hourly rate"
                      min="1"
                    />
                  </div>
                  {errors.hourly && <div className={styles.errorText}>{errors.hourly}</div>}
                </div>
                <button type="button" className={styles.nextButton} onClick={handleNextStep}>
                  Next
                </button>
              </>
            )}

            {step === 2 && (
              <>
                <h3 className={styles.sectionTitle}>Documents</h3>

                {/* 2x2 Picture */}
                <div className={styles.formGroup}>
                  <label htmlFor="picture">
                    2x2 picture of you (Profile Picture) <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.fileInputContainer}>
                    <input
                      type="file"
                      id="picture"
                      name="picture"
                      onChange={handleFileChange}
                      accept="image/*"
                      className={styles.fileInput}
                    />
                    <button
                      type="button"
                      className={styles.chooseFileBtn}
                      onClick={() => document.getElementById("picture").click()}
                    >
                      Choose File
                    </button>
                    <span className={styles.fileName}>
                      {formData.picture ? formData.picture.name : "No file chosen"}
                    </span>
                  </div>
                  {errors.picture && <div className={styles.errorMessage}>{errors.picture}</div>}
                </div>

                {/* TESDA Certificates */}
                <div className={styles.formGroup}>
                  <label>
                    TESDA Certificates <span className={styles.required}>*</span>
                  </label>
                  <div className={styles.certificateInputs}>
                    <div className={styles.formGroup}>
                      <label htmlFor="courseName">Course Name</label>
                      <input
                        type="text"
                        id="courseName"
                        name="courseName"
                        value={certificateForm.courseName}
                        onChange={handleCertificateInputChange}
                        placeholder="Enter course name"
                      />
                      {errors.courseName && <div className={styles.errorMessage}>{errors.courseName}</div>}
                    </div>

                    <div className={styles.formGroup}>
                      <label htmlFor="certificateNumber">Certificate Number</label>
                      <input
                        type="text"
                        id="certificateNumber"
                        name="certificateNumber"
                        value={certificateForm.certificateNumber}
                        onChange={handleCertificateInputChange}
                        placeholder="Enter certificate number"
                      />
                      {errors.certificateNumber && (
                        <div className={styles.errorMessage}>{errors.certificateNumber}</div>
                      )}
                    </div>

                    <div className={styles.formGroup}>
                      <label htmlFor="issueDate">Issue Date</label>
                      <input
                        type="date"
                        id="issueDate"
                        name="issueDate"
                        value={certificateForm.issueDate}
                        onChange={handleCertificateInputChange}
                      />
                      {errors.issueDate && <div className={styles.errorMessage}>{errors.issueDate}</div>}
                    </div>

                    <div className={styles.formGroup}>
                      <label htmlFor="certificateFile">Certificate File</label>
                      <div className={styles.fileInputContainer}>
                        <input
                          type="file"
                          id="certificateFile"
                          name="certificateFile"
                          onChange={handleCertificateFileChange}
                          accept="image/*,application/pdf"
                          className={styles.fileInput}
                        />
                        <button
                          type="button"
                          className={styles.chooseFileBtn}
                          onClick={() => document.getElementById("certificateFile").click()}
                        >
                          Choose File
                        </button>
                        <span className={styles.fileName}>
                          {certificateForm.certificateFile
                            ? certificateForm.certificateFile.name
                            : "No file chosen"}
                        </span>
                      </div>
                      {errors.certificateFile && (
                        <div className={styles.errorMessage}>{errors.certificateFile}</div>
                      )}
                    </div>

                    <button type="button" className={styles.addCertificateBtn} onClick={addCertificate}>
                      Add Certificate
                    </button>
                  </div>

                  {/* Display added certificates */}
                  {formData.certificates.length > 0 && (
                    <div className={styles.certificateList}>
                      <h4>Added Certificates:</h4>
                      {formData.certificates.map((cert, index) => (
                        <div key={index} className={styles.certificateItem}>
                          <p>
                            {cert.courseName} - {cert.certificateNumber} (Issued: {cert.issueDate})
                          </p>
                          <button
                            type="button"
                            className={styles.removeCertificateBtn}
                            onClick={() => removeCertificate(index)}
                          >
                            Remove
                          </button>
                        </div>
                      ))}
                    </div>
                  )}
                  {errors.certificates && <div className={styles.errorMessage}>{errors.certificates}</div>}
                </div>

                <div className={styles.formNavigation}>
                  <button type="button" className={styles.prevButton} onClick={handlePrevStep}>
                    Back
                  </button>
                  <button type="submit" className={styles.signupButton} disabled={isSubmitting}>
                    {isSubmitting ? "Submitting..." : "Sign Up"}
                  </button>
                </div>
              </>
            )}
          </div>
        </div>
      </form>
    </div>
  )
}

export default RegisterTrabahador