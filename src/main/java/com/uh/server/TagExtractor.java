package com.uh.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TagExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(TagExtractor.class);

    private final ExifTool exifTool;

    public TagExtractor(final ExifTool exifTool) {
        this.exifTool = exifTool;
    }

    public Map<String, String> getTags(final MultipartFile file) {
        final File tempFile;
        try {
            tempFile = File.createTempFile(UUID.randomUUID().toString(), null);
        }
        catch (IOException e) {
            LOG.error("Temp file could not be created.", e);
            throw new RuntimeException(e);
        }
        try {
            final var stream = new FileOutputStream(tempFile);
            try {
                stream.write(file.getBytes());
            }
            catch (IOException e) {
                LOG.error("Error reading file when creating media", e);
                throw new RuntimeException(e);
            }
            finally {
                stream.close();
            }
        }
        catch (@SuppressWarnings("OverlyBroadCatchBlock") IOException e) {
            LOG.error("Error reading file when creating media", e);
        }
        final Map<Tag, String> imageMeta;
        try {
            imageMeta = exifTool.getImageMeta(tempFile);
        }
        catch (IOException e) {
            LOG.error("ExifTool could not get image metadata", e);
            throw new RuntimeException(e);
        }

        var tags = new HashMap<String, String>();

        imageMeta.keySet().forEach(key -> {
            tags.put(key.getName(), imageMeta.get(key));
        });

        return tags;
    }
}
