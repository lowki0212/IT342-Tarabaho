"use client";

import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Cookies from "js-cookie";
import AdminNavbar from "../components/AdminNavbar";
import "../styles/admin-manage-users.css";

const AdminManageUsers = () => {
  const [users, setUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [error, setError] = useState(null);
  const [editing, setEditing] = useState(null);
  const [editForm, setEditForm] = useState({});
  const [creating, setCreating] = useState(false);
  const [createForm, setCreateForm] = useState({
    firstname: "",
    lastname: "",
    username: "",
    email: "",
    password: "",
    phoneNumber: "",
    birthday: "",
  });
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [userToDelete, setUserToDelete] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await axios.get("http://localhost:8080/api/admin/users", {
          withCredentials: true,
        });
        setUsers(res.data);
        setFilteredUsers(res.data);
        setIsLoading(false);
      } catch (err) {
        console.error("Failed to fetch users:", err);
        setError("Failed to load users");
        setIsLoading(false);
        if (err.response?.status === 401) {
          navigate("/admin-login");
        }
      }
    };

    fetchUsers();
  }, [navigate]);

  useEffect(() => {
    const filtered = users.filter((user) =>
      ["firstname", "lastname", "username", "email"].some((field) =>
        String(user[field]).toLowerCase().includes(searchTerm.toLowerCase())
      )
    );
    setFilteredUsers(filtered);
  }, [searchTerm, users]);

  const handleDeleteClick = (user) => {
    setUserToDelete(user);
    setShowDeleteModal(true);
  };

  const confirmDelete = async () => {
    try {
      await axios.delete(`http://localhost:8080/api/admin/users/delete/${userToDelete.id}`, {
        withCredentials: true,
      });
      setUsers(users.filter((u) => u.id !== userToDelete.id));
      setShowDeleteModal(false);
      setUserToDelete(null);
    } catch (err) {
      setError("Failed to delete: " + (err.response?.data || err.message));
    }
  };

  const cancelDelete = () => {
    setShowDeleteModal(false);
    setUserToDelete(null);
  };

  const startEditing = (user) => {
    setEditing({ id: user.id });
    setEditForm({ ...user });
  };

  const handleEditChange = (e) => {
    setEditForm({ ...editForm, [e.target.name]: e.target.value });
  };

  const handleEditSubmit = async (id) => {
    try {
      const res = await axios.put(
        `http://localhost:8080/api/admin/users/edit/${id}`,
        editForm,
        { withCredentials: true }
      );
      setUsers(users.map((u) => (u.id === id ? res.data : u)));
      setEditing(null);
    } catch (err) {
      setError("Failed to update: " + (err.response?.data || err.message));
    }
  };

  const handleCreateChange = (e) => {
    setCreateForm({ ...createForm, [e.target.name]: e.target.value });
  };

  const handleCreateSubmit = async () => {
    try {
      const res = await axios.post(
        "http://localhost:8080/api/admin/users/register",
        createForm,
        { withCredentials: true }
      );
      setUsers([...users, res.data]);
      setCreating(false);
      setCreateForm({
        firstname: "",
        lastname: "",
        username: "",
        email: "",
        password: "",
        phoneNumber: "",
        birthday: "",
      });
    } catch (err) {
      setError("Failed to create: " + (err.response?.data || err.message));
    }
  };

  const handleLogout = async () => {
    try {
      await axios.post("http://localhost:8080/api/admin/logout", {}, { withCredentials: true });
      Cookies.remove("jwtToken", { path: "/", domain: "localhost" });
      navigate("/admin-login");
    } catch (err) {
      console.error("Logout failed:", err.response?.status, err.response?.data);
      Cookies.remove("jwtToken", { path: "/", domain: "localhost" });
      navigate("/admin-login");
    }
  };

  return (
    <div className="admin-manage-users-page">
      <AdminNavbar activePage="manage-users" />

      <div className="manage-users-container">
        <h1 className="manage-users-title">MANAGE USERS</h1>

        <div className="search-bar-container">
          <div className="search-bar">
            <svg
              className="search-icon"
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <circle cx="11" cy="11" r="7" stroke="#666" strokeWidth="2" />
              <path d="M16 16L20 20" stroke="#666" strokeWidth="2" strokeLinecap="round" />
            </svg>
            <input
              type="text"
              placeholder="Search by name, username, or email..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              aria-label="Search users"
            />
          </div>
          <button onClick={() => setCreating(true)} className="add-button">
            Add User
          </button>
          <button onClick={handleLogout} className="logout-button">
            Logout
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="users-table-container">
          {isLoading ? (
            <div className="loading-spinner">Loading...</div>
          ) : (
            <div className="users-table">
              <div className="users-table-header">
                <div className="table-cell id-cell">ID</div>
                <div className="table-cell name-cell">First Name</div>
                <div className="table-cell name-cell">Last Name</div>
                <div className="table-cell username-cell">Username</div>
                <div className="table-cell email-cell">Email</div>
                <div className="table-cell phone-cell">Phone</div>
                <div className="table-cell birthday-cell">Birthday</div>
                <div className="table-cell actions-cell"></div>
              </div>
              {filteredUsers.length === 0 ? (
                <div className="no-users-message">No users found.</div>
              ) : (
                filteredUsers.map((user, index) => (
                  <div
                    key={user.id}
                    className={`users-table-row ${index % 2 === 0 ? "even-row" : "odd-row"}`}
                  >
                    <div className="table-cell id-cell">{user.id}</div>
                    {editing?.id === user.id ? (
                      <>
                        <div className="table-cell name-cell">
                          <input
                            name="firstname"
                            value={editForm.firstname}
                            onChange={handleEditChange}
                            className="edit-input"
                          />
                        </div>
                        <div className="table-cell name-cell">
                          <input
                            name="lastname"
                            value={editForm.lastname}
                            onChange={handleEditChange}
                            className="edit-input"
                          />
                        </div>
                        <div className="table-cell username-cell">
                          <input
                            name="username"
                            value={editForm.username}
                            onChange={handleEditChange}
                            className="edit-input"
                          />
                        </div>
                        <div className="table-cell email-cell">
                          <input
                            name="email"
                            value={editForm.email}
                            onChange={handleEditChange}
                            className="edit-input"
                          />
                        </div>
                        <div className="table-cell phone-cell">
                          <input
                            name="phoneNumber"
                            value={editForm.phoneNumber || ""}
                            onChange={handleEditChange}
                            className="edit-input"
                          />
                        </div>
                        <div className="table-cell birthday-cell">
                          <input
                            name="birthday"
                            value={editForm.birthday || ""}
                            onChange={handleEditChange}
                            type="date"
                            className="edit-input"
                          />
                        </div>
                        <div className="table-cell actions-cell">
                          <button
                            onClick={() => handleEditSubmit(user.id)}
                            className="save-button"
                          >
                            Save
                          </button>
                          <button
                            onClick={() => setEditing(null)}
                            className="cancel-button"
                          >
                            Cancel
                          </button>
                        </div>
                      </>
                    ) : (
                      <>
                        <div className="table-cell name-cell">{user.firstname}</div>
                        <div className="table-cell name-cell">{user.lastname}</div>
                        <div className="table-cell username-cell">{user.username}</div>
                        <div className="table-cell email-cell">{user.email}</div>
                        <div className="table-cell phone-cell">{user.phoneNumber || "N/A"}</div>
                        <div className="table-cell birthday-cell">{user.birthday || "N/A"}</div>
                        <div className="table-cell actions-cell">
                          <button
                            className="edit-button"
                            onClick={() => startEditing(user)}
                            aria-label={`Edit user ${user.firstname} ${user.lastname}`}
                          >
                            <svg
                              width="16"
                              height="16"
                              viewBox="0 0 24 24"
                              fill="none"
                              xmlns="http://www.w3.org/2000/svg"
                            >
                              <path
                                d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13"
                                stroke="#0078ff"
                                strokeWidth="2"
                                strokeLinecap="round"
                                strokeLinejoin="round"
                              />
                              <path
                                d="M18.5 2.5C18.8978 2.10217 19.4374 1.87868 20 1.87868C20.5626 1.87868 21.1022 2.10217 21.5 2.5C21.8978 2.89783 22.1213 3.43739 22.1213 4C22.1213 4.56261 21.8978 5.10217 21.5 5.5L12 15L8 16L9 12L18.5 2.5Z"
                                stroke="#0078ff"
                                strokeWidth="2"
                                strokeLinecap="round"
                                strokeLinejoin="round"
                              />
                            </svg>
                            EDIT
                          </button>
                          <button
                            className="delete-button"
                            onClick={() => handleDeleteClick(user)}
                            aria-label={`Delete user ${user.firstname} ${user.lastname}`}
                          >
                            <svg
                              width="16"
                              height="16"
                              viewBox="0 0 24 24"
                              fill="none"
                              xmlns="http://www.w3.org/2000/svg"
                            >
                              <path
                                d="M3 6H5H21"
                                stroke="#ff0000"
                                strokeWidth="2"
                                strokeLinecap="round"
                                strokeLinejoin="round"
                              />
                              <path
                                d="M8 6V4C8 3.46957 8.21071 2.96086 8.58579 2.58579C8.96086 2.21071 9.46957 2 10 2H14C14.5304 2 15.0391 2.21071 15.4142 2.58579C15.7893 2.96086 16 3.46957 16 4V6M19 6V20C19 20.5304 18.7893 21.0391 18.4142 21.4142C18.0391 21.7893 17.5304 22 17 22H7C6.46957 22 5.96086 21.7893 5.58579 21.4142C5.21071 21.0391 5 20.5304 5 20V6H19Z"
                                stroke="#ff0000"
                                strokeWidth="2"
                                strokeLinecap="round"
                                strokeLinejoin="round"
                              />
                            </svg>
                            DELETE
                          </button>
                        </div>
                      </>
                    )}
                  </div>
                ))
              )}
            </div>
          )}

        </div>

        {/* Create Form Modal */}
        {creating && (
          <div className="modal-overlay">
            <div className="create-modal">
              <h2>Create New User</h2>
              <input
                name="firstname"
                value={createForm.firstname}
                onChange={handleCreateChange}
                placeholder="First Name"
                required
              />
              <input
                name="lastname"
                value={createForm.lastname}
                onChange={handleCreateChange}
                placeholder="Last Name"
                required
              />
              <input
                name="username"
                value={createForm.username}
                onChange={handleCreateChange}
                placeholder="Username"
                required
              />
              <input
                name="email"
                value={createForm.email}
                onChange={handleCreateChange}
                placeholder="Email"
                type="email"
                required
              />
              <input
                name="password"
                value={createForm.password}
                onChange={handleCreateChange}
                placeholder="Password"
                type="password"
                required
              />
              <input
                name="phoneNumber"
                value={createForm.phoneNumber}
                onChange={handleCreateChange}
                placeholder="Phone Number"
              />
              <input
                name="birthday"
                value={createForm.birthday}
                onChange={handleCreateChange}
                type="date"
              />
              <div className="modal-actions">
                <button onClick={handleCreateSubmit} className="confirm-button">
                  Create
                </button>
                <button onClick={() => setCreating(false)} className="cancel-button">
                  Cancel
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Delete Confirmation Modal */}
        {showDeleteModal && (
          <div className="modal-overlay">
            <div className="delete-modal">
              <h2 className="delete-modal-title">
                Are you sure you want to delete {userToDelete?.firstname} {userToDelete?.lastname}?
              </h2>
              <div className="delete-modal-actions">
                <button className="delete-confirm-button" onClick={confirmDelete}>
                  Yes, Delete
                </button>
                <button className="delete-cancel-button" onClick={cancelDelete}>
                  Cancel
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default AdminManageUsers;