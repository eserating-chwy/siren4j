package com.google.code.siren4j.component.testpojos;

import java.util.Collection;
import java.util.Date;

import com.google.code.siren4j.annotations.Siren4JAction;
import com.google.code.siren4j.annotations.Siren4JActionField;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JInclude;
import com.google.code.siren4j.annotations.Siren4JInclude.Include;
import com.google.code.siren4j.annotations.Siren4JLink;
import com.google.code.siren4j.annotations.Siren4JSubEntity;
import com.google.code.siren4j.component.impl.ActionImpl.Method;

@Siren4JInclude(Include.NON_NULL)
@Siren4JEntity(name = "course", uri = "/courses/{courseid}",
   links = {
       @Siren4JLink(rel = "reviews", href="/courseReviews/course/{courseid}")
   },
   actions = {
       @Siren4JAction(
           name = "addReview",
           method = Method.POST,
           href = "/courseReviews/course/{courseid}",
           fields = {
        	    @Siren4JActionField(name = "userid", type = "text", required = true ),
        	    @Siren4JActionField(name = "body", type = "text", required = true, maxLength = 250)
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
    
    @Siren4JSubEntity(rel = "authors", uri = "/authors?courseid={parent.courseid}")
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
