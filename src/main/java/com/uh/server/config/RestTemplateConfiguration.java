package com.uh.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    /**
     * Spring Sleuth requires a RestTemplate bean defined, in order to inject span and trace id.
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
