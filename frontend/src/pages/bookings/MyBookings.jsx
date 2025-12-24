import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { bookingService } from '../../services/bookingService';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import Badge from '../../components/common/Badge';
import Modal from '../../components/common/Modal';
import { Calendar, Train, MapPin, Users, Ticket } from 'lucide-react';

const MyBookings = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [cancelModal, setCancelModal] = useState({ open: false, booking: null });
  const [cancelling, setCancelling] = useState(false);

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      setLoading(true);
      const response = await bookingService.getUserBookings(user.id);
      setBookings(response.data?.content || []);
    } catch (err) {
      console.error('Error fetching bookings:', err);
      setError('Failed to load bookings');
    } finally {
      setLoading(false);
    }
  };

  const handleCancelBooking = async () => {
    if (!cancelModal.booking) return;

    try {
      setCancelling(true);
      await bookingService.cancelBooking(cancelModal.booking.id);
      setBookings(
        bookings.map((b) =>
          b.id === cancelModal.booking.id ? { ...b, status: 'CANCELLED' } : b
        )
      );
      setCancelModal({ open: false, booking: null });
    } catch (err) {
      console.error('Cancel error:', err);
      setError('Failed to cancel booking. Please try again.');
    } finally {
      setCancelling(false);
    }
  };

  const getStatusBadge = (status) => {
    const variants = {
      CONFIRMED: 'success',
      PENDING: 'warning',
      CANCELLED: 'danger',
    };
    return <Badge variant={variants[status] || 'info'}>{status}</Badge>;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <Spinner size="large" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-6xl mx-auto px-4">
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
            <Ticket size={28} />
            My Bookings
          </h1>
          <Button onClick={() => navigate('/search')} variant="primary">
            Book New Ticket
          </Button>
        </div>

        {error && (
          <Alert type="error" message={error} onClose={() => setError('')} className="mb-4" />
        )}

        {bookings.length === 0 ? (
          <Card>
            <div className="text-center py-12">
              <Ticket size={48} className="mx-auto text-gray-400 mb-4" />
              <h3 className="text-lg font-semibold text-gray-900 mb-2">No bookings yet</h3>
              <p className="text-gray-600 mb-4">Start booking your train tickets now!</p>
              <Button onClick={() => navigate('/search')} variant="primary">
                Search Trains
              </Button>
            </div>
          </Card>
        ) : (
          <div className="space-y-4">
            {bookings.map((booking) => (
              <Card key={booking.id} hover>
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <h3 className="text-lg font-semibold text-gray-900">
                        PNR: {booking.pnrNumber}
                      </h3>
                      {getStatusBadge(booking.status)}
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-3 text-sm text-gray-600">
                      <div className="flex items-center gap-2">
                        <Train size={16} />
                        <span>
                          {booking.trainNumber} - {booking.trainName}
                        </span>
                      </div>

                      <div className="flex items-center gap-2">
                        <Calendar size={16} />
                        <span>Journey: {booking.journeyDate}</span>
                      </div>

                      <div className="flex items-center gap-2">
                        <MapPin size={16} />
                        <span>
                          {booking.route}
                        </span>
                      </div>

                      <div className="flex items-center gap-2">
                        <Users size={16} />
                        <span>{booking.numberOfPassengers} Seat(s)</span>
                      </div>
                    </div>

                    <div className="mt-3 pt-3 border-t">
                      <div className="flex items-center justify-between">
                        <span className="text-sm text-gray-600">Total Fare:</span>
                        <span className="text-lg font-bold text-primary-600">
                          ₹{booking.totalFare}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="flex flex-col gap-2">
                    <Button
                      variant="secondary"
                      size="sm"
                      onClick={() => navigate(`/bookings/${booking.id}`)}
                    >
                      View Details
                    </Button>
                    {booking.status === 'CONFIRMED' && (
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => setCancelModal({ open: true, booking })}
                      >
                        Cancel Booking
                      </Button>
                    )}
                  </div>
                </div>
              </Card>
            ))}
          </div>
        )}

        {/* Cancel Confirmation Modal */}
        <Modal
          isOpen={cancelModal.open}
          onClose={() => setCancelModal({ open: false, booking: null })}
          title="Cancel Booking"
          size="sm"
        >
          <div className="space-y-4">
            <p className="text-gray-600">
              Are you sure you want to cancel booking <strong>{cancelModal.booking?.pnrNumber}</strong>?
            </p>
            <Alert
              type="warning"
              message="This action cannot be undone. Refund will be processed according to the cancellation policy."
            />
            <div className="flex gap-3">
              <Button
                variant="danger"
                className="flex-1"
                onClick={handleCancelBooking}
                loading={cancelling}
                disabled={cancelling}
              >
                Yes, Cancel Booking
              </Button>
              <Button
                variant="secondary"
                className="flex-1"
                onClick={() => setCancelModal({ open: false, booking: null })}
                disabled={cancelling}
              >
                No, Keep It
              </Button>
            </div>
          </div>
        </Modal>
      </div>
    </div>
  );
};

export default MyBookings;
