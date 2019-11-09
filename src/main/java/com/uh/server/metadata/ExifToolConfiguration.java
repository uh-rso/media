package com.uh.server.metadata;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExifToolConfiguration {

    @Bean
    public ExifTool exifTool() {
        return new ExifToolBuilder()
                .enableStayOpen(60000) // Try to clean resources once per minute.
                .build();
    }

}
