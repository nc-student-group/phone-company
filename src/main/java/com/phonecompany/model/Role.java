package com.phonecompany.model;

public class Role extends DomainEntity {

    private String name;

    public Role() {
        super();
    }

    public Role(Long id) {
        super(id);
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

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                "}";
    }
}
