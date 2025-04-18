import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/home';
import Login from './pages/login';
import Register from './pages/register';
import About from './pages/aboutus';
import Wardrobe from './pages/wardrobe';
import Weather from './pages/weather';
import Activities from './pages/activities';
import Dashboard from './pages/dashboard'; // Import the Dashboard component
import UserProfile from './pages/userprofile'; // Import the UserProfile component

function App() {
  return (
    <Router>
      <div style={{ width: "100vw" }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/about" element={<About />} />
          <Route path="/wardrobe" element={<Wardrobe />} />
          <Route path="/weather" element={<Weather />} />
          <Route path="/activities" element={<Activities />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/userprofile" element={<UserProfile />} /> {/* Add UserProfile route */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
