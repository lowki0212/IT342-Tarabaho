import AdminNavbar from "../components/AdminNavbar"
import "../styles/admin-manage-users.css"

const AdminManageUsers = () => {
  // Sample user data - in a real app, this would come from an API or database
  const users = [
    {
      id: 1,
      name: "Angelo C. Quieta",
      username: "angeloquieta123",
      email: "quietaangelo@gmail.com",
      phone: "09266517720",
      birthday: "11/02/2002",
    },
    {
      id: 2,
      name: "Edrey P. Valle",
      username: "ederi23",
      email: "edersi@yahoo.com",
      phone: "9287632180",
      birthday: "05/31/2003",
    },
  ]

  return (
    <div className="admin-manage-users-page">
      {/* Using your existing AdminNavbar component */}
      <AdminNavbar />

      {/* Main Content */}
      <div className="manage-users-container">
        <h1 className="manage-users-title">MANAGE USERS</h1>

        <div className="users-table-container">
          <div className="users-table">
            <div className="users-table-header">
              <div className="table-cell id-cell">ID</div>
              <div className="table-cell name-cell">NAME</div>
              <div className="table-cell username-cell">USERNAME</div>
              <div className="table-cell email-cell">EMAIL</div>
              <div className="table-cell phone-cell">PHONE</div>
              <div className="table-cell birthday-cell">BIRTHDAY</div>
              <div className="table-cell actions-cell"></div>
            </div>

            {users.map((user) => (
              <div key={user.id} className="users-table-row">
                <div className="table-cell id-cell">{user.id}</div>
                <div className="table-cell name-cell">{user.name}</div>
                <div className="table-cell username-cell">{user.username}</div>
                <div className="table-cell email-cell">{user.email}</div>
                <div className="table-cell phone-cell">{user.phone}</div>
                <div className="table-cell birthday-cell">{user.birthday}</div>
                <div className="table-cell actions-cell">
                  <button className="edit-button">EDIT</button>
                  <button className="delete-button">DELETE</button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

export default AdminManageUsers
