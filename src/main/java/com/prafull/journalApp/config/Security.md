# Spring Security in Spring Boot

## Table of Contents

1. [Introduction](#introduction)
2. [Key Concepts](#key-concepts)
3. [Configuration](#configuration)
4. [Authentication](#authentication)
5. [Authorization](#authorization)
6. [Password Encoding](#password-encoding)
7. [Example Configuration](#example-configuration)
8. [Conclusion](#conclusion)

## Introduction

Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto
standard for securing Spring-based applications. Spring Security is a framework that focuses on providing both
authentication and authorization to Java applications.

## Key Concepts

1. **Authentication**: The process of verifying the identity of a user.
2. **Authorization**: The process of determining if a user has proper permission to perform an action or access a
   resource.
3. **Principal**: Currently logged in user.
4. **Granted Authority**: Permission or right given to Principal.
5. **Role**: Group of authorities

## Configuration

Spring Security can be configured using Java configuration. The main configuration class should be annotated
with `@Configuration` and `@EnableWebSecurity`.

```java

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Configuration details here
}
```

## Authentication

Spring Security supports various authentication methods:

1. In-memory authentication
2. JDBC authentication
3. UserDetailsService
4. LDAP authentication

In the provided example, custom authentication is implemented using `UserDetailsService`.

```java

@Autowired
public SecurityConfig(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
}

@Bean
public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
}
```

## Authorization

Authorization rules are defined in the `securityFilterChain` method. This method configures which endpoints are
accessible based on the user's role or authentication status.

```java

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .authorizeHttpRequests(request -> request
                    .requestMatchers("/public/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/journal/**", "/user/**").authenticated()
                    .anyRequest().authenticated())
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(Customizer.withDefaults())
            .build();
}
```

In this configuration:

- `/public/**` endpoints are accessible to all
- `/admin/**` endpoints require ADMIN role
- `/journal/**` and `/user/**` endpoints require authentication
- All other endpoints require authentication

## Password Encoding

It's crucial to encode passwords before storing them. Spring Security provides various password encoders. In this
example, `BCryptPasswordEncoder` is used:

```java

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

## Example Configuration

Here's a breakdown of the provided `SecurityConfig` class:

```java

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuration for HTTP security
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Configuration for authentication provider
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

This configuration:

1. Enables web security
2. Sets up a custom `UserDetailsService` for authentication
3. Configures authorization rules
4. Sets up an `AuthenticationManager`
5. Configures a `DaoAuthenticationProvider`
6. Sets up password encoding

## Conclusion

Spring Security provides a robust set of features for securing your Spring Boot application. By understanding and
properly configuring authentication, authorization, and password encoding, you can ensure that your application is
protected against common security threats.

Remember to always keep your security dependencies up to date and regularly review your security configuration to ensure
it meets your application's evolving needs.