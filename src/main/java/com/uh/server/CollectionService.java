package com.uh.server;

import com.uh.server.dto.MediaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CollectionService {

    private static final Logger LOG = LoggerFactory.getLogger(CollectionService.class);

    private final RestTemplate restTemplate;
    private final boolean evaluateMedia;
    private final String collectionApiUrl;

    public CollectionService(
            final RestTemplate restTemplate,
            @Value("${media-evaluator.evaluate:false}") final String evaluateMedia,
            @Value("${media-evaluator.collection-api-url:#{null}}") final String collectionApiUrl) {
        this.evaluateMedia = "true".equalsIgnoreCase(evaluateMedia);
        this.collectionApiUrl = collectionApiUrl;
        this.restTemplate = restTemplate;
    }

    public void evaluateMedia(final MediaDto mediaDto) {
        if (evaluateMedia) {
            restTemplate.postForObject(collectionApiUrl + "/v1/collections/media", mediaDto, Object.class);
        }
    }

}
