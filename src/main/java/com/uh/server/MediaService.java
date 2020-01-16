package com.uh.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.uh.server.dto.MediaDto;
import com.uh.server.persistence.jpa.MediaEntity;
import com.uh.server.persistence.jpa.MediaRepository;
import com.uh.server.persistence.jpa.TagEntity;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MediaService {

    private static final Logger LOG = LoggerFactory.getLogger(MediaService.class);

    private final MediaRepository mediaRepository;
    private final TagExtractor tagExtractor;
    private final CollectionService collectionService;
    private final MeterRegistry meterRegistry;

    public MediaService(
            final MediaRepository mediaRepository,
            final TagExtractor tagExtractor,
            final CollectionService collectionService,
            final MeterRegistry meterRegistry) {
        this.mediaRepository = mediaRepository;
        this.tagExtractor = tagExtractor;
        this.collectionService = collectionService;
        this.meterRegistry = meterRegistry;
    }

    private static MediaDto mapToDto(final MediaEntity entity, final boolean loadTags) {
        final MediaDto mediaDto = new MediaDto();
        mediaDto.setId(entity.getId().toString());
        mediaDto.setOriginalFilename(entity.getOriginalFilename());
        // mediaDto.setStorageResourceId(entity.getStorageResourceId());
        mediaDto.setContentType(entity.getContentType());
        // mediaDto.setInternalType(entity.getInternalType());
        mediaDto.setOwnerId(entity.getOwnerId());
        mediaDto.setCreateDateTime(entity.getCreateDateTime());
        /*
        mediaDto.setTags(entity.getTags().stream().map(t -> {
            var tag = new TagDto();
            tag.setName(t.getName());
            tag.setValue(t.getValue());
            return tag;
        }).collect(Collectors.toList()));
         */
        if (loadTags) {
            mediaDto.setTags(new HashMap<>());
            entity.getTags().forEach(t -> {
                mediaDto.getTags().put(t.getName(), t.getValue());
            });
        }
        return mediaDto;
    }

    private static MediaDto mapToDto(final MediaEntity entity) {
        return mapToDto(entity, false);
    }

    public List<MediaDto> getAll() {
        final Iterable<MediaEntity> all = mediaRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .map(MediaService::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MediaDto> getAll(final String ownerId) {
        final Stream<MediaEntity> all = mediaRepository.findAllByOwnerId(ownerId);
        return all
                .map(MediaService::mapToDto)
                .collect(Collectors.toList());
    }

    public MediaDto get(final String id, final String userId) {
        final Optional<MediaEntity> byId = mediaRepository.findByIdAndOwnerId(Long.valueOf(id), userId);
        return byId.map(MediaService::mapToDto).orElseThrow();
    }

    public byte[] binary(final String id, final String userId) {
        final Optional<MediaEntity> byId = mediaRepository.findByIdAndOwnerId(Long.valueOf(id), userId);
        return byId.map(MediaEntity::getBlob).orElseThrow();
    }

    public MediaDto create(
            final MultipartFile file,
            final MediaDto media,
            final String ownerId) {
        final MediaEntity entity = new MediaEntity();
        entity.setOwnerId(ownerId);
        try {
            entity.setBlob(file.getBytes());
        }
        catch (IOException e) {
            LOG.error("Error reading file when creating media", e);
        }
        entity.setOriginalFilename(file.getOriginalFilename());
        if (file.getContentType() != null) {
            entity.setContentType(file.getContentType());
            // entity.setInternalType();
        }
        // entity.setStorageResourceId();

        // Set tags
        // TODO Move to another class
        final Map<String, String> imageMeta = tagExtractor.getTags(file);

        final MediaEntity saved = mediaRepository.save(entity);

        imageMeta.keySet().forEach(key -> {
            try {
                var tag = new TagEntity();
                tag.setName(key);
                tag.setValue(imageMeta.get(key));
                tag.setMedia(saved);
                entity.getTags().add(tag);
            }
            catch (Exception e) {
                LOG.error("Could not add meta tags");
                throw new RuntimeException(e);
            }
        });

        final MediaEntity saved2 = mediaRepository.save(entity);

        MediaDto mediaDto = mapToDto(saved2, true);

        try {
            collectionService.evaluateMedia(mediaDto);
        }
        catch (Exception e) {
            LOG.error("Could not evaluate media", e);
        }

        incrementMediaCounter();

        return mediaDto;
    }

    public void delete(final String id, final String userId) {
        // TODO Check ownerId!
        mediaRepository.deleteById(Long.valueOf(id));
    }

    private void incrementMediaCounter() {
        // https://blog.autsoft.hu/defining-custom-metrics-in-a-spring-boot-application-using-micrometer/
        // https://www.baeldung.com/micrometer
        // mediaCounter = this.meterRegistry.counter("media.created");
        final var mediaCounter = Counter.builder("media.created")
                .description("Number of all medium created.")
                .register(meterRegistry);
        mediaCounter.increment();
    }

}

