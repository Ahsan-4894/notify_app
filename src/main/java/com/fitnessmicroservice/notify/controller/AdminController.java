package com.fitnessmicroservice.notify.controller;

import com.fitnessmicroservice.notify.dto.response.UserDashboardDto;
import com.fitnessmicroservice.notify.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<List<UserDashboardDto>> getDashboard() {
        List<UserDashboardDto> dashboard = adminService.getDashboard();
        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }
}

