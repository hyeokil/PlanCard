package com.ssafy.backend.domain.fcm.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMTokenDto {
    @NotNull
    private String token;

}
