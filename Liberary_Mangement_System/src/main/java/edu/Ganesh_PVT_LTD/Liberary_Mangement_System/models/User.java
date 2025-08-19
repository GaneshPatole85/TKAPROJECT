package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class User {
	    public String getTwoFactorAuthCode() {
		return TwoFactorAuthCode;
	}

	public void setTwoFactorAuthCode(String twoFactorAuthCode) {
		TwoFactorAuthCode = twoFactorAuthCode;
	}

	public boolean isTwoFactorAuthEnabled() {
		return TwoFactorAuthEnabled;
	}

	public void setTwoFactorAuthEnabled(boolean twoFactorAuthEnabled) {
		TwoFactorAuthEnabled = twoFactorAuthEnabled;
	}

		public User() {
		
	}

		public boolean isIsblocked() {
		return Isblocked;
	}

	public void setIsblocked(boolean isblocked) {
		Isblocked = isblocked;
	}

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long userId;

	    private String name;

	    private String email;

	    private String password;

	    private String role; // Example: "ADMIN", "STUDENT"
	    
	    private boolean Isblocked;
        
	    private String TwoFactorAuthCode;
	    
	    private boolean TwoFactorAuthEnabled;
	    
	    @OneToMany(mappedBy = "issuedTo", cascade = CascadeType.ALL)
	    private List<Book> booksIssued;
       
	    @OneToMany(mappedBy = "FineTo", cascade = CascadeType.ALL)
	    private List<Fine> FineApplied;
	    // Getters and Setters
	    public Long getUserId() {
	        return userId;
	    }

	    public void setUserId(Long userId) {
	        this.userId = userId;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getRole() {
	        return role;
	    }

	    public void setRole(String role) {
	        this.role = role;
	    }

	}

