import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { bookingService } from '../../services/bookingService';

const BookingDetails = () => {
    const { bookingId } = useParams();
    const [booking, setBooking] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        fetchBooking();
    }, []);

    const fetchBooking = async () => {
        try {
            const res = await bookingService.getBookingById(bookingId);
            setBooking(res.data);
        } catch (err) {
            console.error(err);
            setError('Failed to load booking details');
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>{error}</p>;
    if (!booking) return null;

    return (
        <div className="max-w-3xl mx-auto p-6">
            <h2 className="text-2xl font-bold mb-4">Booking Details</h2>

            <div className="space-y-2">
                <p><b>PNR:</b> {booking.pnrNumber}</p>
                <p><b>Status:</b> {booking.status}</p>
                <p><b>Train:</b> {booking.train?.trainNumber} - {booking.train?.trainName}</p>
                <p><b>Journey Date:</b> {booking.journeyDate}</p>
                <p><b>Route:</b> {booking.train?.sourceStation} → {booking.train?.destinationStation}</p>
                <p><b>Passengers:</b> {booking.numberOfPassengers}</p>
                <p><b>Total Fare:</b> ₹{booking.totalFare}</p>
            </div>
            {booking.reservations && booking.reservations.length > 0 && (
                <div className="mt-4">
                    <h3 className="text-lg font-bold mb-2">Passenger Details</h3>

                    {booking.reservations.map((p, index) => (
                        <div
                            key={index}
                            className="border rounded-lg p-3 mb-2 bg-gray-50"
                        >
                            <p><b>Name:</b> {p.passengerName}</p>
                            <p><b>Gender:</b> {p.passengerGender}</p>
                            <p><b>Seat:</b> {p.coachNumber} / {p.seatNumber}</p>
                        </div>
                    ))}
                </div>
            )}

        </div>
    );
};

export default BookingDetails;
