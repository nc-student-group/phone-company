package com.phonecompany.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.phonecompany.service.interfaces.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

@Service
@PropertySource("classpath:s3.properties")
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    @Value("${bucket.name}")
    private String BUCKET_NAME;
    @Value("${access.key.id}")
    private String ACCESS_KEY_ID;
    @Value("${secret.access.key}")
    private String SECRET_ACCESS_KEY;
    @Value("${s3.url}")
    private String S3_URL;

    public File convert(MultipartFile file, String path) throws IOException {
        File resultFile = new File(path + file.getOriginalFilename());
        resultFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(resultFile);
        fos.write(file.getBytes());
        fos.close();
        return resultFile;
    }

    public String stringToFile(String picture, String path) {
        if (picture != null) {
            if (picture.substring(0, 10).equals("data:image")) {
                String base64Image = picture.split(",")[1];
                byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    File file = new File("tariff-picture." + picture.split(";")[0].split("/")[1]);
                    ImageIO.write(img, picture.split(";")[0].split("/")[1], file);
                    return uploadFileToAmazon(file, path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return picture;
    }

    public String uploadFileToAmazon(File file, String path) {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
        TransferManager tm = new TransferManager(credentials);
        try {
//            path += path+"/"+ Calendar.getInstance().getTimeInMillis()+"/";
            Upload upload = tm.upload(BUCKET_NAME, path + "/" + file.getName(), file);
            LOGGER.info("TARIFF PICTURE: " + path + file.getName());
            upload.waitForCompletion();
            file.delete();
            return S3_URL + BUCKET_NAME + "/" + path + "/" + file.getName();
        } catch (AmazonClientException amazonClientException) {
            amazonClientException.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}
