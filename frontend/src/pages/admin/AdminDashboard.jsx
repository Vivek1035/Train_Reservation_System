import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { trainService } from '../../services/trainService';
import { bookingService } from '../../services/bookingService';
import { userService } from '../../services/userService';
import Card from '../../components/common/Card';
import Spinner from '../../components/common/Spinner';
import { LayoutDashboard, Train, Users, Ticket, MapPin } from 'lucide-react';

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalTrains: 0,
    totalUsers: 0,
    totalBookings: 0,
    totalStations: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      setLoading(true);
      
      // Fetch all data in parallel
      const [trains, users, bookings, stations] = await Promise.all([
        trainService.getAllTrains({ page: 0, size: 1 }),
        userService.getAllUsers({ page: 0, size: 1 }),
        bookingService.getAllBookings({ page: 0, size: 1 }),
        trainService.getAllStations(),
      ]);

      setStats({
        totalTrains: trains.totalElements || trains.length || 0,
        totalUsers: users.totalElements || users.length || 0,
        totalBookings: bookings.totalElements || bookings.length || 0,
        totalStations: stations.length || 0,
      });
    } catch (err) {
      console.error('Error fetching stats:', err);
    } finally {
      setLoading(false);
    }
  };

  const statCards = [
    {
      title: 'Total Trains',
      value: stats.totalTrains,
      icon: Train,
      color: 'bg-blue-500',
      link: '/admin/trains',
    },
    {
      title: 'Total Users',
      value: stats.totalUsers,
      icon: Users,
      color: 'bg-green-500',
      link: '/admin/users',
    },
    {
      title: 'Total Bookings',
      value: stats.totalBookings,
      icon: Ticket,
      color: 'bg-purple-500',
      link: '/admin/bookings',
    },
    {
      title: 'Total Stations',
      value: stats.totalStations,
      icon: MapPin,
      color: 'bg-orange-500',
      link: '/admin/stations',
    },
  ];

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <Spinner size="large" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4">
        <h1 className="text-2xl font-bold text-gray-900 mb-6 flex items-center gap-2">
          <LayoutDashboard size={28} />
          Admin Dashboard
        </h1>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {statCards.map((stat, index) => {
            const Icon = stat.icon;
            return (
              <Link key={index} to={stat.link}>
                <Card hover className="h-full">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm text-gray-600 mb-1">{stat.title}</p>
                      <p className="text-3xl font-bold text-gray-900">{stat.value}</p>
                    </div>
                    <div className={`${stat.color} p-3 rounded-full`}>
                      <Icon className="text-white" size={24} />
                    </div>
                  </div>
                </Card>
              </Link>
            );
          })}
        </div>

        {/* Quick Actions */}
        <Card>
          <h2 className="text-lg font-semibold mb-4">Quick Actions</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <Link to="/admin/trains/new">
              <button className="w-full p-4 text-left border-2 border-gray-200 rounded-lg hover:border-primary-500 hover:bg-primary-50 transition-all">
                <Train size={20} className="mb-2 text-primary-600" />
                <p className="font-medium">Add New Train</p>
              </button>
            </Link>
            
            <Link to="/admin/stations/new">
              <button className="w-full p-4 text-left border-2 border-gray-200 rounded-lg hover:border-primary-500 hover:bg-primary-50 transition-all">
                <MapPin size={20} className="mb-2 text-primary-600" />
                <p className="font-medium">Add New Station</p>
              </button>
            </Link>
            
            <Link to="/admin/trains">
              <button className="w-full p-4 text-left border-2 border-gray-200 rounded-lg hover:border-primary-500 hover:bg-primary-50 transition-all">
                <Train size={20} className="mb-2 text-primary-600" />
                <p className="font-medium">Manage Trains</p>
              </button>
            </Link>
            
            <Link to="/admin/users">
              <button className="w-full p-4 text-left border-2 border-gray-200 rounded-lg hover:border-primary-500 hover:bg-primary-50 transition-all">
                <Users size={20} className="mb-2 text-primary-600" />
                <p className="font-medium">Manage Users</p>
              </button>
            </Link>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default AdminDashboard;
