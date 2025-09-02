import React from 'react'

function CourseDetailInfo({Name , Modules }) {
    
  return (
    <>
      <h2>{Name}</h2>
      <ul>
        {Modules.map((module) => (
          <li>{module}</li>
        ))}
      </ul>
    </>
  )
}

export default CourseDetailInfo
