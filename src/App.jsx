
import './App.css'
import ProductInfo from './ProductInfo'
import Usercard from './Usercard'
import GreetingInfo from './Greeting'
import BlogPostInfo from './BlogPost'
import StudentMarkInfo from './StudentMark'
import EmployeeSalaryInfo from './EmployeeSalary'
import MovieDetailInfo from './MovieDetail'
import ProductCostCalculation from './ShoppingCart'
import CourseDetailInfo from './CourseDetail'
import WheatherReportData from './WheatherReport'
function App() {
  //problem statement 1 data
  const userCard ={
    name: "Rahit Mehta",
    email: "Rohit.Mehta@example.com",
    roll: "Admin"
  }

  // problem statement 2 data

  const productinfo = {
    ProductName:'blutooth HeadPhone',
    Price:2499,
    InStock:true
  }

  //problem statement 3 data
  const nameForGreetings = {
    Name:'Sneha'
  }

  //problem statement 4 data
  const post ={
    Title:"Greeting Started with React",
    Author:"Kunal Varma",
    Content:"React is Java Script Library for UI development"
  }

  //problem statement 5 data
  const studentMarkInfo = {
    Name:'Aditi Sharma',
    RollNo:'STU102',
    Marks:35
  }

  //problem statement 6 data
const employeeSalary = {
  Name:'Rajesh Kumar',
  Department:'Finance',
  Salary:55000
}


//problem statement 7 data
const movieDetail = {
  Title:'3 idiots',
  Director:{
    Name:'Rajkumar Hirani',
    Age:60
  }
}

// problem statement 8 data
  const productCost = {
    Item:'Laptop',
    Quantity:2,
    Price:45000
    
  }

  // problem statement 9 data
  const coursedetails ={
    Name:'ReactJS',
    Modules:['Html' , 'css' , 'Js' , 'ReactJs']
  }
  
  // problem statement 10 data
  const wheatercondition ={
    City:'Delhi',
    Temprature:28,
    Condition:'Sunny'
  }
  return (
    <>
      <Usercard {...userCard} />
      <ProductInfo {...productinfo }  />
      <GreetingInfo {...nameForGreetings} />
      <BlogPostInfo {...post} />
      <StudentMarkInfo {...studentMarkInfo} />
      <EmployeeSalaryInfo {...employeeSalary} />
      <MovieDetailInfo {...movieDetail} />
      <ProductCostCalculation {...productCost} />
      <CourseDetailInfo  {...coursedetails} />
      <WheatherReportData {...wheatercondition} />
    </>
  )
}

export default App
