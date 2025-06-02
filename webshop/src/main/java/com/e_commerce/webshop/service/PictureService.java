package com.e_commerce.webshop.service;

import com.e_commerce.webshop.controller.ProductController;
import com.e_commerce.webshop.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@Component
public class PictureService {

    // Inject the Windows directory path from application.properties
    @Value("${external.directory.path}")
    private String externalDirPath;

    public void deleteAllFilesFromPicturesDirectory() {
        Path externalDir = Paths.get(externalDirPath);
        if (Files.exists(externalDir)) {
            try {
                Files.walk(externalDir)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                System.out.println("Directory and its contents deleted: " + externalDirPath);
            } catch (IOException e) {
                System.err.println("Failed to delete directory: " + externalDirPath);
                throw new RuntimeException(e);
            }
        }
    }
    public void writePicturesToFile(Long id, String base64String) {
        Path externalDir = Paths.get(externalDirPath);
        if (!externalDir.toFile().exists()) {
            if (externalDir.toFile().mkdirs()) {
                System.out.println("Directory created: " + externalDirPath);
            } else {
                System.err.println("Failed to create directory: " + externalDirPath);
            }
        }
        // Example: Check if the directory exists
        if (externalDir.toFile().exists() && externalDir.toFile().isDirectory()) {

            System.out.println("External directory exists: " + externalDirPath);
        } else {
            System.out.println("External directory does not exist: " + externalDirPath);
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(externalDir + "/" + id.toString() + ".txt"));
            writer.write(base64String);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Example: List all files in the directory
        File[] files = externalDir.toFile().listFiles();
        if (files != null) {
            for (File file : files) {
                System.out.println("Found file: " + file.getName());
            }
        }
    }

    public String getProductPictureById(Long id) {
        String fileName = id + ".txt";
        // Create a File object with the specified path
        File file = new File(externalDirPath, fileName);


        StringBuilder picture = new StringBuilder();
        // Check if the file exists
        if (file.exists()) {
            // Read the file using BufferedReader
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    picture.append(line);
                }
            } catch (IOException e) {
                System.err.println("An error occurred while reading the file: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("The file does not exist: " + file.getAbsolutePath());
        }
        assert picture != null;
        return  picture.toString();
    }
}
