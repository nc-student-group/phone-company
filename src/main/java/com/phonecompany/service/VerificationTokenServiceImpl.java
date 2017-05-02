package com.phonecompany.service;

import com.phonecompany.model.User;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.service.interfaces.VerificationTokenService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificationTokenServiceImpl extends CrudServiceImpl<VerificationToken>
        implements VerificationTokenService {

    @Override
    public VerificationToken saveTokenForUser(User user) {
        String randomID = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, randomID);

        return super.save(verificationToken);
    }
}
