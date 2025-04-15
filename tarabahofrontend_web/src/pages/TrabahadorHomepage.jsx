import { Link } from "react-router-dom"
import TrabahadorNavbar from "../components/TrabahadorNavbar"
import Footer from "../components/Footer"
import "../styles/TrabahadorHomepage.css"

const TrabahadorHomepage = () => {
  // Mock Trabahador name (in a real app, this would come from an API or localStorage)
  const trabahadorName = "Paul Dave"

  return (
    <div className="trabahador-homepage">
      {/* Navigation Bar */}
      <TrabahadorNavbar activePage="homepage" />

      {/* Main Content */}
      <div className="trabahador-main-content">
        <div className="trabahador-content-overlay">
          <div className="trabahador-welcome-container">
            <div className="trabahador-logo-container">
              <div className="trabahador-logo">
                T A R A B A H
                <svg
                  className="trabahador-logo-icon"
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
              <div className="trabahador-tagline">T A R A ! T R A B A H O</div>
            </div>

            <h1 className="trabahador-welcome-heading">WELCOME {trabahadorName.toUpperCase()}!</h1>

            <div className="trabahador-actions">
              <Link to="/trabahador-history" className="trabahador-action-button">
                VIEW HISTORY
              </Link>
            </div>
          </div>
        </div>
      </div>
      <Footer/>
    </div>
  )
}

export default TrabahadorHomepage