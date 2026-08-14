package com.resumeiq.backend.service;

import com.resumeiq.backend.request.LoginRequest;
import com.resumeiq.backend.request.RegisterRequest;
import com.resumeiq.backend.response.AuthenticationResponse;

public interface AuthService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

}