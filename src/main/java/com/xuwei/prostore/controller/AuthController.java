package com.xuwei.prostore.controller;

import com.xuwei.prostore.dto.UserDto;

import com.xuwei.prostore.repository.UserRepository;
import com.xuwei.prostore.request.LoginRequest;
import com.xuwei.prostore.request.RegisterRequest;
import com.xuwei.prostore.response.ApiResponse;
import com.xuwei.prostore.security.jwt.JwtUtils;
import com.xuwei.prostore.security.user.ShopUserDetails;
import com.xuwei.prostore.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt =
                    jwtUtils.generateTokenForUser(authentication);

            ShopUserDetails userDetails =
                    (ShopUserDetails) authentication.getPrincipal();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", jwt);
            responseData.put("user", Map.of(
                    "userId", userDetails.getUserId(),
                    "email", userDetails.getEmail()
            ));

            return ResponseEntity.ok(new ApiResponse("Login " +
                    "successful", responseData));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // 检查邮箱是否已存在
            if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
                return ResponseEntity.badRequest().body(new ApiResponse("Error: Email is already taken!", null));
            }

            // 创建新用户
            UserDto userDto = new UserDto();
            userDto.setFirstName(registerRequest.getFirstName());
            userDto.setLastName(registerRequest.getLastName());
            userDto.setEmail(registerRequest.getEmail());
            userDto.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

            UserDto createdUser = userService.createUser(userDto);

            return ResponseEntity.ok(new ApiResponse("User " +
                    "registered successfully!", createdUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }
}