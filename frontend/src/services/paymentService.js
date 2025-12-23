import api from './api';

const PAYMENT_URL = '/payments';

export const paymentService = {
  // Process payment
  processPayment: async (paymentData) => {
    const response = await api.post(`${PAYMENT_URL}/process`, paymentData);
    return response.data;
  },

  // Retry failed payment
  retryPayment: async (paymentId) => {
    const response = await api.post(`${PAYMENT_URL}/${paymentId}/retry`);
    return response.data;
  },

  // Get payment by transaction ID
  getPaymentByTransactionId: async (transactionId) => {
    const response = await api.get(`${PAYMENT_URL}/transaction/${transactionId}`);
    return response.data;
  },

  // Get payment by booking ID
  getPaymentByBookingId: async (bookingId) => {
    const response = await api.get(`${PAYMENT_URL}/booking/${bookingId}`);
    return response.data;
  },

  // Get user payments
  getUserPayments: async (userId) => {
    const response = await api.get(`${PAYMENT_URL}/user/${userId}`);
    return response.data;
  },

  // Refund payment
  refundPayment: async (paymentId) => {
    const response = await api.post(`${PAYMENT_URL}/${paymentId}/refund`);
    return response.data;
  },

  // Mask card number for display
  maskCardNumber: (cardNumber) => {
    if (!cardNumber) return '';
    const lastFour = cardNumber.slice(-4);
    return `**** **** **** ${lastFour}`;
  },
};
