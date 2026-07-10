package com.parking.parking_system.support.dto;

import java.util.List;

public record DtoAuthUserResponse(
        String subject,
        String username,
        String email,
        List<String> roles
) {}
