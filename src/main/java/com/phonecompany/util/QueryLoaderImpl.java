package com.phonecompany.util;

import com.phonecompany.util.interfaces.QueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * {@inheritDoc}
 */
@Component
@PropertySource("classpath:queries.properties")
public class QueryLoaderImpl implements QueryLoader {

    private Environment env;

    @Autowired
    public QueryLoaderImpl(Environment env) {
        this.env = env;
    }

    @Override
    public String getQuery(String queryName) {
        return env.getProperty(queryName);
    }
}
