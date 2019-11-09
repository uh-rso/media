package com.uh.server.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TagDto implements Serializable {

    // private MediaDto media;
    private String name;
    private String value;

}
