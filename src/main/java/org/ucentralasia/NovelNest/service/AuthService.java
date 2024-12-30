package org.ucentralasia.NovelNest.service;

import org.springframework.http.ResponseEntity;
import org.ucentralasia.NovelNest.dto.*;

public interface AuthService {
    ResponseEntity<String> register(RegisterRequest registerRequest);
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest) throws Exception;
    ResponseEntity<String> verifyEmail(String token);
    ResponseEntity<String> forgotPassword(ForgetPasswordRequest request);
    ResponseEntity<String> resetPassword(ResetPasswordRequest request);
}
