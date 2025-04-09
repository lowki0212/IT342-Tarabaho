import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Homepage from "./pages/Homepage"
import SignIn from "./pages/SignIn"
import Register from "./pages/Register"
import RegisterUser from "./pages/RegisterUser"
import RegisterAdmin from "./pages/RegisterAdmin"
import AdminLogin from "./pages/AdminLogin"
import AdminHomepage from "./pages/AdminHomepage"
import AdminContactUs from "./pages/AdminContactUs"
import AdminProfile from "./pages/AdminProfile"
import ContactUs from "./pages/ContactUs"

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/signin" element={<SignIn />} />
        <Route path="/register" element={<Register />} />
        <Route path="/register-user" element={<RegisterUser />} />
        <Route path="/register-worker" element={<div>Worker Registration Form</div>} />
        <Route path="/register-admin" element={<RegisterAdmin />} />
        <Route path="/admin-login" element={<AdminLogin />} />
        <Route path="/admin/homepage" element={<AdminHomepage />} />
        <Route path="/admin/contact" element={<AdminContactUs />} />
        <Route path="/admin/profile" element={<AdminProfile />} />
        <Route path="/contact" element={<ContactUs />} />
      </Routes>
    </Router>
  )
}

export default App