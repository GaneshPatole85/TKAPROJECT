package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.controller;


import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Fine;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Report;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Review;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.User;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.servie.Service;

@RestController
public class Controller {
 @Autowired
 Service service;
    //user services
 @PostMapping("/register")
 public String RegisterUser(@RequestBody User user  , HttpSession session ) { 
	 String role = (String) session.getAttribute("role");
	 if(!"admin".trim().equalsIgnoreCase(role)) {
		 return "you cant be admin";
	  }
	 return  service.RegisterUser(user);
 }
 
 
 @PostMapping("/login")
 public String LoginUser( @RequestBody User user, HttpSession session) {
	    User loggedInUser = service.LoginUser(user); // service returns user from DB

	    if (loggedInUser != null) {
	        // ✅ Set session values based on the DB-verified user
	        session.setAttribute("userId", loggedInUser.getUserId());
	        session.setAttribute("role", loggedInUser.getRole()); // optional

	        return "✅ Login successful! Welcome, " + loggedInUser.getEmail();
	    } else   {
	    	return "❌ Login failed. Invalid credentials or user not found.";
	    }
	   
 }
 @GetMapping("/logout")
 public String logout( HttpSession session) {
	return service.logout(session);
 }
 @PutMapping("/forget")
 public String ForgetPassword(@RequestBody List<String> DataInput , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
	    if (userId== null) {
	        return "You must be logged in to Reset your Password";
	    }
	 return service.ForgetPassword(DataInput);
	 
 }
 @GetMapping("/getprofile")
 public List<User> GetProfile(HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
	    if (userId== null) {
	        return Collections.emptyList();
	    }
	return service.GetProfile(userId);
	
 }
 @GetMapping("/getallusers")
 public List<User> GetAllUser(HttpSession session , User user) {
	 String role = (String) session.getAttribute("role");
	 if(!"admin".trim().equalsIgnoreCase(role)) {
		 return Collections.emptyList();
	  }
	return service.GetAllUser(user);
	
 }
 @PutMapping("/update/role")
 public String UpdateUserRole(HttpSession session , @RequestBody User user) {
	 // ✅ Get userId from session only
	    Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
	    if (userId== null) {
	        return "❌ You must be logged in to update your role";
	    }
	    if(!"user".trim().equalsIgnoreCase(role)) {
	    	return "❌ You are not user Access diened!!";
	    }
	    
	return service.UpdateUserRole(userId , user);
 }
 @PutMapping("update/name")
 public String UpdateUserName(HttpSession session , @RequestBody User user) {
	// ✅ Get userId from session only
	    Long userId = (Long) session.getAttribute("userId");
	    if (userId== null) {
	        return "❌ You must be logged in to update your Name";
	    }
	    
	return service.UpdateUserName(userId , user);
 }
 @PutMapping("update/email/{id}")
 public String UpdateUserEmail(HttpSession session , @RequestBody User user) {
	 Long userId = (Long) session.getAttribute("userId");
	    if (userId== null) {
	        return "❌ You must be logged in to update your Email";
    }
	return service.UpdateUserEmail(userId , user);
 }
 @PostMapping("/delete/user")
 public String DeleteUser(@RequestBody User user  , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
     String role = (String) session.getAttribute("role");
	    if (userId== null) {
	        return "❌ You must be logged in to Delete profile";
	    }
	    if(!"admin".trim().equalsIgnoreCase(role)) {
	    	return "❌ You are not admin Access diened!!";
	    }
	return service.DeleteUser(user , role);
 }
 
 //book services
 @PostMapping("issued")
 public String IssueBook(HttpSession session,@RequestBody Book book) { 
	 Long userId = (Long) session.getAttribute("userId");
     String role = (String) session.getAttribute("role");
	    if (userId== null) {
	        return "❌ You must be logged in to Issue Book";
	    }
	    if(!"STUDENT".trim().equalsIgnoreCase(role)) {
	    	return "❌ You are not Student Access diened!!";
	    }
	    
	return service.IssueBook(userId , book);
 }
 @PostMapping("book/add")
 public String AddNewBook(@RequestBody Book book  , HttpSession session) {
	 // ✅ Get userId from session only
	    Long userId = (Long) session.getAttribute("userId");
	    String role = (String) session.getAttribute("role");
	    if (userId== null) {
	        return "❌ You must be logged in to add a book!";
	    }
	    if(!"admin".trim().equalsIgnoreCase(role)) {
	    	return "❌ You are not Admin Access diened!!";
	    }
	    return service.AddNewBook(book, userId, session); // pass session userId
 }
 @PutMapping("delete/book")
 public String DeleteBook(@RequestBody Book book  , HttpSession session)
 {  Long userId = (Long) session.getAttribute("userId");
     String role = (String) session.getAttribute("role");
    if (userId== null) {
        return "❌ You must be logged in to Delete Book";
    }
    if(!"admin".trim().equalsIgnoreCase(role)) {
    	return "❌ You are not Admin Access diened!!";
    }
   return	service.DeleteBook(book , userId , role); 
 }
 @PutMapping("update/author")
 public String UpdateBookInfoAuthor(@RequestBody Book book ,HttpSession session ) {
	 Long userId = (Long) session.getAttribute("userId");
     String role = (String) session.getAttribute("role");
    if (userId== null) {
        return "❌ You must be logged in to update Book author";
    }
    if(!"admin".trim().equalsIgnoreCase(role)) {
    	return "❌ You are not Admin Access diened!!";
    } 
	return service.UpdateBookInfoAuthor(book , userId , role);
 }
 @PutMapping("/update/totalcopies")
 public String UpdateTotalBookCopies(@RequestBody Book book , HttpSession session ) {
	 Long userId = (Long) session.getAttribute("userId");
     String Urole = (String) session.getAttribute("role");
    if (userId== null) {
        return "❌ You must be logged in to update Total Copies of Books";
    }
    if(!"admin".trim().equalsIgnoreCase(Urole)) {
    	return "❌ You are not Admin Access diened!!";
    } 
	return  service.UpdateTotalBookCopies(book , userId , Urole);
 }
 @PutMapping("user/setrole")
 public String SetRole(@RequestBody User user , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
     String Urole = (String) session.getAttribute("role");
    if (userId== null) {
        return "❌ You must be logged in to Set your role";
    }
    if(!"admin".trim().equalsIgnoreCase(Urole)) {
    	return "❌ You are not Admin Access diened!!";
    } 
	return service.SetRole(user , Urole);
 }
 @PutMapping("update/booktitle")
 public String UpdateBookTitle(@RequestBody Book book , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
     String Urole = (String) session.getAttribute("role");
    if (userId== null) {
        return "❌ You must be logged in to update Book Title";
    }
    if(!"admin".trim().equalsIgnoreCase(Urole)) {
    	return "❌ You are not Admin Access diened!!";
    } 
	return service.UpdateBookTitle(book , userId , Urole);
 }
 
 @PutMapping("update/bookcategory")
 public String UpdateBookCategoery(@RequestBody Book book , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
     String Urole = (String) session.getAttribute("role");
    if (userId== null) {
        return "❌ You must be logged in to update Book Category";
    }
    if(!"admin".trim().equalsIgnoreCase(Urole)) {
    	return "❌ You are not Admin Access diened!!";
    } 
	return service.UpdateBookCategoery(book , userId , Urole);
 }
 @GetMapping("/getallbooks")
 public List<Book> GetAllBooks() {
	return service.GetAllBooks();
 }
 
 @GetMapping("/getbookbyid")
 public Book GetBookById(@RequestBody Book book) {
	return service.GetBookById(book);
 }
 @PostMapping("book/return")
 public String ReturnBook(HttpSession session, @RequestBody Book book ) {
	 Long userId = (Long) session.getAttribute("userId");
     String Urole = (String) session.getAttribute("role");
    if (userId== null) {
        return "❌ You must be logged in to Return Book";
    }
    if(!"STUDENT".trim().equalsIgnoreCase(Urole)) {
    	return "❌ You are not Student Access diened!!";
    } 
	return	service.ReturnBook(userId , book);
	}
 @GetMapping("book/searchbyname")
 public Book SearchBookByName(@RequestBody Book book , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
    if (userId== null) {
        return null;
    }
     
	return service.SearchBookByName(book , userId);
 }
 
 @GetMapping("/books/count")
 public Number CountTotalBook() {
	 
	    return service.CountTotalBook();
 }
 @GetMapping("book/availablility")
 public int checkBookAvailability(@RequestBody Book book , HttpSession session ) {
	 Long userId = (Long) session.getAttribute("userId");
     
    if (userId== null) {
        return 0;
    } 
	return service.checkBookAvailability(book , userId);
 }
 @GetMapping("book/recently/issuedto")
 public User ViewRecentlyIssuedBook(@RequestBody Book book ,HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
    if (userId== null) {
        return null;
    }
	return service.ViewRecentlyIssuedBook(book , userId);
 }
 @GetMapping("book/viewIssuedHistory")
 public Book ViewBookissuedHistorybyUser(@RequestBody Book book ,HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
    if (userId== null) {
        return null;
    }
   	return service.ViewBookissuedHistorybyUser(book , userId);
 }
 @GetMapping("book/searchbyauthor")
 public Book SearchBookByAuthor(@RequestBody Book book , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
    if (userId== null) {
        return null;
    }
    
	return service.SearchBookByAuthor(book , userId);
 }
 @GetMapping("book/searchbycategory")
 public Book SearchBookByCategory(@RequestBody Book book ,HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
	    if (userId== null) {
	        return null;
	    }
	return service.SearchBookByCategory(book , userId);
 }
 @PutMapping("user/blockuser")
 public String BlockUser(@RequestBody User user  , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
     String Urole = (String) session.getAttribute("role");
    if (userId== null) {
        return "❌ You must be logged in to block user";
    }
    if(!"admin".trim().equalsIgnoreCase(Urole)) {
    	return "❌ You are not Admin Access diened!!";
    } 
	return service.BlockUser(user);
 }
 @PutMapping("user/unblockuser")
 public String UnBlockUser(@RequestBody User user , HttpSession session ) {
	 Long userId = (Long) session.getAttribute("userId");
     String Urole = (String) session.getAttribute("role");
    if (userId== null) {
        return "❌ You must be logged in to unblock user";
    }
    if(!"admin".trim().equalsIgnoreCase(Urole)) {
    	return "❌ You are not Admin Access diened!!";
    } 
	
	 return service.UnBlockUser(user);
 }
 @PostMapping("notice/duedate")
 public String NotifyUserOverdue(@RequestBody Book book , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
     String Urole = (String) session.getAttribute("role");
    if (userId== null) {
        return "❌ You must be logged in to unblock user";
    }
    if(!"admin".trim().equalsIgnoreCase(Urole)) {
    	return "❌ You are not Admin Access diened!!";
    } 
	 return service.NotifyUserOverdue(book  , userId);
 }
 @PostMapping("returnbook/daysleft")
 public long daysLeft(@RequestBody Book book , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
    if (userId== null) {
        return -3;
    }
	return  service.dayLeft(book ,  userId);
 }
 @GetMapping("book/issuedbookcountbyuser")
 public Long TotalIssuedBookCount( HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
	    if (userId== null) {
	        return -3L;
	    }
	return service.TotalIssuedBookCount( userId);
 }
 @PostMapping("/fineprocess")
 public String FineProcess(@RequestBody Fine fine , HttpSession session,@RequestParam Long bookid) {
	 Long userId = (Long) session.getAttribute("userId");
	    if (userId== null) {
	        return "You must be Logged in to pay fine";
	    }
	 if(bookid==null) {
		 return "book not found ";
	 }
	return   service.FineProcess(fine , userId , bookid);
 }
 @PostMapping("book/rating")
 public String BookRatingPoints(@RequestBody Book book  , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
	 String Urole = (String) session.getAttribute("role");
	    if (userId== null) {
	        return "You must be Logged in to Rate book";
	    }
	    if(!"student".trim().equalsIgnoreCase(Urole)) {
	    	return "❌ You are not student Access diened!!";
	    }
	   return service.BookRatingPoints(book , userId);
 }
 @GetMapping("book/searchbyrating")
 public List<Book> SearchBookByrating(@RequestBody Book book , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
	 String Urole = (String) session.getAttribute("role");
	    if (userId== null) {
	        return  Collections.emptyList();
	    }
	    if(!"student".trim().equalsIgnoreCase(Urole)) {
	    	return Collections.emptyList();
	    }
	return service.SearchBookByrating(book , userId);	
 }
 @GetMapping("permission/list")
 public Map<String, String> ListOfPermission() {
	return service.ListOfPermission();
 }
 @GetMapping("permission/list/student")
 public Map<String, String> ListOfpermissionforstudent() {
	return service.ListOfpermissionforstudent();
 }
 @GetMapping("permission/list/admin")
 public Map<String, String> ListOfpermissionforadmin() {
	return service.ListOfpermissionforadmin();
 }
 @PostMapping("enable2fa")
 @ResponseBody
 public String Enable2FA( @RequestBody User user , HttpSession session ) {
	 Long userId = (Long) session.getAttribute("userId");
	 String Urole = (String) session.getAttribute("role");
	    if (userId== null) {
	        return  "❌ You must be logged in to Disable 2FA";
	    }
	    if(!"student".trim().equalsIgnoreCase(Urole)) {
	    	return "this feature is only for student";
	    }
	return service.Enable2FA( user);
 }
 @PostMapping("disable2fa")
	public String Disable2FA(@RequestBody User user , HttpSession session) {
	 Long userId = (Long) session.getAttribute("userId");
	 String Urole = (String) session.getAttribute("role");
	    if (userId== null) {
	        return  "❌ You must be logged in to Disable 2FA";
	    }
	    if(!"student".trim().equalsIgnoreCase(Urole)) {
	    	return "this feature is only for student";
	    }
	return	service.Disable2FA(user);
	}
    @PostMapping("get2fastatus")
	public String Get2FAStatus(@RequestBody User user , HttpSession session) {
    	 Long userId = (Long) session.getAttribute("userId");
    	 String Urole = (String) session.getAttribute("role");
    	    if (userId== null) {
    	        return  "❌ You must be logged in to Disable 2FA";
    	    }
    	    if(!"student".trim().equalsIgnoreCase(Urole)) {
    	    	return "this feature is only for student";
    	    }
	return	service.Get2FAStatus(user);
	}
   
   @PostMapping("getfineinfo")
	public List GetFineInfo(@RequestBody Fine fine, HttpSession session) {
		Long UserId = (Long) session.getAttribute("userId");
		String role = (String) session.getAttribute("role");
		if (UserId == null) {
			return null; // User not logged in
		}
		if (!"student".trim().equalsIgnoreCase(role)) {
			return null; // Access denied for non-students
		}
	 return  service.GetFineInfo(fine, UserId);
   }
    @GetMapping("getallfineinfo")
	public List<Fine> getAllFineInfo(HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		String role = (String) session.getAttribute("role");
		if (userId == null) {
			return null; // User not logged in
		}
		if (!"admin".trim().equalsIgnoreCase(role)) {
			return null; // Access denied for non-admins
		}
	return   service.getAllFine(userId);
   }
   
    
   @DeleteMapping("deletefine")
   public String DeleteFine(@RequestBody Fine fine, HttpSession session) {
	           Long userId = (Long) session.getAttribute("userId");
	           String role = (String) session.getAttribute("role");
				if (userId == null) {
					return "❌ You must be logged in to delete a fine";
				}
				if (!"admin".trim().equalsIgnoreCase(role)) {
					return "❌ You are not authorized to delete fines";
				}
	     return service.DeleteFine(fine, userId);
   }
   
   @PutMapping("updatefineamount")
	public String UpdateFineAmount(@RequestBody Fine fine, HttpSession session , @RequestParam Long FineToUserId) {
		Long userId = (Long) session.getAttribute("userId");
		String role = (String) session.getAttribute("role");
		if (userId == null) {
			return "❌ You must be logged in to update ammount of  fine";
		}
		if (!"admin".trim().equalsIgnoreCase(role)) {
			return "❌ You are not authorized to to update amount of fines";
		}
	 return service.UpdateFineAmount(fine, userId , FineToUserId);
	   
   }
   
    @PutMapping("updatefinestatus")
	public String UpdateFineStatus(@RequestBody Fine fine, HttpSession session, @RequestParam Long FineToUserId) {
		Long userId = (Long) session.getAttribute("userId");
		String role = (String) session.getAttribute("role");
		if (userId == null) {
			return "❌ You must be logged in to update status of fine";
		}
		if (!"admin".trim().equalsIgnoreCase(role)) {
			return "❌ You are not authorized to update status of fines";
		}
	return	service.UpdateFineStatus(fine, userId, FineToUserId);
		
	}
   @GetMapping("getfinestatus")  
   public String GetFineStatus(@RequestBody Fine fine, HttpSession session) {
	           Long userId = (Long) session.getAttribute("userId");
	           String role = (String) session.getAttribute("role");
	                           if (userId == null) {
	                        return "❌ You must be logged in to get fine status";
	                           }
	                           if (!"student".trim().equalsIgnoreCase(role)) {
	                       return "❌ You are not authorized to get fine status";
	                           }
	         return   service.GetFineStatus(fine, userId);
   }
   
   @PostMapping("review/book/add")
   public String reviewBook(@RequestParam  Long bookId,  HttpSession session , @RequestBody Review review ) {
	     Long userId = (Long) session.getAttribute("userId");
			if (userId == null) {
				return "❌ You must be logged in to review a book";
			}
			String role = (String) session.getAttribute("role");
			if (!"student".trim().equalsIgnoreCase(role)) {
				return "❌ You are not authorized to review books" + ResponseEntity.status(HttpStatus.FORBIDDEN);
			}

	 return  service.reviewBook( bookId, userId  , review);
   }
   
   @DeleteMapping("review/delete")
	public String deleteReview(@RequestBody Review review, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			return " you Must be Logged in to Delete review"; // User not logged in
		}
		String role = (String) session.getAttribute("role");
		if (!"student".trim().equalsIgnoreCase(role)) {
			return "you are not Authorized"; // Access denied for non-students
		}
	 return	service.deleteReview(review, userId);
	}

	@GetMapping("getreview/book")
	public String getBookReviews(@RequestParam Long bookId, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			return "you must be logged in to get book reviews"; // User not logged in
		}
		String role = (String) session.getAttribute("role");
		if (!"student".trim().equalsIgnoreCase(role)) {
			return "not authorized"; // Access denied for non-students
		}
		return service.getBookReviews(bookId, userId);
	}
   @PutMapping("review/update")
	public String UpdateReview(@RequestBody Review review, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			return "you must be logged in to Update book reviews"; // User not logged in
		}
		String role = (String) session.getAttribute("role");
		if (!"student".trim().equalsIgnoreCase(role)) {
			return "you are not Authorized"; // Access denied for non-students
		}
	return	service.UpdateReview(review, userId);
	}
   
   @GetMapping("review/count")
   public int GetReviewCountByBookId(@RequestBody Book book, HttpSession session) {
	           Long userId = (Long) session.getAttribute("userId");
				if (userId == null) {	
					return -2 ; // User not logged in, do nothing
				}
				String role = (String) session.getAttribute("role");
				if (!"student".trim().equalsIgnoreCase(role)) {
					return -2 ; // Access denied for non-students, do nothing
					
					
				}
			return	service.GetReviewCountByBookId(book, userId);
   }
   
   @GetMapping("review/getall")
   public List<Review> GetAllReviews(HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		String role = (String) session.getAttribute("role");
		if (userId == null) {
			return Collections.emptyList(); // User not logged in, return empty list
			
		}
		if (!"admin".trim().equalsIgnoreCase(role)) {
			return Collections.emptyList(); // Access denied for non-admins, return empty list
		}
		return service.GetAllReviews(userId);
   }
    
    @PostMapping("report/review")
	public String reportreview(@RequestBody Review review, HttpSession session, Report report) {
		// ✅ Get userId from session only
	           Long userId = (Long) session.getAttribute("userId");
	                           if (userId == null) {
	                       return "You Must logged in to report review"; // User not logged in, do nothing
	                           }
	                           String role = (String) session.getAttribute("role");
	                             if (!"student".trim().equalsIgnoreCase(role)) {
	                            	 return "you are not authorized"; // Access denied for non-students, do nothing
	                             }
	                          
     return service.reportreview(review, userId , report);
   }
    @GetMapping("report/getall")
	public List<Report> GetAllReports(HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		String role = (String) session.getAttribute("role");
		if (userId == null) {
			return Collections.emptyList(); // User not logged in, return empty list
		}
		if (!"admin".trim().equalsIgnoreCase(role)) {
			return Collections.emptyList(); // Access denied for non-admins, return empty list
		}
		return service.GetAllReports(userId);
	}
     @PostMapping("report/searchbyid")
    public Report searchReportById(@RequestBody Report report, HttpSession session) {
     Long userId = (Long) session.getAttribute("userId");
     String role = (String) session.getAttribute("role");
		if (userId == null) {
			return null; // User not logged in, do nothing
		}
		if (!"admin".trim().equalsIgnoreCase(role)) {
			return null; // Access denied for non-admins, do nothing
		}
	return	service.searchReportById(report, userId);
    }
     
     @PutMapping("report/updatestatus")
     public String UpdateReportStatus(@RequestBody Report report , HttpSession session) {
    	 Long userId = (Long) session.getAttribute("userId");
         String role = (String) session.getAttribute("role");
    		if (userId == null) {
    			return "You must be Logged in to Update Report Status"; // User not logged in, do nothing
    		}
    		if (!"admin".trim().equalsIgnoreCase(role)) {
    			return " Access denied for non-admins"; // Access denied for non-admins, do nothing
    		}
    	  return  service.UpdateReportStatus(report, userId);
 	}
   
    @DeleteMapping("report/delete")
	public String DeleteReport(@RequestBody Report report , HttpSession session) {
    	 Long userId = (Long) session.getAttribute("userId");
         String role = (String) session.getAttribute("role");
    		if (userId == null) {
    			return "you must be logged in to Delete Report"; // User not logged in, do nothing
    		}
    		if (!"admin".trim().equalsIgnoreCase(role)) {
    			return "Acesss denied "; // Access denied for non-admins, do nothing
    		}
    return	service.DeleteReport(report, userId);
	}
    
    @GetMapping("report/resolved")
	public String SeeReportedReslovedReview(@RequestBody Report report, HttpSession session) {
    	Long userId = (Long) session.getAttribute("userId");
    	String role = (String) session.getAttribute("role");
    	        if (userId == null) {
    	      return "you must be logged in to See Resolved reports"; // User not logged in, do nothing
    	        }
    	        if (!"admin".trim().equalsIgnoreCase(role)) {
    	        	return "Access denied"; // Access denied for non-admins, do nothing
    	        }
    return  service.SeeReportedReslovedReview(report, userId);
	}
}

	

