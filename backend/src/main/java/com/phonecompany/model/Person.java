package com.phonecompany.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Person extends DomainEntity {
    private String firstName;
    private String lastName;
    private String secondName;
    private String address;
    private String phone;
}
