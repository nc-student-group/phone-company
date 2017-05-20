package com.phonecompany.util;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

//TODO: remove it please
/**
 * Created by nik9str on 14.05.2017.
 */
public class Query {


    private final String query;
    private final String countQuery;
    private final List<Object> preparedStatementParams;
    private final List<Object> countParams;

    private Query(Builder builder) {
        query = builder.query.toString();
        countQuery = builder.countQuery.toString();
        preparedStatementParams = builder.preparedStatementParams;
        countParams = builder.countParams;
    }

    public String getQuery() {
        return query;
    }

    public List<Object> getPreparedStatementParams() {
        return preparedStatementParams;
    }

    public String getCountQuery() {
        return countQuery;
    }

    public List<Object> getCountParams() {
        return countParams;
    }

    public static class Builder {
        private StringBuilder query;
        private String selectClause = "SELECT * FROM ";
        private String countClause = "SELECT count(*) FROM ";
        private StringBuilder countQuery;
        private List<Object> preparedStatementParams = new ArrayList<>();
        private List<Object> countParams = new ArrayList<>();

        public Builder(String category) {
            query = new StringBuilder();
            query.append(selectClause);
            query.append(category);
            countQuery = new StringBuilder();
            countQuery.append(countClause);
            countQuery.append(category);
        }

        public Builder and() {
            query.append(" and ");
            countQuery.append(" and ");
            return this;
        }

        public Builder or() {
            query.append(" or ");
            countQuery.append(" or ");
            return this;
        }

        public Builder where() {
            query.append(" where ");
            countQuery.append(" where ");
            return this;
        }

        public Builder addCondition(String condition, Object param) {
            query.append(" ");
            query.append(condition);
            query.append(" ");
            countQuery.append(" ");
            countQuery.append(condition);
            countQuery.append(" ");
            preparedStatementParams.add(param);
            countParams.add(param);
            return this;
        }

        public Builder addIsNullCondition(String field) {
            query.append(" ");
            query.append(field);
            query.append(" IS NULL ");
            countQuery.append(" ");
            countQuery.append(field);
            countQuery.append(" IS NULL ");
            return this;
        }

        public Builder addLikeCondition(String field, String part) {
            query.append(" ");
            query.append(field);
            query.append(" LIKE CONCAT('%',?,'%')");
            query.append(" ");
            countQuery.append(" ");
            countQuery.append(field);
            countQuery.append(" LIKE CONCAT('%',?,'%')");
            countQuery.append(" ");
            preparedStatementParams.add(part);
            countParams.add(part);
            return this;
        }

        public Builder addBetweenCondition(String field, Object param1, Object param2) {
            query.append(" ");
            query.append(field);
            query.append(" BETWEEN ? AND ?");
            query.append(" ");
            countQuery.append(" ");
            countQuery.append(field);
            countQuery.append(" BETWEEN ? AND ?");
            countQuery.append(" ");
            preparedStatementParams.add(param1);
            preparedStatementParams.add(param2);
            countParams.add(param1);
            countParams.add(param2);
            return this;
        }

        public Builder orderBy(String field) {
            query.append(" ORDER BY ");
            query.append(field);
            query.append(" ");
            return this;
        }

        public Builder orderByType(String type) {
            query.append(" ");
            query.append(type);
            query.append(" ");
            return this;
        }

        public Builder addPaging(int page, int size) {
            query.append(" LIMIT ? OFFSET ? ");
            preparedStatementParams.add(size);
            preparedStatementParams.add(size * page);
            return this;
        }

        public Query build() {
            return new Query(this);
        }

    }


}
