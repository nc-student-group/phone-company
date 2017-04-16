package com.phonecompany.service.interfaces;

public interface EMailService {
    /***
     *
     * @param recipientEmail - to whom the eMail will be sent
     * @param text - body of message
     * @param subject - theme of the message
     */
    void sendMail(String recipientEmail,String text, String subject);
}
