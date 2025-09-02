

function ProductInfo({ProductName , Price , InStock}) {
  return (
    <>
      <h2>Product Info</h2>
      <table border={1}>
        <thead>
            <th>Name</th>
            <th>Price</th>
            <th>Availability</th>
        </thead>
        <tr>
            <td>{ProductName}</td>
            <td>{Price}</td>
            <td>{InStock ? "In Stock" : "Out of Stock"}</td>
        </tr>
      </table>
    </>
  )
}

export default ProductInfo
