package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.models;
import javax.persistence.*;
import java.util.Date;

@Entity
public class Report {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long reportId;

 @ManyToOne
 private Review review;

 @ManyToOne
 private User reportedBy;

 private String reason;

 @Temporal(TemporalType.TIMESTAMP)
 private Date createdAt;

 private boolean status; // true = resolved, false = pending

 public Report(Long reportId, User reportedBy, Review review2, Date date) {
		this.reportId = reportId;
		this.reportedBy = reportedBy;
		this.review = review2;
		this.createdAt = date;
		this.status = false; // default status is pending
 }

 public Report() {
	
}

public Report(Review review, User reportedBy, String reason, Date createdAt, boolean status) {
     this.review = review;
     this.reportedBy = reportedBy;
     this.reason = reason;
     this.createdAt = createdAt;
     this.status = status;
 }

 // Getters and setters
 public Long getReportId() { return reportId; }
 public void setReportId(Long reportId) { this.reportId = reportId; }

 public Review getReview() { return review; }
 public void setReview(Review review) { this.review = review; }

 public User getReportedBy() { return reportedBy; }
 public void setReportedBy(User reportedBy) { this.reportedBy = reportedBy; }

 public String getReason() { return reason; }
 public void setReason(String reason) { this.reason = reason; }

 public Date getCreatedAt() { return createdAt; }
 public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

 public boolean isStatus() { return status; }
 public void setStatus(boolean status) { this.status = status; }
}
