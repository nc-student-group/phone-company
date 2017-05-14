package com.phonecompany.util;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nik9str on 14.05.2017.
 */
public class Query {


    private final String query;
    private final List<Object> preparedStatementParams;

    public static class Builder {
        private StringBuilder query;
        private String selectClause = "SELECT * FROM ? ";
        private List<Object> preparedStatementParams = new ArrayList<>();

        public Builder(String category) {
            query = new StringBuilder();
            query.append(selectClause);
            preparedStatementParams.add(category);
        }

        public Builder and() {
            query.append(" and ");
            return this;
        }

        public Builder or() {
            query.append(" or ");
            return this;
        }

        public Builder where() {
            query.append(" where ");
            return this;
        }

        public Builder addCondtiion(String condition,List<Object> params) {
            query.append(" ");
            query.append(condition);
            query.append(" ");
            preparedStatementParams.add(params);
            return this;
        }

        public Builder addLikeCondtiion(String field,String part) {
            query.append(" ");
            query.append("? LIKE CONCAT('%',?,'%')");
            query.append(" ");
            preparedStatementParams.add(field);
            preparedStatementParams.add(part);
            return this;
        }

        public Query build() {
            return new Query(this);
        }
    }

    private Query(Builder builder) {
        query = builder.query.toString();
        preparedStatementParams = builder.preparedStatementParams;
    }

}
