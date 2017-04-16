package com.phonecompany.service;

import com.phonecompany.Application;
import com.phonecompany.service.interfaces.EMailService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EMailServiceImplTest {

    @Autowired
    EMailServiceImpl eMailService;
    @Test
    public void sendToUser() {
        eMailService.sendMail("nik9str@gmail.com","Message","subject");
    }
}
