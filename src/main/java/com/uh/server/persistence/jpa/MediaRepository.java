package com.uh.server.persistence.jpa;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;


public interface MediaRepository extends CrudRepository<MediaEntity, Long> {
    Stream<MediaEntity> findAllByOwnerId(final String ownerId);

    Optional<MediaEntity> findByIdAndOwnerId(final Long id, final String ownerId);
}
