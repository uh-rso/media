package com.uh.server;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.uh.server.dto.MediaDto;
import com.uh.server.persistence.jpa.MediaEntity;
import com.uh.server.persistence.jpa.MediaRepository;
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

    public MediaService(final MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    private static MediaDto mapToDto(final MediaEntity entity) {
        final MediaDto mediaDto = new MediaDto();
        mediaDto.setId(entity.getId().toString());
        mediaDto.setOriginalFilename(entity.getOriginalFilename());
        // mediaDto.setStorageResourceId(entity.getStorageResourceId());
        mediaDto.setContentType(entity.getContentType());
        // mediaDto.setInternalType(entity.getInternalType());
        mediaDto.setOwnerId(entity.getOwnerId());
        mediaDto.setCreateDateTime(entity.getCreateDateTime());
        return mediaDto;
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
            LOG.error("Error creating media", e);
        }
        entity.setOriginalFilename(file.getOriginalFilename());
        if (file.getContentType() != null) {
            entity.setContentType(file.getContentType());
            // entity.setInternalType();
        }
        // entity.setStorageResourceId();

        final MediaEntity saved = mediaRepository.save(entity);
        return mapToDto(saved);
    }

    public void delete(final String id) {
        mediaRepository.deleteById(Long.valueOf(id));
    }

}

