package com.microservices.loginservice.feignInterface;

import com.microservices.loginservice.constants.MicroServiceConstants;
import com.microservices.loginservice.requestDTO.UserRequestDTO;
import com.microservices.loginservice.responseDTO.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.microservices.loginservice.constants.MicroServiceConstants.*;

@FeignClient(name = UserMicroServiceConstants.BASE, path = MicroServiceConstants.BASE_API)
@Service
public interface UserInterface {

    @RequestMapping(value = UserMicroServiceConstants.SEARCH_USER)
    UserResponseDTO searchUser(@RequestBody UserRequestDTO requestDTO);

    @RequestMapping(value = UserMicroServiceConstants.UPDATE_USER)
    void updateUser(@RequestBody UserResponseDTO responseDTO);
}
