package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.servie;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.Email_validation.ValidEmailChecker;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.Password_Security.Secure_Password;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.TransactionIdgenerator.GenerateTID;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.dao.Dao;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Fine;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Report;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Review;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.User;

@org.springframework.stereotype.Service
public class Service {
   @Autowired
   Dao dao;
	public String RegisterUser(User user) {
	    // Step 2: Validate Email Format
	    String emailValidation = ValidEmailChecker.Isvalid(user);
	    if (emailValidation.contains("❌")) {
	        return "❌ Invalid Email Format";
	    }

	    // Step 3: Validate Password Strength (before encryption)
	    String passwordCheck = Secure_Password.passcheck(user);
	    if (!passwordCheck.contains("✅")) {
	        return passwordCheck;
	    }
	    // Step 1: Validate Empty Inputs
	    String checkEmptyInput = ValidEmailChecker.EmptyInputIdentifier(user);
	    if (checkEmptyInput != null) {
	        return "❌ Invalid input: " + checkEmptyInput;
	    }
	    // Step 4: Encrypt Password
	    String encryptedPassword = Secure_Password.EncryptPass(user.getPassword());
	     user.setPassword(encryptedPassword);
        boolean Isregistered = dao.RegisterUser(user );
     return Isregistered ? "✅ Registered Successful" : "❌ Invalid credentials OR Email already Exist";
   
	}
	public User LoginUser(User user) {
		
	   
	   
	    // Step 4: Call DAO
	    return dao.LoginUser(user); // DAO will verify email & password
	}
	public String ForgetPassword(List<String> dataInput) {
//		// Step 1: Validate Empty Inputs
//	    String checkEmptyInput = ValidEmailChecker.EmptyInputIdentifierForLogin(user);
//	    if (checkEmptyInput != null) {
//	        return "❌ Invalid input: " + checkEmptyInput;
//	    }
		 // Step 2: Validate Email Format
	    String emailValidation = ValidEmailChecker.IsvalidForForget(dataInput);
	    if (emailValidation.contains("❌")) {
	        return "❌ Invalid Email Format";
	    }

	    // Step 3: Validate Password Strength (before encryption)
	    String passwordCheck = Secure_Password.passcheckForForget(dataInput);
	    if (!passwordCheck.contains("✅")) {
	        return passwordCheck;
	    }
	    String passwordCheckNew = Secure_Password.passcheckForForgetForNewPassword(dataInput);
	    if(!passwordCheckNew.contains("✅")) {
	    	return passwordCheckNew;
	    }
	    String oldPasswordEncrypted = Secure_Password.EncryptPass(dataInput.get(1));
        String newPasswordEncrypted = Secure_Password.EncryptPass(dataInput.get(2));
        if(oldPasswordEncrypted.equals(newPasswordEncrypted)) {
     	   return "❌ New Password Can Not Be Current Password";
      }
	Boolean IsUpdated =	dao.ForgetPassword(dataInput);
	return IsUpdated ? "✅ Password Updated  Successful" : "❌ Invalid credentials OR Email Dont Exist";
	}
	public List<User> GetProfile(Long id) {
	
	return	dao.GetProfile(id);
	
		
	}
	public List<User> GetAllUser(User user) {
	 return	dao.GetAllUser(user);
		
	}
	public String UpdateUserRole(Long id, User user) {
		 // Step 2: Validate Email Format
	    String emailValidation = ValidEmailChecker.Isvalid(user);
	    if (emailValidation.contains("❌")) {
	        return "❌ Invalid Email Format";
	    }
		String EmptyRole = ValidEmailChecker.EmptyInputRole(user);
		 if (EmptyRole != null) {
		        return "❌ Invalid input: " + EmptyRole;
		    }
	 
	Boolean IsRoleUpdated=	dao.UpdateUserRole(id  , user);
	return IsRoleUpdated ? "✅ Role Updated  Successful" : "❌ Invalid credentials OR ID Dont Exist";
		
	}
	public String UpdateUserName(Long id, User user) {
		 // Step 2: Validate Email Format
	    String emailValidation = ValidEmailChecker.Isvalid(user);
	    if (emailValidation.contains("❌")) {
	        return "❌ Invalid Email Format";
	    }
	String EmptyName = ValidEmailChecker.EmptyInputName(user);
	 if (EmptyName != null) {
	        return "❌ Invalid input: " + EmptyName;
	    }
	 Boolean IsNameUpdated = dao.UpdateUserName(id  , user);
	 return IsNameUpdated ? "✅ Name Updated  Successful" : "❌ Invalid credentials OR ID Dont Exist";
	}
	public String UpdateUserEmail(Long id, User user) {
		 String emailValidation = ValidEmailChecker.Isvalid(user);
		    if (emailValidation.contains("❌")) {
		        return "❌ Invalid Email Format";
		    }
		String EmptyName = ValidEmailChecker.EmptyInputEmail(user);
		 if (EmptyName != null) {
		        return "❌ Invalid input: " + EmptyName;
		    }
		
	boolean IsEmailUpdated =	dao.UpdateUserEmail(id , user);
	return IsEmailUpdated ? "✅ Email Updated  Successful" : "❌ Invalid credentials OR Email Already Taken";
	}
	public String IssueBook(Long userId, Book book) {
	
		String IsIdEmpty =  ValidEmailChecker.EmptyInputId(book);
	     
		 if (IsIdEmpty != null) {
		        return "❌ Invalid input: " + IsIdEmpty;
		    }
	 Boolean isIssued=	dao.IssueBook(userId , book);
	 return isIssued ? "✅ Book Issued  Successful" : "❌ Book Not Available OR User Not Register";
		
	}
	public String AddNewBook(Book book, Long userId, HttpSession session ) {
		String EmptyAddBookInfo =  ValidEmailChecker.EmptyInputForBook( book);
		 if (EmptyAddBookInfo != null) {
		        return "❌ Invalid input: " +EmptyAddBookInfo;
		    }
	 Boolean isAdded =	dao.AddNewBook(book , userId  ,session);
	 return isAdded ? "✅ Book Added Successful" : "❌ Register  First Or Book Already present";
		
	}
	
	
	
	public String DeleteBook(Book book, Long userId, String role) {
		

	    // Step 2: Validate book input before DAO call
	    String EmptyBookInput = ValidEmailChecker.EmptyInputForDeleteBook(book);
	    if (EmptyBookInput != null) {
	        return "❌ Invalid input: " + EmptyBookInput;
	    }

	    // Step 3: Proceed to delete
	    boolean isBookDeleted = dao.DeleteBook(book, userId, role);
	    return isBookDeleted ? "✅ Book Deleted Successfully" : "❌ Book Not Found or User Not Registered";
	}
	
	
	public String UpdateBookInfoAuthor(Book book, Long userId, String role) {
		 
		 // Validate input fields
	    String EmptyBookInput = ValidEmailChecker.EmptyInputForDeleteBook(book);
	    if (EmptyBookInput != null) {
	        return "❌ Invalid input: " +EmptyBookInput;
	    }
	    String EmptyBookInputAuthor = ValidEmailChecker.EmptyInputForAuthor(book);
	    if (EmptyBookInputAuthor != null) {
	        return "❌ Invalid input: " +EmptyBookInputAuthor;
	    }
		boolean isAuthorUpdated =dao.UpdateBookInfoAuthor(book, userId , role);
		return isAuthorUpdated ? "✅ Author Updated Successful" : "❌ Book Not Found Or User Not Register";
		
	}
	public String UpdateTotalBookCopies(Book book, Long userId, String urole) {
		 // Validate input fields
	    String EmptyBookInput = ValidEmailChecker.EmptyInputForDeleteBook(book);
	    if (EmptyBookInput != null) {
	        return "❌ Invalid input: " +EmptyBookInput;
	    }
	    String EmptyBookInputTotalCopy = ValidEmailChecker.EmptyInputForTotalCopies(book);
	    if (EmptyBookInputTotalCopy != null) {
	        return "❌ Invalid input: " +EmptyBookInputTotalCopy;
	    }
	boolean isTotalCopiesUpdated=	dao.UpdateTotalBookCopies(book , userId , urole);
	return isTotalCopiesUpdated ? "✅  Copies Updated Successful" : "❌ Book Not Found Or User Not Register";
	}
	public String logout(HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");

	    if (userId != null) { // ✅ user is logged in
	        session.invalidate(); // destroy session
	        return "✅ Logout successful!";
	    } else { // ❌ no user found in session
	        return "❌ No active session to logout.";
	    }
	}
	public String DeleteUser(User user, String role) {
  
	String IsEmailempty=	ValidEmailChecker.EmptyInputEmail(user);
	 if(IsEmailempty!=null) {
		 return "invalid Input:"+IsEmailempty;
	 }
	
	 
	Boolean IsDeleted =	dao.DeleteUser(user ,role);
	return IsDeleted ? "✅  User Deleted Successfull" : "❌  User Not Found";
	}
	public String SetRole(User user, String role) {
		String IsnameEmpty =  ValidEmailChecker.OnlyEmptyInputName(user);
		String isrollempty = ValidEmailChecker.OnlyEmptyInputrole(user);
		if(IsnameEmpty!=null) {
			return "invalid input:"+IsnameEmpty;
		}
		if(isrollempty!=null) {
			return "invalid input:"+isrollempty;
		}
	 boolean IsroleSet=	dao.SetRole(user , role);
	 return IsroleSet ? "✅  Role has been set " : "❌  User Not Found";
	}
	public String UpdateBookTitle(Book book, Long userId, String role) {
		String isUserIdorBookIdempty = ValidEmailChecker.EmptyInputId(userId ,book);
		if(isUserIdorBookIdempty!=null) {
			return "invalid input:"+isUserIdorBookIdempty;
		}
		String EmptyBooktitle = ValidEmailChecker.EmptyInputTitle(book);
		if(EmptyBooktitle!=null) {
			return "invalid input:"+EmptyBooktitle;
		}
		
	boolean isTitleUpdated =	dao.UpdateBookTitle(book , userId , role);
	return isTitleUpdated ? "✅  Book Title has been Updated Successfully " : "❌  User Not Found or Book not Found";	
	}
	public String UpdateBookCategoery(Book book, Long userId, String role) {
		String isUserIdorBookIdempty = ValidEmailChecker.EmptyInputId(userId ,book);
		if(isUserIdorBookIdempty!=null) {
			return "invalid input:"+isUserIdorBookIdempty;
		}
		String EmptyBooktitle = ValidEmailChecker.EmptyInputCategory(book);
		if(EmptyBooktitle!=null) {
			return "invalid input:"+EmptyBooktitle;
		}
		
	 boolean iscategoryUpdated =	dao.UpdateBookCategoery(book , userId , role);
	return iscategoryUpdated ? "✅  Book category has been Updated Successfully " : "❌  User Not Found or Book not Found";	
	}
	public List<Book> GetAllBooks() {
		// TODO Auto-generated method stub
		 return dao.GetAllBooks();
	}
	public Book GetBookById(Book book) {
		 String isIdempty =   ValidEmailChecker.EmptyInputId(book);
		 if(isIdempty!=null) {
			 return null;
		 }
	return	dao.GetBookById(book);
		
	}
	public String ReturnBook(Long userId, Book book) {
		String IsBookidEmpty=   ValidEmailChecker.EmptyInputId(book);
		if(IsBookidEmpty!=null) {
			return "invalid input"+IsBookidEmpty;
			}
	Boolean isBookReturned =dao.ReturnBook(userId , book);
	return isBookReturned 
		    ? "✅ Book has been successfully returned." 
		    : "❌ Book not found, not issued to this user, or user not found.";
	}
	public Book SearchBookByName(Book book, Long userId) {
		String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
		if(IsUserExistAndIsBookExist!=null) {return null;}
		String istitleempty = ValidEmailChecker.EmptyInputTitle(book);
		if(istitleempty!=null) {return null;}
	return	dao.SearchBookByName(book , userId);
		
	}
	public Number CountTotalBook() {
	return	dao.CountTotalBook( );
	}
	public int checkBookAvailability(Book book, Long userId) {
		String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
		if(IsUserExistAndIsBookExist!=null) {return 0;}
		String istitleempty = ValidEmailChecker.EmptyInputTitle(book);
		if(istitleempty!=null) {return 0;}
		return dao.checkBookAvailability(book , userId);
		
	}
	public User ViewRecentlyIssuedBook(Book book, Long userId) {
		String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
		if(IsUserExistAndIsBookExist!=null) {return null;}
		String istitleempty = ValidEmailChecker.EmptyInputTitle(book);
		if(istitleempty!=null) {return null;}
        return	dao.ViewRecentlyIssuedBook(book , userId);
		
	}
	public Book ViewBookissuedHistorybyUser(Book book, Long userId) {
		String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
		if(IsUserExistAndIsBookExist!=null) {return null;}
		String isissuedisempty = ValidEmailChecker.EmptyInputIssuedTo(book);
		if(isissuedisempty!=null) {return null;}
    	return  dao.ViewBookissuedHistorybyUser(book , userId);
		
	}
	public Book SearchBookByAuthor(Book book, Long userId) {
		String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
		if(IsUserExistAndIsBookExist!=null) {return null;}
		String istitleempty = ValidEmailChecker.EmptyInputForAuthor(book);
		if(istitleempty!=null) {return null;}
	  return	dao.SearchBookByAuthor(book , userId);
		
	}
	public Book SearchBookByCategory(Book book, Long userId) {
		String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
		if(IsUserExistAndIsBookExist!=null) {return null;}
		String istitleempty = ValidEmailChecker.EmptyInputCategory(book);
		if(istitleempty!=null) {return null;}
	return	dao.SearchBookByCategory(book , userId);
		
	}
	public String BlockUser(User user) {
		String ismailEmpty = ValidEmailChecker.EmptyInputEmail(user);
		if(ismailEmpty!=null) {return "Invalid Input:"+ismailEmpty;}
	String IsEmailValid =	ValidEmailChecker.IsvalidForLogin(user);
	if(IsEmailValid!=null) {return "invalid Input:"+IsEmailValid;}
	Boolean isUserBlocked = dao.BlockUser(user);
	return isUserBlocked   ? "✅ User has been Blocked successfully ." : "❌ Email Not Found or User Already Blocked .";
		
	}
	public String UnBlockUser(User user) {
		String ismailEmpty = ValidEmailChecker.EmptyInputEmail(user);
		if(ismailEmpty!=null) {return "Invalid Input:"+ismailEmpty;}
	String IsEmailValid =	ValidEmailChecker.IsvalidForLogin(user);
	if(IsEmailValid!=null) {return "invalid Input:"+IsEmailValid;}
	Boolean IsUnblocked =	dao.UnBlockUser(user);
	return IsUnblocked   ? "✅ User has been unblocked successfully ." : "❌ Email Not Found Or User Already Unblocked .";
		
	}
	public String NotifyUserOverdue(Book book, Long userId) {
		String IsIdempty =	ValidEmailChecker.EmptyInputId(book);
		if(IsIdempty!=null) {return "invalid input"+IsIdempty;}
		String isIssuedUseridEmpty =	ValidEmailChecker.EmptyInputIssuedTo(book);
		if(isIssuedUseridEmpty!=null) {return "invalid Input"+isIssuedUseridEmpty;}	
	 Boolean isNotificationSend =	dao.NotifyUserOverdue(book , userId);
	 return isNotificationSend   ? "✅ Over due Notification Sent to User ." : "❌ Book is not Issued to this user or return date is not overdue or Book not found";
		
	}
	public long dayLeft(Book book, Long userId) {
	return	dao.daysLeft(book , userId);
		
	}
	public Long TotalIssuedBookCount(Long userId) {
	return	dao.TotalIssuedBookCount( userId);
		
	}
	public String FineProcess(Fine fine, Long userId, Long bookid) {
		String txid = GenerateTID.processDummyPayment();
        if(txid==null) {
	     return "Failed to generate transaction id";
        }
        boolean isfinealreadypaid = fine.getStatus();
	    if(isfinealreadypaid==false ) {
	    	return "fine is already paid";
	    }
	Boolean isFinePaid =	dao.FineProcess(fine , userId , bookid);
	return isFinePaid   ? "✅ Fine has been paid ." : "❌ Book is not Issued to this user or not over due ";
	}
	public String BookRatingPoints(Book book, Long userId) {
		double ratingpoints = book.getRating_points();
		if(ratingpoints>10.0 || ratingpoints<0.0) {
			return "The Book Rating Points Cant be 11 or more  ";
		}
		 String isIdempty = ValidEmailChecker.EmptyInputId(book);
		  if(isIdempty!=null) {
			  return "invalid input:"+isIdempty;
		  }
	boolean isBookRatingGiven =	dao.BookRatingPoints(book , userId);
	return isBookRatingGiven   ? "✅ Thanks For Rating." : "❌ Book is not Issued to this user or book not found ";	
	}
	public List<Book> SearchBookByrating(Book book, Long userId) {
	return	dao.SearchBookByrating(book , userId);
		
	}
	public Map<String, String> ListOfPermission() {
	return	dao.ListOfPermission();
		
	}
	public Map<String, String> ListOfpermissionforstudent() {
	return	dao.ListOfpermissionforstudent();
		
	}
	public Map<String, String> ListOfpermissionforadmin() {
		
	return	dao.ListOfpermissionforadmin();
	}
	public String Enable2FA(User user) {
		String IsnameinputEmpty =	ValidEmailChecker.EmptyInputName(user);
		if(IsnameinputEmpty!=null) {
			System.out.println("❌ Invalid Input (Name): " + IsnameinputEmpty);
			return "invalid input:"+IsnameinputEmpty;
		}
		
  	boolean is2faenbaled =	dao.Enable2FA( user);
   return is2faenbaled ? "2FA has been enabled":"username not found or Seceret key does not match or already enabled or key generataion is processing";
	}
	public String Disable2FA(User user) {
		String IsnameinputEmpty =	ValidEmailChecker.OnlyEmptyInputName(user);
		if(IsnameinputEmpty!=null) {
			System.out.println("❌ Invalid Input (Name): " + IsnameinputEmpty);
			return "invalid input:"+IsnameinputEmpty;
		}
	boolean is2FaDisabaled =	dao.Disable2FA(user);
	return is2FaDisabaled ? "2FA has been disabled" : "username not found or 2FA is not enabled or already disabled ";
		
	}
	public String Get2FAStatus(User user) {
		String IsnameinputEmpty =	ValidEmailChecker.OnlyEmptyInputName(user);
        if(IsnameinputEmpty!=null) {
            System.out.println("❌ Invalid Input (Name): " + IsnameinputEmpty);
            return "invalid input:"+IsnameinputEmpty;
        }
	boolean getstatus =	dao.Get2FAStatus(user);
	return getstatus ? "2FA is enabled" : "2FA is not enabled";	
	}
	public List GetFineInfo(Fine fine, Long userId) {
	return	dao.GetFineInfo(fine, userId);
		
	}
	public List<Fine> getAllFine(Long userId) {
	return	dao.getAllFine(userId);
		
	}
	public  String DeleteFine(Fine fine, Long userId) {
		String isBookIdEmpty = ValidEmailChecker.EmptyInputIdforfine(fine);
		if (isBookIdEmpty != null) {
			System.out.println("Book ID is empty");
			return "invalid input: " + isBookIdEmpty;
		}
		String FineIdempty = ValidEmailChecker.EmptyInputFineId(fine);
		if(FineIdempty != null) {
			System.out.println("Fine ID is empty");
			return "invalid input: " + FineIdempty;
		}
		String FineToEmpty = ValidEmailChecker.EmptyInputForFineForFineTo(fine);
        if (FineToEmpty != null) {
         System.out.println("FineTo user ID is empty");
          return "invalid input: " + FineToEmpty;
         }
	boolean isFineDeleted =	dao.DeleteFine(fine, userId);
	return isFineDeleted ? "✅ Fine Deleted Successfully" : "❌ Fine Not Found or User Not Registered";
		
	}
	public String reviewBook( Long bookId , Long userId, Review review) {
			if (ValidEmailChecker.isCommentEmpty(review) != null) {
	           
	            return "comment is empty";
	        }
	boolean isreviwed =	dao.reviewBook(bookId, userId , review);
	return isreviwed ? "✅ Thanks for feedback": "book not found or not issued to this user or review already given ";
		
	}
	public String UpdateFineAmount(Fine fine, Long userId, Long fineToUserId) {
		String isBookIdEmpty = ValidEmailChecker.EmptyInputIdforfine(fine);
		if (isBookIdEmpty != null) {
			System.out.println("Book ID is empty");
			return "invalid input: " + isBookIdEmpty;
		}
		String FineIdempty = ValidEmailChecker.EmptyInputFineId(fine);
		if(FineIdempty != null) {
			System.out.println("Fine ID is empty");
			return "invalid input: " + FineIdempty;
		}

	boolean isFineAmountUpdated =	dao.UpdateFineAmount(fine, userId, fineToUserId);
	return isFineAmountUpdated ? "✅ Fine Amount Updated Successfully" : "❌ Fine Not Found or User Not Registered";	
	}
	public String UpdateFineStatus(Fine fine, Long userId, Long fineToUserId) {
		String isBookIdEmpty = ValidEmailChecker.EmptyInputIdforfine(fine);
		if (isBookIdEmpty != null) {
			System.out.println("Book ID is empty");
			return "invalid input: " + isBookIdEmpty;
		}
		String FineIdempty = ValidEmailChecker.EmptyInputFineId(fine);
		if(FineIdempty != null) {
			System.out.println("Fine ID is empty");
			return "invalid input: " + FineIdempty;
		}
		boolean IsfineStatusAlreadyPaid = fine.getStatus();
		if(IsfineStatusAlreadyPaid) return "❌ Fine is already paid, no need to update status";; 
	boolean isStatusUpdated =	dao.UpdateFineStatus(fine, userId, fineToUserId);
	return isStatusUpdated ? "✅ Fine Status Updated Successfully" : "❌ Fine Not Found or User Not Registered";
	}
	public String GetFineStatus(Fine fine, Long userId) {
		String isBookIdEmpty = ValidEmailChecker.EmptyInputIdforfine(fine);
		if (isBookIdEmpty != null) {
			System.out.println("Book ID is empty");
			return "invalid input: " + isBookIdEmpty;
		}
		String FineIdempty = ValidEmailChecker.EmptyInputFineId(fine);
		if (FineIdempty != null) {
			System.out.println("Fine ID is empty");
			return "invalid input: " + FineIdempty;
		}
		
	boolean isPaid =	dao.GetFineStatus(fine, userId);
	return isPaid ? "✅ Fine is paid" : "❌ Fine is not paid";
		
	}
	public String deleteReview(Review review, Long userId) {
		String isreviewIdEmpty = ValidEmailChecker.isReviewIdEmpty(review);
		if(isreviewIdEmpty != null) {
			return "invalid input: " + isreviewIdEmpty;
		}
		String isReviewBookIdempty = ValidEmailChecker.isReviewBookIdEmpty(review);
        if(isReviewBookIdempty != null) {
        return "invalid input: " + isReviewBookIdempty;
        }
	boolean isFinedelete =	dao.deleteReview(review, userId);
	return isFinedelete ? "✅ Review Deleted Successfully" : "❌ Review Not Found or User Not Registered";
		
	}
	public String getBookReviews(Long bookId, Long userId) {
	return	dao.getBookReviews(bookId, userId);
		
	}
	public String UpdateReview(Review review, Long userId) {
		String isreviewIdEmpty = ValidEmailChecker.isReviewIdEmpty(review);
		if (isreviewIdEmpty != null) {
			return "invalid input: " + isreviewIdEmpty;
		}
		String isReviewBookIdempty = ValidEmailChecker.isReviewBookIdEmpty(review);
		if (isReviewBookIdempty != null) {
			return "invalid input: " + isReviewBookIdempty;
		}
	boolean isReviewUpdated =	dao.UpdateReview(review, userId);
	return isReviewUpdated ? "✅ Review Updated Successfully" : "❌ Review Not Found or User Not Registered";
		
	}
	public int GetReviewCountByBookId(Book book, Long userId) {
		String isBookIdEmpty = ValidEmailChecker.EmptyInputId(book);
		if (isBookIdEmpty != null) {
			System.out.println("Book ID is empty");
			return -5; // Return -5 if book ID is empty;
		}
		
	int isreviewCountExist =	dao.GetReviewCountByBookId(book, userId);
	return isreviewCountExist >= 0 ? isreviewCountExist : -4; // Return -4 if no reviews found or book not found
		
	}
	public List<Review> GetAllReviews(Long userId) {
		
		return dao.GetAllReviews(userId);
	}
	public String reportreview(Review review, Long userId, Report report) {
		String isreviewIdEmpty = ValidEmailChecker.isReviewIdEmpty(review);
		if (isreviewIdEmpty != null) {
			System.out.println("❌ Invalid Input (Review ID): " + isreviewIdEmpty);
			return "invalid input: " + isreviewIdEmpty;
		}
		String isReviewBookIdempty = ValidEmailChecker.isReviewBookIdEmpty(review);
		if (isReviewBookIdempty != null) {
			System.out.println("❌ Invalid Input (Book ID): " + isReviewBookIdempty);
			return "invalid input: " + isReviewBookIdempty;
		}
		boolean isReported = dao.reportreview(review, userId , report);
		return isReported ? "✅ Review reported successfully." : "❌ Failed to report review or review not found.";
		
	}

}
