package com.jwtdemo.controller;

import com.jwtdemo.controller.service.CustomeUserDetailsService;
import com.jwtdemo.helper.JwtUtil;
import com.jwtdemo.model.JwtRequest;
import com.jwtdemo.model.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    private CustomeUserDetailsService customeUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public ResponseEntity generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        System.out.println("jwtRequest  " + jwtRequest);
        try {
            Authentication authenticate = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword()));
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        } catch (BadCredentialsException bce) {
            bce.printStackTrace();
            throw new Exception("Bad Credentials");
        }

        UserDetails userDetails = this.customeUserDetailsService.loadUserByUsername(jwtRequest.getUserName());
        String token = this.jwtUtil.generateToken(userDetails);
        System.out.println("Token - " + token);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
