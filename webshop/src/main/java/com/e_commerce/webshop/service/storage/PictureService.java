package com.e_commerce.webshop.service.storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;
@Service
public class PictureService {
    // Injects the Windows directory path from application.properties
    @Value("${external.directory.path}")
    private String externalDirPath;
    public void deleteAllFilesFromPicturesDirectory() {
        Path externalDir = Paths.get(externalDirPath);
        if (Files.exists(externalDir)) {
            try (Stream<Path> walk = Files.walk(externalDir)) {
                walk.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                throw new UncheckedIOException("Failed to delete " + path, e);
                            }
                        });
                System.out.println("Directory and its contents deleted: " + externalDirPath);
            } catch (IOException e) {
                System.err.println("Failed to delete directory: " + externalDirPath);
                throw new RuntimeException(e);
            }
        }
    }
    public void writePicturesToFile(Long id, String base64String) {
        Path externalDir = Paths.get(externalDirPath);
        try {
            if (Files.notExists(externalDir)) {
                Files.createDirectories(externalDir);
                System.out.println("Directory created: " + externalDirPath);
            }
            Path filePath = externalDir.resolve(id + ".txt");
            Files.writeString(filePath, base64String);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file", e);
        }
    }
    public String getProductPictureById(Long id) throws IOException {
        Path filePath = Paths.get(externalDirPath, id + ".txt");
        if (!Files.exists(filePath)) {
            System.err.println("File does not exist: " + filePath.toAbsolutePath());
            return "";
        }
        return Files.readString(filePath);  // Java 11+ API, beolvassa a teljes fájlt egyszerűen
    }
    public String readPictureFromResources(String fileNameInResources) throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileNameInResources)) {
            if (in == null) {                          // resource not found
                System.err.println("Resource not found: " + fileNameInResources);
                return "";
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
