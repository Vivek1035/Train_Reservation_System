import { useState, useEffect } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { trainService } from '../../services/trainService';
import { bookingService } from '../../services/bookingService';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import Badge from '../../components/common/Badge';
import { ArrowLeft, User, Check } from 'lucide-react';

const SeatSelection = () => {
  const { trainId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { user, isAuthenticated } = useAuth();

  const [train, setTrain] = useState(location.state?.train || null);
  const [seats, setSeats] = useState([]);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [loading, setLoading] = useState(true);
  const [booking, setBooking] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const searchParams = location.state?.searchParams;

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login', { state: { from: location } });
      return;
    }
    fetchSeats();
  }, [trainId]);

  const fetchSeats = async () => {
    try {
      setLoading(true);
      if (!train) {
        const trainData = await trainService.getTrainById(trainId);
        setTrain(trainData);
      }

      const seatsData = await trainService.getTrainSeats(trainId, {
        journeyDate: (searchParams?.journeyDate ?? new Date().toISOString().slice(0, 10)),
      });
      setSeats(seatsData);
    } catch (err) {
      console.error('Error fetching seats:', err);
      setError('Failed to load seats. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleSeatClick = (coach, seat) => {
    if (!seat.available) return;

    const isSelected = selectedSeats.some(
      (s) => s.seatId === seat.id
    );

    if (isSelected) {
      setSelectedSeats((prev) =>
        prev.filter((s) => s.seatId !== seat.id)
      );
    } else {
      setSelectedSeats((prev) => [
        ...prev,
        {
          seatId: seat.id,
          coachId: seat.coachId,
          coachNumber: coach.coachCode,
          coachType: seat.coachType,
          seatNumber: seat.seatNumber,
          fare: train?.baseFare ?? 0
        },
      ]);
    }
  };

  const getTotalFare = () => {
    return selectedSeats.reduce((sum, seat) => sum + seat.fare, 0);
  };

  const getValidJourneyDate = () => {
    if (searchParams?.journeyDate) {
      return searchParams.journeyDate;
    }

    const date = new Date();
    date.setDate(date.getDate() + 1); // force future
    return date.toISOString().split('T')[0];
  };


  const handleBooking = async () => {
    if (selectedSeats.length === 0) {
      setError('Please select at least one seat');
      return;
    }

    if (!user?.id) {
      setError('User not loaded. Please re-login.');
      return;
    }

    try {
      setBooking(true);
      setError('');

      const bookingData = {
        userId: user.id,
        trainId: Number(trainId),
        journeyDate: getValidJourneyDate(),

        passengers: selectedSeats.map((seat) => ({
          passengerName: user.firstName + ' ' + user.lastName,
          passengerAge: 25,
          passengerGender: 'MALE',
          quotaType: 'GENERAL',
          coachId: seat.coachId,   // ✅ NOW EXISTS
          seatPreference: null
        })),

        specialRequests: null
      };

      console.log('BOOKING PAYLOAD:', bookingData);

      const response = await bookingService.createBooking(bookingData);

      setSuccessMessage('Booking created successfully!');

      setTimeout(() => {
        navigate('/payment', { state: { booking: response.data   } });
      }, 1000);

    } catch (err) {
      console.error('Booking error:', err);
      setError(
        err.response?.data?.message ||
        'Failed to create booking. Please try again.'
      );
    } finally {
      setBooking(false);
    }
  };

  const getSeatColor = (status, isSelected) => {
    if (isSelected) return 'bg-primary-600 text-white border-primary-600';
    switch (status) {
      case 'AVAILABLE':
        return 'bg-white hover:bg-primary-50 border-gray-300 hover:border-primary-400 cursor-pointer';
      case 'BOOKED':
        return 'bg-gray-300 text-gray-600 border-gray-400 cursor-not-allowed';
      case 'RESERVED':
        return 'bg-yellow-100 text-yellow-800 border-yellow-400 cursor-not-allowed';
      default:
        return 'bg-gray-100 border-gray-300';
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <Spinner size="large" />
      </div>
    );
  }

  const seatsByCoach = seats.reduce((acc, seat) => {
    if (!acc[seat.coachCode]) {
      acc[seat.coachCode] = {
        coachNumber: seat.coachCode,
        coachType: seat.coachType,
        seats: []
      };
    }
    acc[seat.coachCode].seats.push(seat);
    return acc;
  }, {});


  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4">
        {/* Header */}
        <div className="mb-6">
          <Button
            variant="ghost"
            onClick={() => navigate(-1)}
            className="mb-4"
          >
            <ArrowLeft size={18} />
            Back to Search
          </Button>

          {train && (
            <Card>
              <h1 className="text-2xl font-bold text-gray-900">
                {train.trainNumber} - {train.trainName}
              </h1>
              <p className="text-gray-600 mt-1">
                {searchParams?.journeyDate && `Journey Date: ${searchParams.journeyDate}`}
              </p>
            </Card>
          )}
        </div>

        {error && (
          <Alert type="error" message={error} onClose={() => setError('')} className="mb-4" />
        )}

        {successMessage && (
          <Alert type="success" message={successMessage} className="mb-4" />
        )}

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Seat Map */}
          <div className="lg:col-span-2 space-y-6">
            {seats.length === 0 ? (
              <Card>
                <p className="text-center text-gray-600 py-12">No seats available</p>
              </Card>
            ) : (
              Object.values(seatsByCoach).map((coach) => (
                <Card key={coach.coachNumber}>
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg font-semibold">
                      Coach {coach.coachNumber}
                    </h3>
                    <Badge variant="info">{coach.coachType}</Badge>
                  </div>

                  <div className="grid grid-cols-4 gap-2">
                    {coach.seats.map((seat) => {
                      const isSelected = selectedSeats.some(
                        (s) => s.seatId === seat.id
                      );

                      return (
                        <button
                          key={seat.id}
                          onClick={() => handleSeatClick(coach, seat)}
                          disabled={!seat.available}
                          className={`
                p-3 rounded-lg border-2 text-sm font-medium
                ${isSelected
                              ? 'bg-primary-600 text-white'
                              : seat.available
                                ? 'bg-white hover:bg-primary-50 border-gray-300'
                                : 'bg-gray-300 cursor-not-allowed'
                            }
              `}
                        >
                          {seat.seatNumber}
                          {isSelected && <Check size={14} className="inline ml-1" />}
                        </button>
                      );
                    })}
                  </div>
                </Card>
              ))
            )}



          </div>

          {/* Booking Summary */}
          <div className="lg:col-span-1">
            <Card className="sticky top-4">
              <h3 className="text-lg font-semibold mb-4">Booking Summary</h3>

              <div className="space-y-3 mb-4">
                <div className="flex items-center gap-2 text-sm text-gray-600">
                  <User size={16} />
                  <span>Passenger: {user?.fullName}</span>
                </div>

                <div className="pt-3 border-t">
                  <p className="text-sm text-gray-600 mb-2">Selected Seats:</p>
                  {selectedSeats.length === 0 ? (
                    <p className="text-sm text-gray-500 italic">No seats selected</p>
                  ) : (
                    <div className="space-y-1">
                      {selectedSeats.map((seat) => (
                        <div
                          key={seat.seatId}
                          className="flex justify-between text-sm bg-gray-50 p-2 rounded"
                        >
                          <span>
                            {seat.coachNumber}-{seat.seatNumber} ({seat.coachType})
                          </span>
                          <span className="font-medium">₹{seat.fare}</span>
                        </div>
                      ))}
                    </div>
                  )}
                </div>

                <div className="pt-3 border-t">
                  <div className="flex justify-between text-lg font-bold">
                    <span>Total:</span>
                    <span className="text-primary-600">₹{getTotalFare()}</span>
                  </div>
                </div>
              </div>

              <Button
                variant="primary"
                className="w-full"
                onClick={handleBooking}
                disabled={selectedSeats.length === 0 || booking}
                loading={booking}
              >
                Proceed to Payment
              </Button>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SeatSelection;
