import React from 'react'

function WheatherReportData({City , Temprature ,Condition }) {
  return (
    <>
      <h2>Wheather Report</h2>
      <table border={1}>
        <thead>
            <th>City</th>
            <th>Temprature in C</th>
            <th>Condition</th>
            <th>Temprature in F</th>
        </thead>
        <tr>
            <td>{City}</td>
            <td>{Temprature}</td>
            <td>{Condition}</td>
            <td>{(Temprature * 9/5) +32}</td>
        </tr>
      </table>
    </>
  )
}

export default WheatherReportData
