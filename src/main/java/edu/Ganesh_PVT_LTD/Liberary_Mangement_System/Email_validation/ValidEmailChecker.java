package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.Email_validation;

import java.awt.print.Book;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Fine;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Report;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Review;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.User;

public class ValidEmailChecker {
	 public static final  String Isvalid(User user) {
		 if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
		        return "❌ Email is required";
		    }
	        // Regex pattern for valid email addresses
	        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

	        // Compile the pattern
	        Pattern pattern = Pattern.compile(emailRegex);

	        // Match the input email
	        Matcher matcher = pattern.matcher(user.getEmail());

	        // Return true if matches, false otherwise
	        if (matcher.matches()) {
	           
	            return "✅ Valid email address:" + user.getEmail();
	        } else {
	            System.out.println();
	            return "❌ Invalid email address:" + user.getEmail();
	        }
	    }
	 
	 
	 public static String EmptyInputIdentifier(User user) {
		    if (user.getName() == null || user.getName().isBlank() ||
		        user.getEmail() == null || user.getEmail().isBlank() ||
		        user.getPassword() == null || user.getPassword().isBlank() ||
		        user.getRole() == null) {
		        
		        return "❌ All fields are required and must not be empty";
		    }
		    return null;
		}
	 public  static String EmptyInputIdentifierForLogin(User user ) {
		  if (
			        user.getEmail() == null || user.getEmail().isBlank() ||
			        user.getPassword() == null || user.getPassword().isBlank()) {
			        
			        return "❌ Email, Current Password, and New Password must not be empty";
			    }
			    return null; // ✅ Input is valid
	 }
	 public  static String EmptyInputIdentifierForForget(List<String> DataInput ) {
		  if (
			        DataInput.get(0) == null || DataInput.get(0).isBlank() ||
			        DataInput.get(1) == null || DataInput.get(0).isBlank() ||
			        DataInput.get(2) == null || DataInput.get(0).isBlank()) {
			        
			        return "❌ Email, Current Password, and New Password must not be empty";
			    }
			    return null; // ✅ Input is valid
	 }
	 public  static String EmptyInputName(User user ) {
		  if (user.getName()==null || user.getName().isBlank() ||user.getEmail()==null || user.getEmail().isBlank()) {
			        
			        return "❌ ALL Fields  Are Required";
			    }
			    return null; // ✅ Input is valid
	 }
	 public  static String OnlyEmptyInputName(User user ) {
		  if (user.getName()==null || user.getName().isBlank()) {
			        
			        return "❌ ALL Fields  Are Required";
			    }
			    return null; // ✅ Input is valid
	 }
	 public  static String OnlyEmptyInputrole(User user ) {
		  if (user.getRole()==null || user.getRole().isBlank() ) {
			        
			        return "❌ ALL Fields  Are Required";
			    }
			    return null; // ✅ Input is valid
	 }
	 public  static String EmptyInputRole(User user ) {
		  if (user.getRole()==null || user.getRole().isBlank() || user.getEmail()==null || user.getEmail().isBlank()) {
			        
			        return "❌ ALL Fields  Are Required";
			    }
			    return null; // ✅ Input is valid
	 }
	 public  static String EmptyInputEmail(User user ) {
		  if (user.getEmail()==null || user.getEmail().isBlank()) {
			        
			        return "❌ ALL Fields  Are Required";
			    }
			    return null; // ✅ Input is valid
	 }
	 public  static String EmptyInputId( edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book book ) {
		  if (  book.getBookId()<=0.0  || book.getBookId()<0 || book.getBookId()==null) {
			        
			        return "❌ Book Id Cant be Zero ";
			    }
			    return null; // ✅ Input is valid
	 }
	 public  static String EmptyInputIdforfine( Fine fine ) {
		  if ( fine.getBookId()<=0.0  || fine.getBookId()<0 || fine.getBookId()==null) {
			        
			        return "❌ Book Id Cant be Zero ";
			    }
			    return null; // ✅ Input is valid
	 }

		public static String EmptyInputForFineForFineTo(Fine fine) {
         if (fine.getFineTo() == null || fine.getFineTo() == null ) {
                return "❌ ALL Fields Are Required";
            }
            return null; // ✅ Input is valid
		}
	public static String IsvalidForLogin(User user) {
		 String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
		    Pattern pattern = Pattern.compile(emailRegex);
		    Matcher matcher = pattern.matcher(user.getEmail());

		    if (matcher.matches()) {
		    	 return null;  // null means valid
		    } else {
		        return "❌ Invalid email address: " + user.getEmail();
		    }
	}
	
	public static String IsvalidForForget(List<String> DataInput) {
		 String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
		    Pattern pattern = Pattern.compile(emailRegex);
		    Matcher matcher = pattern.matcher(DataInput.get(0));

		    if (matcher.matches()) {
		        return "✅ Valid email address: " + DataInput.get(0);
		    } else {
		        return "❌ Invalid email address: " +DataInput.get(0);
		    }
	}


	public static String EmptyInputId(Long userId, edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book book) {
		 if (userId==null || book.getBookId()==null) {
		        
		        return "❌ ALL Fields  Are Required";
		    }
		    return null; // ✅ Input is valid
	}
	public static String EmptyInputIdReview( Review review) {
		 if (review.getBookId()==null) {
		        
		        return "❌ Book id required for review";
		    }
		    return null; // ✅ Input is valid
	}
	
	public static String EmptyInputForBook( edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book book) {
		 if (book.getTitle()==null || book.getTitle().isBlank() 
			 || book.getAuthor()==null || book.getAuthor().isBlank() || book.getCategory()==null || book.getCategory().isBlank()  ) {
		        
		        return "❌ ALL Fields  Are Required";
		    }
		    return null; // ✅ Input is valid
	}
	public static String EmptyInputTitle( edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book book) {
		 if (book.getTitle()==null || book.getTitle().isBlank()) {
		        
		        return "❌ ALL Fields  Are Required";
		    }
		    return null; // ✅ Input is valid
	}
	public static String EmptyInputIssuedTo( edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book book) {
		 if (book.getIssuedTo()==null) {
		        
		        return "❌ ALL Fields  Are Required";
		    }
		    return null; // ✅ Input is valid
	}
	public static String EmptyInputCategory( edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book book) {
		 if (book.getCategory()==null || book.getCategory().isBlank()) {
		        
		        return "❌ ALL Fields  Are Required";
		    }
		    return null; // ✅ Input is valid
	}
	public static String EmptyInputForDeleteBook( edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book book) {
		 if (book.getBookId()==null || book.getTitle()==null || book.getTitle().isBlank()) {
		        
		        return "❌ ALL Fields  Are Required";
		    }
		    return null; // ✅ Input is valid
	}
	public static String EmptyInputForAuthor( edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book book) {
		 if (book.getAuthor()==null || book.getAuthor().isBlank()) {
		        
		        return "❌ ALL Fields  Are Required";
		    }
		    return null; // ✅ Input is valid
	}
	public static String EmptyInputForTotalCopies( edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book book) {
		 if (book.getTotalCopies()==0) {
		        
		        return "❌ Total Copy Value Required";
		    }
		    return null; // ✅ Input is valid
	}
	public static String EmptyInputIdforlogin( Long userId) {
		 if (userId==null || userId<0.0) {
		        
		        return "❌ user id required";
		    }
		    return null; // ✅ Input is valid
	}
	
	public static String EmptyInputIdforbook( Long bookId) {
		 if ( bookId<0.0 || bookId<0) {
		        
		        return "❌ book id required";
		    }
		    return null; // ✅ Input is valid
	}
	public static String EmptyInputFineId( Fine fine) {
		 if ( fine.getFineId()<0.0 || fine.getFineId()<0) {
		        
		        return "❌ fine id required";
		    }
		    return null; // ✅ Input is valid
	}
	public  static String isCommentEmpty(Review review) {
         if (review.getComment()==null || review.getComment().isBlank()) {
                    
                    System.out.println("❌ Comment is required");
                }
          return null; // ✅ Input is valid
        }
	public  static String isReviewIdEmpty(Review review) {
		if (review.getReviewId() == null || review.getReviewId() < 0) {

			return "❌ Review ID is required and must be a positive number";
                   
               }
         return null; // ✅ Input is valid
       }
	public  static String isReviewIdEmptyNewer(Long reviewId) {
        if (reviewId == null || reviewId < 0) {

    return "❌ Review ID is required and must be a positive number";
        
    }
return null; // ✅ Input is valid
}

	public  static String isReviewBookIdEmpty(Review review) {
		if (review.getBookId().getBookId() == null || review.getBookId().getBookId() < 0) {

			return "❌ Review ID is required and must be a positive number";
                   
               }
         return null; // ✅ Input is valid
       }


	public static String isReportIdEmpty(Report report) {
		if (report.getReportId() == null || report.getReportId() < 0) {
			return "❌ Report ID is required and must be a positive number";
		}
		return null;
	}

	}

