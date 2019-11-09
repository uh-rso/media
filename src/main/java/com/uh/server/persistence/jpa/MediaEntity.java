package com.uh.server.persistence.jpa;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class MediaEntity {

    @Id
    @GeneratedValue
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Getter
    @Setter
    private String originalFilename;

    @Lob
    @Getter
    @Setter
    private byte[] blob;

    @Getter
    @Setter
    private String storageResourceId;

    @Getter
    @Setter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "media")
    private Set<TagEntity> tags = new HashSet<>();

    @Getter
    @Setter
    private String contentType;

    @Getter
    @Setter
    private String internalType;

    @Getter
    @Setter
    private String ownerId;

    @CreationTimestamp
    @Getter
    @Setter
    private LocalDateTime createDateTime;

}
