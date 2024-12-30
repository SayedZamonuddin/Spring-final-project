package org.ucentralasia.NovelNest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ucentralasia.NovelNest.dto.*;
import org.ucentralasia.NovelNest.model.user.*;
import org.ucentralasia.NovelNest.repository.*;
import org.ucentralasia.NovelNest.service.AuthService;
import org.ucentralasia.NovelNest.service.CustomUserDetailsService;
import org.ucentralasia.NovelNest.service.EmailService;
import org.ucentralasia.NovelNest.service.JwtUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    @Transactional
    public ResponseEntity<String> register(RegisterRequest request){
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username taken. ");
        }

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exits. ");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(false);

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set role
        String role = request.getRole();
        if(role == null || role.isEmpty()){
            role = "ROLE_USER";
        } else if (!role.equals("ROLE_USER") && !role.equals("ROLE_CONTRIBUTOR")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
        }

        user.setRole(role);

        // Save user
        userRepository.save(user);

        // Generate verification token and send email
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);

        //Call email service
        emailService.sendVerificationEmail(user.getEmail(), token);


        return ResponseEntity.ok("User registered successfully. Please check your email to verify your account.");
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // returns an object of type object: explicity casting (UserDetails) tells the compiler that "hey i know the object returned is actually of type UserDetails, so treat it as such"

            final String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new LoginResponse(jwt));

        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if(verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or expired verification token");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        // Optionally delete the token after verification
        verificationTokenRepository.delete(verificationToken);

        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }

    @Override
    public ResponseEntity<String> forgotPassword(ForgetPasswordRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if(user == null){
            return ResponseEntity.ok("If a user with that email exists, a password reset link has been sent");
        }

        // Generate a unique token
        String token = UUID.randomUUID().toString();

        // Create a PasswordResetToken object
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Token valid for an hour

        // save the token
        passwordResetTokenRepository.save(resetToken);

        // send the reset email
        try {
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send password reset email.");
        }

        return ResponseEntity.ok("If a user with that email exists, a password reset link has been sent");

    }

    @Override
    public ResponseEntity<String> resetPassword(ResetPasswordRequest request) {
        // Find the reset token
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElse(null);

        if(resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        // Get the associated user
        User user = resetToken.getUser();

        // Update the user's password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Invalidate the token after successful password reset
        passwordResetTokenRepository.delete(resetToken);

        return ResponseEntity.ok("Password has been reset successfully");
    }
}
