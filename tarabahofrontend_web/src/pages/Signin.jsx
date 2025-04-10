"use client"

import { useState } from "react"
import { useNavigate, Link } from "react-router-dom"
import logo from "../assets/images/logowhite.png"
import "../styles/signin.css"

const SignIn = () => {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [error, setError] = useState("")
  const navigate = useNavigate()

  // Mock user credentials
  const mockUser = {
    username: "user123",
    password: "password123",
  }

  const mockTrabahador = {
    username: "trabahador123",
    password: "trabahador123",
  }

  const handleLogin = (e) => {
    e.preventDefault()
    setError("")

    // Check if the credentials match our mock user
    if (username === mockUser.username && password === mockUser.password) {
      // Store user info in localStorage to simulate authentication
      localStorage.setItem("isLoggedIn", "true")
      localStorage.setItem("userType", "user")
      localStorage.setItem("username", username)

      // Redirect to browse page
      navigate("/user-browse")
    } else if (username === "admin" && password === "admin123") {
      // Admin login
      localStorage.setItem("isLoggedIn", "true")
      localStorage.setItem("userType", "admin")
      localStorage.setItem("username", username)

      // Redirect to admin homepage
      navigate("/admin/homepage")
    } else if (username === mockTrabahador.username && password === mockTrabahador.password) {
      // Trabahador login
      localStorage.setItem("isLoggedIn", "true")
      localStorage.setItem("userType", "trabahador")
      localStorage.setItem("username", username)

      // Redirect to trabahador homepage
      navigate("/trabahador-homepage")
    } else {
      setError("Invalid username or password")
    }
  }

  const handleBack = () => {
    navigate("/")
  }

  const handleAdminLogin = () => {
    navigate("/admin-login")
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

          <div className="form-overlay">
            <form onSubmit={handleLogin} className="login-form">
              {error && <div className="error-message">{error}</div>}

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
                  placeholder="Username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
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
                  placeholder="Password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>

              <div className="login-help">
                <p className="login-tip">
                  Use username: <strong>user123</strong> and password: <strong>password123</strong> to login as a user, or <strong>trabahador123</strong> and <strong>trabahador123</strong> as a Trabahador.
                </p>
              </div>

              <div className="links-container">
                <Link to="/register" className="auth-link">
                  No account yet?
                </Link>
                <Link to="/forgot-password" className="auth-link">
                  Forgot Password?
                </Link>
              </div>

              <button type="submit" className="login-button">
                Login
              </button>
            </form>
          </div>
        </div>

        <button className="admin-login-button" onClick={handleAdminLogin}>
          Admin Login
        </button>
      </div>
    </div>
  )
}

export default SignIn