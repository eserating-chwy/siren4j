package com.google.code.siren4j.component.testpojos;

import java.util.Date;

import com.google.code.siren4j.annotations.Siren4JAction;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JInclude;
import com.google.code.siren4j.annotations.Siren4JInclude.Include;
import com.google.code.siren4j.annotations.Siren4JLink;
import com.google.code.siren4j.annotations.Siren4JProperty;
import com.google.code.siren4j.component.impl.ActionImpl.Method;

@Siren4JInclude(Include.NON_NULL)
@Siren4JEntity(name = "courseComment", uri = "/comments/{id}",
    links = {
	    @Siren4JLink(rel = "course", href = "/courses/{courseid}"),
	    @Siren4JLink(rel = "replies", href = "/replies/comment/{id}")
    },
    actions = {
		@Siren4JAction(name = "Delete", method = Method.DELETE, href = "/comments/{id}"),
		@Siren4JAction(name = "Approve", method = Method.PUT, href = "/comments/{id}/approve")
    }
		
)
public class Comment extends BasePojo{
	
	
	private String courseid;
	@Siren4JProperty(name = "user")
	private String userid;
	private String commentText;
	private Date createdate;
	private Status status;
	
	
	public String getCourseid() {
		return courseid;
	}




	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}




	public String getUserid() {
		return userid;
	}




	public void setUserid(String userid) {
		this.userid = userid;
	}




	public String getCommentText() {
		return commentText;
	}




	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}




	public Date getCreatedate() {
		return createdate;
	}




	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}




	public Status getStatus() {
		return status;
	}




	public void setStatus(Status status) {
		this.status = status;
	}




	public enum Status {
		APPROVED,
		PENDING,
		REJECTED
	}

}
