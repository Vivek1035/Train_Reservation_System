import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import Button from '../components/common/Button';
import { Train, Search, Shield, Clock } from 'lucide-react';

const Home = () => {
  const { isAuthenticated } = useAuth();

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-50 to-primary-100">
      {/* Hero Section */}
      <div className="max-w-7xl mx-auto px-4 py-20">
        <div className="text-center mb-16">
          <div className="flex justify-center mb-6">
            <div className="bg-primary-600 p-6 rounded-full">
              <Train className="text-white" size={64} />
            </div>
          </div>
          <h1 className="text-5xl font-bold text-gray-900 mb-4">
            Welcome to RailBook
          </h1>
          <p className="text-xl text-gray-600 mb-8 max-w-2xl mx-auto">
            Book your train tickets seamlessly with our easy-to-use platform. 
            Fast, secure, and reliable train reservation system.
          </p>
          <div className="flex gap-4 justify-center">
            {isAuthenticated ? (
              <Link to="/search">
                <Button variant="primary" size="lg">
                  <Search size={20} />
                  Search Trains
                </Button>
              </Link>
            ) : (
              <>
                <Link to="/register">
                  <Button variant="primary" size="lg">
                    Get Started
                  </Button>
                </Link>
                <Link to="/login">
                  <Button variant="outline" size="lg">
                    Sign In
                  </Button>
                </Link>
              </>
            )}
          </div>
        </div>

        {/* Features */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mt-20">
          <div className="bg-white rounded-xl shadow-lg p-8 text-center">
            <div className="bg-blue-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <Search className="text-blue-600" size={32} />
            </div>
            <h3 className="text-xl font-semibold mb-3">Easy Search</h3>
            <p className="text-gray-600">
              Find trains quickly with our intuitive search interface
            </p>
          </div>

          <div className="bg-white rounded-xl shadow-lg p-8 text-center">
            <div className="bg-green-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <Shield className="text-green-600" size={32} />
            </div>
            <h3 className="text-xl font-semibold mb-3">Secure Payments</h3>
            <p className="text-gray-600">
              Your transactions are protected with industry-standard security
            </p>
          </div>

          <div className="bg-white rounded-xl shadow-lg p-8 text-center">
            <div className="bg-purple-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <Clock className="text-purple-600" size={32} />
            </div>
            <h3 className="text-xl font-semibold mb-3">Instant Booking</h3>
            <p className="text-gray-600">
              Get instant confirmation and e-tickets delivered immediately
            </p>
          </div>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-6 mt-20">
          <div className="bg-white rounded-lg shadow p-6 text-center">
            <p className="text-3xl font-bold text-primary-600 mb-2">1000+</p>
            <p className="text-gray-600">Trains</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6 text-center">
            <p className="text-3xl font-bold text-primary-600 mb-2">500+</p>
            <p className="text-gray-600">Stations</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6 text-center">
            <p className="text-3xl font-bold text-primary-600 mb-2">50K+</p>
            <p className="text-gray-600">Happy Users</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6 text-center">
            <p className="text-3xl font-bold text-primary-600 mb-2">24/7</p>
            <p className="text-gray-600">Support</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
