package com.microservices.loginservice.controller;

import com.microservices.loginservice.constants.WebResourceKeyConstants;
import com.microservices.loginservice.requestDTO.LoginRequestDTO;
import com.microservices.loginservice.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = WebResourceKeyConstants.BASE_API)
@CrossOrigin("*")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = WebResourceKeyConstants.LOGIN)
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO requestDTO, HttpServletRequest request) {

        String token = loginService.login(requestDTO, request);
        return ResponseEntity.ok().body(loginService.login(requestDTO, request));
    }

    @GetMapping("/login/test")
    public String test() {
        return "test done";
    }
}
