import api from './api';

const BOOKING_URL = '/bookings';

export const bookingService = {
  // Create booking
  createBooking: async (bookingData) => {
    const response = await api.post(BOOKING_URL, bookingData);
    return response.data;
  },

  // Get booking by ID
  getBookingById: async (id) => {
    const response = await api.get(`${BOOKING_URL}/${id}`);
    return response.data;
  },

  // Get bookings by user ID
  getUserBookings: async (userId, params) => {
  const response = await api.get(`${BOOKING_URL}/user/${userId}/history`,{ params } );
  return response.data;
},


  // Get booking by PNR
  getBookingByPnr: async (pnr) => {
    const response = await api.get(`${BOOKING_URL}/pnr/${pnr}`);
    return response.data;
  },

  // Cancel booking
  cancelBooking: async (id) => {
    const response = await api.delete(`${BOOKING_URL}/${id}`);
    return response.data;
  },

  // Get all bookings (admin)
  getAllBookings: async (params) => {
    const response = await api.get(BOOKING_URL, { params });
    return response.data;
  },
};
