package com.microservices.authservice.constants;

public class MicroServiceConstants {

    public static final String LOGIN_MICROSERVICE = "/login-service/api/login";

    public static final String USER_MICROSERVICE = "user-service";
    public static final String BASE_API = "/api";

    public interface UserMicroServiceConstants {
        String FETCH_USER_BY_USERNAME = "/fetch-user/{username}";
    }
}
