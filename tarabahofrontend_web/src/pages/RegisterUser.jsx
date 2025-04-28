  "use client";

  import { useState } from "react";
  import { useNavigate } from "react-router-dom";
  import axios from "axios";
  import Cookies from "js-cookie";
  import logo from "../assets/images/logowhite.png";
  import "../styles/register-user.css";

  const RegisterUser = () => {
    const navigate = useNavigate();
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
    });
    const [error, setError] = useState(""); // Added for error handling

    const handleChange = (e) => {
      const { name, value } = e.target;
      setFormData((prevState) => ({
        ...prevState,
        [name]: value,
      }));
    };

    const handleSubmit = async (e) => {
      e.preventDefault();
      setError(""); // Clear previous errors

      // Basic password confirmation check
      if (formData.password !== formData.confirmPassword) {
        setError("Passwords do not match");
        return;
      }

      // Prepare payload for API (excluding confirmPassword)
      const payload = {
        firstname: formData.firstName,
        lastname: formData.lastName,
        username: formData.username,
        email: formData.email,
        password: formData.password,
        phoneNumber: formData.contactNo,
        birthday: formData.birthday,
        location: formData.address, // Mapping address to location for consistency with UserAuth
      };

      console.log("Registration Payload:", payload);

      try {
        const res = await axios.post("http://localhost:8080/api/user/register", payload, {
          withCredentials: true,
        });

        console.log("Registration successful");
        alert("Registered successfully! Please log in.");
        navigate("/signin"); // Redirect to login page after signup, matching UserAuth
      } catch (err) {
        setError("Registration Error: " + (err.response?.data || "Something went wrong"));
      }
    };

    const handleBack = () => {
      navigate("/register");
    };

    return (
      <div className="register-user-container">
        <button className="back-button" onClick={handleBack}>
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M15 19L8 12L15 5" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
        </button>

        <form onSubmit={handleSubmit} className="register-user-form">
          <div className="form-left-section">
            <div className="logo-section">
              <div className="logo-container">
                <img src={logo || "/placeholder.svg"} alt="Tarabaho Logo" className="logo" />
              </div>
            </div>

            <div className="left-content">
              <h2 className="form-title">User Sign-Up Form</h2>
              <p className="form-description">
                Sign up now and get started quickly. Create your online account with a few clicks.
              </p>

              {error && <div className="error-message">{error}</div>} {/* Added error display */}

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
                <label htmlFor="password">
                  Password <span className="required">*</span>
                </label>
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
                <label htmlFor="email">
                  Email address <span className="required">*</span>
                </label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
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
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="contactNo">
                  Contact no. <span className="required">*</span>
                </label>
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
                <label htmlFor="birthday">
                  Birthday <span className="required">*</span>
                </label>
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
      </div>
    );
  };

  export default RegisterUser;