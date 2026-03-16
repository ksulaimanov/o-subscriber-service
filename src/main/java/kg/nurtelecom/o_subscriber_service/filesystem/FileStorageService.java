package kg.nurtelecom.o_subscriber_service.filesystem;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveFile(MultipartFile file);
}