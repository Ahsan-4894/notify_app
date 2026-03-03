package com.fitnessmicroservice.notify.controller;

import com.fitnessmicroservice.notify.dto.request.UserLoginRequestDto;
import com.fitnessmicroservice.notify.dto.request.UserRegisterRequestDto;
import com.fitnessmicroservice.notify.dto.response.UserLoginResponseDto;
import com.fitnessmicroservice.notify.dto.response.UserLogoutResponseDto;
import com.fitnessmicroservice.notify.dto.response.UserRegisterResponseDto;
import com.fitnessmicroservice.notify.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("HELLO!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        return new ResponseEntity<>(userService.login(userLoginRequestDto, response), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return new ResponseEntity<>(userService.register(userRegisterRequestDto), HttpStatus.CREATED);
    }

}
