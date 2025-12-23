import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import Button from '../common/Button';
import { Train, Menu, X, User, LogOut, LayoutDashboard, Ticket } from 'lucide-react';
import { useState } from 'react';

const Navbar = () => {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <nav className="bg-white shadow-md sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to="/" className="flex items-center gap-2 text-primary-600 font-bold text-xl">
            <Train size={28} />
            <span>RailBook</span>
          </Link>

          {/* Desktop Navigation */}
          <div className="hidden md:flex items-center gap-6">
            <Link to="/search" className="text-gray-700 hover:text-primary-600 transition-colors">
              Search Trains
            </Link>
            
            {user && (
              <>
                <Link to="/bookings" className="text-gray-700 hover:text-primary-600 transition-colors">
                  My Bookings
                </Link>
                
                {isAdmin() && (
                  <Link
                    to="/admin"
                    className="flex items-center gap-1 text-gray-700 hover:text-primary-600 transition-colors"
                  >
                    <LayoutDashboard size={18} />
                    Admin
                  </Link>
                )}
                
                <div className="flex items-center gap-3 pl-4 border-l">
                  <div className="flex items-center gap-2">
                    <User size={18} className="text-gray-600" />
                    <span className="text-sm text-gray-700">{user.fullName}</span>
                  </div>
                  <Button variant="ghost" size="sm" onClick={handleLogout}>
                    <LogOut size={18} />
                    Logout
                  </Button>
                </div>
              </>
            )}
            
            {!user && (
              <div className="flex items-center gap-3">
                <Button variant="ghost" onClick={() => navigate('/login')}>
                  Login
                </Button>
                <Button variant="primary" onClick={() => navigate('/register')}>
                  Sign Up
                </Button>
              </div>
            )}
          </div>

          {/* Mobile Menu Button */}
          <button
            className="md:hidden p-2"
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          >
            {mobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>

        {/* Mobile Navigation */}
        {mobileMenuOpen && (
          <div className="md:hidden py-4 border-t">
            <div className="flex flex-col gap-3">
              <Link
                to="/search"
                className="text-gray-700 hover:text-primary-600 px-3 py-2"
                onClick={() => setMobileMenuOpen(false)}
              >
                Search Trains
              </Link>
              
              {user && (
                <>
                  <Link
                    to="/bookings"
                    className="text-gray-700 hover:text-primary-600 px-3 py-2"
                    onClick={() => setMobileMenuOpen(false)}
                  >
                    My Bookings
                  </Link>
                  
                  {isAdmin() && (
                    <Link
                      to="/admin"
                      className="text-gray-700 hover:text-primary-600 px-3 py-2"
                      onClick={() => setMobileMenuOpen(false)}
                    >
                      Admin Dashboard
                    </Link>
                  )}
                  
                  <div className="border-t pt-3 mt-2">
                    <p className="text-sm text-gray-600 px-3 mb-2">{user.fullName}</p>
                    <Button
                      variant="ghost"
                      className="w-full justify-start"
                      onClick={() => {
                        handleLogout();
                        setMobileMenuOpen(false);
                      }}
                    >
                      <LogOut size={18} />
                      Logout
                    </Button>
                  </div>
                </>
              )}
              
              {!user && (
                <div className="flex flex-col gap-2 px-3">
                  <Button variant="ghost" onClick={() => navigate('/login')}>
                    Login
                  </Button>
                  <Button variant="primary" onClick={() => navigate('/register')}>
                    Sign Up
                  </Button>
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
