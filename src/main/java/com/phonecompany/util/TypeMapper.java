package com.phonecompany.util;

import com.phonecompany.model.DomainEntity;

import java.util.Optional;

public class TypeMapper {

    /**
     * Gets an id of the domain entity which may be null (e.g. entity is being created,
     * so there is yet not primary key in the database associated with it)
     *
     * @param entity object to fetch id from
     * @param <T> entity type
     * @return actual id or {@literal null}
     */
    public static <T extends DomainEntity> Long getNullableId(T entity) {
        return Optional.ofNullable(entity)
                .map(T::getId)
                .orElse(null);
    }
}
