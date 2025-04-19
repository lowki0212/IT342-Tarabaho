"use client";

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios"; // Add axios import
import logo from "../assets/images/logowhite.png";
import "../styles/register-admin.css";

const RegisterAdmin = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    username: "",
    password: "",
    email: "",
    address: "",
  });
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8080/api/admin/register", formData, {
        headers: { "Content-Type": "application/json" },
      });
      console.log("Registration response:", res.data);
      alert("Registration successful!");
      navigate("/admin-login");
    } catch (err) {
      console.error("Registration error:", err.response?.data || err.message);
      const errorMessage = err.response?.data || "Error registering admin";
      setError(errorMessage);
    }
  };

  const handleBack = () => {
    navigate("/register");
  };

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
            {error && <div className="error-message">{error}</div>} {/* Error display */}

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
          </div>
        </div>

        <div className="form-right-section">
          <div className="right-content">
            <div className="name-group">
              <label>Your name</label>
              <div className="name-inputs">
                <input
                  type="text"
                  name="firstname"
                  placeholder="First"
                  value={formData.firstname}
                  onChange={handleChange}
                  required
                />
                <input
                  type="text"
                  name="lastname"
                  placeholder="Last"
                  value={formData.lastname}
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

            <button type="submit" className="signup-button">
              Sign Up
            </button>
          </div>
        </div>
      </form>
    </div>
  );
};

export default RegisterAdmin;