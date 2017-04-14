package com.phonecompany.service.impl;

import com.phonecompany.dao.interfaces.VerificationTokenDao;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.service.interfaces.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Yurii on 14.04.2017.
 */
@Service
public class VerificationTokenServiceImpl extends AbstractServiceImpl<VerificationToken> implements VerificationTokenService {

    @Autowired
    private VerificationTokenDao verificationTokenDao;

    @Autowired
    public VerificationTokenServiceImpl(VerificationTokenDao dao) {
        super(dao);
    }
}
