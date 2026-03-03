package com.fitnessmicroservice.notify.service;

import com.fitnessmicroservice.notify.dto.request.UserLoginRequestDto;
import com.fitnessmicroservice.notify.dto.request.UserRegisterRequestDto;
import com.fitnessmicroservice.notify.dto.response.UserLoginResponseDto;
import com.fitnessmicroservice.notify.dto.response.UserLogoutResponseDto;
import com.fitnessmicroservice.notify.dto.response.UserRegisterResponseDto;
import com.fitnessmicroservice.notify.dto.response.UserResponseDto;
import com.fitnessmicroservice.notify.entity.User;
import com.fitnessmicroservice.notify.entity.UserPrincipal;
import com.fitnessmicroservice.notify.exception.custom.UserAlreadyExistException;
import com.fitnessmicroservice.notify.exception.custom.UserNotFoundException;
import com.fitnessmicroservice.notify.repository.UserRepo;
import com.fitnessmicroservice.notify.util.AuthContext;
import com.fitnessmicroservice.notify.util.CookieUtil;
import com.fitnessmicroservice.notify.util.JwtAuthUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthUtil jwtAuthUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthContext authContext;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CookieUtil cookieUtil;

    public User getUserById(String userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new UserNotFoundException("User with this id: " + userId + " not found");
        return user;
    }

    public UserLoginResponseDto login(UserLoginRequestDto loginRequest, HttpServletResponse response) throws BadCredentialsException {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userPrincipal.getUser();
            String token = jwtAuthUtil.generateAccessToken(user);

            cookieUtil.setCookieInTheBrowser(
                    response, "token", token, 7 * 24 * 60 * 60
            );
            return UserLoginResponseDto.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .token(token)
                    .message("User logged in successfully")
                    .success(Boolean.TRUE)
                    .build();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequest) {
        User user = userRepo.findByUsername(userRegisterRequest.getUsername()).orElse(null);
        if(user != null)
            throw new UserAlreadyExistException("User with this username: " + userRegisterRequest.getUsername() + " already exists");

        user = User.builder()
                .username(userRegisterRequest.getUsername())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .role("ROLE_USER")
                .build();
        userRepo.save(user);
        return UserRegisterResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .message("User registered successfully")
                .success(Boolean.TRUE)
                .build();

    }

    public UserLogoutResponseDto logout(HttpServletResponse response){
        cookieUtil.deleteCookieFromBrowser(
                response, "token"
        );

        return UserLogoutResponseDto.builder()
                .message("User logged out successfully")
                .success(Boolean.TRUE)
                .build();
    }

    public UserResponseDto getMyself() {
        UserPrincipal loggedInUser = authContext.getCurrentLoggedInUser();
        User user = loggedInUser.getUser();
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public Boolean editMyself(String newPassword) {
        UserPrincipal loggedInUser = authContext.getCurrentLoggedInUser();
        User user = loggedInUser.getUser();

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));

        userRepo.save(user);
        return Boolean.TRUE;
    }

    public UserRegisterResponseDto registerAsAdmin(@Valid UserRegisterRequestDto userRegisterRequest) {
        User user = userRepo.findByUsername(userRegisterRequest.getUsername()).orElse(null);
        if(user != null)
            throw new UserAlreadyExistException("Admin with this username: " + userRegisterRequest.getUsername() + " already exists");

        user = User.builder()
                .username(userRegisterRequest.getUsername())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .role("ROLE_ADMIN")
                .build();
        userRepo.save(user);
        return UserRegisterResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .message("Admin registered successfully")
                .success(Boolean.TRUE)
                .build();
    }
}
