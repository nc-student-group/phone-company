package com.phonecompany.service.interfaces;

/**
 * Created by nik9str on 15.04.2017.
 */
public interface EMailService {

    /***
     *
     * @param recipientEmail - to whom the eMail will be sent
     * @param text - body of message
     * @param subject - theme of the message
     */
    void sendMail(String recipientEmail,String text, String subject);
}

