package com.phonecompany.service;

import com.phonecompany.service.interfaces.EMailService;
import org.junit.Test;


/**
 * Created by nik9str on 15.04.2017.
 */
public class EMailServiceImplTest {
    @Test
    public void sendToUser() {
        EMailService eMailService = new EMailServiceImpl();
        eMailService.sendMail("nik9str@gmail.com","Message","subject");
    }
}
