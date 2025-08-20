package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
@Entity
public class Book {
	 public double getRating_points() {
		return rating_points;
	}

	public void setRating_points(double rating_points) {
		this.rating_points = rating_points;
	}

	public User getIssuedTo() {
		return issuedTo;
	}

	public void setIssuedTo(User issuedTo) {
		this.issuedTo = issuedTo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public boolean isIssued() {
		return isIssued;
	}

	public void setIssued(boolean isIssued) {
		this.isIssued = isIssued;
	}

	
	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getTotalCopies() {
		return totalCopies;
	}

	public void setTotalCopies(int totalCopies) {
		this.totalCopies = totalCopies;
	}

	public int getAvailableCopies() {
		return availableCopies;
	}

	public void setAvailableCopies(int availableCopies) {
		this.availableCopies = availableCopies;
	}

	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private Long bookId;

	    private String title;

	    private String author; 

	    private String category; // Added directly as a column

	    private int totalCopies;
	    
	    private double rating_points;
	    
	    @ManyToOne
	    @JoinColumn(name = "issued_to_user_id", referencedColumnName = "userId")
	    private User issuedTo;
	    
	    private int availableCopies;
	    @Temporal(TemporalType.DATE)
	    private Date issueDate;

	    @Temporal(TemporalType.DATE)
	    private Date returnDate;
	    
	    private boolean isIssued;

	  
}
