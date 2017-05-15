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

    private Query(Builder builder) {
        query = builder.query.toString();
        preparedStatementParams = builder.preparedStatementParams;
    }

    public String getQuery() {
        return query;
    }

    public List<Object> getPreparedStatementParams() {
        return preparedStatementParams;
    }

    public static class Builder {
        private StringBuilder query;
        private String selectClause = "SELECT * FROM ";
        private List<Object> preparedStatementParams = new ArrayList<>();

        public Builder(String category) {
            query = new StringBuilder();
            query.append(selectClause);
            query.append(category);
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

        public Builder addCondition(String condition,Object param) {
            query.append(" ");
            query.append(condition);
            query.append(" ");
            preparedStatementParams.add(param);
            return this;
        }

        public Builder addIsNullCondition(String field) {
            query.append(" ");
            query.append(field);
            query.append(" IS NULL ");
            return this;
        }

        public Builder addLikeCondition(String field,String part) {
            query.append(" ");query.append(field);
            query.append(" LIKE CONCAT('%',?,'%')");
            query.append(" ");
            preparedStatementParams.add(part);
            return this;
        }

        public Builder addPaging(int page, int size){
            query.append(" ");
            query.append(" LIMIT ? ");
            query.append(" OFFSET ? ");
            preparedStatementParams.add(size);
            preparedStatementParams.add(size*page);
            return this;
        }

        public Query build() {
            return new Query(this);
        }
    }


}
