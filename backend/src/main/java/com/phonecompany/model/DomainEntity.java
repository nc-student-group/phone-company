package com.phonecompany.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public abstract class DomainEntity {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Long id;

    public DomainEntity(Long id) {
        this.id = id;
    }
}
