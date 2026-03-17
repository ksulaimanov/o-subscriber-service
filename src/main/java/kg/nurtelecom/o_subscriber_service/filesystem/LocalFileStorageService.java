package kg.nurtelecom.o_subscriber_service.filesystem;

import kg.nurtelecom.o_subscriber_service.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path uploadPath;

    public LocalFileStorageService(@Value("${app.file.upload-dir}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new FileStorageException("Не удалось создать директорию для загрузки файлов", e);
        }
    }

    @Override
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Файл пустой");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);

        if (!isImageExtensionAllowed(extension)) {
            throw new FileStorageException("Разрешены только изображения: jpg, jpeg, png");
        }

        String generatedFileName = UUID.randomUUID() + extension;

        try {
            Path targetLocation = this.uploadPath.resolve(generatedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "uploads/" + generatedFileName;
        } catch (IOException e) {
            throw new FileStorageException("Не удалось сохранить файл", e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new FileStorageException("Файл должен иметь расширение");
        }

        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }

    private boolean isImageExtensionAllowed(String extension) {
        return extension.equals(".jpg")
                || extension.equals(".jpeg")
                || extension.equals(".png");
    }
}