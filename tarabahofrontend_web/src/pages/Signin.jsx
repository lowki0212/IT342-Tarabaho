"use client"

import { useState } from "react"
import { useNavigate, Link } from "react-router-dom"
import axios from "axios"
import logo from "../assets/images/logowhite.png"
import "../styles/signin.css"

const SignIn = () => {
  const [loginType, setLoginType] = useState("user") // Default to user login

  // Separate state for user and trabahador credentials
  const [userCredentials, setUserCredentials] = useState({ username: "", password: "" })
  const [trabahadorCredentials, setTrabahadorCredentials] = useState({ username: "", password: "" })

  const [error, setError] = useState("")
  const navigate = useNavigate()

  const handleUserLogin = async (e) => {
    e.preventDefault()
    setError("")

    const payload = userCredentials
    console.log("User Login Payload:", payload)

    try {
      const res = await axios.post("http://localhost:8080/api/user/token", payload, {
        withCredentials: true,
      })

      console.log("User login successful, token set in cookie")
      alert("Logged in as User successfully!")

      // Store user info in localStorage
      localStorage.setItem("isLoggedIn", "true")
      localStorage.setItem("userType", "user")
      localStorage.setItem("username", userCredentials.username)

      // Redirect to user dashboard
      navigate("/user-browse")
    } catch (err) {
      setError("User Login Error: " + (err.response?.data || "Invalid user credentials"))
    }
  }

  const handleTrabahadorLogin = async (e) => {
    e.preventDefault()
    setError("")

    const payload = trabahadorCredentials
    console.log("Trabahador Login Payload:", payload)

    try {
      const res = await axios.post(
        "http://localhost:8080/api/trabahador/token", // Different endpoint for trabahador
        payload,
        {
          withCredentials: true,
        },
      )

      console.log("Trabahador login successful, token set in cookie")
      alert("Logged in as Trabahador successfully!")

      // Store trabahador info in localStorage
      localStorage.setItem("isLoggedIn", "true")
      localStorage.setItem("userType", "trabahador")
      localStorage.setItem("username", trabahadorCredentials.username)

      // Redirect to trabahador dashboard
      navigate("/trabahador-homepage")
    } catch (err) {
      setError("Trabahador Login Error: " + (err.response?.data || "Invalid trabahador credentials"))
    }
  }

  const handleGoogleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google"
  }

  const handleBack = () => {
    navigate("/")
  }

  const handleAdminLogin = () => {
    navigate("/admin-login")
  }

  const handleUserInputChange = (e) => {
    const { name, value } = e.target
    setUserCredentials((prev) => ({ ...prev, [name]: value }))
  }

  const handleTrabahadorInputChange = (e) => {
    const { name, value } = e.target
    setTrabahadorCredentials((prev) => ({ ...prev, [name]: value }))
  }

  return (
    <div className="signin-page">
      <div className="signin-container">
        <button className="back-button" onClick={handleBack}>
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M15 19L8 12L15 5" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
        </button>

        <div className="signin-content">
          <div className="logo-container">
            <img src={logo || "/placeholder.svg"} alt="Tarabaho Logo" className="logo" />
          </div>

          <div className="login-heading">SIGN IN</div>

          <div className="login-type-selector">
            <button
              className={`login-type-btn ${loginType === "user" ? "active" : ""}`}
              onClick={() => setLoginType("user")}
            >
              User
            </button>
            <button
              className={`login-type-btn ${loginType === "trabahador" ? "active" : ""}`}
              onClick={() => setLoginType("trabahador")}
            >
              Trabahador
            </button>
          </div>

          <div className="form-overlay">
            {error && <div className="error-message">{error}</div>}

            {loginType === "user" ? (
              <form onSubmit={handleUserLogin} className="login-form">
                <div className="input-group">
                  <svg
                    className="input-icon"
                    width="20"
                    height="20"
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21"
                      stroke="white"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                    <path
                      d="M12 11C14.2091 11 16 9.20914 16 7C16 4.79086 14.2091 3 12 3C9.79086 3 8 4.79086 8 7C8 9.20914 9.79086 11 12 11Z"
                      stroke="white"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                  </svg>
                  <input
                    type="text"
                    name="username"
                    placeholder="Username"
                    value={userCredentials.username}
                    onChange={handleUserInputChange}
                    required
                  />
                </div>

                <div className="input-group">
                  <svg
                    className="input-icon"
                    width="20"
                    height="20"
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <rect
                      x="3"
                      y="11"
                      width="18"
                      height="11"
                      rx="2"
                      stroke="white"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                    <path
                      d="M7 11V7C7 5.67392 7.52678 4.40215 8.46447 3.46447C9.40215 2.52678 10.6739 2 12 2C13.3261 2 14.5979 2.52678 15.5355 3.46447C16.4732 4.40215 17 5.67392 17 7V11"
                      stroke="white"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                    <circle cx="12" cy="16" r="1" fill="white" />
                  </svg>
                  <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={userCredentials.password}
                    onChange={handleUserInputChange}
                    required
                  />
                </div>

                <div className="links-container">
                  <Link to="/register-user" className="auth-link">
                    Register as User
                  </Link>
                  <Link to="/forgot-password" className="auth-link">
                    Forgot Password?
                  </Link>
                </div>

                <button type="submit" className="login-button">
                  Login as User
                </button>

                {/* Google Login Button */}
                <button type="button" onClick={handleGoogleLogin} className="google-login-button">
                  Continue with Google
                </button>
              </form>
            ) : (
              <form onSubmit={handleTrabahadorLogin} className="login-form">
                <div className="input-group">
                  <svg
                    className="input-icon"
                    width="20"
                    height="20"
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21"
                      stroke="white"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                    <path
                      d="M12 11C14.2091 11 16 9.20914 16 7C16 4.79086 14.2091 3 12 3C9.79086 3 8 4.79086 8 7C8 9.20914 9.79086 11 12 11Z"
                      stroke="white"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                  </svg>
                  <input
                    type="text"
                    name="username"
                    placeholder="Trabahador Username"
                    value={trabahadorCredentials.username}
                    onChange={handleTrabahadorInputChange}
                    required
                  />
                </div>

                <div className="input-group">
                  <svg
                    className="input-icon"
                    width="20"
                    height="20"
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <rect
                      x="3"
                      y="11"
                      width="18"
                      height="11"
                      rx="2"
                      stroke="white"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                    <path
                      d="M7 11V7C7 5.67392 7.52678 4.40215 8.46447 3.46447C9.40215 2.52678 10.6739 2 12 2C13.3261 2 14.5979 2.52678 15.5355 3.46447C16.4732 4.40215 17 5.67392 17 7V11"
                      stroke="white"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                    <circle cx="12" cy="16" r="1" fill="white" />
                  </svg>
                  <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={trabahadorCredentials.password}
                    onChange={handleTrabahadorInputChange}
                    required
                  />
                </div>

                <div className="links-container">
                  <Link to="/register-worker" className="auth-link">
                    Register as Trabahador
                  </Link>
                  <Link to="/forgot-password" className="auth-link">
                    Forgot Password?
                  </Link>
                </div>

                <button type="submit" className="login-button trabahador-button">
                  Login as Trabahador
                </button>

                {/* Google Login Button */}
              </form>
            )}

            <div className="admin-login-link">
              <button onClick={handleAdminLogin} className="admin-link-btn">
                Admin Login
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default SignIn
