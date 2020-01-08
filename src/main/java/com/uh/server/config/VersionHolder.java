package com.uh.server.config;

import java.util.Optional;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class VersionHolder {

    private final String version;

    public VersionHolder(final ApplicationContext context) {
        version = context.getBeansWithAnnotation(SpringBootApplication.class).entrySet().stream()
                .findFirst()
                .flatMap(es -> {
                    final String implementationVersion = es.getValue().getClass().getPackage().getImplementationVersion();
                    return Optional.ofNullable(implementationVersion);
                }).orElse("unknown");
    }

    public String getVersion() {
        return version;
    }
}
