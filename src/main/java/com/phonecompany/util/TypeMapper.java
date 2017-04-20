package com.phonecompany.util;

import com.phonecompany.model.DomainEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class TypeMapper {

    public static LocalDate toLocalDate(Date date) {
        return date.toLocalDate();
    }

    public static Date toSqlDate(LocalDate localDate) {
        return Date.valueOf(localDate);
    }
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
