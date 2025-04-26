"use client"

import { useState, Component } from "react"
import { useNavigate, Link } from "react-router-dom"
import axios from "axios"
import logo from "../assets/images/logowhite.png"
import "../styles/signin.css"

// Error Boundary Component
class ErrorBoundary extends Component {
  state = { hasError: false }

  static getDerivedStateFromError(error) {
    return { hasError: true }
  }

  componentDidCatch(error, errorInfo) {
    console.error("ErrorBoundary caught an error:", error, errorInfo)
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="signin-page">
          <div className="signin-container">
            <div className="error-container">
              <h2>Something went wrong</h2>
              <p>Please try refreshing the page or contact support.</p>
              <button onClick={() => window.location.reload()} className="primary-button">
                Refresh
              </button>
            </div>
          </div>
        </div>
      )
    }
    return this.props.children
  }
}

const SignIn = () => {
  const [loginType, setLoginType] = useState("user") // Default to user login
  const [userCredentials, setUserCredentials] = useState({ username: "", password: "" })
  const [trabahadorCredentials, setTrabahadorCredentials] = useState({ username: "", password: "" })
  const [error, setError] = useState("") // Ensure error is always a string
  const [isLoading, setIsLoading] = useState(false) // Added loading state
  const navigate = useNavigate()

  const handleUserLogin = async (e) => {
    e.preventDefault()
    setError("")
    setIsLoading(true)

    const payload = userCredentials
    console.log("User Login Payload:", payload)

    try {
      const res = await axios.post("http://localhost:8080/api/user/token", payload, {
        withCredentials: true,
      })

      console.log("User login successful, token set in cookie")

      // Store user info in localStorage
      localStorage.setItem("isLoggedIn", "true")
      localStorage.setItem("userType", "user")
      localStorage.setItem("username", userCredentials.username)

      // Redirect to user dashboard
      navigate("/user-browse")
    } catch (err) {
      // Ensure error is a string
      const errorMessage = err.response?.data?.message || err.response?.data?.error || "Invalid username or password"
      setError(errorMessage)
    } finally {
      setIsLoading(false)
    }
  }

  const handleTrabahadorLogin = async (e) => {
    e.preventDefault()
    setError("")
    setIsLoading(true)

    const payload = trabahadorCredentials
    console.log("Trabahador Login Payload:", payload)

    try {
      const res = await axios.post("http://localhost:8080/api/worker/token", payload, {
        withCredentials: true,
      })

      console.log("Trabahador login successful, token set in cookie")

      // Store trabahador info in localStorage
      localStorage.setItem("isLoggedIn", "true")
      localStorage.setItem("userType", "trabahador")
      localStorage.setItem("username", trabahadorCredentials.username)

      // Redirect to trabahador dashboard
      navigate("/trabahador-homepage")
    } catch (err) {
      // Ensure error is a string
      const errorMessage = err.response?.data?.message || err.response?.data?.error || "Invalid username or password"
      setError(errorMessage)
    } finally {
      setIsLoading(false)
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
    setError("") // Clear error on input change
  }

  const handleTrabahadorInputChange = (e) => {
    const { name, value } = e.target
    setTrabahadorCredentials((prev) => ({ ...prev, [name]: value }))
    setError("") // Clear error on input change
  }

  return (
    <ErrorBoundary>
      <div className="signin-page">
        <div className="signin-overlay"></div>

        <button className="back-button" onClick={handleBack} aria-label="Go back">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M15 19L8 12L15 5" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
        </button>

        <div className="signin-container">
          <div className="signin-content">
            {/* Left side - Branding and information */}
            <div className="signin-left">
              <div className="brand-container">
                <img src={logo || "/placeholder.svg"} alt="Tarabaho Logo" className="brand-logo" />
              </div>
              <div className="brand-message">
                <h2>Find Work. Hire Talent.</h2>
                <p>Connect with skilled workers or find opportunities that match your skills.</p>
                <div className="feature-list">
                  <div className="feature-item">
                    <div className="feature-icon">
                      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path
                          d="M22 11.08V12C21.9988 14.1564 21.3005 16.2547 20.0093 17.9818C18.7182 19.709 16.9033 20.9725 14.8354 21.5839C12.7674 22.1953 10.5573 22.1219 8.53447 21.3746C6.51168 20.6273 4.78465 19.2461 3.61096 17.4371C2.43727 15.628 1.87979 13.4881 2.02168 11.3363C2.16356 9.18455 2.99721 7.13631 4.39828 5.49706C5.79935 3.85781 7.69279 2.71537 9.79619 2.24013C11.8996 1.7649 14.1003 1.98232 16.07 2.85999"
                          stroke="white"
                          strokeWidth="2"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        />
                        <path
                          d="M22 4L12 14.01L9 11.01"
                          stroke="white"
                          strokeWidth="2"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        />
                      </svg>
                    </div>
                    <span>Verified workers</span>
                  </div>
                  <div className="feature-item">
                    <div className="feature-icon">
                      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path
                          d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z"
                          stroke="white"
                          strokeWidth="2"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        />
                        <path
                          d="M12 6V12L16 14"
                          stroke="white"
                          strokeWidth="2"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        />
                      </svg>
                    </div>
                    <span>Flexible schedules</span>
                  </div>
                  <div className="feature-item">
                    <div className="feature-icon">
                      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path
                          d="M20 6L9 17L4 12"
                          stroke="white"
                          strokeWidth="2"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        />
                      </svg>
                    </div>
                    <span>Secure payments</span>
                  </div>
                </div>
              </div>
            </div>

            {/* Right side - Login form */}
            <div className="signin-right">
              <div className="form-container">
                <div className="form-header">
                  <h2>Sign In</h2>
                  <div className="login-type-tabs">
                    <button
                      className={`tab-button ${loginType === "user" ? "active" : ""}`}
                      onClick={() => setLoginType("user")}
                    >
                      User
                    </button>
                    <button
                      className={`tab-button ${loginType === "trabahador" ? "active" : ""}`}
                      onClick={() => setLoginType("trabahador")}
                    >
                      Trabahador
                    </button>
                  </div>
                </div>

                {error && (
                  <div className="error-message">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="16"
                      height="16"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    >
                      <circle cx="12" cy="12" r="10"></circle>
                      <line x1="12" y1="8" x2="12" y2="12"></line>
                      <line x1="12" y1="16" x2="12.01" y2="16"></line>
                    </svg>
                    {error}
                  </div>
                )}

                {loginType === "user" ? (
                  <form onSubmit={handleUserLogin} className="login-form">
                    <div className="form-group">
                      <label htmlFor="username">Username</label>
                      <div className="input-wrapper">
                        <div className="input-icon-wrapper">
                          <svg
                            className="input-icon"
                            viewBox="0 0 24 24"
                            fill="none"
                            xmlns="http://www.w3.org/2000/svg"
                          >
                            <path
                              d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21"
                              stroke="currentColor"
                              strokeWidth="2"
                              strokeLinecap="round"
                              strokeLinejoin="round"
                            />
                            <path
                              d="M12 11C14.2091 11 16 9.20914 16 7C16 4.79086 14.2091 3 12 3C9.79086 3 8 4.79086 8 7C8 9.20914 9.79086 11 12 11Z"
                              stroke="currentColor"
                              strokeWidth="2"
                              strokeLinecap="round"
                              strokeLinejoin="round"
                            />
                          </svg>
                        </div>
                        <input
                          id="username"
                          type="text"
                          name="username"
                          placeholder="Enter your username"
                          value={userCredentials.username}
                          onChange={handleUserInputChange}
                          required
                          disabled={isLoading}
                          className="form-input"
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <label htmlFor="password">Password</label>
                      <div className="input-wrapper">
                        <div className="input-icon-wrapper">
                          <svg
                            className="input-icon"
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
                              stroke="currentColor"
                              strokeWidth="2"
                              strokeLinecap="round"
                              strokeLinejoin="round"
                            />
                            <path
                              d="M7 11V7C7 5.67392 7.52678 4.40215 8.46447 3.46447C9.40215 2.52678 10.6739 2 12 2C13.3261 2 14.5979 2.52678 15.5355 3.46447C16.4732 4.40215 17 5.67392 17 7V11"
                              stroke="currentColor"
                              strokeWidth="2"
                              strokeLinecap="round"
                              strokeLinejoin="round"
                            />
                          </svg>
                        </div>
                        <input
                          id="password"
                          type="password"
                          name="password"
                          placeholder="Enter your password"
                          value={userCredentials.password}
                          onChange={handleUserInputChange}
                          required
                          disabled={isLoading}
                          className="form-input"
                        />
                      </div>
                    </div>

                    <div className="form-links">
                      <Link to="/forgot-password" className="form-link">
                        Forgot Password?
                      </Link>
                    </div>

                    <button type="submit" className="submit-button" disabled={isLoading}>
                      {isLoading ? <span className="loading-spinner"></span> : "Sign In"}
                    </button>

                    <div className="divider">
                      <span>OR</span>
                    </div>

                    <button type="button" onClick={handleGoogleLogin} className="google-button" disabled={isLoading}>
                      <svg className="google-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path
                          d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                          fill="#4285F4"
                        />
                        <path
                          d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                          fill="#34A853"
                        />
                        <path
                          d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                          fill="#FBBC05"
                        />
                        <path
                          d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                          fill="#EA4335"
                        />
                      </svg>
                      Continue with Google
                    </button>

                    <div className="register-prompt">
                      <span>Don't have an account?</span>
                      <Link to="/register-user" className="register-link">
                        Register as User
                      </Link>
                    </div>
                  </form>
                ) : (
                  <form onSubmit={handleTrabahadorLogin} className="login-form">
                    <div className="form-group">
                      <label htmlFor="trabahador-username">Username</label>
                      <div className="input-wrapper">
                        <div className="input-icon-wrapper">
                          <svg
                            className="input-icon"
                            viewBox="0 0 24 24"
                            fill="none"
                            xmlns="http://www.w3.org/2000/svg"
                          >
                            <path
                              d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21"
                              stroke="currentColor"
                              strokeWidth="2"
                              strokeLinecap="round"
                              strokeLinejoin="round"
                            />
                            <path
                              d="M12 11C14.2091 11 16 9.20914 16 7C16 4.79086 14.2091 3 12 3C9.79086 3 8 4.79086 8 7C8 9.20914 9.79086 11 12 11Z"
                              stroke="currentColor"
                              strokeWidth="2"
                              strokeLinecap="round"
                              strokeLinejoin="round"
                            />
                          </svg>
                        </div>
                        <input
                          id="trabahador-username"
                          type="text"
                          name="username"
                          placeholder="Enter your username"
                          value={trabahadorCredentials.username}
                          onChange={handleTrabahadorInputChange}
                          required
                          disabled={isLoading}
                          className="form-input"
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <label htmlFor="trabahador-password">Password</label>
                      <div className="input-wrapper">
                        <div className="input-icon-wrapper">
                          <svg
                            className="input-icon"
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
                              stroke="currentColor"
                              strokeWidth="2"
                              strokeLinecap="round"
                              strokeLinejoin="round"
                            />
                            <path
                              d="M7 11V7C7 5.67392 7.52678 4.40215 8.46447 3.46447C9.40215 2.52678 10.6739 2 12 2C13.3261 2 14.5979 2.52678 15.5355 3.46447C16.4732 4.40215 17 5.67392 17 7V11"
                              stroke="currentColor"
                              strokeWidth="2"
                              strokeLinecap="round"
                              strokeLinejoin="round"
                            />
                          </svg>
                        </div>
                        <input
                          id="trabahador-password"
                          type="password"
                          name="password"
                          placeholder="Enter your password"
                          value={trabahadorCredentials.password}
                          onChange={handleTrabahadorInputChange}
                          required
                          disabled={isLoading}
                          className="form-input"
                        />
                      </div>
                    </div>

                    <div className="form-links">
                      <Link to="/forgot-password" className="form-link">
                        Forgot Password?
                      </Link>
                    </div>

                    <button type="submit" className="submit-button trabahador-button" disabled={isLoading}>
                      {isLoading ? <span className="loading-spinner"></span> : "Sign In"}
                    </button>

                    <div className="register-prompt">
                      <span>Don't have an account?</span>
                      <Link to="/register-worker" className="register-link">
                        Register as Trabahador
                      </Link>
                    </div>
                  </form>
                )}

                <div className="admin-login">
                  <button onClick={handleAdminLogin} className="admin-button" disabled={isLoading}>
                    Admin Login
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </ErrorBoundary>
  )
}

export default SignIn
