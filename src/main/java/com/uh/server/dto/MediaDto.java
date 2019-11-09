package com.uh.server.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data
public class MediaDto implements Serializable {

    private String id;
    private String originalFilename;
    // private String storageResourceId;
    // private List<TagDto> tags;
    private Map<String, String> tags;
    private String contentType;
    // private String internalType;
    private String ownerId;
    private LocalDateTime createDateTime;

}
