
# Spring-Helios

This project's aim is to simplify the management of an all-in-one registration/connection service.
This service is self-configured and implemented using Oauth2 and OpenId Connect without any required configuration. Despite this, all modules can still be customized.
Integration is possible with either an application or a micro-services architecture.

## helios-client-spring-boot-starter
Secure the resource by filtering and verifying the authorization token for an incoming HTTP request.
It is primarily utilized when an external server is responsible for delegating Oauth2 and user management.

```xml
<dependency>
    <groupId>com.s3b</groupId>
    <artifactId>helios-client-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```


## helios-core-spring-boot-starter
Contains all the interfaces to be implemented, DTO models, and constants. It can be used to redefine the main services for registration and user manipulations.

```xml
<dependency>
    <groupId>com.s3b</groupId>
    <artifactId>helios-core-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

## helios-jpa-spring-boot-starter
Provides a default JPA implementation to manipulate and store users.

```xml
<dependency>
    <groupId>com.s3b</groupId>
    <artifactId>helios-jpa-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

## helios-oauth2-spring-boot-starter
Configures Oauth2 and OpenId Connect authentication for an application. The modules module helios-core-spring-boot-starter and helios-client-spring-boot-starter are embedded.

```xml
<dependency>
    <groupId>com.s3b</groupId>
    <artifactId>helios-oauth2-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```


## Getting Started
### Client side only
The [helios-client-spring-boot-starter](#helios-client-spring-boot-starter) module aims to make token validation process easier by providing components to interact with them. The authorization token of HTTP entrant requests can be validated through autoconfiguration.

This module is not mandatory, you can design your own services and behaviors to manage incoming tokens.


#### Maven depency

```xml
<dependency>
    <groupId>com.s3b</groupId>
    <artifactId>helios-client-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

#### Define the JWT secret.
```properties
application.token-provider.jwt.jwtSecret=ur_jwtsecret
```


#### It's important to exclude UserDetailsServiceAutoConfiguration.class when using the client.
```java
@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
```

#### To enable the default HeliosClientAutoConfiguration
```java
@Import(HeliosClientAutoConfiguration.class)
```


### Server side
As a reminder [Helios](#helios-oauth2-spring-boot-starter) aims to provide an all-in-one solution to facilitate the management of Oauth2/OpenId Connect in your environment. The steps to configure an application called server, which will handle authentication and user management, are listed below.

By default, two auto-configurations are activated
- HeliosOauth2AutoConfiguration
- HeliosOauth2WebSecurity

#### Maven dependency
```xml
<dependency>
    <groupId>com.s3b</groupId>
    <artifactId>helios-oauth2-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

#### Required properties
```properties
application.token-provider.jwt.jwtSecret=test
application.token-provider.jwt.expiration=test
```

#### Optional - Required configuration to enable default controller
```java
@Import(HeliosController.class)
```

No implementation for handling and saving usernames has been defined. Two options are available for this

#### 1 - Enable [default JPA implementation](helios-jpa-spring-boot-starter)
#### Maven dependency
```xml
<dependency>
    <groupId>com.s3b</groupId>
    <artifactId>helios-jpa-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

#### Required configuration
To define database connection
```yaml
spring:
  datasource:
    url: jdbc:postgresql://host:port/databse
    username: 
    password:
    jpa:
        properties:
        hibernate:
            dialect: org.hibernate.dialect.PostgreSQLDialect
```

#### To set up the users database.
```yaml
spring:
  sql:
    init:
      mode: always
  jpa:
    defer-datasource-initialization: true
```


#### To disable hibernate ddl-auto.
```yaml
spring:
    jpa:
      hibernate:
        ddl-auto: none
```

#### To enable Oauth2.
```yaml
spring:
    security:
        oauth2:
            client:
                registration:
                    google:
                        client-id: 
                        client-secret:
```

#### To enable JPA configuration
```java
@EnableJpaRepositories(basePackageClasses = {DefaultHeliosRepository.class})
@EntityScan(basePackageClasses = {HeliosUserEntity.class})
@SpringBootApplication
```

#### 2 - Define your own implementation
The following interfaces must be implemented to define your own implementation.
- HeliosRegisterService (User registration interface)
- HeliosUserService (Interface for managing users)

## How to use
- **To sign up, you must go to** : /oauth2/authorization/google
- After a successful login, you will be redirected to the defined page (home page by default), with a **token** as query parameter.
##### The redirect page is defined by the query parameter **'redirect_uri'**.
- e.g : oauth2/authorization/google?redirect_uri=url_to_redirect


## Roadmap
- Docker support
- Add more integrations


## Feedback
If you have any feedback, please reach out to us at sebastien.saez.dev@gmail.com

## Authors

- [@Sébastien SAEZ](https://github.com/NTB3S)













