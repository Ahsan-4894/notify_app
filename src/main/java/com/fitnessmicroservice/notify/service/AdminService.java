package com.fitnessmicroservice.notify.service;

import com.fitnessmicroservice.notify.dto.response.UserDashboardDto;
import com.fitnessmicroservice.notify.dto.response.UserLogoutResponseDto;
import com.fitnessmicroservice.notify.dto.response.UserResponseDto;
import com.fitnessmicroservice.notify.entity.User;
import com.fitnessmicroservice.notify.exception.custom.UserNotFoundException;
import com.fitnessmicroservice.notify.repository.NoteRepo;
import com.fitnessmicroservice.notify.repository.UserRepo;
import com.fitnessmicroservice.notify.util.AuthContext;
import com.fitnessmicroservice.notify.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final NoteRepo noteRepo;
    private final AuthContext authContext;
    private final UserRepo userRepo;
    private final CookieUtil cookieUtil;

    public List<UserDashboardDto> getDashboard() {
        List<Object[]> results = noteRepo.getUsersWithNotesCount();
        return results.stream()
                .map(result -> UserDashboardDto.builder()
                        .userId((String) result[0])
                        .notesCount(((Number) result[1]).longValue())
                        .build())
                .collect(Collectors.toList());
    }

    public UserResponseDto getMyself() {
        String adminId = authContext.getIdOfCurrentLoggedInUser();

        User admin = userRepo.findById(adminId).orElse(null);
        if (admin == null)
            throw new UserNotFoundException("Admin user not found with ID: " + adminId);
        return UserResponseDto.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .role(admin.getRole())
                .build();
    }

    public UserLogoutResponseDto logout(HttpServletResponse response) {
        cookieUtil.deleteCookieFromBrowser(
                response, "token"
        );

        return UserLogoutResponseDto.builder()
                .message("User logged out successfully")
                .success(Boolean.TRUE)
                .build();

    }
}

