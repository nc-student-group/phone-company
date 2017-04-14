package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CrudDao;
import com.phonecompany.model.VerificationToken;
import com.phonecompany.service.interfaces.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl extends CrudServiceImpl<VerificationToken>
        implements VerificationTokenService {

    @Autowired
    public VerificationTokenServiceImpl(CrudDao<VerificationToken> dao) {
        super(dao);
    }

}
