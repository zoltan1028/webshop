package com.e_commerce.webshop.service;

import com.e_commerce.webshop.controller.ProductController;
import com.e_commerce.webshop.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class PictureService {

    // Inject the Windows directory path from application.properties
    @Value("${external.directory.path}")
    private String externalDirPath;

    public void performFileOperations(Long id, String base64String) {
        // Create a File object for the external directory
        File externalDir = new File(externalDirPath);
        if (!externalDir.exists()) {
            if (externalDir.mkdirs()) {
                System.out.println("Directory created: " + externalDirPath);
            } else {
                System.err.println("Failed to create directory: " + externalDirPath);
            }
        }
        // Example: Check if the directory exists
        if (externalDir.exists() && externalDir.isDirectory()) {
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
        File[] files = externalDir.listFiles();
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
                    System.out.println(line);
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
