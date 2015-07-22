# Fluent Builder API #

Using the fluent builder API allows the easy creation of Siren components.
A builder is used by first creating a new builder instance then calling the various methods which will affect the final outputted component when the build method is called. Builders can also validate the component being built to make sure they are Siren specification compliant.


## Example Usage ##
```
   
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

Notice how you can just chain together the various builder method calls as every method passes back the instance of the builder.

You can of course simply reference the builder with a variable to be able to use it in more complex code to build a component such as:
```
    
    List<Link> linksList = getLinksFromSomewhere();
    EntityBuilder builder = EntityBuilder.newInstance();

    builder.setEntityClass("SomeClass");
    builder.addProperties(getPropertiesFromSomewhere());
    
    // Add links one at a time, could have just called
    // builder.addLinks(List<link>) instead.
    for(Link link : linksList)
    {
       builder.addLink(link);
    }

    Entity myEntity = builder.build();

```