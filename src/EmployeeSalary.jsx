import React from 'react'

function EmployeeSalaryInfo({Name , Department , Salary}) {
  return (
    <>
      <h2>Employee salary info</h2>
      <table border={1}>
        <thead>
            <th>Name</th>
            <th>Department</th>
            <th>Salary</th>
        </thead>
        <tr>
            <td>{Name}</td>
            <td>{Department}</td>
            <td>{Salary}</td>
        </tr>
      </table>
    </>
  )
}

export default EmployeeSalaryInfo
