# Siren4J Components #

Siren4J has four component types that represent the pieces of the Siren specification as seen at: https://github.com/kevinswiber/siren

  * Entity (https://github.com/kevinswiber/siren#entities)
  * Link (https://github.com/kevinswiber/siren#links-1)
  * Action (https://github.com/kevinswiber/siren#actions-1)
  * Field (https://github.com/kevinswiber/siren#fields-1)


All components match the properties and rules of the Siren specification except for the Field component which extends the specification slightly to allow for some
HTML5 style validation attributes as was suggested in https://groups.google.com/forum/#!topic/siren-hypermedia/U1Enrwq9za0

So fields support the following extra and optional properties:

  * max
  * min
  * maxLength
  * pattern
  * placeholder
  * required
  * step

Components are built either using the builders or via the reflecting converter if using the Siren4J resources API.

The components will serialize via Jackson to become the correct JSON representation and back. In fact we use Jackson for creating the JSON string representation when you call toString() on any of the components.

**TODO:** Add links to JavaDoc