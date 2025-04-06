import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Homepage from "./components/Homepage"
import SignIn from "./components/Signin"
import Register from "./components/Register"
import RegisterUser from "./components/RegisterUser"
import RegisterAdmin from "./components/RegisterAdmin"
import AdminLogin from "./components/AdminLogin"
import ContactUs from "./components/ContactUs"

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
        <Route path="/contact" element={<ContactUs />} />
      </Routes>
    </Router>
  )
}

export default App

