package com.rtcc.demo.infra.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import lombok.RequiredArgsConstructor;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfigurations {

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;


    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private static final String ROLE_ADMIN = UserRole.ADMIN.getRole();
    private static final String ROLE_COORDINATOR = UserRole.COORDINATOR.getRole();
    private static final String ROLE_ACADEMIC = UserRole.ACADEMIC.getRole();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/coordinator/{id}").hasRole(ROLE_COORDINATOR)
                        .requestMatchers(HttpMethod.PUT, "/coordinator/password/{id}").hasRole(ROLE_ADMIN)
                        .requestMatchers("/coordinator", "/coordinator/{id}").hasRole(ROLE_ADMIN)


                        .requestMatchers(HttpMethod.GET, "/course/{id}", "/course").permitAll()
                        .requestMatchers("/course", "/course/{id}").hasRole(ROLE_ADMIN)

                        .requestMatchers(HttpMethod.GET, "/professor/{id}").permitAll()
                        .requestMatchers("/professor", "professor/{id}").hasRole(ROLE_COORDINATOR)


                        .requestMatchers(HttpMethod.GET,
                                "/tcc/{id}", "/tcc", "/tcc/search",
                                "tcc/view/{filename}", "tcc/search/").permitAll()

                        .requestMatchers(HttpMethod.POST, "tcc/filter", "tcc/search/").permitAll()
                        .requestMatchers("/tcc", "/tcc/{id}").hasRole(ROLE_COORDINATOR)

                        .requestMatchers("/verify-email").permitAll()

                        .requestMatchers("/authenticate").permitAll()

                        .requestMatchers(HttpMethod.POST, "/academic").permitAll()
                        .anyRequest().authenticated()
                ).exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint))
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(conf -> conf.jwt(jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())
                ))
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        var jks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jks);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return authenticationConverter;
    }
}