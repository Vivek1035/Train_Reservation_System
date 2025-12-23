import api from './api';

const TRAIN_URL = '/trains';
const STATION_URL = '/stations';

export const trainService = {
  // Search trains
   searchTrains: async ({ sourceStationId, destinationStationId }) => {
    const response = await api.post(`${TRAIN_URL}/search`, {
      sourceStationId,
      destinationStationId,
      onlyAvailable: false
    });
    return response.data.data;
  },

  // Get train by ID
  getTrainById: async (id) => {
    const response = await api.get(`${TRAIN_URL}/${id}`);
    return response.data;
  },

  // Get all trains (paginated)
  getAllTrains: async (params) => {
    const response = await api.get(TRAIN_URL, { params });
    return response.data;
  },

  // Get train seats availability
  getTrainSeats: async (trainId, params) => {
    const response = await api.get(`${TRAIN_URL}/${trainId}/seats`, { params });
    return response.data;
  },

  // Create train (admin)
  createTrain: async (trainData) => {
    const response = await api.post(TRAIN_URL, trainData);
    return response.data;
  },

  // Update train (admin)
  updateTrain: async (id, trainData) => {
    const response = await api.put(`${TRAIN_URL}/${id}`, trainData);
    return response.data;
  },

  // Delete train (admin)
  deleteTrain: async (id) => {
    const response = await api.delete(`${TRAIN_URL}/${id}`);
    return response.data;
  },

  // Get all stations
  getAllStations: async () => {
    const response = await api.get(STATION_URL);
    return response.data;
  },

  // Get station by ID
  getStationById: async (id) => {
    const response = await api.get(`${STATION_URL}/${id}`);
    return response.data;
  },

  // Create station (admin)
  createStation: async (stationData) => {
    const response = await api.post(STATION_URL, stationData);
    return response.data;
  },

  // Update station (admin)
  updateStation: async (id, stationData) => {
    const response = await api.put(`${STATION_URL}/${id}`, stationData);
    return response.data;
  },

  // Delete station (admin)
  deleteStation: async (id) => {
    const response = await api.delete(`${STATION_URL}/${id}`);
    return response.data;
  },
};
