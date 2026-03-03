package com.fitnessmicroservice.notify.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponseDto {
    private String username;
    private String message;
    private String userId;
    private String token;
    private boolean success;
}