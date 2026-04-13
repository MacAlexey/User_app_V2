import { useState } from "react";
import PropTypes from "prop-types";

export default function TodoForm({ onSubmit, initialData = {}, isEditing = false }) {
  const [form, setForm] = useState(initialData);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.title) return;
    onSubmit(form);
    if (!isEditing) setForm({ title: "", details: "", dueDate: "", completed: false });
  };

  return (
    <form onSubmit={handleSubmit}>
      <h3>{isEditing ? "Edit Todo" : "Add Todo"}</h3>
      <input
        name="title"
        placeholder="Title"
        value={form.title || ""}
        onChange={handleChange}
        required
      />
      <textarea
        name="details"
        placeholder="Details"
        value={form.details || ""}
        onChange={handleChange}
      />
      <input
        name="dueDate"
        type="date"
        value={form.dueDate || ""}
        onChange={handleChange}
      />
      {isEditing && (
        <label>
          <input
            name="completed"
            type="checkbox"
            checked={form.completed || false}
            onChange={handleChange}
          />
          Completed
        </label>
      )}
      <button type="submit">{isEditing ? "Update" : "Add"} Todo</button>
    </form>
  );
}

TodoForm.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  initialData: PropTypes.object,
  isEditing: PropTypes.bool,
};