package com.fitnessmicroservice.notify.controller;


import com.fitnessmicroservice.notify.dto.response.UserLogoutResponseDto;
import com.fitnessmicroservice.notify.dto.response.UserResponseDto;
import com.fitnessmicroservice.notify.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyself(){
        return new ResponseEntity<>(userService.getMyself(), HttpStatus.OK);
    }

    @PostMapping("/editMyself")
    public ResponseEntity<Boolean> editMyself(@RequestBody String newPassword){
        return new ResponseEntity<>(userService.editMyself(newPassword), HttpStatus.OK);
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<UserResponseDto> getUserById(@NotNull @PathVariable String userId) {
//        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
//    }

    @GetMapping("/logout")
    public ResponseEntity<UserLogoutResponseDto> logout(HttpServletResponse response) {
        return new ResponseEntity<>(userService.logout(response), HttpStatus.OK);
    }

}
