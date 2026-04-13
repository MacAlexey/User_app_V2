import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_URL;
const API_V1 = `${API_BASE_URL}/api/v1`;

export const api = {
  // Auth
  register: (data) => axios.post(`${API_V1}/auth/register`, data),
  login: (data) => axios.post(`${API_V1}/auth/login`, data),
  getMe: (token) => axios.get(`${API_V1}/auth/me`, { headers: { Authorization: `Bearer ${token}` } }),

  // Users
  getUsers: () => axios.get(`${API_V1}/users`),
  updateUser: (id, data) => axios.put(`${API_V1}/users/${id}`, data),
  deleteUser: (id) => axios.delete(`${API_V1}/users/${id}`),

  // Todos
  getTodos: (userId) => axios.get(`${API_V1}/users/${userId}/todos`),
  createTodo: (userId, data) => axios.post(`${API_V1}/users/${userId}/todos`, data),
  updateTodo: (userId, todoId, data) => axios.put(`${API_V1}/users/${userId}/todos/${todoId}`, data),
  deleteTodo: (userId, todoId) => axios.delete(`${API_V1}/users/${userId}/todos/${todoId}`),
};