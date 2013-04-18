package com.google.code.siren4j.component.testpojos;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JSubEntity;
import com.google.code.siren4j.resource.BaseResource;
import com.google.code.siren4j.resource.CollectionResource;

@Siren4JEntity(name = "video", uri = "/videos/{id}")
public class Video extends BaseResource {
    
    private String id;    
    private String name;
    private String description;
    private String genre;
    private Rating rating;
    @Siren4JSubEntity(uri = "/video/{parent.id}/reviews", embeddedLink = true)
    private CollectionResource<Review> reviews;
            
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getGenre() {return genre;}
    public void setGenre(String genre) {this.genre = genre;}
    public Rating getRating() {return rating;}
    public void setRating(Rating rating) {this.rating = rating;}
    public CollectionResource<Review> getReviews() {return reviews;}
    public void setReviews(CollectionResource<Review> reviews) {this.reviews = reviews;}


    public enum Rating {G, PG, PG13, R, NR, X}

}
