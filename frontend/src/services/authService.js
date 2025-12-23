import api from './api';

const AUTH_URL = '/auth';

export const authService = {
  // Register new user
  register: async (userData) => {
    const response = await api.post(`${AUTH_URL}/register`, userData);
    if (response.data.accessToken) {
      localStorage.setItem('accessToken', response.data.accessToken);
      localStorage.setItem('refreshToken', response.data.refreshToken);
      localStorage.setItem('user', JSON.stringify(response.data.user));
    }
    return response.data;
  },

  // Login with email and password
  login: async (email, password) => {
    const response = await api.post(`${AUTH_URL}/login`, { email, password });
    if (response.data.accessToken) {
      localStorage.setItem('accessToken', response.data.accessToken);
      localStorage.setItem('refreshToken', response.data.refreshToken);
      localStorage.setItem('user', JSON.stringify(response.data.user));
    }
    return response.data;
  },

  // Logout
  logout: async () => {
    try {
      await api.post(`${AUTH_URL}/logout`);
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
    }
  },

  // Get current user info
  getCurrentUser: async () => {
    const response = await api.get(`${AUTH_URL}/me`);
    localStorage.setItem('user', JSON.stringify(response.data));
    return response.data;
  },

  // Refresh access token
  refreshToken: async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      throw new Error('No refresh token available');
    }
    const response = await api.post(`${AUTH_URL}/refresh-token`, { refreshToken });
    localStorage.setItem('accessToken', response.data.accessToken);
    return response.data;
  },

  // Get stored user
  getStoredUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  // Check if user is authenticated
  isAuthenticated: () => {
    return !!localStorage.getItem('accessToken');
  },

  // Google OAuth2 login URL
  getGoogleLoginUrl: () => {
    return `${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'}/oauth2/authorization/google`;
  },
};
