package com.phonecompany.service.interfaces;

public interface CaptchaService {
    boolean verify(String gReCaptchaResponse);
}
