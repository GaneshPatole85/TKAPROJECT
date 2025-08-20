package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Review {
	
	

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Book getBookId() {
		return bookId;
	}

	public void setBookId(Book bookId) {
		this.bookId = bookId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;
	private String comment;
	@ManyToOne
	@JoinColumn(name = "userId") // foreign key in DB// foreign key in DB
	private User userId;
	
	@ManyToOne
	@JoinColumn(name = "bookId") // foreign key in DB
	private Book bookId;

    private Date createdAt;
	public Review(Long  reviewId, String comment, Book book, User user) {
		this.reviewId = reviewId;
		this.comment = comment;
		this.bookId = book;
		this.userId = user;
		this.createdAt = new Date(); 
	}

	public Review() {
		
	}

	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	

	
}
