package com.rtcc.demo.services;

import com.rtcc.demo.DTOs.AcessDTO;
import com.rtcc.demo.DTOs.AuthenticationDTO;
import com.rtcc.demo.security.JWT.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;
    public AcessDTO login(AuthenticationDTO authRequestDTO) {

        try {
            //Mecanismo de credencial para o Spring
            UsernamePasswordAuthenticationToken userAuth =
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.username(),
                            authRequestDTO.password());

//            System.out.println("Auth: " + authRequestDTO.username());
//            System.out.println("Auth: " + authRequestDTO.password());

            //Prepara Mecanismo para Authenticacao
            Authentication authentication = authenticationManager.authenticate(userAuth);

            //busca usuario logado
            UserDetailImpl userAuthenticate = (UserDetailImpl) authentication.getPrincipal();

            String token = jwtUtils.genereateTokenFromUserDetailsImpl(userAuthenticate);

            return new AcessDTO(token);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }

    }
}
