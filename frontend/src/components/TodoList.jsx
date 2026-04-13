import PropTypes from "prop-types";

export default function TodoList({ todos, onEdit, onDelete }) {
  return (
    <div>
      <h3>Todos</h3>
      {todos.length === 0 ? (
        <p>No todos yet.</p>
      ) : (
        todos.map((todo) => (
          <div key={todo.id} style={{ border: "1px solid #ddd", margin: "5px", padding: "5px" }}>
            <h4>{todo.title}</h4>
            <p>{todo.details}</p>
            <p>Due: {todo.dueDate || "No due date"}</p>
            <p>Completed: {todo.completed ? "Yes" : "No"}</p>
            <p>Created: {new Date(todo.createdAt).toLocaleString()}</p>
            <button onClick={() => onEdit(todo)}>Edit</button>
            <button onClick={() => onDelete(todo.id)}>Delete</button>
          </div>
        ))
      )}
    </div>
  );
}

TodoList.propTypes = {
  todos: PropTypes.array.isRequired,
  onEdit: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};