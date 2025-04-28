"use client"

import { useState, useEffect, useRef } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import TrabahadorNavbar from "../components/TrabahadorNavbar"
import TrabahadorLogoutConfirmation from "../components/TrabahadorLogoutConfirmation"
import Footer from "../components/Footer"
import "../styles/TrabahadorProfile.css"
import { FaStar, FaCertificate, FaPlus, FaTrash, FaEdit } from "react-icons/fa"

const TrabahadorProfile = () => {
  const navigate = useNavigate()
  const [worker, setWorker] = useState(null)
  const [isEditing, setIsEditing] = useState(false)
  const [formData, setFormData] = useState({
    email: "",
    address: "",
    birthday: "",
    biography: "",
    password: "",
  })
  const [selectedFile, setSelectedFile] = useState(null)
  const [profileImage, setProfileImage] = useState("/placeholder.svg")
  const [showLogoutModal, setShowLogoutModal] = useState(false)
  const [error, setError] = useState("")
  const [isAddingCertificate, setIsAddingCertificate] = useState(false)
  const [newCertificate, setNewCertificate] = useState({
    courseName: "",
    certificateNumber: "",
    issueDate: "",
    certificateFile: null,
  })
  const [editingCertificateId, setEditingCertificateId] = useState(null)
  const [selectedCertificateImage, setSelectedCertificateImage] = useState(null)
  const fileInputRef = useRef(null)
  const certificateFileInputRef = useRef(null)

  const BACKEND_URL = "http://localhost:8080"

  useEffect(() => {
    const fetchWorker = async () => {
      try {
        const username = localStorage.getItem("username")
        if (!username) {
          navigate("/signin")
          return
        }
        const response = await axios.get(`${BACKEND_URL}/api/worker/all`, {
          withCredentials: true,
        })
        const workerData = response.data.find((w) => w.username === username)
        if (workerData) {
          setWorker(workerData)
          setFormData({
            email: workerData.email || "",
            address: workerData.address || "",
            birthday: workerData.birthday || "",
            biography: workerData.biography || "",
            password: "",
          })
          setProfileImage(workerData.profilePicture || "/placeholder.svg")
        } else {
          setError("Worker not found.")
        }
      } catch (err) {
        console.error("Failed to fetch worker:", err)
        setError("Failed to load profile. Please try again.")
      }
    }
    fetchWorker()
  }, [navigate])

  const handleFileChange = async (e) => {
    const file = e.target.files[0]
    if (!file) {
      setError("No file selected.")
      return
    }
    setSelectedFile(file)

    const uploadData = new FormData()
    uploadData.append("file", file)

    try {
      const response = await axios.post(
        `${BACKEND_URL}/api/worker/${worker.id}/upload-picture`,
        uploadData,
        {
          withCredentials: true,
          headers: { "Content-Type": "multipart/form-data" },
        }
      )
      setWorker(response.data)
      setProfileImage(response.data.profilePicture || profileImage)
      setSelectedFile(null)
      setError("")
    } catch (err) {
      console.error("Failed to upload picture:", err)
      setError(err.response?.data || "Failed to upload picture. Please try again.")
    }
  }

  const handleImageClick = () => {
    fileInputRef.current.click()
  }

  const handleCertificateFileChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      setNewCertificate((prev) => ({ ...prev, certificateFile: file }))
    }
  }

  const handleCertificateImageClick = () => {
    certificateFileInputRef.current.click()
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleCertificateInputChange = (e) => {
    const { name, value } = e.target
    setNewCertificate((prev) => ({ ...prev, [name]: value }))
  }

  const handleEditToggle = () => {
    setIsEditing(!isEditing)
    setError("")
  }

  const handleSaveChanges = async () => {
    try {
      const updatedWorker = {
        ...worker,
        email: formData.email,
        address: formData.address,
        birthday: formData.birthday,
        biography: formData.biography,
        password: formData.password || undefined,
      }
      const response = await axios.put(
        `${BACKEND_URL}/api/worker/${worker.id}`,
        updatedWorker,
        { withCredentials: true }
      )
      setWorker(response.data)
      setFormData({
        email: response.data.email,
        address: response.data.address,
        birthday: response.data.birthday,
        biography: response.data.biography,
        password: "",
      })
      setIsEditing(false)
      setError("")
    } catch (err) {
      console.error("Failed to update profile:", err)
      setError(err.response?.data || "Failed to update profile. Please try again.")
    }
  }

  const handleAddCertificate = async () => {
    try {
      console.log("Adding certificate for worker ID:", worker.id)
      const certificateData = new FormData()
      certificateData.append("courseName", newCertificate.courseName)
      certificateData.append("certificateNumber", newCertificate.certificateNumber)
      certificateData.append("issueDate", newCertificate.issueDate)
      if (newCertificate.certificateFile) {
        certificateData.append("certificateFile", newCertificate.certificateFile)
      }

      const response = await axios.post(
        `${BACKEND_URL}/api/certificate/worker/${worker.id}`,
        certificateData,
        {
          withCredentials: true,
          headers: { "Content-Type": "multipart/form-data" },
        }
      )
      console.log("Certificate added:", response.data)
      setWorker((prev) => ({
        ...prev,
        certificates: [...(prev.certificates || []), response.data],
      }))
      setNewCertificate({
        courseName: "",
        certificateNumber: "",
        issueDate: "",
        certificateFile: null,
      })
      setIsAddingCertificate(false)
    } catch (err) {
      console.error("Failed to add certificate:", err)
      setError(err.response?.data || "Failed to add certificate. Please try again.")
    }
  }

  const handleEditCertificate = (certificate) => {
    setEditingCertificateId(certificate.id)
    setNewCertificate({
      courseName: certificate.courseName,
      certificateNumber: certificate.certificateNumber,
      issueDate: certificate.issueDate,
      certificateFile: null,
    })
  }

  const handleUpdateCertificate = async (certificateId) => {
    try {
      console.log("Updating certificate ID:", certificateId)
      const certificateData = new FormData()
      certificateData.append("courseName", newCertificate.courseName)
      certificateData.append("certificateNumber", newCertificate.certificateNumber)
      certificateData.append("issueDate", newCertificate.issueDate)
      certificateData.append("workerId", worker.id)
      if (newCertificate.certificateFile) {
        certificateData.append("certificateFile", newCertificate.certificateFile)
      }

      const response = await axios.put(
        `${BACKEND_URL}/api/certificate/${certificateId}`,
        certificateData,
        {
          withCredentials: true,
          headers: { "Content-Type": "multipart/form-data" },
        }
      )
      console.log("Certificate updated:", response.data)
      setWorker((prev) => ({
        ...prev,
        certificates: prev.certificates.map((cert) =>
          cert.id === certificateId ? response.data : cert
        ),
      }))
      setNewCertificate({
        courseName: "",
        certificateNumber: "",
        issueDate: "",
        certificateFile: null,
      })
      setEditingCertificateId(null)
      setError("")
    } catch (err) {
      console.error("Failed to update certificate:", err)
      if (err.response?.status === 401) {
        setError("Session expired. Please sign in again.")
        navigate("/signin")
      } else {
        setError(err.response?.data || "Failed to update certificate. Please try again.")
      }
    }
  }

  const handleDeleteCertificate = async (certificateId) => {
    try {
      console.log("Deleting certificate ID:", certificateId)
      await axios.delete(`${BACKEND_URL}/api/certificate/${certificateId}`, {
        withCredentials: true,
        headers: { "Content-Type": "multipart/form-data" },
      })
      setWorker((prev) => ({
        ...prev,
        certificates: prev.certificates.filter((cert) => cert.id !== certificateId),
      }))
      setError("")
    } catch (err) {
      console.error("Failed to delete certificate:", err)
      if (err.response?.status === 401) {
        setError("Session expired. Please sign in again.")
        navigate("/signin")
      } else {
        setError(err.response?.data || "Failed to delete certificate. Please try again.")
      }
    }
  }

  const handleCertificateClick = (certificate) => {
    if (certificate.certificateFilePath) {
      setSelectedCertificateImage(certificate.certificateFilePath)
    }
  }

  const handleCloseModal = () => {
    setSelectedCertificateImage(null)
  }

  const handleLogout = () => {
    setShowLogoutModal(true)
  }

  const confirmLogout = async () => {
    try {
      await axios.post(`${BACKEND_URL}/api/user/logout`, {}, { withCredentials: true })
      localStorage.removeItem("isLoggedIn")
      localStorage.removeItem("userType")
      localStorage.removeItem("username")
      setShowLogoutModal(false)
      navigate("/signin")
    } catch (err) {
      console.error("Logout failed:", err)
      setError("Logout failed. Please try again.")
    }
  }

  const cancelLogout = () => {
    setShowLogoutModal(false)
  }

  return (
    <div className="trabahador-profile-page">
      <TrabahadorNavbar activePage="profile" />

      <div className="trabahador-profile-content">
        <div className="trabahador-profile-header">
          <h1 className="trabahador-profile-heading">TRABAHADOR PROFILE</h1>
          <button className="logout-button" onClick={handleLogout}>
            <svg
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M9 21H5C4.46957 21 3.96086 20.7893 3.58579 20.4142C3.21071 20.0391 3 19.5304 3 19V5C3 4.46957 3.21071 3.96086 3.58579 3.58579C3.96086 3.21071 4.46957 3 5 3H9"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              />
              <path
                d="M16 17L21 12L16 7"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              />
              <path
                d="M21 12H9"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              />
            </svg>
            LOG OUT
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="profile-container">
          <div className="profile-card">
            <div className="profile-top-section">
              <div className="profile-image-section">
                <img
                  src={profileImage}
                  alt="Worker Profile"
                  className="profile-image"
                  onClick={handleImageClick}
                />
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleFileChange}
                  className="file-input"
                  ref={fileInputRef}
                  style={{ display: "none" }}
                />
              </div>

              <div className="profile-credentials-section">
                <h3>{worker ? `${worker.firstName} ${worker.lastName}` : "Loading..."}</h3>
                <p className="worker-bio">{worker?.biography || "N/A"}</p>
                <div className="worker-rating">
                  {[...Array(5)].map((_, i) => (
                    <FaStar key={i} className="star-icon" />
                  ))}
                </div>
                <div className="worker-rate">₱63.00/hour</div>
                <div className="worker-skills">
                  <span className="skill-tag">CLEANING</span>
                  <span className="skill-tag">GARDENING</span>
                  <span className="skill-tag">TUTORING</span>
                </div>
              </div>
            </div>

            <div className="edit-profile-container">
              {isEditing ? (
                <div className="edit-form">
                  <div className="form-group">
                    <label className="credential-label">Email Address:</label>
                    <input
                      type="email"
                      name="email"
                      value={formData.email}
                      onChange={handleInputChange}
                      className="form-input"
                      placeholder="Enter your email address"
                    />
                  </div>
                  <div className="form-group">
                    <label className="credential-label">Address:</label>
                    <input
                      type="text"
                      name="address"
                      value={formData.address}
                      onChange={handleInputChange}
                      className="form-input"
                      placeholder="Enter your address"
                    />
                  </div>
                  <div className="form-group">
                    <label className="credential-label">Birthday:</label>
                    <input
                      type="date"
                      name="birthday"
                      value={formData.birthday}
                      onChange={handleInputChange}
                      className="form-input"
                      placeholder="Select your birthday"
                    />
                  </div>
                  <div className="form-group">
                    <label className="credential-label">Biography:</label>
                    <textarea
                      name="biography"
                      value={formData.biography}
                      onChange={handleInputChange}
                      className="form-input"
                      rows="3"
                      placeholder="Write a brief biography about yourself"
                    />
                  </div>
                  <div className="form-group">
                    <label className="credential-label">Password:</label>
                    <input
                      type="password"
                      name="password"
                      value={formData.password}
                      onChange={handleInputChange}
                      className="form-input"
                      placeholder="Enter new password (leave blank to keep current)"
                    />
                  </div>
                  <div className="form-actions">
                    <button className="save-btn" onClick={handleSaveChanges}>
                      Save Changes
                    </button>
                    <button className="cancel-btn" onClick={handleEditToggle}>
                      Cancel
                    </button>
                  </div>
                </div>
              ) : (
                <button className="edit-profile-btn" onClick={handleEditToggle}>
                  <FaEdit /> EDIT PROFILE
                </button>
              )}
            </div>

            <div className="profile-details-section">
              <div className="detail-row">
                <div className="detail-label">Full Name:</div>
                <div className="detail-value">
                  {worker ? `${worker.firstName} ${worker.lastName}` : "Loading..."}
                </div>
              </div>
              <div className="detail-row">
                <div className="detail-label">Email:</div>
                <div className="detail-value">{worker?.email || "N/A"}</div>
              </div>
              <div className="detail-row">
                <div className="detail-label">Contact no.:</div>
                <div className="detail-value">{worker?.phoneNumber || "N/A"}</div>
              </div>
              <div className="detail-row">
                <div className="detail-label">Address:</div>
                <div className="detail-value">{worker?.address || "N/A"}</div>
              </div>
              <div className="detail-row">
                <div className="detail-label">Birthday:</div>
                <div className="detail-value">{worker?.birthday || "N/A"}</div>
              </div>
            </div>

            <div className="certificates-section">
              <h3 className="certificates-title">
                CERTIFICATES
                <button
                  className="add-certificate-btn"
                  onClick={() => setIsAddingCertificate(true)}
                >
                  <FaPlus /> Add Certificate
                </button>
              </h3>

              {isAddingCertificate && (
                <div className="certificate-form">
                  <div className="form-group">
                    <label className="credential-label">Course Name:</label>
                    <input
                      type="text"
                      name="courseName"
                      value={newCertificate.courseName}
                      onChange={handleCertificateInputChange}
                      className="form-input"
                      placeholder="Enter course name"
                    />
                  </div>
                  <div className="form-group">
                    <label className="credential-label">Certificate Number:</label>
                    <input
                      type="text"
                      name="certificateNumber"
                      value={newCertificate.certificateNumber}
                      onChange={handleCertificateInputChange}
                      className="form-input"
                      placeholder="Enter certificate number"
                    />
                  </div>
                  <div className="form-group">
                    <label className="credential-label">Issue Date:</label>
                    <input
                      type="date"
                      name="issueDate"
                      value={newCertificate.issueDate}
                      onChange={handleCertificateInputChange}
                      className="form-input"
                      placeholder="Select issue date"
                    />
                  </div>
                  <div className="form-group">
                    <label className="credential-label">Certificate File:</label>
                    <div className="certificate-image-upload">
                      <img
                        src={
                          newCertificate.certificateFile
                            ? URL.createObjectURL(newCertificate.certificateFile)
                            : "/placeholder.svg"
                        }
                        alt="Certificate Preview"
                        className="certificate-preview-image"
                        onClick={handleCertificateImageClick}
                      />
                      <input
                        type="file"
                        accept="image/*"
                        onChange={handleCertificateFileChange}
                        className="file-input"
                        ref={certificateFileInputRef}
                        style={{ display: "none" }}
                      />
                    </div>
                  </div>
                  <div className="form-actions">
                    <button className="save-btn" onClick={handleAddCertificate}>
                      Add
                    </button>
                    <button
                      className="cancel-btn"
                      onClick={() => setIsAddingCertificate(false)}
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              )}

              {worker?.certificates?.map((certificate) => (
                <div key={certificate.id} className="certificate-item">
                  {editingCertificateId === certificate.id ? (
                    <div className="certificate-form">
                      <div className="form-group">
                        <label className="credential-label">Course Name:</label>
                        <input
                          type="text"
                          name="courseName"
                          value={newCertificate.courseName}
                          onChange={handleCertificateInputChange}
                          className="form-input"
                          placeholder="Enter course name"
                        />
                      </div>
                      <div className="form-group">
                        <label className="credential-label">Certificate Number:</label>
                        <input
                          type="text"
                          name="certificateNumber"
                          value={newCertificate.certificateNumber}
                          onChange={handleCertificateInputChange}
                          className="form-input"
                          placeholder="Enter certificate number"
                        />
                      </div>
                      <div className="form-group">
                        <label className="credential-label">Issue Date:</label>
                        <input
                          type="date"
                          name="issueDate"
                          value={newCertificate.issueDate}
                          onChange={handleCertificateInputChange}
                          className="form-input"
                          placeholder="Select issue date"
                        />
                      </div>
                      <div className="form-group">
                        <label className="credential-label">Certificate File:</label>
                        <div className="certificate-image-upload">
                          <img
                            src={
                              newCertificate.certificateFile
                                ? URL.createObjectURL(newCertificate.certificateFile)
                                : certificate.certificateFilePath || "/placeholder.svg"
                            }
                            alt="Certificate Preview"
                            className="certificate-preview-image"
                            onClick={handleCertificateImageClick}
                          />
                          <input
                            type="file"
                            accept="image/*"
                            onChange={handleCertificateFileChange}
                            className="file-input"
                            ref={certificateFileInputRef}
                            style={{ display: "none" }}
                          />
                        </div>
                      </div>
                      <div className="form-actions">
                        <button
                          className="save-btn"
                          onClick={() => handleUpdateCertificate(certificate.id)}
                        >
                          Save
                        </button>
                        <button
                          className="cancel-btn"
                          onClick={() => setEditingCertificateId(null)}
                        >
                          Cancel
                        </button>
                      </div>
                    </div>
                  ) : (
                    <>
                      <div
                        className="certificate-details"
                        onClick={() => handleCertificateClick(certificate)}
                      >
                        <FaCertificate className="certificate-icon" />
                        <div>
                          <h4>{certificate.courseName}</h4>
                          <p>Certificate Number: {certificate.certificateNumber}</p>
                          <p className="certificate-date">
                            Issued: {certificate.issueDate}
                          </p>
                        </div>
                      </div>
                      <div className="certificate-actions">
                        <button
                          className="edit-btn"
                          onClick={() => handleEditCertificate(certificate)}
                        >
                          Edit
                        </button>
                        <button
                          className="delete-btn"
                          onClick={() => handleDeleteCertificate(certificate.id)}
                        >
                          <FaTrash />
                        </button>
                      </div>
                    </>
                  )}
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="tarabaho-watermark">
          <span className="watermark-text">TARABAHO</span>
          <span className="watermark-subtext">TARA! TRABAHO</span>
        </div>
      </div>

      {showLogoutModal && (
        <TrabahadorLogoutConfirmation onConfirm={confirmLogout} onCancel={cancelLogout} />
      )}

      {selectedCertificateImage && (
        <div className="certificate-modal">
          <div className="certificate-modal-content">
            <span className="certificate-modal-close" onClick={handleCloseModal}>
              ×
            </span>
            <img
              src={selectedCertificateImage}
              alt="Certificate"
              className="certificate-modal-image"
            />
          </div>
        </div>
      )}

      <Footer />
    </div>
  )
}

export default TrabahadorProfile