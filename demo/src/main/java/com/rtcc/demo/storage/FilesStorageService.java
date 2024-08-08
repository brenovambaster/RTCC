package com.rtcc.demo.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FilesStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FilesStorageService(StorageProperties storageProperties) {
        this.fileStorageLocation = Paths.get(storageProperties.getLocation())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public void renameFile(String oldFileName, String newFileName) throws IOException {

        Path source = this.fileStorageLocation.resolve(oldFileName);
        Path target = this.fileStorageLocation.resolve(newFileName);

        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public void saveFile(MultipartFile file, String uniqueNewFileName) throws IOException {
        // Verificação do tipo de arquivo
        if (!file.getContentType().equals("application/pdf")) {
            throw new IOException("Only PDF files are allowed");
        }

        //218 caracteres livres

        Path targetLocation = this.fileStorageLocation.resolve(uniqueNewFileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void deleteByName(String pdfName){
        Path targetLocation = this.fileStorageLocation.resolve(pdfName);
        try {
            Files.delete(targetLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getFileStorageLocation() {
        return this.fileStorageLocation;
    }

}
