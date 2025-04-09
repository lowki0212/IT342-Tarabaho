import { Link } from "react-router-dom"
import AdminNavbar from "../components/AdminNavbar"
import "../styles/admin-homepage.css"

const AdminHomepage = () => {
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

            <div className="admin-actions">
              <Link to="/admin/manage-users" className="admin-action-button">
                MANAGE USERS
              </Link>
              <Link to="/admin/manage-trabahador" className="admin-action-button">
                MANAGE TRABAHADOR
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AdminHomepage
