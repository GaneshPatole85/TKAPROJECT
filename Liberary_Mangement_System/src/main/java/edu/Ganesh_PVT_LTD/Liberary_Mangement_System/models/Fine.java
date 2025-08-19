package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Fine {
 public Date getFineDate() {
		return FineDate;
	}
	public void setFineDate(Date fineDate) {
		FineDate = fineDate;
	}
public User getFineTo() {
		return FineTo;
	}
	public void setFineTo(User fineTo) {
		FineTo = fineTo;
	}
public Long getFineId() {
		return fineId;
	}
	public void setFineId(Long fineId) {
		this.fineId = fineId;
	}
	public Long getBookId() {
		return bookId;
	}
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	public double getAmmount() {
		return ammount;
	}
	public void setAmmount(double ammount) {
		this.ammount = ammount;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
@Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private Long fineId;
 private Long bookId;
 private double ammount;
 private boolean status;
 private String tid;
 private Date FineDate;
 @ManyToOne
 @JoinColumn(name = "fine_to_user_id", referencedColumnName = "userId")
 private User FineTo;
public Fine() {
	
} 
 
}
