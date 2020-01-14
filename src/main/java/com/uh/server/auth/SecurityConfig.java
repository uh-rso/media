package com.uh.server.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // @Value("${auth0.audience}")
    // private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);
        // OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator("");
        // OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(jwtDecoder, audienceValidator);
        // jwtDecoder.setJwtValidator(withAudience);
        // return jwtDecoder;
        return JwtDecoders.fromIssuerLocation(issuer);
    }
}
