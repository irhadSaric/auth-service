package com.microservices.loginservice.constants;

/*THIS CLASS CONTAINS CUSTOM ERROR MESSAGES FOR VARIOUS EXCEPTIONS*/
public class ErrorMessageConstants {

    public interface ForgetPassword {
        String DEVELOPER_MESSAGE = "Password didn't match with the original one.";
        String MESSAGE = "Incorrect password.Forgot Password?";
    }

    public interface IncorrectPasswordAttempts {
        String DEVELOPER_MESSAGE = "User is blocked with status 'B'";
        String MESSAGE = "User is blocked. Please contact your system administrator.";
    }

    public interface InvalidUserStatus {
        String DEVELOPER_MESSAGE_FOR_BLOCKED = "The user has status 'B'";
        String DEVELOPER_MESSAGE_FOR_INACTIVE = "The user has status 'N'";
        String MESSAGE_FOR_BLOCKED = "The user is in blocked state.";
        String MESSAGE_FOR_INACTIVE = "The user is in inactive state.";
    }

    public interface InvalidUserUsername {
        String DEVELOPER_MESSAGE = "User entity returned null";
        String MESSAGE = "User with given username doesn't exits.";
    }
}
