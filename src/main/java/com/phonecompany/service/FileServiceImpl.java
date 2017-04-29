package com.phonecompany.service;

import com.phonecompany.service.interfaces.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    public File convert(MultipartFile file, String path) throws IOException {
        File resultFile = new File(path+file.getOriginalFilename());
        resultFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(resultFile);
        fos.write(file.getBytes());
        fos.close();
        return resultFile;
    }
}
