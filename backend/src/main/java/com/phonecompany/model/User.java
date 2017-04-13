package com.phonecompany.model;

import com.phonecompany.model.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class User extends Person {
    private String password;
    private String email;
    private UserRole userRole;
}
