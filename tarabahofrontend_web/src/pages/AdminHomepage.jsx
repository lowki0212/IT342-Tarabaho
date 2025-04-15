import { Link } from "react-router-dom"
import AdminNavbar from "../components/AdminNavbar"
import Footer from "../components/Footer"
import "../styles/admin-homepage.css"

const AdminHomepage = () => {
  // Mock dashboard data (in a real app, this would come from an API)
  const dashboardData = {
    totalUsers: 150,
    totalTrabahadors: 75,
  }

  return (
    <div className="admin-homepage">
      {/* Navigation Bar */}
      <AdminNavbar activePage="homepage" />

      {/* Main Content */}
      <div className="admin-main-content">
        <div className="admin-content-overlay">
          <div className="admin-welcome-container">
            <div className="admin-logo-container">
              <div className="admin-logo">
                T A R A B A H
                <svg
                  className="admin-logo-icon"
                  width="60"
                  height="60"
                  viewBox="0 0 24 24"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <circle cx="12" cy="12" r="8" stroke="#0078FF" strokeWidth="2" fill="none" />
                  <path d="M18 18L22 22" stroke="#0078FF" strokeWidth="2" strokeLinecap="round" />
                </svg>
              </div>
              <div className="admin-tagline">T A R A ! T R A B A H O</div>
            </div>

            <h1 className="admin-welcome-heading">WELCOME ADMIN!</h1>
            <p className="admin-welcome-subheading">
              Manage your platform with ease and efficiency.
            </p>

            <div className="admin-dashboard-summary">
              <div className="summary-item">
                <span className="summary-label">Total Users:</span>
                <span className="summary-value">{dashboardData.totalUsers}</span>
              </div>
              <div className="summary-item">
                <span className="summary-label">Total Trabahadors:</span>
                <span className="summary-value">{dashboardData.totalTrabahadors}</span>
              </div>
            </div>

            <div className="admin-actions">
              <Link
                to="/admin/manage-users"
                className="admin-action-button"
                aria-label="Manage Users"
              >
                MANAGE USERS
              </Link>
              <Link
                to="/admin/manage-trabahador"
                className="admin-action-button"
                aria-label="Manage Trabahador"
              >
                MANAGE TRABAHADOR
              </Link>
            </div>
          </div>
        </div>
      </div>
      <Footer/>
    </div>
  )
}

export default AdminHomepage