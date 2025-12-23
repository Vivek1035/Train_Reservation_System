import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/layout/Navbar';
import Footer from './components/layout/Footer';

// Pages
import Home from './pages/Home';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import OAuth2RedirectHandler from './pages/auth/OAuth2RedirectHandler';
import SearchTrains from './pages/trains/SearchTrains';
import SeatSelection from './pages/trains/SeatSelection';
import Payment from './pages/payment/Payment';
import MyBookings from './pages/bookings/MyBookings';
import AdminDashboard from './pages/admin/AdminDashboard';
import ManageTrains from './pages/admin/ManageTrains';

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="flex flex-col min-h-screen">
          <Navbar />
          <main className="flex-1">
            <Routes>
              {/* Public Routes */}
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />

              {/* Protected Routes */}
              <Route
                path="/search"
                element={
                  <ProtectedRoute>
                    <SearchTrains />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/trains/:trainId/seats"
                element={
                  <ProtectedRoute>
                    <SeatSelection />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/payment"
                element={
                  <ProtectedRoute>
                    <Payment />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/bookings"
                element={
                  <ProtectedRoute>
                    <MyBookings />
                  </ProtectedRoute>
                }
              />

              {/* Admin Routes */}
              <Route
                path="/admin"
                element={
                  <ProtectedRoute adminOnly>
                    <AdminDashboard />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/admin/trains"
                element={
                  <ProtectedRoute adminOnly>
                    <ManageTrains />
                  </ProtectedRoute>
                }
              />

              {/* Fallback */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
