package com.phonecompany.model;

/**
 * Created by Yurii on 14.04.2017.
 */
public class Role extends DomainEntity{
    private String name;

    public Role() {
        super();
    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
