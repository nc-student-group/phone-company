package com.phonecompany.util;

import com.phonecompany.model.DomainEntity;
import com.phonecompany.model.enums.UserRole;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class TypeMapper {

    public static LocalDate toLocalDate(Date date) {
        return date.toLocalDate();
    }

    public static Date toSqlDate(LocalDate localDate) {
        return Date.valueOf(localDate);
    }

    /**
     * Gets a {@code UserRole} enum instance that corresponds to its id
     * containing in the storage
     *
     * @param databaseId id that maps an enum instance to its storage entry
     * @return corresponding enum instance
     *
     * @see UserRole user role enum representation
     */
    public static UserRole getUserRoleByDatabaseId(Long databaseId) {
        return Arrays.stream(UserRole.values())
                .filter(ur -> Objects.equals(ur.getDatabaseId(), databaseId))
                .findFirst().orElse(null);
    }

    /**
     * Gets an id of the domain entity which may be null (e.g. entity is being created,
     * so there is yet no primary key in the database associated with it)
     *
     * @param entity object to fetch id from
     * @param <T>    entity type
     * @return actual id or {@literal null}
     */
    public static <T extends DomainEntity> Long getNullableId(T entity) {
        return Optional.ofNullable(entity)
                .map(T::getId)
                .orElse(null);
    }
}
