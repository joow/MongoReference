# MongoReference

A simple [Jackson] module to handle serialization of objets references in [MongoDB] using only id of serialized reference.

# Usage

Let's say we have two classes :

```java
public class User() {
    private String firstName;
    private String lastName;

    // Constructors omitted.
}

public class Message() {
    private User from;
    private String content;

    // Constructors omitted.
}
```

and two objects :

```java
    final User from = new User("John", "Doe");
    final Message message = new Message(from, "Hello World");
```

By default the serialization in [MongoDB] will be this :

```json
    { "_id" : ObjectId("..."), "firstName" : "John", "lastName" : "Doe" }

    {"_id" : ObjectId("..."), "from": { "_id" : ObjectId("..."), "firstName" : "John", "lastName" : "Doe" }, "content" : "Hello World" }
```

But you might want to only serialize `_id` field of referenced object for `message`.

For this you just have to :

1. Implements `Referable` interface in `User` class
2. Annotate the `from` field as `@Reference`
3. Register module `ReferenceModule` on [Jackson] mapper.

then your objects will be serialized like this :

```json
    { "_id" : ObjectId("..."), "firstName" : "John", "lastName" : "Doe" }

    {"_id" : ObjectId("..."), "from": "...", "content" : "Hello World" }
```

and when deserialized your objets will only have the id set.

[Jackson]: https://github.com/FasterXML/jackson
[MongoDB]: https://www.mongodb.com/