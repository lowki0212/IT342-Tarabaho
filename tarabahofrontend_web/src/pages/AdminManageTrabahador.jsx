import { Link } from "react-router-dom"
import AdminNavbar from "../components/AdminNavbar"
import "../styles/admin-manage-trabahador.css"

const AdminManageTrabahador = () => {
  // Sample trabahador data - in a real app, this would come from an API or database
  const trabahadors = [
    {
      id: 1,
      name: "Paul Dave Q. Binoya",
      username: "polbin",
      email: "pauldeb@gmail.com",
      phone: "09266517720",
      birthday: "11/02/2002",
    },
    {
      id: 2,
      name: "Blake Mongoni",
      username: "blakesoy",
      email: "blakemongoni@yahoo.com",
      phone: "9287632180",
      birthday: "05/31/2003",
    },
  ]

  return (
    <div className="admin-manage-trabahador-page">
      {/* Using your existing AdminNavbar component */}
      <AdminNavbar />

      {/* Main Content */}
      <div className="manage-trabahador-container">
        <h1 className="manage-trabahador-title">MANAGE TRABAHADOR</h1>

        <div className="trabahador-table-container">
          <div className="trabahador-table">
            <div className="trabahador-table-header">
              <div className="table-cell id-cell">ID</div>
              <div className="table-cell name-cell">NAME</div>
              <div className="table-cell username-cell">USERNAME</div>
              <div className="table-cell email-cell">EMAIL</div>
              <div className="table-cell phone-cell">PHONE</div>
              <div className="table-cell birthday-cell">BIRTHDAY</div>
              <div className="table-cell actions-cell"></div>
            </div>

            {trabahadors.map((trabahador) => (
              <div key={trabahador.id} className="trabahador-table-row">
                <div className="table-cell id-cell">{trabahador.id}</div>
                <div className="table-cell name-cell">{trabahador.name}</div>
                <div className="table-cell username-cell">{trabahador.username}</div>
                <div className="table-cell email-cell">{trabahador.email}</div>
                <div className="table-cell phone-cell">{trabahador.phone}</div>
                <div className="table-cell birthday-cell">{trabahador.birthday}</div>
                <div className="table-cell actions-cell">
                  <Link to={`/admin/trabahador/${trabahador.id}`} className="view-details-button">
                    VIEW DETAILS
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

export default AdminManageTrabahador
