package com.google.code.siren4j.component.testpojos;

import java.util.Date;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JProperty;
import com.google.code.siren4j.resource.BaseResource;

@Siren4JEntity(name = "review", uri = "/reviews/{id}")
public class Review extends BaseResource {
   
    private String id;
    private String reviewer;
    
    @Siren4JProperty(name = "reviewText")
    private String body;
    private Date reviewdate;
    
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getReviewer() {return reviewer;}
    public void setReviewer(String reviewer) {this.reviewer = reviewer;}
    public String getBody() {return body;}
    public void setBody(String body) {this.body = body;}
    public Date getReviewdate() {return reviewdate;}
    public void setReviewdate(Date reviewdate) {this.reviewdate = reviewdate;}   
    
}
