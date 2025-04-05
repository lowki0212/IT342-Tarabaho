import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Homepage from "./components/Homepage"
import SignIn from "./components/SignIn"
import Register from "./components/Register"

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/signin" element={<SignIn />} />
        <Route path="/register" element={<Register />} />
      </Routes>
    </Router>
  )
}

export default App

