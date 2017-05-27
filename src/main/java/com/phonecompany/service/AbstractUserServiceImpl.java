package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.model.SecuredUser;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.service.interfaces.AbstractUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

@ServiceStereotype
public abstract class AbstractUserServiceImpl<T extends User>
        extends CrudServiceImpl<T> implements AbstractUserService<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractUserServiceImpl.class);

    @Autowired
    private ShaPasswordEncoder shaPasswordEncoder;

    public AbstractUserServiceImpl() {
    }

    public T getCurrentlyLoggedInUser() {
        Object principal = SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        if(principal.equals("anonymousUser")) {
            return null;
        }
        SecuredUser securedUser = new SecuredUser(principal);
        T userFoundByEmail = this.findByEmail(securedUser.getUserName());
        LOG.debug("User retrieved from the security context: {}", userFoundByEmail);
        return userFoundByEmail;
    }

    @Override
    public T save(T entity) {
        this.validate(entity);
        if(entity.getStatus()==null){
            entity.setStatus(this.getStatus());
        }
        entity.setPassword(shaPasswordEncoder.encodePassword(entity.getPassword(), null));
        return super.save(entity);
    }

    /**
     * Performs a set of validating operations particular to the given
     * entity.
     *
     * @param entity entity to be validated
     */
    public abstract void validate(T entity);

    /**
     * Defines a default status that entities of the given type are
     * being created with.
     *
     * @return default status
     */
    public abstract Status getStatus();
}
