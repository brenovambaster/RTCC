package com.rtcc.demo.infra.config;

import com.rtcc.demo.exception.EmailNotVerifiedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String path = request.getRequestURI();
        String ipAddress = request.getRemoteAddr();

        if (authException instanceof EmailNotVerifiedException) {
            log.warn("Tentativa de login com e-mail não verificado. Caminho: {}, IP: {}", path, ipAddress);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("E-mail não foi verificado. Por favor, verifique seu e-mail.");
        } else if (authException instanceof InsufficientAuthenticationException) {
            log.error("Falha de autenticação. Usuário e senha incorreto ou e-mail não verificado");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("E-mail não verificado ou usuário bloqueado.");
        } else {
            log.error("Credenciais inválidas. Caminho: {}, IP: {}", path, ipAddress);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Credenciais inválidas. Por favor, tente novamente.");
        }
    }
}
