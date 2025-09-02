

function Usercard({name,email,roll}) {
  return (
    <>

      <h2 id="heading">User Info</h2>
      <table border={1}>
        <thead>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
        </thead>
        <tr>
            <td>{name}</td>
            <td>{email}</td>
            <td>{roll}</td>
        </tr>
      </table>
    </>
  )
}

export default Usercard
