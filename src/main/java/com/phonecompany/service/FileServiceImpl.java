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
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@PropertySource("classpath:s3.properties")
public class FileServiceImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${bucket.name}")
    private String BUCKET_NAME;
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
                LOG.debug("Image metadata: {}", picture.substring(0, 10));
                String base64Image = picture.split(",")[1];
                LOG.debug("base64 representation of the binary file with an image: {}", base64Image);
                byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    LOG.debug("picture.split(\";\")[0]: {}", picture.split(";")[0]);
                    LOG.debug("picture.split(\";\")[0].split(\"/\")[1]: {}", picture.split(";")[0].split("/")[1]);
                    File file = new File("picture." + picture.split(";")[0].split("/")[1]);
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
        String access_key_id = System.getenv().get("ACCESS_KEY_ID");
        LOG.debug("Access key id: {}", access_key_id);
        String secret_access_key = System.getenv().get("SECRET_ACCESS_KEY");
        LOG.debug("Secret access key: {}", secret_access_key);
        AWSCredentials credentials = new BasicAWSCredentials(access_key_id, secret_access_key);
        TransferManager tm = new TransferManager(credentials);
        try {
            Upload upload = tm.upload(BUCKET_NAME, path + "/" + file.getName(), file);
            LOG.info("TARIFF PICTURE: " + path + file.getName());
            upload.waitForCompletion();
            file.delete();
            return S3_URL + BUCKET_NAME + "/" + path + "/" + file.getName();
        } catch (AmazonClientException | InterruptedException amazonClientException) {
            amazonClientException.printStackTrace();
        }
        return "";
    }
}
