package kg.nurtelecom.o_subscriber_service.controller;

import kg.nurtelecom.o_subscriber_service.filesystem.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PhotoController {

    private final FileStorageService fileStorageService;

    public PhotoController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/photos/{fileName:.+}")
    public ResponseEntity<Resource> servePhoto(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadAsResource(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(resolveMediaType(fileName))
                .body(resource);
    }

    private MediaType resolveMediaType(String fileName) {
        String lower = fileName.toLowerCase();

        if (lower.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        }

        return MediaType.APPLICATION_OCTET_STREAM;
    }
}