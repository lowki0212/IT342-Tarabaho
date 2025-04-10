import { Link, useLocation } from "react-router-dom"
import logo from "../assets/images/logowhite.png"
import "../styles/user-navbar.css"

const UserNavbar = () => {
  const location = useLocation()

  return (
    <nav className="user-navbar">
      <div className="user-navbar-logo">
        <Link to="/browse">
          <img src={logo || "/placeholder.svg"} alt="Tarabaho Logo" className="logo" />
        </Link>
      </div>

      <div className="user-navbar-links">
        <Link to="/browse" className={location.pathname === "/browse" ? "active" : ""}>
          BROWSE
        </Link>
        <Link to="/user-contact" className={location.pathname === "/user-contact" ? "active" : ""}>
          CONTACT US
        </Link>
        <Link to="/about" className={location.pathname === "/about" ? "active" : ""}>
          ABOUT US
        </Link>
        <Link to="/user-profile" className={location.pathname === "/user-profile" ? "active" : ""}>
          PROFILE
        </Link>
      </div>
    </nav>
  )
}

export default UserNavbar
