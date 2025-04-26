import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Homepage from "./pages/Homepage"
import SignIn from "./pages/SignIn"
import Register from "./pages/Register"
import RegisterUser from "./pages/RegisterUser"
import RegisterAdmin from "./pages/RegisterAdmin"
import RegisterTrabahador from "./pages/RegisterTrabahador"
import AdminLogin from "./pages/AdminLogin"
import AdminHomepage from "./pages/AdminHomepage"
import AdminContactUs from "./pages/AdminContactUs"
import AdminProfile from "./pages/AdminProfile"
import AdminManageUsers from "./pages/AdminManageUsers"
import AdminManageTrabahador from "./pages/AdminManageTrabahador"
import TrabahadorDetails from "./pages/TrabahadorDetails"
import ContactUs from "./pages/ContactUs"
import UserBrowse from "./pages/UserBrowse"
import UserContactUs from "./pages/UserContactUs"
import UserHomepage from "./pages/UserHomepage"
import UserBrowseCategory from "./pages/UserBrowseCategory" // Fix import name
import UserProfile from "./pages/UserProfile"
import TrabahadorHomepage from "./pages/TrabahadorHomepage"
import TrabahadorProfile from "./pages/TrabahadorProfile"
import AboutUs from "./pages/AboutUs"
import AdminAboutUs from "./pages/AdminAboutUs"
import UserAboutUs from "./pages/UserAboutUs"
import TrabahadorAboutUs from "./pages/TrabahadorAboutUs"
import TrabahadorContactUs from "./pages/TrabahadorContactUs"

import WorkerProfile from "./pages/WorkerProfile"
import PaymentPage from "./pages/PaymentPage"
import BookingRequest from "./pages/BookingRequest"
import ChatPage from "./pages/ChatPage"

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/signin" element={<SignIn />} />
        <Route path="/register" element={<Register />} />
        <Route path="/register-user" element={<RegisterUser />} />
        <Route path="/register-worker" element={<RegisterTrabahador />} />
        <Route path="/register-admin" element={<RegisterAdmin />} />
        <Route path="/admin-login" element={<AdminLogin />} />
        <Route path="/about" element={<AboutUs />} />

        {/* Admin routes */}
        <Route path="/admin/homepage" element={<AdminHomepage />} />
        <Route path="/admin/contact" element={<AdminContactUs />} />
        <Route path="/admin/profile" element={<AdminProfile />} />
        <Route path="/admin/manage-users" element={<AdminManageUsers />} />
        <Route path="/admin/manage-trabahador" element={<AdminManageTrabahador />} />
        <Route path="/admin/trabahador/:id" element={<TrabahadorDetails />} />
        <Route path="/admin/about" element={<AdminAboutUs />} />

        {/* User-specific routes */}
        <Route path="/user-home" element={<UserHomepage />} />
        <Route path="/user-browse" element={<UserBrowse />} />
        <Route path="/user-contact" element={<UserContactUs />} />
        <Route path="/user-profile" element={<UserProfile />} />
        <Route path="/user-about" element={<UserAboutUs />} />

        {/* Dynamic category browse route */}
        <Route path="/user-browse/:categoryName" element={<UserBrowseCategory />} />

        {/* Trabahador-specific routes */}
        <Route path="/trabahador-homepage" element={<TrabahadorHomepage />} />
        <Route path="/trabahador-contact" element={<TrabahadorContactUs />} />
        <Route path="/trabahador-history" element={<div>Trabahador History Page</div>} />
        <Route path="/trabahador-profile" element={<TrabahadorProfile />} />
        <Route path="/trabahador-about" element={<TrabahadorAboutUs />} />

        {/* General routes */}
        <Route path="/contact" element={<ContactUs />} />

        <Route path="/category/:categoryName" element={<UserBrowseCategory />} />
        <Route path="/worker/:workerId" element={<WorkerProfile />} />
        <Route path="/booking/:workerId/payment" element={<PaymentPage />} />
        <Route path="/booking/:workerId/request" element={<BookingRequest />} />
        <Route path="/chat/:bookingId" element={<ChatPage />} />
      </Routes>
    </Router>
  )
}

export default App