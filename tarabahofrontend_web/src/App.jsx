import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Homepage from "./pages/Homepage"
import SignIn from "./pages/Signin"
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
import ClientDetails from "./pages/ClientDetails"
import ContactUs from "./pages/ContactUs"
import UserBrowse from "./pages/UserBrowse"
import UserContactUs from "./pages/UserContactUs"
import UserHomepage from "./pages/UserHomepage"
import UserBrowseCategory from "./pages/UserBrowseCategory"
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
import BookingHistory from "./pages/BookingHistory"
import TrabahadorHistory from "./pages/TrabahadorHistory"
import UserBookmarks from "./pages/UserBookmarks"
import ChatPage from "./pages/ChatPage"
import WorkerProfileDetail from "./pages/WorkerProfileDetail"
import Terms from "./pages/Terms"
import Privacy from "./pages/Privacy"
import SuccessPage from "./pages/SuccessPage"
import FailedPage from "./pages/FailedPage"

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
        <Route path="/terms" element={<Terms />} />
        <Route path="/privacy" element={<Privacy />} />

        {/* Admin routes */}
        <Route path="/admin/homepage" element={<AdminHomepage />} />
        <Route path="/admin/contact" element={<AdminContactUs />} />
        <Route path="/admin/profile" element={<AdminProfile />} />
        <Route path="/admin/manage-users" element={<AdminManageUsers />} />
        <Route path="/admin/manage-trabahador" element={<AdminManageTrabahador />} />
        <Route path="/admin/trabahador/:id" element={<TrabahadorDetails />} />
        <Route path="/admin/client/:id" element={<ClientDetails />} />
        <Route path="/admin/about" element={<AdminAboutUs />} />
        <Route path="/admin/manage-trabahador/register-worker" element={<RegisterTrabahador/>}/>
        <Route path="/admin/manage-users/register-user"element={<RegisterUser/>}/>
        {/* User-specific routes */}
        <Route path="/user-home" element={<UserHomepage />} />
        <Route path="/user-browse" element={<UserBrowse />} />
        <Route path="/user-contact" element={<UserContactUs />} />
        <Route path="/user-profile" element={<UserProfile />} />
        <Route path="/user-about" element={<UserAboutUs />} />
        <Route path="/booking-history" element={<BookingHistory />} />
        <Route path="/user-bookmarks" element={<UserBookmarks />} />

        {/* Dynamic category browse route */}
        <Route path="/user-browse/:categoryName" element={<UserBrowseCategory />} />

        {/* Trabahador-specific routes */}
        <Route path="/trabahador-homepage" element={<TrabahadorHomepage />} />
        <Route path="/trabahador-contact" element={<TrabahadorContactUs />} />
        <Route path="/trabahador-history" element={<TrabahadorHistory />} />
        <Route path="/trabahador-profile" element={<TrabahadorProfile />} />
        <Route path="/trabahador-about" element={<TrabahadorAboutUs />} />
        <Route path="/worker-profile-detail/:workerId" element={<WorkerProfileDetail />} />

        {/* General routes */}
        <Route path="/contact" element={<ContactUs />} />

        <Route path="/category/:categoryName" element={<UserBrowseCategory />} />
        <Route path="/worker/:workerId" element={<WorkerProfile />} />
        <Route path="/booking/:workerId/payment" element={<PaymentPage />} />
        <Route path="/booking/:workerId/request" element={<BookingRequest />} />
        <Route path="/chat/:bookingId" element={<ChatPage />} />

        {/* PAYMONGO ROUTES */}
        <Route path="/booking/:workerId/success" element={<SuccessPage />} />
        <Route path="/booking/:workerId/failed" element={<FailedPage />} /> 
      </Routes>
    </Router>
  )
}

export default App