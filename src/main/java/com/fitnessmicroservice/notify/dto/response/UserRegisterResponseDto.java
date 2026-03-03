package com.fitnessmicroservice.notify.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterResponseDto {
    private String username;
    private String userId;
    private boolean success;
    private String message;
}
