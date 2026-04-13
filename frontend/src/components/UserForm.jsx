import { useState } from "react";
import PropTypes from "prop-types";

export default function UserForm({ onSubmit, initialData = {}, isEditing = false }) {
  const [form, setForm] = useState(initialData);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.name || !form.email) return;
    onSubmit(form);
    if (!isEditing) setForm({ name: "", email: "" });
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>{isEditing ? "Edit User" : "Add User"}</h2>
      <input
        name="name"
        placeholder="Name"
        value={form.name || ""}
        onChange={handleChange}
        required
      />
      <input
        name="email"
        placeholder="Email"
        type="email"
        value={form.email || ""}
        onChange={handleChange}
        required
      />
      <button type="submit">{isEditing ? "Update" : "Add"} User</button>
    </form>
  );
}

UserForm.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  initialData: PropTypes.object,
  isEditing: PropTypes.bool,
};