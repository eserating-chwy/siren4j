package com.google.code.siren4j.component.testpojos;

import java.util.Date;

import com.google.code.siren4j.annotations.SirenEntity;

@SirenEntity(name = "course")
public class Course {
    
    
    public Course() {

    }

    private String courseid;
    private String title;
    private String description;
    private String type;
    private Date createdate;

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

}
