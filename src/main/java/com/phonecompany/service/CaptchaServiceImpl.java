package com.phonecompany.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonecompany.service.interfaces.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaServiceImpl.class);
    private static final String SECRET = "6Lci1CAUAAAAAMk7uq5NfITwvVdTg19tws4Sfd6T";
    private static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Override
    public boolean verify(String gReCaptchaResponse) {
        if (gReCaptchaResponse == null || gReCaptchaResponse.length() == 0) {
            return false;
        }
        try {
            URL verifyUrl = new URL(SITE_VERIFY_URL);
            // Open Connection to URL
            HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();
            // Add Request Header
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            // Data will be sent to the server.
            String postParams = "secret=" + SECRET + "&response=" + gReCaptchaResponse;
            // Send Request
            conn.setDoOutput(true);
            // Get the output stream of Connection
            // Write data in this stream, which means to send data to Server.
            OutputStream outStream = conn.getOutputStream();
            outStream.write(postParams.getBytes());
            outStream.flush();
            outStream.close();
            InputStream is = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            Map jsonMap = mapper.readValue(is, Map.class);
            LOGGER.debug("ReCaptcha verify result: {}", jsonMap.get("success"));
            return (boolean) jsonMap.get("success");
        } catch (Exception e) {
            return false;
        }
    }
}
