idea-gradle-javaee-template
===

template project of JavaEE-Glassfish application for IntelliJ IDEA and Gradle

get start
===

1. clone this repository
1. run gradle `rmDummy` task and then run `idea` task.
1. open IntelliJ IDEA with created ipr file.
1. create new Run/Debug Configuration for glassfish server as follows

![Run/Debug Configuration](http://googledrive.com/host/0B4hhdHWLP7RRQW14VFFMT1U4NlE)

* The context root is the project name.

generate persistence.xml
===

To generate persistence.xml, add task with type `JpaPersistenceXml`.

```groovy
task persistenceXml(type: JpaPersistenceXml) {
    baseJdbcUrl 'jdbc:h2:tcp://localhost:9092/~/h2database'
    jdbcUser 'sa'
    jdbcPassword 'sa'
}
```

Available configurations are listed bellow.

configuration | type | description | default|notes
:-|:-:|:-|:-|:-
version | String | jpa version | `2.1` | -
unitName | String | persistence unit name | `javaee` | -
transactionType | String | type of transaction | `JTA` | only `JTA` is available(currently)
providerName | String | name of JPA provider | `eclipse` | only `eclipse` is available(currently). If you use another provider(like Hibernate), please customize `JpaPersistenceXml.groovy`
jdbcDriver | String | a name of fully qualified class name of JDBC Driver | `org.h2.Driver` | -
baseJdbcUrl | String | a base name of database url | `jdbc:h2:tcp://localhost:9092/~/h2` | This url is base url so that the url becomes "jdbc:h2:tcp://localhost:9092/~/h2/project-name"
jdbcUser | String | user name of database | null | -
jdbcPassword | String | password of the user | null | -
jdbcProperties | Map | jpa's property key and value | becomes `<property name="key" value="value"/>`
