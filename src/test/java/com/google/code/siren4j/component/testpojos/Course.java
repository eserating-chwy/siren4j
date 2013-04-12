package com.google.code.siren4j.component.testpojos;

import java.util.Collection;
import java.util.Date;

import com.google.code.siren4j.annotations.SirenAction;
import com.google.code.siren4j.annotations.SirenActionField;
import com.google.code.siren4j.annotations.SirenEntity;
import com.google.code.siren4j.annotations.SirenInclude;
import com.google.code.siren4j.annotations.SirenInclude.Include;
import com.google.code.siren4j.annotations.SirenLink;
import com.google.code.siren4j.annotations.SirenSubEntity;
import com.google.code.siren4j.component.impl.ActionImpl.Method;

@SirenInclude(Include.NON_NULL)
@SirenEntity(name = "course", uri = "/courses/{courseid}",
   links = {
       @SirenLink(rel = "reviews", href="/courseReviews/course/{courseid}")
   },
   actions = {
       @SirenAction(
           name = "addReview",
           method = Method.POST,
           href = "/courseReviews/course/{courseid}",
           fields = {
        	    @SirenActionField(name = "userid", type = "text", required = true ),
        	    @SirenActionField(name = "body", type = "text", required = true, maxLength = 250)
           }
       )
   }
)
public class Course extends BasePojo{
    
    
    public Course() {

    }

    private String courseid;
    private String title;
    private String description;
    private String type;
    private Date createdate;
    
    @SirenSubEntity(rel = "authors", uri = "/authors?courseid={parent.courseid}")
    private Collection<Author> authors;

    public String getCourseid() {
	return courseid;
    }

    public void setCourseid(String courseid) {
	this.courseid = courseid;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public Date getCreatedate() {
	return createdate;
    }

    public void setCreatedate(Date createdate) {
	this.createdate = createdate;
    }

    public Collection<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<Author> authors) {
        this.authors = authors;
    }
    
    

}
