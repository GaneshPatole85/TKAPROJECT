

function BlogPostInfo({Title , Author , Content}) {
  return (
    <div>
      <h2>Blog Post</h2>
      <table border={1}>
        <thead>
            <th>Title</th>
            <th>Author</th>
            <th>Content</th>
        </thead>
        <tr>
            <td>{Title}</td>
            <td>{Author}</td>
            <td>{Content}</td>
        </tr>
      </table>
    </div>
  )
}

export default BlogPostInfo
