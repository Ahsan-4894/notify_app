package com.fitnessmicroservice.notify.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLogoutResponseDto {
    private String message;
    private boolean success;
}
