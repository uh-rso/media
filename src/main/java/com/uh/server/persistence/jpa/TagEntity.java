package com.uh.server.persistence.jpa;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"media_id", "name"})})
public class TagEntity {

    // TODO Remove
    @Id
    @GeneratedValue
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private MediaEntity media;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String value;

    @CreationTimestamp
    @Getter
    @Setter
    private LocalDateTime createDateTime;

}
