package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.Password_Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models.User;

public class Secure_Password {
	public static String IsPasswordStrong(User user) {
		String password = user.getPassword();
		 if (password == null || password.isBlank()) {
		        return "❌ Password must not be null or empty.";
		    }

		    if (password.length() < 12)
		        return "❌ Password must be at least 12 characters long.";

		    if (!password.matches(".*[A-Z].*"))
		        return "❌ Password must contain at least one uppercase letter.";

		    if (!password.matches(".*[a-z].*"))
		        return "❌ Password must contain at least one lowercase letter.";

		    if (!password.matches(".*\\d.*"))
		        return "❌ Password must contain at least one digit.";

		    if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
		        return "❌ Password must contain at least one special character.";

		    if (password.toLowerCase().contains("password") || password.toLowerCase().contains("admin"))
		        return "❌ Password should not contain common words like 'password' or 'admin'.";

		    return "✅ Strong Password!";
        
        
    }
	
	 public static String passcheck(User user) {
		  String check = IsPasswordStrong(user); // Calling the existing method
		    if (!check.contains("✅")) {
		        return "❌ Invalid or Weak Password: " + check;
		    }
		    return "✅ Valid Password";
	    }
	public static String IsPasswordStrongForForget(List<String> DataInput) {
		String password = DataInput.get(1);
		 if (password == null || password.isBlank()) {
		        return "❌ Password must not be null or empty.";
		    }

		    if (password.length() < 12)
		        return "❌ Password must be at least 12 characters long.";

		    if (!password.matches(".*[A-Z].*"))
		        return "❌ Password must contain at least one uppercase letter.";

		    if (!password.matches(".*[a-z].*"))
		        return "❌ Password must contain at least one lowercase letter.";

		    if (!password.matches(".*\\d.*"))
		        return "❌ Password must contain at least one digit.";

		    if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
		        return "❌ Password must contain at least one special character.";

		    if (password.toLowerCase().contains("password") || password.toLowerCase().contains("admin"))
		        return "❌ Password should not contain common words like 'password' or 'admin'.";

		    return "✅ Strong Password!";
        
        
    }
	 public static String passcheckForForget(List<String> DataInput) {
		  String check = IsPasswordStrongForForget(DataInput); 
		    if (!check.contains("✅")) {
		        return "❌ Invalid or Weak Password: " + check;
		    }
		    return "✅ Valid Password";
	    }
	
	 public static String IsPasswordStrongForForgetForNewPassword(List<String> DataInput) {
			String password = DataInput.get(2);
			 if (password == null || password.isBlank()) {
			        return "❌ Password must not be null or empty.";
			    }

			    if (password.length() < 12)
			        return "❌ Password must be at least 12 characters long.";

			    if (!password.matches(".*[A-Z].*"))
			        return "❌ Password must contain at least one uppercase letter.";

			    if (!password.matches(".*[a-z].*"))
			        return "❌ Password must contain at least one lowercase letter.";

			    if (!password.matches(".*\\d.*"))
			        return "❌ Password must contain at least one digit.";

			    if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
			        return "❌ Password must contain at least one special character.";

			    if (password.toLowerCase().contains("password") || password.toLowerCase().contains("admin"))
			        return "❌ Password should not contain common words like 'password' or 'admin'.";

			    return "✅ Strong Password!";
	        
	        
	    }
	 public static String passcheckForForgetForNewPassword(List<String> DataInput) {
		  String check = IsPasswordStrongForForgetForNewPassword(DataInput); 
		    if (!check.contains("✅")) {
		        return "❌ Invalid or Weak Password: " + check;
		    }
		    return "✅ Valid Password";
	    }
    public static final String EncryptPass(String password) {
    	  try {
              MessageDigest md = MessageDigest.getInstance("SHA-256");
              byte[] hash = md.digest(password.getBytes());

              // Encode to Base64 instead of using StringBuilder for hex
              return Base64.getEncoder().encodeToString(hash);
          } catch (NoSuchAlgorithmException e) {
              throw new RuntimeException("SHA-256 not available", e);
          }
    }
}
