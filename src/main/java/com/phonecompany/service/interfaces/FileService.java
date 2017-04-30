package com.phonecompany.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {
    File convert(MultipartFile file, String path) throws IOException;
    String stringToFile(String picture, String path);
}
