import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { paymentService } from '../../services/paymentService';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import Card from '../../components/common/Card';
import Alert from '../../components/common/Alert';
import { CreditCard, Calendar, Lock, CheckCircle } from 'lucide-react';

const Payment = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const booking = location.state?.booking;

  console.log('PAYMENT BOOKING OBJECT:', booking);

  const [paymentData, setPaymentData] = useState({
    paymentMethod: 'CREDIT_CARD',
    cardNumber: '',
    cardHolderName: '',
    expiryMonth: '',
    expiryYear: '',
    cvv: '',
  });
  const [errors, setErrors] = useState({});
  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState('');
  const [paymentSuccess, setPaymentSuccess] = useState(false);
  const [paymentResponse, setPaymentResponse] = useState(null);

  if (!booking) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <Card>
          <Alert type="error" message="No booking found. Please create a booking first." />
          <Button onClick={() => navigate('/search')} className="mt-4">
            Go to Search
          </Button>
        </Card>
      </div>
    );
  }

  const handleChange = (e) => {
    const { name, value } = e.target;

    // Format card number with spaces
    if (name === 'cardNumber') {
      const cleaned = value.replace(/\s/g, '');
      const formatted = cleaned.match(/.{1,4}/g)?.join(' ') || cleaned;
      setPaymentData((prev) => ({ ...prev, [name]: formatted }));
    } else {
      setPaymentData((prev) => ({ ...prev, [name]: value }));
    }

    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }));
    }
  };

  const validate = () => {
    const newErrors = {};
    const cardNumberClean = paymentData.cardNumber.replace(/\s/g, '');

    if (!paymentData.cardNumber) {
      newErrors.cardNumber = 'Card number is required';
    } else if (cardNumberClean.length !== 16) {
      newErrors.cardNumber = 'Card number must be 16 digits';
    }

    if (!paymentData.cardHolderName.trim()) {
      newErrors.cardHolderName = 'Card holder name is required';
    }

    if (!paymentData.expiryMonth) {
      newErrors.expiryMonth = 'Month is required';
    }

    if (!paymentData.expiryYear) {
      newErrors.expiryYear = 'Year is required';
    }

    if (!paymentData.cvv) {
      newErrors.cvv = 'CVV is required';
    } else if (paymentData.cvv.length !== 3) {
      newErrors.cvv = 'CVV must be 3 digits';
    }

    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const newErrors = validate();
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    try {
      setProcessing(true);

      const payload = {
        bookingId: booking.id,
        amount: booking.totalFare,
        paymentMethod: paymentData.paymentMethod,

        cardNumber: paymentData.cardNumber.replace(/\s/g, ''),
        cardHolderName: paymentData.cardHolderName,
        expiryMonth: paymentData.expiryMonth,
        expiryYear: paymentData.expiryYear,
        cvv: paymentData.cvv,

        forceFailure: false
      };

      console.log('PAYMENT PAYLOAD:', payload);

      const response = await paymentService.processPayment(payload);

      setPaymentResponse(response);
      setPaymentSuccess(true);

      setTimeout(() => navigate('/bookings'), 3000);
    } catch (err) {
      console.error('Payment error:', err);
      setError(err.response?.data?.message || 'Payment failed');
    } finally {
      setProcessing(false);
    }
  };


  const handleRetry = async () => {
    if (!paymentResponse?.id) return;

    try {
      setProcessing(true);
      setError('');
      const response = await paymentService.retryPayment(paymentResponse.id);
      setPaymentResponse(response);
      setPaymentSuccess(true);

      setTimeout(() => {
        navigate('/bookings');
      }, 3000);
    } catch (err) {
      console.error('Retry error:', err);
      setError(err.response?.data?.message || 'Retry failed. Please try again.');
    } finally {
      setProcessing(false);
    }
  };

  if (paymentSuccess) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
        <Card className="max-w-md w-full text-center">
          <div className="flex justify-center mb-4">
            <div className="bg-green-100 p-4 rounded-full">
              <CheckCircle size={48} className="text-green-600" />
            </div>
          </div>
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Payment Successful!</h2>
          <p className="text-gray-600 mb-4">
            Your booking has been confirmed.
          </p>
          <div className="bg-gray-50 rounded-lg p-4 mb-4 text-left">
            <p className="text-sm text-gray-600">PNR Number</p>
            <p className="text-lg font-bold text-primary-600">{booking.pnrNumber}</p>
            <p className="text-sm text-gray-600 mt-2">Transaction ID</p>
            <p className="font-mono text-sm">{paymentResponse?.transactionId}</p>
          </div>
          <Button onClick={() => navigate('/bookings')} variant="primary" className="w-full">
            View My Bookings
          </Button>
        </Card>
      </div>
    );
  }

  return (
    
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-4xl mx-auto px-4">
        <h1 className="text-2xl font-bold text-gray-900 mb-6">Complete Payment</h1>

        {error && (
          <Alert type="error" message={error} onClose={() => setError('')} className="mb-4" />
        )}

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Payment Form */}
          <div className="lg:col-span-2">
            <Card>
              <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                <CreditCard size={20} />
                Payment Details
              </h2>

              <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Payment Method
                  </label>
                  <select
                    className="input"
                    name="paymentMethod"
                    value={paymentData.paymentMethod}
                    onChange={handleChange}
                  >
                    <option value="CREDIT_CARD">Credit Card</option>
                    <option value="DEBIT_CARD">Debit Card</option>
                    <option value="UPI">UPI</option>
                    <option value="NET_BANKING">Net Banking</option>
                    <option value="WALLET">Wallet</option>
                  </select>

                </div>

                <Input
                  label="Card Number"
                  name="cardNumber"
                  value={paymentData.cardNumber}
                  onChange={handleChange}
                  error={errors.cardNumber}
                  placeholder="1234 5678 9012 3456"
                  maxLength={19}
                  required
                />

                <Input
                  label="Card Holder Name"
                  name="cardHolderName"
                  value={paymentData.cardHolderName}
                  onChange={handleChange}
                  error={errors.cardHolderName}
                  placeholder="JOHN DOE"
                  required
                />

                <div className="grid grid-cols-3 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      <Calendar size={14} className="inline mr-1" />
                      Month
                    </label>
                    <select
                      className="input"
                      name="expiryMonth"
                      value={paymentData.expiryMonth}
                      onChange={handleChange}
                      required
                    >
                      <option value="">MM</option>
                      {Array.from({ length: 12 }, (_, i) => i + 1).map((m) => (
                        <option key={m} value={m}>
                          {String(m).padStart(2, '0')}
                        </option>
                      ))}
                    </select>
                    {errors.expiryMonth && (
                      <p className="mt-1 text-sm text-red-600">{errors.expiryMonth}</p>
                    )}
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Year</label>
                    <select
                      className="input"
                      name="expiryYear"
                      value={paymentData.expiryYear}
                      onChange={handleChange}
                      required
                    >
                      <option value="">YYYY</option>
                      {Array.from({ length: 10 }, (_, i) => new Date().getFullYear() + i).map(
                        (y) => (
                          <option key={y} value={y}>
                            {y}
                          </option>
                        )
                      )}
                    </select>
                    {errors.expiryYear && (
                      <p className="mt-1 text-sm text-red-600">{errors.expiryYear}</p>
                    )}
                  </div>

                  <Input
                    label="CVV"
                    name="cvv"
                    type="password"
                    value={paymentData.cvv}
                    onChange={handleChange}
                    error={errors.cvv}
                    placeholder="123"
                    maxLength={3}
                    required
                  />
                </div>

                <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                  <div className="flex items-start gap-2">
                    <Lock size={16} className="text-blue-600 mt-0.5" />
                    <div className="text-sm text-blue-800">
                      <p className="font-medium">Secure Payment</p>
                      <p className="text-blue-600">
                        Your payment information is encrypted and secure
                      </p>
                    </div>
                  </div>
                </div>

                <div className="flex gap-3">
                  <Button
                    type="submit"
                    variant="primary"
                    className="flex-1"
                    loading={processing}
                    disabled={processing}
                  >
                    Pay ₹{booking.totalFare}
                  </Button>

                  {paymentResponse?.canRetry && (
                    <Button
                      type="button"
                      variant="secondary"
                      onClick={handleRetry}
                      loading={processing}
                      disabled={processing}
                    >
                      Retry Payment
                    </Button>
                  )}
                </div>
              </form>
            </Card>

            {/* Test Card Info */}
            <Card className="mt-4 bg-yellow-50">
              <h3 className="text-sm font-semibold text-yellow-900 mb-2">Test Cards (Demo Mode)</h3>
              <div className="text-xs text-yellow-800 space-y-1">
                <p>• Use any card ending in 0000-9999 for various test scenarios</p>
                <p>• Cards ending in 0000: Insufficient funds</p>
                <p>• Cards ending in 1111: Card expired</p>
                <p>• Cards ending in 2222: Invalid CVV</p>
                <p>• Cards ending in 3333: Card blocked</p>
                <p>• Other cards: 90% success rate</p>
              </div>
            </Card>
          </div>

          {/* Order Summary */}
          <div className="lg:col-span-1">
            <Card className="sticky top-4">
              <h3 className="text-lg font-semibold mb-4">Booking Summary</h3>

              <div className="space-y-3 text-sm">
                <div>
                  <p className="text-gray-600">PNR Number</p>
                  <p className="font-semibold">{booking.pnrNumber}</p>
                </div>

                <div className="pt-3 border-t">
                  <p className="text-gray-600">Passenger Name</p>
                  <p className="font-semibold">{booking.passengerName}</p>
                </div>

                <div className="pt-3 border-t">
                  <p className="text-gray-600">Number of Seats</p>
                  <p className="font-semibold">{booking.numberOfSeats}</p>
                </div>

                <div className="pt-3 border-t">
                  <p className="text-gray-600">Journey Date</p>
                  <p className="font-semibold">{booking.journeyDate}</p>
                </div>

                <div className="pt-3 border-t">
                  <div className="flex justify-between text-lg font-bold">
                    <span>Total Amount:</span>
                    <span className="text-primary-600">₹{booking.totalFare}</span>
                  </div>
                </div>
              </div>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Payment;
