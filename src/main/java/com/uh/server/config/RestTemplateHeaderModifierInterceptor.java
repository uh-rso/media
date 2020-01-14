package com.uh.server.config;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        Jwt token = (Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (token != null) {
            request.getHeaders().add("Authorization", "Bearer " + token.getTokenValue());
        }
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }
}
