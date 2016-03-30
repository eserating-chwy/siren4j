

This is a java library to help with the creation and use of    
Hypermedia entities as specified by the Siren hypermedia specification. See https://github.com/kevinswiber/siren for more detail on the specification.

It contains classes to that represent the Siren components (Entity, Link, Action, Field).
See: https://github.com/eserating/siren4j/wiki/Siren4J-Components

These can be easily built with a fluent builder API.
```java
EXAMPLE BUILDER:
   
    // Create a new self Link
    Link selfLink = LinkBuilder.newInstance()
       .setRelationship(Link.RELATIONSHIP_SELF)
       .setHref("/self/link")
       .build();

    // Create a new Entity
    Entity result = EntityBuilder.newInstance()
       .setEntityClass("test")
       .addProperty("foo", "hello")
       .addProperty("number", 1)
       .addLink(selfLink)
       .build();
``` 

See: https://github.com/eserating/siren4j/wiki/Fluent-Builder-API

There is also a resource API that simplifies entity creation and management using reflection and annotations.


EXAMPLE RESOURCE:
```java
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
```
AN INSTANCE OF A RESOURCE CLASS CAN BE CONVERTED LIKE THIS:    
```java      
    ResourceConverter converter = ReflectingConverter.newInstance();
    Entity videoEntity = converter.toEntity(videoResource);
```

AND WILL RESULT IN SIREN JSON THAT LOOKS LIKE THIS:
```xml
{
  "class":[
    "video"
  ],
  "properties":{
    "name":"Star Wars",
    "id":"z1977",
    "description":"An epic science fiction space opera",
    "genre":"scifi",
    "rating":"PG"
  },
  "entities":[
    {
      "class":[
        "siren4J.collectionResource",
        "collection"
      ],
      "rel":[
        "reviews"
      ],
      "href":"/video/z1977/reviews"
    }
  ],
  "links":[
    {
      "rel":[
        "self"
      ],
      "href":"/videos/z1977"
    }
  ]
}
```    


See: https://github.com/eserating/siren4j/wiki/Siren4J-Resource-API

[http://wiki.siren4j.googlecode.com/hg/javadoc/index.html JavaDoc]

*Note:* This projects documentation is a work in progress.
