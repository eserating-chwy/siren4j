### @Siren4JEntity ###
Annotation to define an entity. Every resource class should have one of these annotations to define the name and self uri. Adding links and actions here is convenient but optional as they can also be added dynamically at runtime.
```
    @Siren4JEntity(entityClass = {"course"}, uri = "/courses/{courseid}",
         links = {
             &#064;Siren4JLink(rel = "reviews", href="/courseReviews/course/{courseid}")
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
```
| **Property** | **Required** | **Description** |
|:-------------|:-------------|:----------------|
| entityClass  | no           | Array of strings to classify the entity. |
| name         | no           | Unique name for the entity. Defaults to class name. Deprecated use enityClass. |
| suppressClassProperty | no           | If true, will suppress the $class property which is used for deserialization. |
| uri          | no           | URI pattern to self |
| links        | no           | One or more {@link Siren4JLink} annotations. |
| actions      | no           | One or more {@link Siren4JAction} annotations.|

**Target:** Type

### @Siren4JSubEntity ###
Annotation to define a sub-entity. Typically used on a resource collection field, but can also be on a single type entity field. Values set in this annotation override values set by the entities own @Siren4JEnity annotation.
```
    @Siren4JSubEntity(rel = "authors", uri = "/authors?courseid={parent.courseid}")
```

| **Property** | **Required** | **Description** |
|:-------------|:-------------|:----------------|
| rel          | no           | Array of strings that indicate the subentites relationship with its parent. |
| uri          | no           | URI pattern to self |
| type         | no           | Defines media type of the linked resource, per Web Linking (RFC5899) |
| links        | no           | One or more @Siren4JLink annotations. |
| actions      | no           | One or more @Siren4JAction annotations. |
| embeddedLink | no           | Marks the subentity as an embedded link, defaults to false. |

**Target:** Field

### @Siren4JLink ###
Annotation to for adding links to entities.

```
    @Siren4JLink(rel = "user", href = "/users/{userid}")
```
| **Property** | **Required** | **Description** |
|:-------------|:-------------|:----------------|
| rel          | yes          | Array of string to indicate link relationship to its parent. |
| href         | yes          | The link's URI pattern. |
| type         | no           | Defines media type of the linked resource, per Web Linking (RFC5899) |
| title        | no           | Display text for the link |
| linkClass    | no           | Array of string to classify the link. |
| condition    | no           | A condition must evaluate to true for the action to be rendered. |

**Target:** Annotation

### @Siren4JAction ###
Annotation that defines an action to be added to the entity.
This annotation is used within @Siren4JEntity or @Siren4JSubEntity.
```
     @Siren4JAction(
         name = "addReview",
         title = "Add a Review",
         method = Method.POST,
         href = "/courseReviews/course/{courseid}",
         type = "application/json",
         condition = @Siren4JCondition(name = "somefield", logic=Is.TRUE),
         fields = {
       	    @Siren4JActionField(name = "userid", type = "text", required = true ),
       	    @Siren4JActionField(name = "body", type = "text", required = true, maxLength = 250)
          }
      )
```

| **Property** | **Required** | **Description** |
|:-------------|:-------------|:----------------|
| name         | yes          | Unique name for the action. |
| href         | yes          | URI of the action. |
| method       | no           | Method for the action Method, defaults to Method.GET |
| actionClass  | no           | Array of string to classify the action. |
| title        | no           | Display title for the action. |
| fields       | no           | One or more @Siren4JActionField annotation |
| condition    | no           | A condition must evaluate to true for the action to be rendered. |

**Target:** Annotation

### @Siren4JActionField ###
Annotation to define a field for an action.

```
    @Siren4JActionField(name = "body", type = "text", required = true, maxLength = 250)
```

| **Property** | **Required** | **Description** |
|:-------------|:-------------|:----------------|
| name         | yes          | Unique name for the field. |
| title        | no           | Display title for the action field. |
| fieldClass   | no           | Array of string to classify the field.|
| type         | no           | HTML5 form field type, see below for allowed types. |
| value        | no           | Default value for the field. |
| pattern      | no           | HTML5 validation pattern. |
| required     | no           | Required validation, true or false, defaults to false |
| min          | no           | Validation for minimum numerical value. Type must equal 'number' |
| max          | no           | Validation for maximum numerical value. Type must equal 'number' |
| maxLength    | no           | Validation for maximum string length. |
| step         | no           | The step attribute indicates the granularity that is expected (and required) of the value, by limiting the allowed values. Type must equal 'number' |
| placeHolder  | no           | The placeholder attribute specifies a short hint that describes the expected valueof an input field (e.g. a sample value or a short description of the expected format).|
| options      | no           | One or more @Siren4JFieldOption annotation |
| optionsURL   | no           | URL that points to web resource that return options. |

**Target:** Annotation

### @Siren4JFieldOption ###
Annotation to add options for fields.

```
    condition = @Siren4JFieldOption(title = "Foo", value = "foo", optionDefault = true)
```
| **Property** | **Required** | **Description** |
|:-------------|:-------------|:----------------|
| title        | no           |The display name for the option |
| value        | no           | The options value |
| optionDefault | no           | Flag to indicate this is the default option, defaults to false. |
| data         | no           | One or more @Siren4JOptionData annotation |


**Target:** Annotation

### @Siren4JOptionData ###
Annotation to for adding option data elements. This is for transporting some extra meta data that is useful for field options.

```
    condition = @Siren4JOptionData(key = "someKey", value = "someValue")
```
| **Property** | **Required** | **Description** |
|:-------------|:-------------|:----------------|
| key          | yes          | The key to be used for the property name of the data element. |
| value        | yes          | The value of the data element |


**Target:** Annotation

### @Siren4JProperty ###
Annotation to add meta to a field and change serialization behavior.
Right now it is used to modify the property name.

```
    @Siren4JProperty(name = "courseid")
```

| **Property** | **Required** | **Description** |
|:-------------|:-------------|:----------------|
| name         | no           | Hint to serialization to use this name instead of the field name. |

**Target:** Field

### @Siren4JPropertyIgnore ###
A flag that mark the property to not be included when serialized.

```
    @Siren4JPropertyIgnore
```

**Target:** Method, Field

### @Siren4JInclude ###
Indicates property the inclusion policy.

```
    @PropertyInclude(Include.NON_NULL)
```

**Target:** Type, Field

Possible includes policies:
  * ALWAYS - indicates that property is to be always included, independent of value of the property. (Default)
  * NON\_NULL - indicates that only properties with non-null values are to be included.
  * NON\_EMPTY - indicates that only properties that have values that values that are null or what is considered empty are not to be included.

### @Siren4JCondition ###
Used with Siren4JAction and Siren4JLink to allow logic to include/exclude an action or link based on the value in a field or returned from a method. Only no argument methods can be called.

```
    condition = @Siren4JCondition(name = "somefield", logic=Is.TRUE)
```
| **Property** | **Required** | **Description** |
|:-------------|:-------------|:----------------|
| name         | yes          |Name of the field or method to be eveluated. |
| logic        | no           | The logic type see Siren4JCondition.Is, defaults to Is.TRUE |
| type         | no           | The condition type see Siren4JCondition.Type, defaults to Type.FIELD |

**Target:** Annotation