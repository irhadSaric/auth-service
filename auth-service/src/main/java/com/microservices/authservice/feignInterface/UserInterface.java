package com.microservices.authservice.feignInterface;

import com.microservices.authservice.constants.MicroServiceConstants;
import com.microservices.authservice.responseDTO.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import static com.microservices.authservice.constants.MicroServiceConstants.*;
import static com.microservices.authservice.constants.MicroServiceConstants.UserMicroServiceConstants.FETCH_USER_BY_USERNAME;

@FeignClient(name = USER_MICROSERVICE, path = BASE_API)
@Service
public interface UserInterface {

    @RequestMapping(value = FETCH_USER_BY_USERNAME)
    Optional<UserResponseDTO> fetchUserByUsername(@PathVariable("username") String username);
}
