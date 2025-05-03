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
import WeeklyForecast from './pages/weeklyforecast'; // Import the WeeklyForecast component
import RecommendedWardrobe from './pages/recommendedwardrobe'; // Import the RecommendedWardrobe component
import RecommendedActivity from './pages/recommendedactivity'; // Import the RecommendedActivity component
import MyActivity from './pages/myactivity'; // Import the MyActivity component
import ScheduleActivity from "./pages/scheduleactivity"; // Import the ScheduleActivity component
import NotificationPage from './pages/notification'; // Import the NotificationPage component
import UserAddActivity from "./pages/useraddactivity"; // Import the UserAddActivity component
import RecommendedActivityWeek from "./pages/recommendedactivityweek"; // Import the component

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
          <Route path="/weeklyforecast" element={<WeeklyForecast />} /> {/* Add WeeklyForecast route */}
          <Route path="/recommendedwardrobe" element={<RecommendedWardrobe />} /> {/* Add RecommendedWardrobe route */}
          <Route path="/recommendedactivity" element={<RecommendedActivity />} /> {/* Add RecommendedActivity route */}
          <Route path="/myactivity" element={<MyActivity />} /> {/* Add MyActivity route */}
          <Route path="/scheduleactivity" element={<ScheduleActivity />} /> {/* Add ScheduleActivity route */}
          <Route path="/notifications" element={<NotificationPage />} /> {/* Add NotificationPage route */}
          <Route path="/useraddactivity" element={<UserAddActivity />} /> {/* Add this route */}
          <Route path="/recommendedactivityweek" element={<RecommendedActivityWeek />} /> {/* Add this route */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
