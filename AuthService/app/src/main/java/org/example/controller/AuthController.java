package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.model.UserInfoDTO;
import org.example.response.JWTResponseDTO;
import org.example.service.JWTService;
import org.example.service.RefreshTokenService;
import org.example.service.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @PostMapping("auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDTO userInfoDTO) {

        try {
            Boolean isSignUped = userDetailsService.signupUser(userInfoDTO);
            if(Boolean.FALSE.equals(isSignUped)) {
                return new ResponseEntity<>("Already Exists", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDTO.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDTO.getUsername());
            return new ResponseEntity<>(JWTResponseDTO.builder().accessToken(jwtToken).
                    token(refreshToken.getToken()).build(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
