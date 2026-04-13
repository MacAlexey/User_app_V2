import { useState, useEffect } from "react";
import { api } from "../api";
import UserForm from "./UserForm";
import UserList from "./UserList";
import TodoForm from "./TodoForm";
import TodoList from "./TodoList";

export default function TaskWindow() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [todos, setTodos] = useState([]);
  const [editingUserId, setEditingUserId] = useState(null);
  const [editingTodoId, setEditingTodoId] = useState(null);

  // Fetch users
  const fetchUsers = async () => {
    try {
      const res = await api.getUsers();
      setUsers(res.data);
    } catch (err) {
      console.error("Error fetching users:", err);
    }
  };

  // Fetch todos for selected user
  const fetchTodos = async (userId) => {
    try {
      const res = await api.getTodos(userId);
      setTodos(res.data);
    } catch (err) {
      console.error("Error fetching todos:", err);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  // User handlers
  const handleUserSubmit = async (formData) => {
    try {
      if (editingUserId) {
        await api.updateUser(editingUserId, formData);
        setEditingUserId(null);
      } else {
        await api.createUser(formData);
      }
      fetchUsers();
    } catch (err) {
      console.error("Error submitting user:", err);
    }
  };

  const handleUserEdit = (user) => {
    setEditingUserId(user.id);
  };

  const handleUserDelete = async (id) => {
    try {
      await api.deleteUser(id);
      fetchUsers();
      if (selectedUser && selectedUser.id === id) {
        setSelectedUser(null);
        setTodos([]);
      }
    } catch (err) {
      console.error("Error deleting user:", err);
    }
  };

  const handleUserSelect = (user) => {
    setSelectedUser(user);
    fetchTodos(user.id);
  };

  // Todo handlers
  const handleTodoSubmit = async (formData) => {
    try {
      if (editingTodoId) {
        await api.updateTodo(selectedUser.id, editingTodoId, formData);
        setEditingTodoId(null);
      } else {
        await api.createTodo(selectedUser.id, formData);
      }
      fetchTodos(selectedUser.id);
    } catch (err) {
      console.error("Error submitting todo:", err);
    }
  };

  const handleTodoEdit = (todo) => {
    setEditingTodoId(todo.id);
  };

  const handleTodoDelete = async (id) => {
    try {
      await api.deleteTodo(selectedUser.id, id);
      fetchTodos(selectedUser.id);
    } catch (err) {
      console.error("Error deleting todo:", err);
    }
  };

  return (
    <div>
      <UserForm
        onSubmit={handleUserSubmit}
        initialData={editingUserId ? users.find(u => u.id === editingUserId) : {}}
        isEditing={!!editingUserId}
      />
      <UserList
        users={users}
        onEdit={handleUserEdit}
        onDelete={handleUserDelete}
        onSelect={handleUserSelect}
      />
      {selectedUser && (
        <div>
          <h2>Todos for {selectedUser.name}</h2>
          <TodoForm
            onSubmit={handleTodoSubmit}
            initialData={editingTodoId ? todos.find(t => t.id === editingTodoId) : {}}
            isEditing={!!editingTodoId}
          />
          <TodoList
            todos={todos}
            onEdit={handleTodoEdit}
            onDelete={handleTodoDelete}
          />
        </div>
      )}
    </div>
  );
}
