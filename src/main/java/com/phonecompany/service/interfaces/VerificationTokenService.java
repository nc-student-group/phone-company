package com.phonecompany.service.interfaces;

import com.phonecompany.model.User;
import com.phonecompany.model.VerificationToken;

public interface VerificationTokenService
        extends CrudService<VerificationToken> {
    VerificationToken saveTokenForUser(User user);
}