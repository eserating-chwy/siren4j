# Siren4J Resource API #

While you can simply just use the fluent builder API and components to work with Siren specified JSON, I wanted to provide a more complete solution to model web resources and to not need to directly manipulate the Siren components. To that end I created the Resource API.

## What exactly is a resource? ##
A web resource is any thing or entity that can be identified, named, addressed or handled, in any way whatsoever. Web resources can be a document, image, file, web service output, etc...

In our case we want to represent the resources produced or processed for machine to machine interaction via a REST service.

So in our case a resource maps to a Siren entity but is more specific to what it is trying to represent.

Let's say we have a video catalog REST service that allows reviews. The resources for that may be something like:
  * Video
  * Review

Now you could simply represents these in Java as Siren Entity objects and things would be fine, but there is probably going to be a lot more work to do to get from the actual object representation of these types in your application to a Siren Entity and vise versa. Entity's need to all have links to point to themselves, other links and actions to describe what things can be done next. How do you get those links set? More code, more work.

Here is where the Resource API comes in.

## The Resource API in Action ##

So let's build those resources with help from the Resource API.

A couple of rules:
  * The resource class must implement `com.google.code.siren4j.resource.Resource`
  * A `com.google.code.siren4j.annotation.Siren4JEntity` annotation must be present
  * The class should be done in java bean style, ie setters/getters for member variables
  * Fields that are sub entities must have a `com.google.code.siren4j.annotation.Siren4JSubEntity` annotation or they will not be picked up by the reflecting converter.
  * Any field that should be a collection sub entity should be of type `com.google.code.siren4j.resource.CollectionResource`

We will do the video resource first. Assume the following REST service urls will be created for a video service:

  * GET /videos        - gets all videos
  * GET /videos/{id}   - get a specific video instance

```
@Siren4JEntity(name = "video")
public class Video extends BaseResource {
    
    private String id;    
    private String name;
    private String description;
    private String genre;
    private Rating rating;
            
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

    public enum Rating {G, PG, PG13, R, NR, X}

}
```
Here is the initial video resource class. A simple bean with just an siren entity annotation set that defines a unique name. It also extends `com.google.code.siren4j.resource.BaseResource` which implements the `Resource` interface. As it is, an instance of this class would successfully convert to an Entity object, but would not be HATEAOS compliant as there would be no 'self' link. We can rectify this by adding a 'uri' value to the siren entity annotation.
```
   @Siren4JEntity(name = "video", uri = "/videos/{id}")
```
The {id} is a token indicating that it should be replaced by the value in the field named 'id' for the instance.

Let's make a video instance object and use the `ReflectingConverter` to convert it to a Siren4J Entity component.
```
        Video video = new Video();
        video.setId("z1977");
        video.setName("Star Wars");
        video.setDescription("An epic science fiction space opera");
        video.setRating(Rating.PG);
        video.setGenre("scifi");
        
        ResourceConverter converter = ReflectingConverter.newInstance();
        Entity videoEntity = converter.toEntity(video);
        System.out.println(videoEntity.toString());
```
So this will convert to an entity and then we use `toString()` on the entity to get the JSON string representation (Jackson is used under the hood for the JSON serialization).
```
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
Look at that!!! Our resource converted nicely into a Siren entity including a fully resolved 'self' link. (Note: the above JSON was formatted by me, toString does not prettify).

Let's create a review resource class now.
```
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
```
Notice the `@Siren4JProperty` annotation, it is telling the converter to use the name 'reviewText' instead of the field name for the 'body' property.
## Collection Sub Entity ##
Now we will revist the `Video` class and add 'reviews' as a new field which will be a collection of `Review` objects.
```
    @Siren4JSubEntity(uri = "/video/{parent.id}/reviews")
    private CollectionResource<Review> reviews;
```
We added the new 'reviews' field and used type `CollectionResource` and notice that we also added `@Siren4JSubEntity` annotation to the field. If we did not add this annotation the field would not make it into the converted entity. Also note the '{parent.id}' token, a sub entity annotation is applied to the field object and not the object that contains the field. If we just used '{id}' then the review id would be used but we want to use the video id in this case.

I'll add a couple reviews (off screen) and run the converter again.
```
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
      "properties":{
        "offset":0,
        "total":0,
        "limit":0
      },
      "entities":[
        {
          "class":[
            "review"
          ],
          "rel":[
            "item"
          ],
          "properties":{
            "id":"1",
            "reviewer":"Fred",
            "reviewText":"I loved it!!",
            "reviewdate":"2013-04-17T23:06:08.828+0000"
          },
          "links":[
            {
              "rel":[
                "self"
              ],
              "href":"/reviews/1"
            }
          ]
        },
        {
          "class":[
            "review"
          ],
          "rel":[
            "item"
          ],
          "properties":{
            "id":"1",
            "reviewer":"John",
            "reviewText":"Overwelmed!!!!",
            "reviewdate":"2013-04-17T23:06:08.828+0000"
          },
          "links":[
            {
              "rel":[
                "self"
              ],
              "href":"/reviews/1"
            }
          ]
        }
      ],
      "links":[
        {
          "rel":[
            "self"
          ],
          "href":"/video/z1977/reviews"
        }
      ]
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
Nice!! We now have a collection of embedded sub entities for reviews on the video.

If we don't want to embed the 'review' entities inside the collection to possibly save on bandwidth we can change the annotation to:
```
@Siren4JSubEntity(uri = "/video/{parent.id}/reviews", embeddedLink = true)
```

Which results in:
```
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

## Adding Links to an Entity ##
Links can be added  to an entity using annotations or can be dynamically  added or overridden programatically at run time.

So let's add a "next" link via an annotation:
```
    @Siren4JEntity(name = "video", uri = "/videos/{id}",
        links {
            @Siren4JLink(rel = "next", href = "/videos/{id}/nextstep")
        }     
    )
```

Or we could add dynamically at run time like so:
```
   Video video = getVideo(videoId);
   Collection<Link> links = new ArrayList<Link>();
   links.add(LinkBuilder.newInstance().setRel("next").setHref("/videos/{id}/nextstep").build());
   video.setEntityLinks(links);
   
```

Links set dynamically at run time always have precedence and will overwrite links set by annotation with the same relationship defined.

## Adding Actions to Entities ##
Similar to links, actions can also be added via annotations or programatically at run time.

So once again let's add it via annotations:
```
    @Siren4JEntity(name = "video", uri = "/videos/{id}",
        actions = {
            @Siren4JAction(
                name = "someaction", 
                href = "/videos/{id}/dosomething",
                fields = {
                    @Siren4JActionField(name = "videoname", required = true);
                }    
        }
    )
```

And the same thing dynamically:
```
     Video video = getVideo(videoId);
     Collection<Action> actions = new ArrayList<Action>();
     
     //Create the field first     
     Field videonameField = FieldBuilder.newInstance()
         .setName("videoname")
         .setRequired(true)
         .build();
     //Now the action
     Action someAction = ActionBuilder.newInstance()
          .setName("someaction")
          .setHref("/videos/{id}/dosomething")
          .addField(videonameField)
          .build()

     actions.add(someAction);
     video.setEntityActions(actions);
```
The same precedence rules as links apply for actions.

## URI/HREF Token Resolving ##
You probably noticed above that most of the uris and hrefs have tokens in them.
Tokens will be resolved by the converter at conversion time.  The key in the the token refers to a field name
on the object converted. So a token like so **`{description}`** would map to the objects 'description' field and would be
replaced with the actual value in the instance.

### Scope ###
So things get a little more tricky with a sub entity, a @Siren4JSubEntity annotation applies its values to the sub entity object
of the object being converted. So in the case of **`{description}`**, this would end up being the 'description' value in the
sub entity object instance. But what if we actually want to use the parents 'description' object value in the sub entities uri?
Simple just add the parent prefix like so **`{parent.description}`** and like magic the parent value will be used.

### Keeping The Token a Token ###
Sometimes you don't want to have the token resolve, but instead remain a token that stays in the uri so that the client can use it on
its end to fill in the token value. This can be done by putting square brackets around the tokens key like so: **`{[description]}`**.
When it is serialized to an Entity the uri token will be **`{description}`**, no square brackets ready for use by the client.