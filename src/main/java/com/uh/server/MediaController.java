package com.uh.server;

import java.util.List;

import com.uh.server.dto.MediaDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/v1/media")
public class MediaController {

    private final MediaService mediaService;

    private final String userId = "1";

    public MediaController(final MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping
    public List<MediaDto> getAll() {
        return mediaService.getAll(userId);
    }

    @GetMapping("/{id}")
    public MediaDto get(@PathVariable("id") final String id) {
        return mediaService.get(id, userId);
    }

    @GetMapping("/{id}/binary")
    public Object binary(@PathVariable("id") final String id) {
        return mediaService.binary(id, userId);
    }

    @PostMapping
    public MediaDto create(@RequestParam("file") final MultipartFile file) {
        final MediaDto mediaDto = mediaService.create(file, null, userId);
        return mediaService.get(mediaDto.getId(), userId); // This way, created timestamp is fetched as well
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final String id) {
        // TODO Check ownerId!
        mediaService.delete(id);
    }

}

