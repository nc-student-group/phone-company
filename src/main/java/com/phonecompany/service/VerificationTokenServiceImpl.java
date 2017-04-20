package com.phonecompany.service;

import com.phonecompany.dao.interfaces.VerificationTokenDao;
import com.phonecompany.model.User;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.service.interfaces.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl extends CrudServiceImpl<VerificationToken>
        implements VerificationTokenService {

    private VerificationTokenDao verificationTokenDao;

    @Autowired
    public VerificationTokenServiceImpl(VerificationTokenDao verificationTokenDao) {
        this.verificationTokenDao = verificationTokenDao;
    }
}
