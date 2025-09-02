import React from 'react'

function MovieDetailInfo({Title , Director }) {
  return (
    <>
      <h2>Movie Info</h2>
      <table border={1}>
        <thead>
            <th>Title</th>
            <th>Director Name</th>
            <th> Director Age </th>
        </thead>
        <tr>
            <td>{Title}</td>
            <td>{Director.Name}</td>
           <td> {Director.Age}</td>
        </tr>
      </table>
    </>
  )
}

export default MovieDetailInfo
