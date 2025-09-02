import React from 'react'

function ProductCostCalculation({Item , Price , Quantity }) {
  return (
    <>
      <h2>Item Cost</h2>
      <table border={1}>
        <thead>
            <th>Item</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Total</th>
        </thead>
        <tr>
            <td>{Item}</td>
            <td>{Price}</td>
            <td>{Quantity}</td>
             <td>{Quantity*Price}</td>
        </tr>
      </table>
    </>
  )
}

export default ProductCostCalculation
