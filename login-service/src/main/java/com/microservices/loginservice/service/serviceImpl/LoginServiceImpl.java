package com.microservices.loginservice.service.serviceImpl;

import com.microservices.loginservice.exceptions.UnauthorisedException;
import com.microservices.loginservice.feignInterface.UserInterface;
import com.microservices.loginservice.jwt.JwtTokenProvider;
import com.microservices.loginservice.requestDTO.LoginRequestDTO;
import com.microservices.loginservice.requestDTO.UserRequestDTO;
import com.microservices.loginservice.responseDTO.UserResponseDTO;
import com.microservices.loginservice.service.LoginService;
import com.microservices.loginservice.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.microservices.loginservice.constants.PatternConstants.*;
import com.microservices.loginservice.constants.ErrorMessageConstants.*;

@Service
@Transactional("transactionManager")
public class LoginServiceImpl implements LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserInterface userInterface;

    @Override
    public String login(LoginRequestDTO requestDTO, HttpServletRequest request) {

        LOGGER.info("LOGIN PROCESS STARTED ::::");

        long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        UserResponseDTO user = fetchUserDetails.apply(requestDTO);

        validateUserUsername.accept(user);

        validateUserStatus.accept(user);

        validatePassword.accept(requestDTO, user);

        String jwtToken = jwtTokenProvider.createToken(requestDTO.getUserCredential(), request);

        LOGGER.info("LOGIN PROCESS COMPLETED IN ::: " + (DateUtils.getTimeInMillisecondsFromLocalDate() - startTime)
                + " ms");

        return jwtToken;
    }

    private Function<LoginRequestDTO, UserResponseDTO> fetchUserDetails = (loginRequestDTO) -> {

        Pattern pattern = Pattern.compile(EmailConstants.EMAIL_PATTERN);
        Matcher m = pattern.matcher(loginRequestDTO.getUserCredential());

        return m.find() ? userInterface.searchUser
                (UserRequestDTO.builder().username(null).emailAddress(loginRequestDTO.getUserCredential()).build())
                : userInterface.searchUser
                (UserRequestDTO.builder().username(loginRequestDTO.getUserCredential()).emailAddress(null).build());
    };

    private Consumer<UserResponseDTO> validateUserUsername = (user) -> {
        if (Objects.isNull(user))
            throw new UnauthorisedException(InvalidUserUsername.MESSAGE, InvalidUserUsername.DEVELOPER_MESSAGE);
        LOGGER.info(":::: USER USERNAME VALIDATED ::::");
    };

    private Consumer<UserResponseDTO> validateUserStatus = (user) -> {

        switch (user.getStatus()) {
            case 'B':
                throw new UnauthorisedException(InvalidUserStatus.MESSAGE_FOR_BLOCKED,
                        InvalidUserStatus.DEVELOPER_MESSAGE_FOR_BLOCKED);

            case 'N':
                throw new UnauthorisedException(InvalidUserStatus.MESSAGE_FOR_INACTIVE,
                        InvalidUserStatus.DEVELOPER_MESSAGE_FOR_INACTIVE);
        }
        LOGGER.info(":::: USER STATUS VALIDATED ::::");
    };

    private BiConsumer<LoginRequestDTO, UserResponseDTO> validatePassword = (requestDTO, user) -> {

        LOGGER.info(":::: USER PASSWORD VALIDATION ::::");

        if (BCrypt.checkpw(requestDTO.getPassword(), user.getPassword())) {
            user.setLoginAttempt(0);
            userInterface.updateUser(user);
        } else {
            user.setLoginAttempt(user.getLoginAttempt() + 1);

            if (user.getLoginAttempt() >= 3) {
                user.setStatus('B');
                userInterface.updateUser(user);

                LOGGER.debug("USER IS BLOCKED DUE TO MULTIPLE WRONG ATTEMPTS...");
                throw new UnauthorisedException(IncorrectPasswordAttempts.MESSAGE,
                        IncorrectPasswordAttempts.DEVELOPER_MESSAGE);
            }

            LOGGER.debug("INCORRECT PASSWORD...");
            throw new UnauthorisedException(ForgetPassword.MESSAGE, ForgetPassword.DEVELOPER_MESSAGE);
        }

        LOGGER.info(":::: USER PASSWORD VALIDATED ::::");
    };
}
