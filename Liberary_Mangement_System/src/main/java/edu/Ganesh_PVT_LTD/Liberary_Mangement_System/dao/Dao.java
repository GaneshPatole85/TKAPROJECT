package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.dao;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.Email_Sender.Email_Automation;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.Email_validation.ValidEmailChecker;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.Password_Security.Secure_Password;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.TransactionIdgenerator.GenerateTID;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Book;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Fine;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Report;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.Review;
import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.User;

@Repository
public class Dao {
	 @Autowired
	 SessionFactory factory;
	 final Logger logger = Logger.getLogger(Dao.class.getName());
	public boolean RegisterUser(User user) {
		 Session session = factory.openSession();
		    Transaction tx1 = session.beginTransaction();

		    try {
		        // ✅ Corrected: Validate password using actual user input, not passwordValidation string
		        String passwordValidation = Secure_Password.IsPasswordStrong(user);
		        if (!passwordValidation.contains("✅")) {
		            System.out.println("❌ Weak password: " + passwordValidation);
		            return false;
		        }

		        // ✅ Corrected: Encrypt the actual user password, not validation string
		        String encryptedPassword = Secure_Password.EncryptPass(user.getPassword()); 
		        user.setPassword(encryptedPassword); 

		        // ✅ No change needed here — still validating for empty input
		        String checkEmptyInput = ValidEmailChecker.EmptyInputIdentifier(user);
		        if (checkEmptyInput != null) {
		            System.out.println("❌ Invalid input: " + checkEmptyInput);
		            return false;
		        }

		        // ✅ No change — email format validation
		        String emailValidation = ValidEmailChecker.Isvalid(user);
		        if (emailValidation.contains("❌")) {
		            System.out.println(emailValidation);
		            return false;
		        }

		        // ✅ Corrected: Get User object from DB instead of casting to String
		        Criteria ct = session.createCriteria(User.class);
		        ct.add(Restrictions.eq("email", user.getEmail()));
		        User existingUser = (User) ct.uniqueResult(); 
		        if (existingUser != null) { 
		            System.out.println("❌ User already exists.");
		            return false;
		        }
             
		       
		         session.save(user);
		        tx1.commit();
		        // ✅ No change — send email after successful registration
		        Email_Automation.sendEmail(user);

		        return true;

		    } catch (Exception e) {
		        tx1.rollback();
		        System.out.println("ROLLBACK: " + e.getMessage());
		    } finally {
		        session.close();
		    }

		    return false;
		
	}
	public User LoginUser(User user) {
		  Session session = factory.openSession();
		    Transaction tx1 = session.beginTransaction();

		    try {
		    	Long userIdforLogin = user.getUserId();
		    	ValidEmailChecker.EmptyInputIdforlogin(userIdforLogin);
		    
		        @SuppressWarnings({ "unchecked", "deprecation" })
		        NativeQuery<User> nq = session.createSQLQuery(
		        	    "SELECT * FROM user WHERE user_id = :id"
		        	)
		        	.addEntity(User.class)
		        	.setParameter("id", userIdforLogin);
		        User result = nq.uniqueResult();
		        tx1.commit();
		        return result; 

		    } catch (Exception e) {
		        tx1.rollback();
		        System.out.println("ROLLBACK: " + e.getMessage());
		    } finally {
		        session.close();
		    }
		    return null;  		
	}
	public boolean ForgetPassword(List<String> DataInput) {
		 Session session = factory.openSession();
		    Transaction tx1 = session.beginTransaction();

		    try {
		        // Encrypt passwords
		        String oldPasswordEncrypted = Secure_Password.EncryptPass(DataInput.get(1));
		        String newPasswordEncrypted = Secure_Password.EncryptPass(DataInput.get(2));

		        // Step 1: Validate inputs
		        String checkEmptyInput = ValidEmailChecker.EmptyInputIdentifierForForget(DataInput);
		        if (checkEmptyInput != null) {
		            System.out.println("❌ Invalid input: " + checkEmptyInput);
		            return false;
		        }

		        // Step 2: Validate email format
		        String emailValidation = ValidEmailChecker.IsvalidForForget(DataInput);
		        if (emailValidation.contains("❌")) {
		            System.out.println(emailValidation);
		            return false;
		        }

		        // Step 3: Query to check user by email and current password
		        @SuppressWarnings("deprecation")
				Criteria ct = session.createCriteria(User.class);
		        ct.add(Restrictions.eq("email", DataInput.get(0)));
		        ct.add(Restrictions.eq("password", oldPasswordEncrypted));

		        @SuppressWarnings("unchecked")
				List<User> matchedUsers = ct.list(); // use list() instead of uniqueResult()

		        if (matchedUsers.size() == 1) {
		            User existingUser = matchedUsers.get(0);
		            existingUser.setPassword(newPasswordEncrypted);
		            session.update(existingUser);
		            tx1.commit();
		            Email_Automation.sendEmailForgetPassword(existingUser);
		            return true;
		          
		        } else if (matchedUsers.size() > 1) {
		            System.out.println("❌ Multiple users found with same credentials. Data issue.");
		        } else {
		            System.out.println("❌ Invalid credentials.");
		        }

		    } catch (Exception e) {
		        tx1.rollback();
		        System.out.println("ROLLBACK: " + e.getMessage());
		    } finally {
		        session.close();
		    }

		    return false;
		
	}
	 
	public List<User> GetProfile(Long id) {
		 Session session = factory.openSession();
		    Transaction tx = session.beginTransaction();
		    String hql = "FROM User WHERE userId = :id";
		    Query<User> query = session.createQuery(hql, User.class);
		    query.setParameter("id", id);
		   List<User> userlist = query.getResultList();
		    if (userlist == null) {
		    	throw new Error("User not found with ID: " + id);
		    }
		    tx.commit();
		    return  userlist;	    
		
	}
	
	public List<User> GetAllUser(User user) {
		Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();
	    String hql = "FROM User";
	  
	    Query<User> query = session.createQuery(hql, User.class);
	   List<User> ListOfUser = query.list();
	    if (ListOfUser == null) {
	    	throw new Error("User not found with ID: ");
	    }
	    tx.commit();
	    return ListOfUser;
		
	}
	@SuppressWarnings("deprecation")
	public boolean UpdateUserRole(Long id  , User user ) {
		 Session session = factory.openSession();
		    Transaction tx1 = session.beginTransaction();

		    try {
		        // ✅ Fetch the user from DB by session user ID (id)
		        User existingUser = session.get(User.class, id);
                String EmailExist = user.getEmail();
		        // ❌ Don't fetch using incoming email again — you are updating existing user, not checking another
		        // So just validate the format of email, not duplicate
		        Criteria ct = session.createCriteria(User.class);
		        ct.add(Restrictions.eq("email", EmailExist));
		        User isemailExist = (User) ct.uniqueResult();
                if(isemailExist==null) {
                	return false;
                }
		        // ✅ Check for empty role input
		        String emptyRole = ValidEmailChecker.EmptyInputRole(user);
		        if (emptyRole != null) {
		            System.out.println("❌ Invalid Role Input: " + emptyRole);
		            return false;  // ❌ You mistakenly had 'return true;' here
		        }

		        // ✅ Validate email format (if required)
		        String emailValidation = ValidEmailChecker.Isvalid(user);
		        if (emailValidation.contains("❌")) {
		            System.out.println(emailValidation);
		            return false;
		        }

		        // ✅ Update only if user exists
		        if (existingUser != null && EmailExist!=null) {
		           
		            existingUser.setRole(user.getRole());

		            session.update(existingUser);
		            tx1.commit();

		            // ✅ Optional: send email update confirmation
		            Email_Automation.sendEmailInfoUpdate(existingUser);

		            return true;
		        }

		    } catch (Exception e) {
		        tx1.rollback();
		        System.out.println("ROLLBACK: " + e.getMessage());
		    } finally {
		        session.close();
		    }

		    return false;
		
	}
	@SuppressWarnings("deprecation")
	public boolean UpdateUserName(Long id, User user) {
		Session session = factory.openSession();
        Transaction tx1 = session.beginTransaction();
        try {
            User existingUser = session.get(User.class, id);
            String EmailEXist = user.getEmail();
			Criteria ct = session.createCriteria(User.class);
            ct.add(Restrictions.eq("email", EmailEXist));
           User isemailExist = (User) ct.uniqueResult();
           String emailValidation = ValidEmailChecker.Isvalid(user);
           if (emailValidation.contains("❌")) {
               System.out.println(emailValidation);  // Print or log invalid email
               return false;
           }
          String EmptyName =  ValidEmailChecker.EmptyInputName(user);
          if (EmptyName != null ) {
	            System.out.println("❌ Invalid Name Or Empty Name: " + EmptyName);
	            return false;
	        }
            if (existingUser!= null && isemailExist!=null) {
            	existingUser.setName(user.getName());
                session.update(existingUser);
                tx1.commit();
                Email_Automation.sendEmailInfoUpdate(user);
                return true;
            }

        } catch (Exception e) {
            tx1.rollback();
            System.out.println("ROLLBACK: " + e.getMessage());
        } finally {
            session.close();
        }
        return false;
		
	}
	@SuppressWarnings("deprecation")
	public boolean UpdateUserEmail(Long id, User user) {
		Session session = factory.openSession();
        Transaction tx1 = session.beginTransaction();
        try {
            User existingUser = session.get(User.class, id);
            String NewEmail = user.getEmail();
         
            Criteria ct = session.createCriteria(User.class);
            ct.add(Restrictions.eq("email", NewEmail));
           User isemailExist = (User) ct.uniqueResult();
           String emailValidation = ValidEmailChecker.Isvalid(user);
           if (emailValidation.contains("❌")) {
               System.out.println(emailValidation);  // Print or log invalid email
               return false;
           }
          String EmptyEmail =  ValidEmailChecker.EmptyInputEmail(user);
          if (EmptyEmail != null ) {
	            System.out.println("❌ Invalid Name Or Empty Name: " + EmptyEmail);
	            return true;
	        }
            if(isemailExist!=null) {
            	return false;
            }
            if (existingUser!= null ) {
            	existingUser.setEmail(NewEmail);
                session.update(existingUser);
                tx1.commit();
                Email_Automation.sendEmailInfoUpdate(user);
                return true;
            }

        } catch (Exception e) {
            tx1.rollback();
            System.out.println("ROLLBACK: " + e.getMessage());
        } finally {
            session.close();
        }
        return false;
		
	}
	public boolean IssueBook(Long userId,  Book books) {
		  Session session = factory.openSession();
		    Transaction tx = session.beginTransaction();

		    try {
		        // Fetch the book by ID
		        Book book = session.get(Book.class, books.getBookId());
		        if (book == null) {
		            System.out.println("Book not found with ID: " + books.getBookId());
		            return false;
		        }

		        // Check if copies are available
		        if (book.getAvailableCopies() <= 0 || book.getTotalCopies() <= 0) {
		            System.out.println("No copies available for book ID: " + book.getBookId());
		            return false;
		        }

		        // Fetch the user by ID
		        User user = session.get(User.class, userId);
		        if (user == null) {
		            System.out.println("User not found with ID: " + userId);
		            return false;
		        }

		        // Set issue date as current date
		        Date issueDate = new Date();
		        book.setIssueDate(issueDate);

		        // Calculate return date (due date) as 14 days from issue date
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(issueDate);
		        calendar.add(Calendar.DAY_OF_MONTH, 14);
		        book.setReturnDate(calendar.getTime());

		        // Decrease available copies by 1
		        book.setAvailableCopies(book.getAvailableCopies() - 1);

		        // Assign book to user
		        book.setIssuedTo(user);

		        // Set issued flag (true because at least one copy is issued now)
		        book.setIssued(true);

		        // Debug logging
		        System.out.println("Issuing book ID: " + book.getBookId() + " to user ID: " + userId);
		        System.out.println("Available copies now: " + book.getAvailableCopies());

		        // Update the book record
		        session.update(book);
		        // Commit transaction
		        tx.commit();

		        return true;

		    } catch (Exception e) {
		        if (tx != null) tx.rollback();
		        e.printStackTrace();
		    } finally {
		        session.close();
		    }

		    return false;
	}
	public boolean AddNewBook(Book book,  Long userId, HttpSession session2) {
		 Session session = factory.openSession();
		    Transaction tx = session.beginTransaction();

		    try {
		        // Step 1: Check if book with same title exists
		        Criteria ct = session.createCriteria(Book.class);
		        ct.add(Restrictions.eq("title", book.getTitle()));
		        Book existingBook = (Book) ct.uniqueResult();

		        if (existingBook != null) {
		            return false; // Book already exists
		        }

		        // Step 2: Validate user
		        User user = session.get(User.class, userId);
		        if (user == null) {
		            return false; // Invalid user
		        }
		        String EmptyAddBookInfo =  ValidEmailChecker.EmptyInputForBook( book);
		        if (EmptyAddBookInfo != null) {
		            System.out.println("❌ Invalid Input : " + EmptyAddBookInfo);
		            return false; 
		        }
		        // Step 3: Set default values
		        book.setIssued(false);             
		        book.setIssuedTo(null);            
		        book.setIssueDate(null);           
		        book.setReturnDate(null);          

		        if (book.getTotalCopies() == 0 || book.getTotalCopies() <= 0) {
		            book.setTotalCopies(1);  // fallback
		        }

		        // ALWAYS set availableCopies same as totalCopies initially
		        book.setAvailableCopies(book.getTotalCopies());

		        // Step 4: Save
		        session.save(book);
		        tx.commit();
		        return true;

		    } catch (Exception e) {
		        if (tx != null) tx.rollback();
		        e.printStackTrace();
		    } finally {
		        session.close();
		    }

		    return false;
	}
	
	
	
	public boolean DeleteBook(Book book, Long userId, String role) {

	    // ✅ Early null checks
	    if (book == null || book.getBookId() == null || book.getTitle() == null) {
	        System.out.println("❌ Book ID or Title is missing");
	        return false;
	    }

	    Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	        
	        Criteria ct2 = session.createCriteria(Book.class);
	        ct2.add(Restrictions.eq("title", book.getTitle()));
	        ct2.add(Restrictions.eq("bookId", book.getBookId()));
	        Book bookExist = (Book) ct2.uniqueResult();


	        if (bookExist == null) {
	            System.out.println("❌ Book not found");
	            return false;
	        }

	        // ✅ Book input validation
	        String EmptyBookInput = ValidEmailChecker.EmptyInputForDeleteBook(bookExist);
	        if (EmptyBookInput != null) {
	            System.out.println("❌ Invalid Book Input: " + EmptyBookInput);
	            return false;
	        }

	        // ✅ Delete book
	        session.delete(bookExist);
	        tx.commit();
	        System.out.println("✅ Book deleted successfully");
	        return true;

	    } catch (Exception e) {
	        tx.rollback();
	        System.out.println("❌ Rollback: " + e.getMessage());
	    } finally {
	        session.close();
	    }

	    return false;
	}
	
	
	
	public boolean UpdateBookInfoAuthor(Book book, Long userId, String role) {
		 // ✅ Early null checks
	    if (book == null || book.getBookId() == null || book.getTitle() == null) {
	        System.out.println("❌ Book ID or Title is missing");
	        return false;
	    }

	    Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	        
	        Criteria ct2 = session.createCriteria(Book.class);
	        ct2.add(Restrictions.eq("title", book.getTitle()));
	        ct2.add(Restrictions.eq("bookId", book.getBookId()));
	        Book bookExist = (Book) ct2.uniqueResult();
	        if (bookExist == null) {
	            System.out.println("Book not found");
	            return false;
	        }
		    // Validate input fields  for book id and book title
		    String EmptyBookInput = ValidEmailChecker.EmptyInputForDeleteBook(book);
		    if (EmptyBookInput != null) {
		        System.out.println("❌ Invalid Input (General): " + EmptyBookInput);
		        return false;
		    }
            //valid input for author
		    String EmptyBookInputAuthor = ValidEmailChecker.EmptyInputForAuthor(book);
		    if (EmptyBookInputAuthor != null) {
		        System.out.println("❌ Invalid Input (Author): " + EmptyBookInputAuthor);
		        return false;
		    }
	        // Update logic
	        bookExist.setAuthor(book.getAuthor());

	        session.update(bookExist);
	        tx.commit();
	        return true;

	    } catch (Exception e) {
	        tx.rollback();
	        System.out.println("Rollback: " + e.getMessage());
	    } finally {
	        session.close();
	    }
	    return false;	
	}
	public boolean UpdateTotalBookCopies(Book book, Long userId, String urole) {
		 // ✅ Early null checks
	    if (book == null || book.getBookId() == null || book.getTitle() == null) {
	        System.out.println("❌ Book ID or Title is missing");
	        return false;
	    }

	    Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	       
	        Criteria ct2 = session.createCriteria(Book.class);
	        ct2.add(Restrictions.eq("title", book.getTitle()));
	        ct2.add(Restrictions.eq("bookId", book.getBookId()));
	        Book bookExist = (Book) ct2.uniqueResult();

	        if (bookExist == null) {
	            System.out.println("Book not found");
	            return false;
	        }
		    // Validate input fields  for book id and book title
		    String EmptyBookInput = ValidEmailChecker.EmptyInputForDeleteBook(book);
		    if (EmptyBookInput != null) {
		        System.out.println("❌ Invalid Input (General): " + EmptyBookInput);
		        return false;
		    }
            //valid input for author
		    String EmptyBookInputTotalCopy = ValidEmailChecker.EmptyInputForTotalCopies(book);
		    if (EmptyBookInputTotalCopy != null) {
		        System.out.println("❌ Invalid Input (Author): " + EmptyBookInputTotalCopy);
		        return false;
		    }
	        // Update logic
	        bookExist.setTotalCopies(book.getTotalCopies());

	        session.update(bookExist);
	        tx.commit();
	        return true;

	    } catch (Exception e) {
	        tx.rollback();
	        System.out.println("Rollback: " + e.getMessage());
	    } finally {
	        session.close();
	    }
	    return false;	
		
	}
	public boolean DeleteUser(User user, String role) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String emailinput = user.getEmail();
			Criteria ct = session.createCriteria(User.class);
			ct.add(Restrictions.eq("email", emailinput));
		  User users = (User) ct.uniqueResult();
		String IsemailEmpty =  ValidEmailChecker.EmptyInputEmail(users);
		
		if(IsemailEmpty!=null) {
			return false;
		}
		
		  if(users==null) {
			  return false;
		  }
		  session.delete(users);
		  tx.commit();
		 Email_Automation.sendEmailACdelete(users);
		  return true;
		  
		} catch (Exception e) {
			tx.rollback();
			System.out.println("roll back");
		}
		finally {
			session.close();
		}
		return false;
		
	}
	public boolean SetRole(User user, String role) {
		Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();
	    try {
	        Criteria ct = session.createCriteria(User.class);
	        ct.add(Restrictions.eq("name", user.getName()));

	        User existingUser = (User) ct.uniqueResult();
	        if (existingUser == null) {
	            return false; // no such user
	        }

	        String isNameEmpty = ValidEmailChecker.OnlyEmptyInputName(existingUser);
	        if (isNameEmpty != null) {
	            return false;
	        }

	        if (role == null || role.trim().isEmpty()) {
	            return false; // avoid empty role
	        }

	        existingUser.setRole(user.getRole());
	        session.update(existingUser);
	        tx.commit();

	        Email_Automation.sendEmailInfoUpdate(existingUser);
	        return true;
	    } catch (Exception e) {
	        tx.rollback();
	        System.out.println("roll back: " + e.getMessage());
	    } finally {
	        session.close();
	    }
	    return false;		
	}
	public boolean UpdateBookTitle(Book book, Long userId, String role) {
		 Session session = factory.openSession();
		    Transaction tx = session.beginTransaction();
		    try {
		        // Check if user exists with given role
		        Criteria ct = session.createCriteria(User.class);
		        ct.add(Restrictions.eq("userId", userId));
		        User existingUser = (User) ct.uniqueResult();

		        if (existingUser == null) {
		            return false; // No such user
		        }

		        
		       
		        // Check if book exists
		        Criteria ct2 = session.createCriteria(Book.class);
		        ct2.add(Restrictions.eq("bookId", book.getBookId()));
		        Book bookIdExist = (Book) ct2.uniqueResult();

		        if (bookIdExist == null) {
		            return false; // No such book
		        }

		        // Validate inputs (if you want)
		        String isUserIdorBookIdempty = ValidEmailChecker.EmptyInputId(userId, bookIdExist);
		        if (isUserIdorBookIdempty != null) {
		            return false;
		        }
		       
		        String EmptyBooktitle = ValidEmailChecker.EmptyInputTitle(book);
		        if(EmptyBooktitle!=null) {
		        	return false;
		        }
		        bookIdExist.setTitle(book.getTitle());

		        // Save changes
		        session.update(bookIdExist);
		        tx.commit();
		        return true;

		    } catch (Exception e) {
		        tx.rollback();
		        System.out.println("Rollback: " + e.getMessage());
		    } finally {
		        session.close();
		    }
		    return false;
		
	}
	public boolean UpdateBookCategoery(Book book, Long userId, String role) {
		 Session session = factory.openSession();
		    Transaction tx = session.beginTransaction();
		    try {
		        // Check if user exists with given role
		        Criteria ct = session.createCriteria(User.class);
		        ct.add(Restrictions.eq("userId", userId));
		        User existingUser = (User) ct.uniqueResult();

		        if (existingUser == null) {
		            return false; // No such user
		        }

		        
		       
		        // Check if book exists
		        Criteria ct2 = session.createCriteria(Book.class);
		        ct2.add(Restrictions.eq("bookId", book.getBookId()));
		        Book bookIdExist = (Book) ct2.uniqueResult();

		        if (bookIdExist == null) {
		            return false; // No such book
		        }

		        // Validate inputs (if you want)
		        String isUserIdorBookIdempty = ValidEmailChecker.EmptyInputId(userId, bookIdExist);
		        if (isUserIdorBookIdempty != null) {
		            return false;
		        }
		       
		        String EmptyBooktitle = ValidEmailChecker.EmptyInputCategory(book);
		        if(EmptyBooktitle!=null) {
		        	return false;
		        }
		        bookIdExist.setCategory(book.getCategory());

		        // Save changes
		        session.update(bookIdExist);
		        tx.commit();
		        return true;

		    } catch (Exception e) {
		        tx.rollback();
		        System.out.println("Rollback: " + e.getMessage());
		    } finally {
		        session.close();
		    }
		    return false;
	}
	public List<Book> GetAllBooks() {
		Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();
	    String hql = "FROM Book";
	  
	    Query<Book> query = session.createQuery(hql, Book.class);
	   List<Book> ListOfUser = query.list();
	    if (ListOfUser == null) {
	    	throw new Error("Book not found  ");
	    }
	    tx.commit();
	    return ListOfUser;
	}
	public Book GetBookById(Book book) {
		Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();
	    Long bookid = book.getBookId();
	    if(bookid==null) {
	    	return  null;
	    }
	 String isIdempty =   ValidEmailChecker.EmptyInputId(book);
	 if(isIdempty!=null) {
		 return null;
	 }
	    NativeQuery<Book> nt = session.createNativeQuery("SELECT * FROM book WHERE book_id = :id", Book.class).setParameter("id", bookid);
	   Book ListOfUser = nt.uniqueResult();
	    if (ListOfUser == null) {
	    	return  null;
	    }
	    tx.commit();
	    return ListOfUser;
	}
	public boolean ReturnBook(Long userId, Book book) {
		  Session session = factory.openSession();
		    Transaction tx = session.beginTransaction();

		    try {
		        // 1. Validate input before hitting DB
		        if (book == null || book.getBookId() == null) {
		            return false;
		        }

		        // 2. Fetch book from DB
		        Book book1 = session.get(Book.class, book.getBookId());
		        if (book1 == null) {
		            tx.rollback();
		            return false;
		        }

		        // 3. Check if this book was issued to the given user
		        if (book1.getIssuedTo() == null || !book1.getIssuedTo().getUserId().equals(userId)) {
		            tx.rollback();
		            return false; // Book not issued to this user
		        }

		        // 4. Update book as returned
		        book1.setAvailableCopies(book1.getAvailableCopies() + 1);
		        book1.setIssuedTo(null);
		        book1.setIssueDate(null);
		        book1.setReturnDate(new Date()); // Return date is now
		        book1.setIssued(false);

		        session.update(book1);
		        tx.commit();
		        return true;

		    } catch (Exception e) {
		        if (tx != null) tx.rollback();
		        e.printStackTrace();
		    } finally {
		        session.close();
		    }

		    return false;
	}
	public Book SearchBookByName(Book book, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
			if(IsUserExistAndIsBookExist!=null) {return null;}
			String istitleempty = ValidEmailChecker.EmptyInputTitle(book);
			if(istitleempty!=null) {return null;}
			Criteria ct1 = session.createCriteria(User.class);
			ct1.add(Restrictions.eq("userId", userId));
	     	User  existinguserid =	(User) ct1.uniqueResult();
		   if(existinguserid==null) {return null;}
			Criteria ct = session.createCriteria(Book.class);
			ct.add(Restrictions.eq("bookId", book.getBookId()));
			ct.add(Restrictions.eq("title", book.getTitle()));
	     	Book existingbook =	(Book) ct.uniqueResult();
	     	if(existingbook==null) {
	     		return null;
	     		}
	     	return existingbook;
		} catch (Exception e) {
			tx.rollback();
			System.out.println("the transaction has been rolled back :"+e.getMessage());
			
		}
		finally {
			session.close();
		}
		return null;
	}
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public Number CountTotalBook() {
		
		Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();
	    try {
	        // Native SQL query to count all books
			Query query = session.createSQLQuery("SELECT COUNT(book_id) FROM book");
	        Long count = ((Number) query.uniqueResult()).longValue();

	        tx.commit();
	        return count;
	    } catch (Exception e) {
	        tx.rollback();
	        System.out.println("Transaction rolled back: " + e.getMessage());
	    } finally {
	        session.close();
	    }
	    return null;

	}
	public int checkBookAvailability(Book book, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
			if(IsUserExistAndIsBookExist!=null) {return 0;}
			String istitleempty = ValidEmailChecker.EmptyInputTitle(book);
			if(istitleempty!=null) {return 0;}
			Criteria ct1 = session.createCriteria(User.class);
			ct1.add(Restrictions.eq("userId", userId));
	     	User  existinguserid =	(User) ct1.uniqueResult();
		   if(existinguserid==null) {return 0;}
			Criteria ct = session.createCriteria(Book.class);
			ct.add(Restrictions.eq("bookId", book.getBookId()));
			ct.add(Restrictions.eq("title", book.getTitle()));
	     	Book existingbook =	(Book) ct.uniqueResult();
	     	if(existingbook==null) {
	     		return 0;
	     		}
	     	return existingbook.getAvailableCopies();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("the transaction has been rolled back :"+e.getMessage());
			
		}
		finally {
			session.close();
		}
		return 0;
	}
	public User ViewRecentlyIssuedBook(Book book, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
			if(IsUserExistAndIsBookExist!=null) {return null;}
			String istitleempty = ValidEmailChecker.EmptyInputTitle(book);
			if(istitleempty!=null) {return null;}
			Criteria ct1 = session.createCriteria(User.class);
			ct1.add(Restrictions.eq("userId", userId));
	     	User  existinguserid =	(User) ct1.uniqueResult();
		   if(existinguserid==null) {return null;}
			Criteria ct = session.createCriteria(Book.class);
			ct.add(Restrictions.eq("bookId", book.getBookId()));
			ct.add(Restrictions.eq("title", book.getTitle()));
	     	Book existingbook =	(Book) ct.uniqueResult();
	     	if(existingbook==null) {
	     		return null;
	     		}
	     	return existingbook.getIssuedTo();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("the transaction has been rolled back :"+e.getMessage());
			
		}
		finally {
			session.close();
		}
		return null;
		
	}
	public Book ViewBookissuedHistorybyUser(Book book, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
			if(IsUserExistAndIsBookExist!=null) {return null;}
			String isissuedtoEmpty = ValidEmailChecker.EmptyInputIssuedTo(book);
			if(isissuedtoEmpty!=null) {return null;}
			 // Check if user exists
	        User existingUser = (User) session.createCriteria(User.class)
	            .add(Restrictions.eq("userId", userId))
	            .uniqueResult();
//	        if (existingUser == null) return null;

	        // Check if book is issued to this user
	        Book issuedBook = (Book) session.createCriteria(Book.class)
	            .add(Restrictions.eq("bookId", book.getBookId()))
	            .add(Restrictions.eq("issuedTo", existingUser))
	            .uniqueResult();

	        tx.commit();
	        return issuedBook;
		} catch (Exception e) {
			tx.rollback();
			System.out.println("the transaction has been rolled back :"+e.getMessage());
			
		}
		finally {
			session.close();
		}
		return null;
		
	}
	public Book SearchBookByAuthor(Book book, Long userId) {
		
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
			if(IsUserExistAndIsBookExist!=null) {return null;}
			String istitleempty = ValidEmailChecker.EmptyInputForAuthor(book);
			if(istitleempty!=null) {return null;}
			Criteria ct1 = session.createCriteria(User.class);
			ct1.add(Restrictions.eq("userId", userId));
	     	User  existinguserid =	(User) ct1.uniqueResult();
		   if(existinguserid==null) {return null;}
			Criteria ct = session.createCriteria(Book.class);
			ct.add(Restrictions.eq("bookId", book.getBookId()));
			ct.add(Restrictions.eq("author", book.getAuthor()));
	     	Book existingbook =	(Book) ct.uniqueResult();
	     	if(existingbook==null) {
	     		return null;
	     		}
	     	return existingbook;
		} catch (Exception e) {
			tx.rollback();
			System.out.println("the transaction has been rolled back :"+e.getMessage());
			
		}
		finally {
			session.close();
		}
		return null;
	}
	public Book SearchBookByCategory(Book book, Long userId) {
		
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String IsUserExistAndIsBookExist = ValidEmailChecker.EmptyInputId(userId, book);
			if(IsUserExistAndIsBookExist!=null) {return null;}
			String istitleempty = ValidEmailChecker.EmptyInputCategory(book);
			if(istitleempty!=null) {return null;}
			Criteria ct1 = session.createCriteria(User.class);
			ct1.add(Restrictions.eq("userId", userId));
	     	User  existinguserid =	(User) ct1.uniqueResult();
		   if(existinguserid==null) {return null;}
			Criteria ct = session.createCriteria(Book.class);
			ct.add(Restrictions.eq("bookId", book.getBookId()));
			ct.add(Restrictions.eq("category", book.getCategory()));
	     	Book existingbook =	(Book) ct.uniqueResult();
	     	if(existingbook==null) {
	     		return null;
	     		}
	     	return existingbook;
		} catch (Exception e) {
			tx.rollback();
			System.out.println("the transaction has been rolled back :"+e.getMessage());
			
		}
		finally {
			session.close();
		}
		return null;
	}
	public boolean BlockUser(User user) {
		Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();
	    try {
	        // Validate email is not empty and valid
	        String isMailEmpty = ValidEmailChecker.EmptyInputEmail(user);
	        if (isMailEmpty != null) {
	            return false;
	        }
	        String isEmailValid = ValidEmailChecker.IsvalidForLogin(user);
	        if (isEmailValid != null) {
	            return false;
	        }

	        // Fetch user from DB by email (assuming email is unique)
	        @SuppressWarnings("deprecation")
	        Criteria criteria = session.createCriteria(User.class);
	        criteria.add(Restrictions.eq("userId", user.getUserId()));
	        criteria.add(Restrictions.eq("email", user.getEmail()));
	        User existingUser = (User) criteria.uniqueResult();

	        if (existingUser == null) {
	            // User not found
	            return false;
	        }

	        if (existingUser.isIsblocked()==true) { // Check if already blocked
	            return false;
	        }

	        // Set blocked flag to true
	        // Update the fetched persistent entity
	        existingUser.setIsblocked(true);
	        session.update(existingUser);
	        tx.commit();
	        return true;
	        

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        System.out.println("Transaction rolled back: " + e.getMessage());
	        return false;
	    } finally {
	        session.close();
	    }
		
	}
	public boolean UnBlockUser(User user) {
		Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();
	    try {
	        // Validate email is not empty and valid
	        String isMailEmpty = ValidEmailChecker.EmptyInputEmail(user);
	        if (isMailEmpty != null) {
	            return false;
	        }
	        String isEmailValid = ValidEmailChecker.IsvalidForLogin(user);
	        if (isEmailValid != null) {
	            return false;
	        }

	        // Fetch user from DB by email (assuming email is unique)
	        @SuppressWarnings("deprecation")
	        Criteria criteria = session.createCriteria(User.class);
	        criteria.add(Restrictions.eq("userId", user.getUserId()));
	        criteria.add(Restrictions.eq("email", user.getEmail()));
	        User existingUser = (User) criteria.uniqueResult();

	        if (existingUser == null) {
	            // User not found
	            return false;
	        }

	        if (existingUser.isIsblocked()==false) { // Check if already Unblocked
	            return false;
	        }

	        existingUser.setIsblocked(false);
	        session.update(existingUser);
	        tx.commit();
	        return true;
	        

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        System.out.println("Transaction rolled back: " + e.getMessage());
	        return false;
	    } finally {
	        session.close();
	    }
		
	}
	@SuppressWarnings("deprecation")
	public boolean NotifyUserOverdue(Book book, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {

			User isuseridexist = session.get(User.class, userId);
			if(isuseridexist==null) { return false;}
		String IsIdempty =	ValidEmailChecker.EmptyInputId(book);
		if(IsIdempty!=null) {return false;}
		String isIssuedUseridEmpty =	ValidEmailChecker.EmptyInputIssuedTo(book);
		if(isIssuedUseridEmpty!=null) {return false;}
			Criteria ct1 = session.createCriteria(Book.class);
			ct1.add(Restrictions.eq("bookId",book.getBookId() ));
			ct1.add(Restrictions.eq("issuedTo",book.getIssuedTo().getUserId() ));
		     Book Isissuedbyuser =	(Book) ct1.uniqueResult();
		    if(Isissuedbyuser==null) {return false;}
			Date currentDate = new Date();
			Date duedate = Isissuedbyuser.getReturnDate();
			
			if(currentDate.before(duedate)) {
				return false;
			}
			if(currentDate.after(duedate)) {
				Email_Automation.sendEmailDueDate(Isissuedbyuser);
				return true;
				
			}	
		} catch (Exception e) {
			tx.rollback();
			System.out.println("the transaction has been rolled back:"+e.getMessage());
			
		}	
		finally {
			session.close();
		}
		return false;
	}
	@SuppressWarnings("deprecation")
	public long daysLeft(Book book, Long userId) {
		Session session  =factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Criteria ct = session.createCriteria(User.class);
			ct.add(Restrictions.eq("userId", userId));
	     	User ExistingUser =	(User) ct.uniqueResult();
	     String isIdempty =	ValidEmailChecker.EmptyInputId(book);
	     if(isIdempty!=null)return -6;
	     	Criteria ct2 = session.createCriteria(Book.class);
	     	ct2.add(Restrictions.eq("bookId", book.getBookId()));
	     	ct2.add(Restrictions.eq("issuedTo", ExistingUser));
	        Book books =  (Book) ct2.uniqueResult();
	         if(books==null) return -2;
	      Date issuedate =  books.getIssueDate();
	        Date Returndate =   books.getReturnDate();
	        if(issuedate==null) return -4;
	        if(Returndate==null) return -5;
	        // Calculate days between issue and return dates
	          long daysBetween = (Returndate.getTime() - issuedate.getTime()) / (24 * 60 * 60 * 1000);
	           tx.commit();
	           return daysBetween;
			 
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction is rolled back :"+e.getMessage());
		}
		finally {
			session.close();
		}
		return -1;	
	}
	@SuppressWarnings("deprecation")
	public Long TotalIssuedBookCount(Long userId) {
	       Session session = factory.openSession();
	       Transaction tx = session.beginTransaction();
	       Long count = 0L;
	    try {
	    	Criteria ct = session.createCriteria(User.class);
			ct.add(Restrictions.eq("userId", userId));
	     	User ExistingUser =	(User) ct.uniqueResult();
			@SuppressWarnings("unchecked")
			NativeQuery<Book> nq = session.createSQLQuery("SELECT COUNT(*) FROM book WHERE issued_to_user_id = :id").
			setParameter("id", ExistingUser.getUserId());
		  Object result = nq.uniqueResult();
         if(result== null) return -1L;
        if (result != null) {
            if (result instanceof Number) {
                count = ((Number) result).longValue();
            }
            tx.commit();
            return count;
        } 
		} catch (Exception e) {
			tx.rollback();
			System.out.println("the transaction is Rolled Back :"+e.getMessage());
		}
	     finally {
	    	   session.close();
	       }
		return -1L;
	}
	public boolean FineProcess(Fine fine, Long userId, Long bookid) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existinguser = session.get(User.class, userId);
		    Criteria ct = session.createCriteria(Book.class);
		    ct.add(Restrictions.eq("bookId",  bookid ));
		    ct.add(Restrictions.eq("issuedTo", existinguser));
		    Book isBookIssuedbyuser =   (Book) ct.uniqueResult();
		    if(isBookIssuedbyuser==null) return false;
		    Date currentDate = new Date();
			Date duedate = isBookIssuedbyuser.getReturnDate();
			
			if(currentDate.after(duedate)) {
				return false;
			}
	     	String txid = GenerateTID.processDummyPayment();
		        if(txid==null) {
			     return false;
		        }
		    boolean isfinealreadypaid = fine.getStatus();
		    if(isfinealreadypaid==false ) {
		    	return false;
		    }
		    
//		     double fixedfineammont = 500;
			if(currentDate.before(duedate)) {
				
				fine.setBookId(bookid);
				fine.setStatus(true);
				fine.setTid(txid);
				fine.setFineTo(existinguser);
				fine.setAmmount(fine.getAmmount());
				fine.setFineDate(new Date());
				session.save(fine);
				tx.commit();
				return true;
			}	
		   
		} catch (Exception e) {
			tx.rollback();
			System.out.println("transaction has been roll backed :"+e.getMessage());
		}
		finally {
			session.close();
		}
		return false;
	}
	public boolean BookRatingPoints(Book book, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existingUser = session.get(User.class, userId);
			Criteria ct = session.createCriteria(Book.class);
			ct.add(Restrictions.eq("bookId", book.getBookId()));
			ct.add(Restrictions.eq("issuedTo", existingUser));
		Book existingBookinfo =	(Book) ct.uniqueResult();
		if(existingBookinfo==null) {
			return false;
		}
		double ratingpoints = book.getRating_points();
		if(ratingpoints>10.0 || ratingpoints<0.0) {
			return false;
		}
		
	  String isIdempty = ValidEmailChecker.EmptyInputId(book);
	  if(isIdempty!=null) {
		  return false;
	  }
		
	  existingBookinfo.setRating_points(book.getRating_points());
	   session.update(existingBookinfo);
		tx.commit();
		return true;
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction has been rolled back due to:"+e.getMessage());
		}
		finally {
			session.close();
		}
		return false;
		
	}
	@SuppressWarnings("unchecked")
	public  List<Book> SearchBookByrating(Book book, Long userId) {
		
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existingUser = session.get(User.class, userId);
			 String hql = "FROM Book WHERE rating_points = :rate";
		        Query<Book> nt = session.createQuery(hql, Book.class)
		                                .setParameter("rate", book.getRating_points());

		        return nt.list(); // Will return empty list if no books match
		
	 
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction has been rolled back due to:"+e.getMessage());
		}
		finally {
			session.close();
		}
		return Collections.emptyList();
		
	}
	public Map<String, String> ListOfPermission() {
	Session session = factory.openSession();
	Transaction tx = session.beginTransaction();
    Map<String, String> permissions = new HashMap<>();
    try {
         permissions.put("1", "Add Book");
		permissions.put("2", "Delete Book");
		permissions.put("3", "Update Book Info");
		permissions.put("4", "Issue Book");
		permissions.put("5", "Return Book");
		permissions.put("6", "View All Books");
		permissions.put("7", "Search Book by Title");
		permissions.put("8", "Search Book by Author");
		permissions.put("9", "Search Book by Category");
		permissions.put("10", "Block User");
		permissions.put("11", "Unblock User");
		permissions.put("12", "Set User Role");
	tx.commit();
	return permissions;
		
		
	} catch (Exception e) {
		tx.rollback();
		System.out.println("Transaction has been rolled back due to:"+e.getMessage());
	}
	finally {
		session.close();
		
	}
	return Collections.emptyMap();
	}
	public Map<String, String> ListOfpermissionforstudent() {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Map<String, String> permissionsforstudent = new HashMap<>();
			permissionsforstudent.put("1", "Issue Book");
			permissionsforstudent.put("2", "Return Book");
			permissionsforstudent.put("3", "View All Books");
			permissionsforstudent.put("4", "Search Book by Title");
			permissionsforstudent.put("5", "Search Book by Author");
			permissionsforstudent.put("6", "Search Book by Category");
			tx.commit();
			return permissionsforstudent;
			
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction has been rolled back due to:"+e.getMessage());
		}
		finally {
			session.close();
			
		}
		return null;
		
	}
	public Map<String, String> ListOfpermissionforadmin() {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Map<String, String> permissionsforadmin = new HashMap<>();
			permissionsforadmin.put("1", "Add Book");
			permissionsforadmin.put("2", "Delete Book");
			permissionsforadmin.put("3", "Update Book Info");
			permissionsforadmin.put("4", "View All Books");
			permissionsforadmin.put("5", "Search Book by Title");
			permissionsforadmin.put("6", "Search Book by Author");
			permissionsforadmin.put("7", "Search Book by Category");
			permissionsforadmin.put("8", "Block User");
			permissionsforadmin.put("9", "Unblock User");
			permissionsforadmin.put("10", "Set User Role");
			tx.commit();
			
			return permissionsforadmin;
			
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction has been rolled back due to:"+e.getMessage());
		}
		finally {
			session.close();
		}
		return null;
		
	}
	@SuppressWarnings("deprecation")
	public boolean Enable2FA(User user) {
		Session session = factory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	    	String IsnameinputEmpty =	ValidEmailChecker.OnlyEmptyInputName(user);
			if(IsnameinputEmpty!=null) {
				System.out.println("❌ Invalid Input (Name): " + IsnameinputEmpty);
				return false;
			}
	        // Find user by name
	        Criteria ct = session.createCriteria(User.class);
	        ct.add(Restrictions.eq("name", user.getName()));
	        User existingUser = (User) ct.uniqueResult();

	        if (existingUser == null) {
	            return false; // User not found
	        }

	        if (existingUser.isTwoFactorAuthEnabled()) {
	            System.out.println("2FA is already enabled for this user");
	            return false;
	        }

	        // Get stored secret key
	        String storedSecretKey = existingUser.getTwoFactorAuthCode();

	        // First-time enabling: generate a key if it doesn't exist
	        if (storedSecretKey == null || storedSecretKey.isEmpty()) {
	          String  Key = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
	            existingUser.setTwoFactorAuthCode(user.getTwoFactorAuthCode());
	            session.update(existingUser);
	            tx.commit();
	            System.out.println("Secret key generated. Provide this to user: " + Key);
	            return false; // key generated, waiting for user verification
	        }

	        // Verification step — compare entered code from request with stored code
	        if (existingUser.getTwoFactorAuthCode() == null || !existingUser.getTwoFactorAuthCode().equals(storedSecretKey)) {
	            return false; // Wrong or missing code
	        }

	        // Enable 2FA
	        existingUser.setTwoFactorAuthEnabled(true);
	        session.update(existingUser);
	        tx.commit();
	        Email_Automation.sendEmailForTwoFactorAuthentication(existingUser);
	        return true;
	        

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        System.out.println("Transaction rolled back due to: " + e.getMessage());
	    } finally {
	        session.close();
	    }

	    return false; 		    
	}
	public boolean Disable2FA(User user) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
		String IsnameinputEmpty =	ValidEmailChecker.EmptyInputName(user);
		if(IsnameinputEmpty!=null) {
			System.out.println("❌ Invalid Input (Name): " + IsnameinputEmpty);
			return false;
		}
			// Find user by name
			Criteria ct = session.createCriteria(User.class);
			ct.add(Restrictions.eq("name", user.getName()));
			User existingUser = (User) ct.uniqueResult();

			if (existingUser == null) {
				System.out.println("User not found");
				return false;
			}

			if (!existingUser.isTwoFactorAuthEnabled()) {
				System.out.println("2FA is not enabled for this user");
				return false;
			}

			// Disable 2FA
			existingUser.setTwoFactorAuthEnabled(false);
			existingUser.setTwoFactorAuthCode(null); // Clear the secret key
			session.update(existingUser);
			tx.commit();
			Email_Automation.sendEmailToDisableTwoFactorAuthentication(existingUser);
			return true;

		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction rolled back due to: " + e.getMessage());
		} finally {
			session.close();
		}
		return false;
		
	}
	public boolean Get2FAStatus(User user) {
	Session session = factory.openSession();
	Transaction tx = session.beginTransaction();
	try {
        String IsnameinputEmpty =	ValidEmailChecker.OnlyEmptyInputName(user);
        if(IsnameinputEmpty!=null) {
            System.out.println("❌ Invalid Input (Name): " + IsnameinputEmpty);
            return false;
        }
        Criteria ct = session.createCriteria(User.class);
        ct.add(Restrictions.eq("name", user.getName()));
        User existingUser = (User) ct.uniqueResult();

        if (existingUser == null) {
            System.out.println("User not found");
            return false;
        }
        tx.commit();
        if (existingUser.isTwoFactorAuthEnabled()) {
            return true; // 2FA is enabled for this user
        } else {
           return false;
        }
        
      
        
    } catch (Exception e) {
        tx.rollback();
        System.out.println("Transaction rolled back due to: " + e.getMessage());
        
    } finally {
        session.close();
		
	}
	return false;
	}
	public List GetFineInfo(Fine fine, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existinguser = session.get(User.class, userId);
			if (existinguser == null) {
				System.out.println("User not found");
				return Collections.emptyList();
			}
			String isBookIdEmpty = ValidEmailChecker.EmptyInputIdforfine(fine);
			if (isBookIdEmpty != null) {
				System.out.println("Book ID is empty");
				return Collections.emptyList();
			}
			
			Criteria ct = session.createCriteria(Fine.class);
			ct.add(Restrictions.eq("bookId", fine.getBookId()));
			List existingFine =  ct.list();
			if (existingFine == null) {
				System.out.println("Fine not found for this user and book");
				return Collections.emptyList();
			}
			return existingFine; // Return true if fine is paid, false otherwise
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction rolled back due to: " + e.getMessage());
		}
		finally {
			session.close();
		}
		return Collections.emptyList();
		
	}
	public List<Fine> getAllFine(Long userId) {
	  Session session = factory.openSession();
	  Transaction tx = session.beginTransaction();
	  try {
		  User existinguser = session.get(User.class, userId);
			if (existinguser == null) {
				System.out.println("User not found");
				return Collections.emptyList();
			}
			String hql = "FROM Fine ";
			Query<Fine> query = session.createQuery(hql, Fine.class);
			List<Fine> finesList = query.list();
			tx.commit();
			return finesList;
		
} catch (Exception e) {
		tx.rollback();
		System.out.println("Transaction rolled back due to: " + e.getMessage());
	}
	  finally {
		  session.close();
	  }
	return Collections.emptyList();
	  
		
	}
	public boolean DeleteFine(Fine fine, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existinguser = session.get(User.class, userId);
			if (existinguser == null) {
				System.out.println("User not found");
				return false;
			}
			User targetUser = session.get(User.class, fine.getFineTo().getUserId());
			if(targetUser == null) {
			    System.out.println("Target user not found");
			    return false;
			}

			String isBookIdEmpty = ValidEmailChecker.EmptyInputIdforfine(fine);
			if (isBookIdEmpty != null) {
				System.out.println("Book ID is empty");
				return false;
			}
			String FineIdempty = ValidEmailChecker.EmptyInputFineId(fine);
			if(FineIdempty != null) {
				System.out.println("Fine ID is empty");
				return false;
			}
             String FineToEmpty = ValidEmailChecker.EmptyInputForFineForFineTo(fine);
            if (FineToEmpty != null) {
             System.out.println("FineTo user ID is empty");
              return false;
             }
            
			Criteria ct = session.createCriteria(Fine.class);
			ct.add(Restrictions.eq("fineId", fine.getFineId()));
			ct.add(Restrictions.eq("bookId", fine.getBookId()));
			ct.add(Restrictions.eq("FineTo",  targetUser));
			Fine existingFine = (Fine) ct.uniqueResult();
			if (existingFine == null) {
				System.out.println("Fine not found for this user and book");
				return false; // Fine not found;
			}

			session.delete(existingFine);
			tx.commit();
			return true;

		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction rolled back due to: " + e.getMessage());
		} finally {
			session.close();
		}
		return false;
		
	}
	public boolean reviewBook(Long bookId, Long userId, Review review) {
		 Session session = factory.openSession();
		    Transaction tx = session.beginTransaction();
		 // Check if comment is empty
	        if (ValidEmailChecker.isCommentEmpty(review) != null) {
	            logger.info("Review comment is empty");
	            return false;
	        }
		    try {
		        // Load user and book entities
		        User existingUser = session.get(User.class, userId);
		        Book existingBook = session.get(Book.class, bookId);
		        
		        if (existingUser == null || existingBook == null) {
		            logger.info("User or Book does not exist");
		            return false;
		        }

		        // Check if the book is issued to the user
		        if (existingBook.getIssuedTo() == null
		                || !existingBook.getIssuedTo().getUserId().equals(existingUser.getUserId())) {
		            logger.info("Book is not issued to this user");
		            return false;
		        }

		        // Check if a review already exists for this user and book
		        Criteria reviewCt = session.createCriteria(Review.class);
		        reviewCt.add(Restrictions.eq("bookId", existingBook)); // use Book object
		        reviewCt.add(Restrictions.eq("userId", existingUser)); // use User object

		        @SuppressWarnings("unchecked")
		        List<Review> reviews = reviewCt.list();
		        if (!reviews.isEmpty()) {
		            logger.info("Review already submitted for this book by this user");
		            return false;
		        }

		       

		        // Set entities in review and save
		        review.setUserId(existingUser);
		        review.setBookId(existingBook);
		        review.setCreatedAt(new Date());
		        session.save(review);

		        tx.commit();
		        logger.info("Book review updated successfully");
		        return true;
		    } catch (Exception e) {
		        tx.rollback();
		        System.out.println("Transaction rolled back due to: " + e.getMessage());
		    } finally {
		        session.close();
		    }
		    return false;
	}
	public boolean UpdateFineAmount(Fine fine, Long userId, Long fineToUserId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existinguser = session.get(User.class, userId);
			if (existinguser == null) {
				logger.info("User not found");
				return false;
			}
		
			
			
			Fine isFineIdexist = session.get(Fine.class, fine.getFineId());
            if(isFineIdexist==null) {
             logger.info("Fine ID does not exist");
             return false;
            }
			

			String isBookIdEmpty = ValidEmailChecker.EmptyInputIdforfine(fine);
			if (isBookIdEmpty != null) {
			logger.info(isBookIdEmpty);
				return false;
			}
			String FineIdempty = ValidEmailChecker.EmptyInputFineId(fine);
			if(FineIdempty != null) {
				logger.info("Fine ID is empty");
				return false;
			}
//             String FineToEmpty = ValidEmailChecker.EmptyInputForFineForFineTo(fine);
//            if (FineToEmpty != null) {
//             logger.info("fine to user ID is empty");
//              return false;
//             }
//           // ✅ Load target student (FineToUserId)
	        User fineToUser = session.get(User.class, fineToUserId);
	        
			if (fineToUser == null) {
				logger.info("Target user not found");
				return false;
			}
			Criteria ct = session.createCriteria(Fine.class);
			ct.add(Restrictions.eq("fineId", isFineIdexist.getFineId()));
			ct.add(Restrictions.eq("bookId", isFineIdexist.getBookId()));
			ct.add(Restrictions.eq("FineTo",  fineToUser));
			Fine existingFine = (Fine) ct.uniqueResult();
			if (existingFine == null) {
				logger.info("Fine not found for this user and book");
				return false; // Fine not found;
			}
           existingFine.setAmmount(fine.getAmmount());
			session.update(existingFine);
			tx.commit();
			return true;

		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction rolled back due to: " + e.getMessage());
		} finally {
			session.close();
		}
		return false;
		
	}
	public boolean UpdateFineStatus(Fine fine, Long userId, Long fineToUserId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existinguser = session.get(User.class, userId);
			if (existinguser == null) {
				logger.info("User not found");
				return false;
			}
		
			
			
			Fine isFineIdexist = session.get(Fine.class, fine.getFineId());
            if(isFineIdexist==null) {
             logger.info("Fine ID does not exist");
             return false;
            }
			

			String isBookIdEmpty = ValidEmailChecker.EmptyInputIdforfine(fine);
			if (isBookIdEmpty != null) {
			logger.info(isBookIdEmpty);
				return false;
			}
			String FineIdempty = ValidEmailChecker.EmptyInputFineId(fine);
			if(FineIdempty != null) {
				logger.info("Fine ID is empty");
				return false;
			}

//           // ✅ Load target student (FineToUserId)
	        User fineToUser = session.get(User.class, fineToUserId);
	        
			if (fineToUser == null) {
				logger.info("Target user not found");
				return false;
			}
			Criteria ct = session.createCriteria(Fine.class);
			ct.add(Restrictions.eq("fineId", isFineIdexist.getFineId()));
			ct.add(Restrictions.eq("bookId", isFineIdexist.getBookId()));
			ct.add(Restrictions.eq("FineTo",  fineToUser));
			Fine existingFine = (Fine) ct.uniqueResult();
			if (existingFine == null) {
				logger.info("Fine not found for this user and book");
				return false; // Fine not found;
			}
			boolean IsfineStatusAlreadyPaid = existingFine.getStatus();
			if(IsfineStatusAlreadyPaid) return false; // Fine is already paid
            existingFine.setStatus(true);
			session.update(existingFine);
			tx.commit();
			return true;

		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction rolled back due to: " + e.getMessage());
		} finally {
			session.close();
		}
		return false;
		
	}
	public boolean GetFineStatus(Fine fine, Long userId) {
		Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        try {
        	User existinguser = session.get(User.class, userId);
            if (existinguser == null) {
                logger.info("User not found");
                return false;
            }
        
            
            
            Fine isFineIdexist = session.get(Fine.class, fine.getFineId());
            if(isFineIdexist==null) {
             logger.info("Fine ID does not exist");
             return false;
            }
            

            String isBookIdEmpty = ValidEmailChecker.EmptyInputIdforfine(fine);
            if (isBookIdEmpty != null) {
            logger.info(isBookIdEmpty);
                return false;
            }
            String FineIdempty = ValidEmailChecker.EmptyInputFineId(fine);
            if(FineIdempty != null) {
                logger.info("Fine ID is empty");
                return false;
            }
            Criteria ct = session.createCriteria(Fine.class);
			ct.add(Restrictions.eq("fineId", isFineIdexist.getFineId()));
			ct.add(Restrictions.eq("bookId", isFineIdexist.getBookId()));
			ct.add(Restrictions.eq("FineTo",  existinguser));
			Fine existingFine = (Fine) ct.uniqueResult();
			if (existingFine == null) {
				logger.info("Fine not found for this user and book");
				return false; // Fine not found;
			}
			
			
			if (existingFine.getStatus() == true) {
				logger.info("Fine is already paid");
				return true; // Fine is paid
			}
			
		
		} catch (Exception e) {
			
		}
        finally {
        session.close();
        }
		return false;
            
		
	}
	public boolean deleteReview(Review review, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Review existingReview = session.get(Review.class, review.getReviewId());
			if (existingReview == null) {
				System.out.println("Review not found");
				return false;
			}
		    User existingUser = session.get(User.class, userId);
		    if (existingUser == null) {
			System.out.println("User not found");
		   return false;
		   }
		String isreviewIdEmpty = ValidEmailChecker.isReviewIdEmpty(review);
		if(isreviewIdEmpty != null) {
			System.out.println("Review ID is empty");
			return false;
		}
		String isReviewBookIdempty = ValidEmailChecker.isReviewBookIdEmpty(review);
        if(isReviewBookIdempty != null) {
        System.out.println("Review Book ID is empty");
        return false;
        }
		  Criteria ct = session.createCriteria(Review.class);
		  ct.add(Restrictions.eq("reviewId", existingReview.getReviewId()));
		  ct.add(Restrictions.eq("userId", existingUser));
		  ct.add(Restrictions.eq("bookId", existingReview.getBookId()));
		   Review reviewToDelete = (Review) ct.uniqueResult();
		   if (reviewToDelete == null) {
			   System.out.println("Review not found for this user and book");
			   return false; // Review not found;
		   }
		   session.delete(reviewToDelete);
		   tx.commit();
		   return true;
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction rolled back due to: " + e.getMessage());
		}
		finally {
			session.close();
		}
		return false;
		
	}
	public String getBookReviews(Long bookId, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existingUser = session.get(User.class, userId);
			if (existingUser == null) {
				System.out.println("User not found");
				return "user not found";
			}
			Criteria ct = session.createCriteria(Book.class);
			ct.add(Restrictions.eq("bookId", bookId));
			Book existingBook = (Book) ct.uniqueResult();
			if (existingBook == null) {
				System.out.println("Book not found");
				return "book not found";
			}

			Criteria reviewCt = session.createCriteria(Review.class);
			reviewCt.add(Restrictions.eq("bookId", existingBook));
			List<Review> reviews = reviewCt.list();

			if (reviews.isEmpty()) {
				System.out.println("No reviews found for this book");
				return "no reviews found";
			}
			
			
		    tx.commit();
			return reviews.stream()
					.map(review -> "Review ID: " + review.getReviewId() + ", Comment: " + review.getComment())
					.collect(Collectors.joining("\n"));

			
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction rolled back due to: " + e.getMessage());
		} finally {
			session.close();
		}
		return null;
		
	}
	@SuppressWarnings("deprecation")
	public boolean UpdateReview(Review review, Long userId) {
	Session session = factory.openSession();
	Transaction tx = session.beginTransaction();
	try {
		Review existingReview = session.get(Review.class, review.getReviewId());
		if (existingReview == null) {
			System.out.println("Review not found");
			return false;
		}
		User existingUser = session.get(User.class, userId);
		if (existingUser == null) {
			System.out.println("User not found");
			return false;
		}
		String isreviewIdEmpty = ValidEmailChecker.isReviewIdEmpty(review);
		if (isreviewIdEmpty != null) {
			System.out.println("Review ID is empty");
			return false;
		}
		String isReviewBookIdempty = ValidEmailChecker.isReviewBookIdEmpty(review);
		if (isReviewBookIdempty != null) {
			System.out.println("Review Book ID is empty");
			return false;
		}
		Criteria ct = session.createCriteria(Review.class);
		ct.add(Restrictions.eq("reviewId", existingReview.getReviewId()));
		ct.add(Restrictions.eq("userId", existingUser));
		ct.add(Restrictions.eq("bookId", existingReview.getBookId()));
		Review reviewToUpdate = (Review) ct.uniqueResult();
		if (reviewToUpdate == null) {
			System.out.println("Review not found for this user and book");
			return false;// Review not found;
		}
		reviewToUpdate.setComment(review.getComment());
		session.update(reviewToUpdate);
		tx.commit();
		return true;
		
	} catch (Exception e) {
		tx.rollback();
		System.out.println("Transaction rolled back due to: " + e.getMessage());
	}
	finally {
		session.close();
	}
	return false;
		
	}
	public int GetReviewCountByBookId(Book book, Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existingUser = session.get(User.class, userId);
			if (existingUser == null) {
				System.out.println("User not found");
				return -1; // User not found;
			}
			
			Criteria ct = session.createCriteria(Book.class);
			ct.add(Restrictions.eq("bookId", book.getBookId()));
			Book existingBook = (Book) ct.uniqueResult();
			if (existingBook == null) {
				System.out.println("Book not found");
				return -2; // Book not found;
			}

			Criteria reviewCt = session.createCriteria(Review.class);
			reviewCt.add(Restrictions.eq("bookId", existingBook));
			List<Review> reviews = reviewCt.list();

			int reviewCount = reviews.size();
			tx.commit();
			return reviewCount;

		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction rolled back due to: " + e.getMessage());
		} finally {
			session.close();
		}
		return 0;
		
	}
	public List<Review> GetAllReviews(Long userId) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existingUser = session.get(User.class, userId);
			if (existingUser == null) {
				System.out.println("User not found");
				return Collections.emptyList(); // User not found;
			}

			Criteria reviewCt = session.createCriteria(Review.class);
			List<Review> reviews = reviewCt.list();

			if (reviews.isEmpty()) {
				System.out.println("No reviews found");
				return Collections.emptyList(); // No reviews found;
			}

			tx.commit();
			return reviews.stream().map(review -> new Review(review.getReviewId(), review.getComment(),
					review.getBookId(), review.getUserId())).collect(Collectors.toList());

		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction rolled back due to: " + e.getMessage());
		} finally {
			session.close();
		}
		return null;
	}
	public boolean  reportreview(Review review, Long userId, Report report) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			User existingUser = session.get(User.class, userId);
			if (existingUser == null) {
				logger.info("User not found");
				return false; // User not found;
			}

			String isreviewIdEmpty = ValidEmailChecker.isReviewIdEmpty(review);
			if (isreviewIdEmpty != null) {
				logger.info("Review ID is empty");
				return false;
			}
              String isReviewBookIdempty = ValidEmailChecker.isReviewBookIdEmpty(review);
               if (isReviewBookIdempty != null) {
               logger.info("Review Book ID is empty");
               return false;
              }
			Criteria ct = session.createCriteria(Review.class);
			ct.add(Restrictions.eq("reviewId", review.getReviewId()));
			ct.add(Restrictions.eq("userId", review.getUserId().getUserId()));
			ct.add(Restrictions.eq("bookId", review.getBookId()));
			Review reviewToReport = (Review) ct.uniqueResult();

			if (reviewToReport == null) {
				System.out.println("Review not found for this user and book");
				return false; // Review not found;
			}
           
           report.setReportedBy(existingUser);
           report.setStatus(false);
           report.setReview(reviewToReport);
           session.save(report);
		   tx.commit();
		  Email_Automation.EmailSendForReportReview(review , report);
		  return true;
		  

		} catch (Exception e) {
			tx.rollback();
			System.out.println("Transaction rolled back due to: " + e.getMessage());
		} finally {
			session.close();
		}
		return false;
	
	
	}
	
	}	
	
	
	
	
	

