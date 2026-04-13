import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_URL;

export const api = {
  // Users
  getUsers: () => axios.get(`${API_BASE_URL}/users`),
  createUser: (data) => axios.post(`${API_BASE_URL}/users`, data),
  updateUser: (id, data) => axios.put(`${API_BASE_URL}/users/${id}`, data),
  deleteUser: (id) => axios.delete(`${API_BASE_URL}/users/${id}`),

  // Todos
  getTodos: (userId) => axios.get(`${API_BASE_URL}/users/${userId}/todos`),
  createTodo: (userId, data) => axios.post(`${API_BASE_URL}/users/${userId}/todos`, data),
  updateTodo: (userId, todoId, data) => axios.put(`${API_BASE_URL}/users/${userId}/todos/${todoId}`, data),
  deleteTodo: (userId, todoId) => axios.delete(`${API_BASE_URL}/users/${userId}/todos/${todoId}`),
};