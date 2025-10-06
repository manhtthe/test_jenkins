package com.web.bookingKol.domain.user.services;

import com.web.bookingKol.common.payload.ApiResponse;
import com.web.bookingKol.domain.user.dtos.UpdateProfileRequest;
import com.web.bookingKol.domain.user.dtos.UserDTO;

import java.util.List;

public interface UserService {
    ApiResponse<List<UserDTO>> getAllUser();

}

