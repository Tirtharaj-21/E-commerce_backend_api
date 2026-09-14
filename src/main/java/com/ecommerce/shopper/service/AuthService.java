package com.ecommerce.shopper.service;

import com.ecommerce.shopper.dto.AuthResponse;
import com.ecommerce.shopper.dto.LoginRequest;
import com.ecommerce.shopper.dto.SignupRequest;
import com.ecommerce.shopper.entity.InvalidatedToken;
import com.ecommerce.shopper.entity.Role;
import com.ecommerce.shopper.entity.User;
import com.ecommerce.shopper.exception.EmailAlreadyExistsException;
import com.ecommerce.shopper.exception.InvalidCredentialsException;
import com.ecommerce.shopper.repository.InvalidatedTokenRepository;
import com.ecommerce.shopper.repository.UserRepository;
import com.ecommerce.shopper.security.JwtUtil;
import com.ecommerce.shopper.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("An account with this email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(new UserPrincipal(saved));

        return toAuthResponse(saved, token);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        String token = jwtUtil.generateToken(new UserPrincipal(user));
        return toAuthResponse(user, token);
    }

    @Transactional
    public void logout(String token) {
        if (invalidatedTokenRepository.existsByToken(token)) {
            return;
        }
        Date expiry = jwtUtil.extractExpiration(token);
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .token(token)
                .expiryDate(LocalDateTime.ofInstant(expiry.toInstant(), java.time.ZoneId.systemDefault()))
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .build();
    }
}
