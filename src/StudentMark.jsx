import React from 'react'

function StudentMarkInfo({Name , RollNo , Marks}) {
  return (
    <>
     <h2>Student Marks Info</h2>
      <table border={1}>
        <thead >
          <th>Name</th>
          <th>RollNO</th>
          <th>Marks</th>
        </thead>
        <tr>
          <td>{Name}</td>
          <td>{RollNo}</td>
          <td>{Marks}</td>
        </tr>
      </table>
    </>
  )
}

export default StudentMarkInfo
