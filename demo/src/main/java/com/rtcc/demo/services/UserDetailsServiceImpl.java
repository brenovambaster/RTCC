package com.rtcc.demo.services;

import com.rtcc.demo.exception.EmailNotVerifiedException;
import com.rtcc.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.rtcc.demo.infra.config.UserAuthenticated;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, EmailNotVerifiedException {
        log.info("Tentando carregar usuário pelo e-mail: {}", username);

        return userRepository.findByEmail(username)
                .map(user -> {
                    if (!user.isEmailVerified()) {
                        log.warn("E-mail não verificado para o usuário: {}", username);
                        throw new EmailNotVerifiedException("E-mail não foi verificado.");
                    }
                    log.info("Usuário carregado com sucesso: {}", username);
                    return new UserAuthenticated(user);
                })
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado com o e-mail: {}", username);
                    return new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username);
                });
    }
}
