# advanced-module-eshipdiary-web
Advanced Module's EShipDiary project's web platform

#### What's this?
A webapplication for managing rowing clubs' members, ships,
shiptypes and oars; and an online logbook.

It originally was made for _Budapest Evezős Egyesület_, with multitenancy
possibilities in mind.

#### Used technologies
[Spring boot](https://projects.spring.io/spring-boot/)
// [Hibernate](http://hibernate.org/)
// [PostgreSQL](https://www.postgresql.org/)
// [Thymeleaf](http://www.thymeleaf.org/)
// [Bootstrap 3](http://getbootstrap.com/)
// [DataTables](https://datatables.net/)
// [Lombok](https://projectlombok.org/)
// [Travis CI](https://travis-ci.org/)
// [JUnit 4](http://junit.org/junit4/)
// [Powermock](http://powermock.github.io/)
// [Maven](https://maven.apache.org/)
// [SLF4J](https://www.slf4j.org/)
// Java 8, VanillaJS + JQuery, HTML 5

#### Current state (27/04/2017)
It's roughly an MVP with some extra features. Every CRUD works in a
mostly consistent and secure way; there's logging, automatizations,
REST API (with token-based authentication for 3rd party apps
(such as Android)), registration (by admins) & password changing;
and our GitHub repo is connected to Travis CI.

#### Apologies & recommendations
First of all, the project structure might not be trivial at first
sight, but much of the technologies were new to us at the beginning
of the project, so it was quite hard to plan ahead.
We'll try to make it clearer in this documentation.

There's very few tests. Sorry for that, we were focusing on delivering
the MVP. We strongly recommend to start writing them, though, it
could help you to understand how the whole thing works, plus it would
be a huge aid for any future developments.

The JavaScript codes are poorly structured, you're most welcome to
do some refactoring. :)

There's also a very basic [Android app](https://github.com/CodecoolBP20161/advanced-module-eshipdiary-android),
but you can't really do more than log in with it. We had to abandon
it and focus on the web application instead.

## Project structure
Let's start with `/src/main/java/com/codecool/eshipdiary/`.

### config
This is where we store Java code responsible for certain
configurations.

**EmailConfig** - pretty much self explanatory

**SpringDataRestConfig** - responsible for `/api/` REST endpoints.

**WebSecurityConfig** - this is where the main security configurations
are, such as the level of "security clearance" by which certain endpoints
are accessible, or the insertion of additional filters.

### controller
The classes here contain code for connecting frontend with backend.
They don't (purposedly) contain business logic.

Methods with `@ModelAttribute` annotation are predefined models that
you can pass over to the template engine (Thymeleaf) using them to fill
variables within the HTML templates.

Methods annotated with `@RequestMapping` serve various HTTP requests,
load templates redirect to other endpoints or return other data in
the response, e.g. JSON.

### exception
Here do we store custom exceptions, we have only one so far:
**RentalCannotBeSavedExceptions**, which we throw when a rental
cannot be saved (crazy, right?).

### model
Database models here, nothing special. `@Data` annotation means that
Lombok will create getters and setters. Some of the others are pretty
much straight forward, the rest can be looked up in Spring boot's
documentation.

### repository
Database interfaces and projections. Projections are used to modify
the (JSON) data that you access through REST API endpoints.

### security
You can find security connected classes here, such as custom filters.

### service
At last, the business logic!

The methods in the classes here are the ones deciding the _how_.
From implementations of the database interfaces to classes with
other special purposes.

This directory is a bit less well structured as others, mainly
because sometimes it's not so clear where a certain logic should
be. You'll see. Apologies for any mess, feel free to refactor, if
you have better ideas :)

#### AdvancedModuleEshipdiaryWebApplication.java
Everything runs through this one.

There's currently some hardcoded database items here, as well.

****

Now let's see `/src/main/resources/`.

**static** and **templates** contain css-images-javascript files and
html templates, respectively. We don't want to get into explaining
too much, as we've said before, js files are quite poorly structured.

The *.yml files store the application properties,
including environmental variables.

**application-test.yml** is used on Travis CI. This online tool can
run your tests automatically when you push to your Git repo.

We used **application-dev.yml** for development (so that we didn't have
to touch the "real" application properties), your IDE can be set up
to use this.

**application.yml** is the one that's used when your application is
being built. Some of the settings deserves a little explanation:
* `jpa.hibernate.ddl-auto`: if it's `create-drop`, then the database
tables will be dropped and created again every time the application
starts. For example, the free plan on Heroku will shut down your app
if there isn't any activity for a while and with _create-drop_,
everything that isn't hardcoded will be lost from the database. On the
other hand, `update` will only modify the database when needed. As a
downside, you'll have to check if the hardcoded items exist, otherwise
it will try to duplicate them.
* `server.session`: these settings handle how long before a session
expires. Currently it's 30 minutes, and there's a "remember me option".
* `purge.cron.expression`: it's used with the password reset logic. It
basically tells when a password reset link will expire.

****

## Final words
Thanks for reading, we hope you've found some useful info here.
if you have any questions, you can find us via GitHub,
or ask a mentor.
Have fun coding, stay clean, and good luck! ;)

Balázs, Eszter, Gyuri, Kristóf