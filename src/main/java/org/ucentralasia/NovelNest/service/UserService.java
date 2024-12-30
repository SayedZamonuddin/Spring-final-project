//package org.ucentralasia.NovelNest.service;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Transient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.ucentralasia.NovelNest.dto.RegisterRequest;
//import org.ucentralasia.NovelNest.repository.PasswordResetTokenRepository;
//import org.ucentralasia.NovelNest.repository.UserRepository;
//import org.ucentralasia.NovelNest.repository.VerificationTokenRepository;
//
//@Entity
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private VerificationTokenRepository verificationTokenRepository;
//
//    @Autowired
//    private PasswordResetTokenRepository passwordResetTokenRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Transient
//    public void registerUser(RegisterRequest request){
//        // Registration logic
//    }
//
//    // methods for password reset can be set here (maybe later)
//}
