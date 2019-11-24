package com.uh.server;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/demo")
public class DemoController {

    @GetMapping(path = "/info")
    public DemoInfoDto getDemoInfo() {
        return new DemoInfoDto(
                Collections.singletonList("uh9830"),
                "Aplikacija za shranjevanje slik, iz katerih izluščimo geolokacijo in prikažemo države, kjer je uporabnik že bil.",
                Arrays.asList("http://35.228.221.209/v1/media", "http://35.228.115.194/v1/collections"),
                Arrays.asList("https://github.com/uh-rso/media", "https://github.com/uh-rso/collection"),
                Arrays.asList("https://travis-ci.org/uh-rso/collection", "https://travis-ci.org/uh-rso/media"),
                Arrays.asList("https://hub.docker.com/r/uroshekic/rso-media", "https://hub.docker.com/r/uroshekic/rso-collection")
        );
    }

    @Data
    @AllArgsConstructor
    public class DemoInfoDto implements Serializable {
        private List<String> clani;
        @JsonProperty("opis_projekta")
        private String opisProjekta;
        private List<String> mikrostoritve;
        private List<String> github;
        private List<String> travis;
        private List<String> dockerhub;
    }

}

