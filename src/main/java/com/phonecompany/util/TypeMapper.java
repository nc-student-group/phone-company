package com.phonecompany.util;

import com.phonecompany.model.DomainEntity;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.UserRole;
import org.springframework.util.Assert;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class TypeMapper {

    /**
     * Converts an instance of {@code java.sql.Date} to its corresponding
     * {@code java.time.LocalDate} representation. This method does aware
     * that its incoming parameter may be {@literal null}.
     *
     * @param date date to convert
     * @return corresponding {@code java.time.LocalDate} object or {@literal null}
     */
    public static LocalDate toLocalDate(Date date) {
        return Optional.ofNullable(date)
                .map(Date::toLocalDate)
                .orElse(null);
    }

    public static Date toSqlDate(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(l -> Date.valueOf(localDate))
                .orElse(null);
    }

    /**
     * Gets a {@code UserRole} enum instance that corresponds to its id
     * containing in the storage
     *
     * @param databaseId id that maps an enum instance to its storage entry
     * @return corresponding enum instance
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

    /**
     * Provides a {@code Function} that reduces service price by the value
     * defined by a method input parameter
     *
     * @param discount percentage that price will be reduced by
     * @return price mapping {@code Function}
     */
    public static Function<Service, Service> getDiscountMapper(double discount) {
        Assert.isTrue(discount <= 100 && discount > 0,
                "Percentage should be greater than 0 and less than 100");
        return service -> {
            double price = service.getPrice();
            price = price * (1 - discount / 100);
            service.setPrice(price);
            return service;
        };
    }
}
