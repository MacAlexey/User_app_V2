import PropTypes from "prop-types";

export default function UserList({ users, onEdit, onDelete, onSelect }) {
  return (
    <div>
      <h2>Users</h2>
      {users.map((user) => (
        <div key={user.id} style={{ border: "1px solid #ccc", margin: "10px", padding: "10px" }}>
          <p>{user.name} — {user.email}</p>
          <p>Roles: {user.roles?.join(", ") || "None"}</p>
          <button onClick={() => onSelect(user)}>Select for Todos</button>
          <button onClick={() => onEdit(user)}>Edit</button>
          <button onClick={() => onDelete(user.id)}>Delete</button>
        </div>
      ))}
    </div>
  );
}

UserList.propTypes = {
  users: PropTypes.array.isRequired,
  onEdit: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
  onSelect: PropTypes.func.isRequired,
};