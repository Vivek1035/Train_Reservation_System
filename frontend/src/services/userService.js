import api from './api';

const USER_URL = '/users';

export const userService = {
  // Get user by ID
  getUserById: async (id) => {
    const response = await api.get(`${USER_URL}/${id}`);
    return response.data;
  },

  // Update user profile
  updateUser: async (id, userData) => {
    const response = await api.put(`${USER_URL}/${id}`, userData);
    return response.data;
  },

  // Delete user (admin)
  deleteUser: async (id) => {
    const response = await api.delete(`${USER_URL}/${id}`);
    return response.data;
  },

  // Get all users (admin)
  getAllUsers: async (params) => {
    const response = await api.get(USER_URL, { params });
    return response.data;
  },

  // Search users (admin)
  searchUsers: async (searchTerm) => {
    const response = await api.get(`${USER_URL}/search`, {
      params: { query: searchTerm },
    });
    return response.data;
  },
};
