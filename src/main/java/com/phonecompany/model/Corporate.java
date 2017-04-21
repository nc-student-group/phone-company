package com.phonecompany.model;

import javax.validation.constraints.NotNull;

public class Corporate extends DomainEntity {

    @NotNull(message = "Corporate name number must not be null")
    private String corporateName;

    public Corporate() {

    }

    public Corporate(String corporateName) {
        this.corporateName = corporateName;
    }

    public Corporate(Long id, String corporateName) {
        super(id);
        this.corporateName = corporateName;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

}
